package com.exosomnia.exoarmory.items.armory.swords;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.items.abilities.ArmoryAbility;
import com.exosomnia.exoarmory.capabilities.resource.ArmoryResourceProvider;
import com.exosomnia.exoarmory.items.abilities.SunfireSurgeAbility;
import com.exosomnia.exoarmory.items.resource.ArmoryResource;
import com.exosomnia.exoarmory.items.resource.ResourcedItem;
import com.exosomnia.exoarmory.items.resource.SolarSwordResource;
import com.exosomnia.exoarmory.managers.ConditionalManager;
import com.exosomnia.exoarmory.networking.PacketHandler;
import com.exosomnia.exoarmory.networking.packets.ArmoryResourcePacket;
import com.exosomnia.exolib.utils.ComponentUtils.DetailLevel;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SolarSword extends ArmorySwordItem implements ResourcedItem {

    private static final Multimap<Attribute, AttributeModifier>[] RANK_ATTRIBUTES = new Multimap[5];
    static {
        RANK_ATTRIBUTES[0] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 5.0, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.4, AttributeModifier.Operation.ADDITION))
                .build();
        RANK_ATTRIBUTES[1] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 6.0, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.4, AttributeModifier.Operation.ADDITION))
                .build();
        RANK_ATTRIBUTES[2] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 7.0, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.4, AttributeModifier.Operation.ADDITION))
                .build();
        RANK_ATTRIBUTES[3] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 8.0, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.4, AttributeModifier.Operation.ADDITION))
                .build();
        RANK_ATTRIBUTES[4] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 9.0, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.4, AttributeModifier.Operation.ADDITION))
                .build();
    }

    private static final ArmoryResource RESOURCE = new SolarSwordResource();


    public SolarSword() {
        super();
    }

    public List<ArmoryAbility> getAbilities(ItemStack itemStack) {
        return switch (getRank(itemStack)) {
            case 0, 1 -> List.of(ExoArmory.REGISTRY.ABILITY_SOLAR_FLARE);
            default -> List.of(ExoArmory.REGISTRY.ABILITY_SOLAR_FLARE, ExoArmory.REGISTRY.ABILITY_SUNFIRE_SURGE);
        };
    }
    public ArmoryResource getResource() { return RESOURCE; }

    public Multimap<Attribute, AttributeModifier>[] getAttributesForAllRanks() { return RANK_ATTRIBUTES; }

    public void addToHover(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag, int rank, DetailLevel detail) {
        components.add(Component.literal(""));

        //Ability Info
        for (ArmoryAbility ability: getAbilities(itemStack)) {
            components.addAll(ability.getTooltip(detail, rank));
        }

        components.add(Component.literal(""));

        //Resource Info
        components.addAll(RESOURCE.getTooltip(detail, rank, itemStack));
    }

    //region IForgeItem Overrides
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ArmoryResourceProvider();
    }

    //TODO: DEBUGGING
    @Nullable
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag shareTag = stack.getTag();
        shareTag.putDouble("ArmoryResource", getResource().getResource(stack));
        return shareTag;
    }

    //TODO: DEBUGGING
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            if (nbt.contains("ArmoryData") && nbt.contains("ArmoryResource")) {
                ExoArmory.RESOURCE_MANAGER.setResource(((CompoundTag)nbt.get("ArmoryData")).getUUID("UUID"), nbt.getDouble("ArmoryResource"));
                nbt.remove("ArmoryResource");
            }
        });
        stack.setTag(nbt);
    }
    //endregion

    //region Item Overrides
    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotIndex, boolean selectedIndex) {
        if (level.isClientSide) { return; }

        if (level.getGameTime() % 10 == 0 && ExoArmory.CONDITIONAL_MANAGER.getPlayerCondition((Player)entity, ConditionalManager.Condition.SOLAR_SWORD)) {
            RESOURCE.addResource(itemStack, getResource().getStatForRank(SolarSwordResource.Stats.CHARGE, getRank(itemStack)));
            PacketHandler.sendToPlayer(new ArmoryResourcePacket(getUUID(itemStack), RESOURCE.getResource(itemStack)),
                    (ServerPlayer)entity);
        }
    }

    @Override
    public int getUseDuration(ItemStack itemStack) { return 20; }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (getRank(itemStack) > 0) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        int rank = getRank(itemStack);
        if (!level.isClientSide && entity instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20 * (int)ExoArmory.REGISTRY.ABILITY_SUNFIRE_SURGE.getStatForRank(SunfireSurgeAbility.Stats.DURATION, rank), 0));
            player.addEffect(new MobEffectInstance(ExoArmory.REGISTRY.EFFECT_SUNFIRE_SURGE.get(), 20 * (int)ExoArmory.REGISTRY.ABILITY_SUNFIRE_SURGE.getStatForRank(SunfireSurgeAbility.Stats.DURATION, rank), 0));
            player.getCooldowns().addCooldown(itemStack.getItem(), 20 * (int)ExoArmory.REGISTRY.ABILITY_SUNFIRE_SURGE.getStatForRank(SunfireSurgeAbility.Stats.COOLDOWN, rank));
        }
        entity.playSound(ExoArmory.REGISTRY.SOUND_FIERY_EFFECT.get(), 0.34F, 1.25F);
        return itemStack;
    }
    //endregion
}

