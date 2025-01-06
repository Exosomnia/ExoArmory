package com.exosomnia.exoarmory.items.swords;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.capabilities.resource.ArmoryResourceProvider;
import com.exosomnia.exoarmory.items.ResourcedItem;
import com.exosomnia.exoarmory.managers.ConditionalManager;
import com.exosomnia.exoarmory.networking.PacketHandler;
import com.exosomnia.exoarmory.networking.packets.ArmoryResourcePacket;
import com.exosomnia.exolib.ExoLib;
import com.exosomnia.exolib.networking.packets.ParticleShapePacket;
import com.exosomnia.exolib.particles.options.RGBSParticleOptions;
import com.exosomnia.exolib.particles.shapes.ParticleShapeDome;
import com.exosomnia.exolib.particles.shapes.ParticleShapeOptions;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mod.EventBusSubscriber(modid = ExoArmory.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SolarSword extends SwordArmoryItem implements ResourcedItem {

    private static final String ITEM_NAME = "solar_sword";
    private static final int ABILITY_RGB = 0xFFBF00;

    private static final float[] BAR_RGB = {1.0F, 0.5F, 0.0F};
    private static final double RESOURCE_MAX = 500.0;

    private static final Item.Properties itemProperties = new Item.Properties()
            .durability(500)
            .rarity(Rarity.UNCOMMON);

    public SolarSword() {
        super(itemProperties);
    }

    public float[] getBarRGB() {
        return BAR_RGB;
    }
    public double getResource(ItemStack itemStack) { return itemStack.getCapability(ArmoryResourceProvider.ARMORY_RESOURCE).resolve().get().getCharge(); }
    public double getResourceMax() { return RESOURCE_MAX; }

    public String getItemName() { return ITEM_NAME; }
    public int getAbilityRGB() {
        return ABILITY_RGB;
    }

    public void buildRanks() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder;

        //Rank 1
        builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 5.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4, AttributeModifier.Operation.ADDITION));
        this.RANK_ATTRIBUTES[0] = builder.build();

        //Rank 2
        builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 6.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4, AttributeModifier.Operation.ADDITION));
        this.RANK_ATTRIBUTES[1] = builder.build();

        //Rank 3
        builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 7.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.3, AttributeModifier.Operation.ADDITION));
        this.RANK_ATTRIBUTES[2] = builder.build();

        //Rank 4
        builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 8.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.3, AttributeModifier.Operation.ADDITION));
        this.RANK_ATTRIBUTES[3] = builder.build();

        //Rank 5
        builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 9.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.2, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(BASE_MOVEMENT_SPEED_UUID, "Weapon modifier", 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
        this.RANK_ATTRIBUTES[4] = builder.build();
    }

    //region IForgeItem Overrides
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ArmoryResourceProvider();
    }
    //endregion

    //region Item Overrides
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(itemStack, level, components, flag);

        //Ability Info
        components.add(Component.translatable("item.exoarmory.info.ability")
                        .withStyle(TITLE_STYLE)
                .append(Component.literal(": ")
                        .withStyle(COLON_STYLE))
                .append(getAbilityName()
                        .withStyle(BLANK_STYLE.withColor(TextColor.fromRgb(ABILITY_RGB)))));

        components.add(Component.literal(""));

        //Resource Info
        components.add(getResourceName()
                        .withStyle(TITLE_STYLE)
                .append(Component.literal(": ")
                        .withStyle(COLON_STYLE))
                .append(Component.literal(String.valueOf((int)getResource(itemStack)))
                        .withStyle(BLANK_STYLE.withColor(ChatFormatting.AQUA)))
                .append(Component.literal("/")
                        .withStyle(BLANK_STYLE.withColor(ChatFormatting.GRAY)))
                .append(Component.literal(String.valueOf((int)RESOURCE_MAX))
                        .withStyle(BLANK_STYLE.withColor(ChatFormatting.AQUA))));

        if (Screen.hasShiftDown()) {
            components.add(4, getAbilityDesc().withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
            components.add(7, getResourceDesc().withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotIndex, boolean selectedIndex) {
        if (level.isClientSide) { return; }

        if (level.getGameTime() % 10 == 0 && ConditionalManager.SOLAR_CONDITIONS.get((Player)entity)) {
            itemStack.getCapability(ArmoryResourceProvider.ARMORY_RESOURCE).ifPresent(iArmoryResourceStorage -> {
                iArmoryResourceStorage.addCharge(1.0, RESOURCE_MAX);
                PacketHandler.sendToPlayer(new ArmoryResourcePacket(getUUID(itemStack), slotIndex, iArmoryResourceStorage.getCharge()),
                        (ServerPlayer)entity);
            });
        }
    }

    @Override
    public int getUseDuration(ItemStack itemStack) { return 20; }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 400, 0));
            player.addEffect(new MobEffectInstance(ExoArmory.REGISTRY.EFFECT_STELLAR_INFUSION.get(), 400, 0));
            player.getCooldowns().addCooldown(itemStack.getItem(), 2400);
        }
        entity.playSound(ExoArmory.REGISTRY.SOUND_FIERY_EFFECT.get(), 0.34F, 1.25F);
        return itemStack;
    }
    //endregion

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();
        if (level.isClientSide || !player.hasEffect(ExoArmory.REGISTRY.EFFECT_STELLAR_INFUSION.get())) { return; }

        BlockPos pos = player.blockPosition();
        BlockPos belowPos = pos.below();
        if (level.getBlockState(pos).canBeReplaced() && level.getBlockState(belowPos).isFaceSturdy(level, belowPos, Direction.UP)) {
            level.setBlock(pos, BaseFireBlock.getState(level, pos), Block.UPDATE_ALL);
            player.setRemainingFireTicks(20);
        }
    }

    @SubscribeEvent
    public static void onEntityCrit(CriticalHitEvent event) {
        Entity target = event.getEntity();
        Level eventLevel = target.level();
        if (eventLevel.isClientSide || !event.isVanillaCritical() || !(event.getTarget() instanceof LivingEntity defender)) return;

        ServerPlayer attacker = (ServerPlayer)event.getEntity();
        ItemStack attackerItem = attacker.getMainHandItem();
        if ( (attackerItem.getItem() instanceof SolarSword solarSword) && (solarSword.getResource(attackerItem) >= 25.0) ) {
            Vec3 defenderPosition = defender.position();
            List<LivingEntity> nearbyEntities = eventLevel.getEntitiesOfClass(LivingEntity.class, new AABB(defenderPosition, defenderPosition).inflate(3.0), livingEntity -> {
                if (!livingEntity.isAlive() || livingEntity == attacker || livingEntity == defender) { return false; }
                else if ( (livingEntity instanceof TamableAnimal tamableAnimal) && (tamableAnimal.isTame()) ) { return false; }
                else return (!(livingEntity instanceof NeutralMob neutralMob)) || (neutralMob.isAngryAt(attacker));
            });
            defender.setSecondsOnFire(5);
            for (LivingEntity entity : nearbyEntities) {
                Vec3 entityPos = new Vec3(entity.position().toVector3f());
                if (entityPos.distanceTo(defenderPosition) <= 3.0) {
                    entity.setSecondsOnFire(5);
                    entity.hurt(attacker.damageSources().explosion(attacker, attacker), 6.0F);
                    entityPos = entityPos.subtract(defenderPosition).normalize().multiply(0.67, 0.33, 0.67);
                    entity.push(entityPos.x, entityPos.y + 0.33, entityPos.z);
                }
            }
            ((ServerLevel) eventLevel).playSeededSound(null, defenderPosition.x, defenderPosition.y, defenderPosition.z, ExoArmory.REGISTRY.SOUND_FIERY_EXPLOSION.get(), SoundSource.PLAYERS,
                    0.34F, 1.25F, 0);
            ((ServerLevel) eventLevel).sendParticles(ParticleTypes.FLAME, defenderPosition.x, defenderPosition.y+.5, defenderPosition.z, 25, 0, 0, 0, 0.075);
            com.exosomnia.exolib.networking.PacketHandler.sendToPlayer(new ParticleShapePacket(
                    new ParticleShapeDome(new RGBSParticleOptions(ExoLib.REGISTRY.SPIRAL_PARTICLE.get(), 1.0F, 0.5F, 0.0F, 0.1f),
                            defenderPosition, new ParticleShapeOptions.Dome(3.0F, 128))), attacker);
            attackerItem.getCapability(ArmoryResourceProvider.ARMORY_RESOURCE).ifPresent(iArmoryResourceStorage -> {
                iArmoryResourceStorage.addCharge(-25.0, RESOURCE_MAX);
                PacketHandler.sendToPlayer(new ArmoryResourcePacket(solarSword.getUUID(attackerItem), attacker.getInventory().selected, iArmoryResourceStorage.getCharge()),
                        attacker);
            });
        }
    }
}

