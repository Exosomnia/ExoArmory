package com.exosomnia.exoarmory.handlers;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.utils.AttributeUtils;
import com.google.common.collect.Multimap;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mod.EventBusSubscriber(modid = ExoArmory.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CombatEventsHandler {

    static final TagKey<DamageType> IS_EXPLOSION_TYPE = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("minecraft", "is_explosion"));

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityHeal(LivingHealEvent event) {
        event.setAmount(event.getAmount() * (float)event.getEntity().getAttributeValue(ExoArmory.REGISTRY.ATTRIBUTE_HEALING_RECEIVED.get()));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityAttack(LivingAttackEvent event) {
        LivingEntity defender = event.getEntity();
        if (defender.level().isClientSide || event.getSource().is(DamageTypeTags.BYPASSES_SHIELD) ||
                (defender.isUsingItem() && defender.getUseItem().canPerformAction(ToolActions.SHIELD_BLOCK))) { return; }

        boolean blocked;
        blocked = tryPassiveBlock(defender, InteractionHand.OFF_HAND, event.getAmount());
        if (!blocked) { blocked = tryPassiveBlock(defender, InteractionHand.MAIN_HAND, event.getAmount()); }
        event.setCanceled(blocked);
    }

    private static boolean tryPassiveBlock(LivingEntity defender, InteractionHand hand, float damage) {
        ItemStack itemStack = defender.getItemInHand(hand);
        Level level = defender.level();
        if (itemStack.canPerformAction(ToolActions.SHIELD_BLOCK)) {
            double blockChance = (AttributeUtils.getAttributeValueOfItemStack(ExoArmory.REGISTRY.ATTRIBUTE_PASSIVE_BLOCK.get(),
                    hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND, itemStack,
                    defender.getAttributeBaseValue(ExoArmory.REGISTRY.ATTRIBUTE_PASSIVE_BLOCK.get())) - 1.0);
            if (level.random.nextDouble() < blockChance) {
                level.playSound(null, defender.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
                if (itemStack.isDamageableItem()) { itemStack.hurtAndBreak((int)Math.max(0.0, damage >= 3.0F ? damage : 0.0), defender, (entity) -> entity.broadcastBreakEvent(hand)); }
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityHurt(LivingHurtEvent event) {
        LivingEntity defender = event.getEntity();
        Entity attacker = event.getSource().getEntity();

        if (defender instanceof ServerPlayer defenderPlayer) {
            ItemStack chestArmor = defender.getItemBySlot(EquipmentSlot.CHEST);
            int luckyProtection = chestArmor.getEnchantmentLevel(ExoArmory.REGISTRY.ENCHANTMENT_LUCKY_PROTECTION.get());
            if (luckyProtection > 0) {
                event.setAmount((float)(event.getAmount() * calculateLuckyProtection(defenderPlayer)));
            }
        }

        if (event.getSource().is(IS_EXPLOSION_TYPE) && (attacker instanceof Creeper || attacker instanceof ServerPlayer)) {
            float modifier = (float)((LivingEntity)attacker).getAttributeValue(ExoArmory.REGISTRY.ATTRIBUTE_EXPLOSION_STRENGTH.get());
            event.setAmount(event.getAmount() * modifier);
        }

        float modifier = 1.0F - (float)defender.getAttributeValue(ExoArmory.REGISTRY.ATTRIBUTE_VULNERABILITY.get());
        event.setAmount(event.getAmount() + (event.getAmount() * modifier));
    }

    private static double calculateLuckyProtection(ServerPlayer player) {
        return (1.0 - (0.1 + (player.getLuck() * 0.05) * player.getRandom().nextDouble()));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerCrit(CriticalHitEvent event) {
        Player attacker = event.getEntity();
        if (attacker.level().isClientSide) { return; }
        boolean isCritical = event.isVanillaCritical();

        if (!isCritical) {
            double chance = (attacker.getAttributeValue(ExoArmory.REGISTRY.ATTRIBUTE_PASSIVE_CRITICAL.get()) - 1.0);
            if (attacker.getRandom().nextDouble() < chance) {
                event.setResult(Event.Result.ALLOW);
                isCritical = true;
            }
        }

        if (isCritical) {
            event.setDamageModifier((float)(1.0 + attacker.getAttributeValue(ExoArmory.REGISTRY.ATTRIBUTE_CRITICAL_DAMAGE.get())));
        }
    }

    @SubscribeEvent
    public static void onShieldBlock(ShieldBlockEvent event) {
        event.setShieldTakesDamage(true);
        float newDamage = event.getOriginalBlockedDamage();
        if (event.getEntity() instanceof Player player) {
            InteractionHand hand = player.getUsedItemHand();
            ItemStack itemStack = player.getItemInHand(hand);
            int stabilityTicks = (int) (AttributeUtils.getAttributeValueOfItemStack(ExoArmory.REGISTRY.ATTRIBUTE_SHIELD_STABILITY.get(),
                    hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND, itemStack,
                    player.getAttributeBaseValue(ExoArmory.REGISTRY.ATTRIBUTE_SHIELD_STABILITY.get())) * 20.0);
            boolean isStable = player.getUseItem().getUseDuration() - player.getUseItemRemainingTicks() <= stabilityTicks;
            if (!isStable) { newDamage /= 2; }
        }
        event.setBlockedDamage(newDamage);
    }

    @SubscribeEvent
    public static void onPlayerDeathDrops(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) { return; }

        List<ItemEntity> soulboundItems = new ArrayList<>();
        Collection<ItemEntity> drops = event.getDrops();
        if (!drops.isEmpty()) {
            drops.forEach(itemEntity -> {
                ItemStack dropItem = itemEntity.getItem();
                if (dropItem.getEnchantmentLevel(ExoArmory.REGISTRY.ENCHANTMENT_SOULBOUND.get()) > 0 &&
                        dropItem.getEnchantmentLevel(Enchantments.VANISHING_CURSE) < 1) {
                    soulboundItems.add(itemEntity);
                }
            });
            drops.removeAll(soulboundItems);

            soulboundItems.forEach(itemEntity -> {
                ItemStack itemStack = itemEntity.getItem();
                int maxDamage = itemStack.getMaxDamage() - 1;
                itemStack.setDamageValue(Math.min(maxDamage, itemStack.getDamageValue() + (maxDamage / 2)));
                player.getInventory().add(itemEntity.getItem());
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        Player newPlayer = event.getEntity();
        Inventory inventory = newPlayer.getInventory();
        for (ItemStack itemStack : original.getInventory().items) {
            if (itemStack.isEmpty()) break;
            inventory.add(itemStack);
        }
    }

    @SubscribeEvent
    public static void enchantmentAttributeEvent(final ItemAttributeModifierEvent event) {
        EquipmentSlot slot = event.getSlotType();
        if (!slot.equals(EquipmentSlot.OFFHAND) && !slot.equals(EquipmentSlot.MAINHAND)) { return; }
        ItemStack itemStack = event.getItemStack();
        Multimap<Attribute, AttributeModifier> originalMods = event.getOriginalModifiers();

        int fortifyingLvl = itemStack.getEnchantmentLevel(ExoArmory.REGISTRY.ENCHANTMENT_FORTIFYING.get());
        if (fortifyingLvl > 0) {
            originalMods.get(Attributes.ARMOR).stream().filter(attributeModifier -> attributeModifier.getOperation().equals(AttributeModifier.Operation.ADDITION)).findFirst()
                    .ifPresentOrElse(mod -> {
                        event.removeModifier(Attributes.ARMOR, mod);
                        event.addModifier(Attributes.ARMOR, new AttributeModifier(mod.getId(),
                                mod.getName(), mod.getAmount() + fortifyingLvl, AttributeModifier.Operation.ADDITION));
                    },
                    () -> {
                        event.addModifier(Attributes.ARMOR, new AttributeModifier(slot.equals(EquipmentSlot.MAINHAND) ? ExoArmory.REGISTRY.SHIELD_ARMOR_UUID : ExoArmory.REGISTRY.OFF_HAND_SHIELD_ARMOR_UUID,
                                "Enchant Bonus", fortifyingLvl, AttributeModifier.Operation.ADDITION));
                    });
        }

        int rallyingLvl = itemStack.getEnchantmentLevel(ExoArmory.REGISTRY.ENCHANTMENT_RALLYING.get());
        if (rallyingLvl > 0) {
            originalMods.get(Attributes.ATTACK_DAMAGE).stream().filter(attributeModifier -> attributeModifier.getOperation().equals(AttributeModifier.Operation.MULTIPLY_TOTAL)).findFirst()
                    .ifPresentOrElse(mod -> {
                                event.removeModifier(Attributes.ATTACK_DAMAGE, mod);
                                event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(mod.getId(),
                                        mod.getName(), mod.getAmount() + ((rallyingLvl + 1) * .025), AttributeModifier.Operation.MULTIPLY_TOTAL));
                            },
                            () -> {
                                event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(slot.equals(EquipmentSlot.MAINHAND) ? ExoArmory.REGISTRY.SHIELD_ATTACK_UUID : ExoArmory.REGISTRY.OFF_HAND_SHIELD_ATTACK_UUID,
                                        "Enchant Bonus", ((rallyingLvl + 1) * .025), AttributeModifier.Operation.MULTIPLY_TOTAL));
                            });
        }
    }

    @SubscribeEvent
    public static void lootingEvent(LootingLevelEvent event) {
        DamageSource source = event.getDamageSource();
        if (source == null || !(source.getEntity() instanceof ServerPlayer player)) return;

        event.setLootingLevel((int)(event.getLootingLevel() + player.getAttributeValue(ExoArmory.REGISTRY.ATTRIBUTE_LOOTING.get())));
    }

    @SubscribeEvent
    public static void playerTickEvent(TickEvent.LevelTickEvent event) {
        if (event.side.isClient() || event.phase.equals(TickEvent.Phase.END)) return;

        ServerLevel level = (ServerLevel)event.level;
        if (level.getGameTime() % 10 != 0) return;

        for (ServerPlayer player : level.players()) {
            for(ItemStack armor : player.getArmorSlots()) {
                if (armor.isEmpty() || !armor.hasTag()) continue;

                CompoundTag tagArmor = armor.getTag();
                if (!tagArmor.contains("PersistentEffect")) continue;

                Tag tagEffect = tagArmor.get("PersistentEffect");
                if (!(tagEffect instanceof CompoundTag compoundEffect) || !compoundEffect.contains("Effect")) continue;

                MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.bySeparator(compoundEffect.getString("Effect"), ':'));
                if (effect == null) continue;

                int amplifier = compoundEffect.getInt("Amplifier");
                player.addEffect(new MobEffectInstance(effect, 20, amplifier, true, false, true));
            }
        }
    }
}
