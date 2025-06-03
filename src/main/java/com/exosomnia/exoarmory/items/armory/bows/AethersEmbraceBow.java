package com.exosomnia.exoarmory.items.armory.bows;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.capabilities.aethersembrace.AethersEmbraceProvider;
import com.exosomnia.exoarmory.capabilities.aethersembrace.IAethersEmbraceStorage;
import com.exosomnia.exoarmory.entities.projectiles.EphemeralArrow;
import com.exosomnia.exoarmory.items.ActivatableItem;
import com.exosomnia.exoarmory.items.abilities.AetherBarrageAbility;
import com.exosomnia.exoarmory.items.abilities.ArmoryAbility;
import com.exosomnia.exoarmory.items.resource.AethersEmbraceResource;
import com.exosomnia.exoarmory.items.resource.ArmoryResource;
import com.exosomnia.exoarmory.items.resource.ResourcedItem;
import com.exosomnia.exoarmory.networking.PacketHandler;
import com.exosomnia.exoarmory.networking.packets.AethersEmbraceTargetPacket;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class AethersEmbraceBow extends ArmoryBowItem implements ResourcedItem, ActivatableItem {

    private static final Multimap<Attribute, AttributeModifier>[] RANK_ATTRIBUTES = new Multimap[5];
    static {
        RANK_ATTRIBUTES[0] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(ExoArmory.REGISTRY.ATTRIBUTE_RANGED_STRENGTH.get(), new AttributeModifier("Default", 0.05, AttributeModifier.Operation.MULTIPLY_BASE))
                .build();
        RANK_ATTRIBUTES[1] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(ExoArmory.REGISTRY.ATTRIBUTE_RANGED_STRENGTH.get(), new AttributeModifier("Default", 0.10, AttributeModifier.Operation.MULTIPLY_BASE))
                .build();
        RANK_ATTRIBUTES[2] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(ExoArmory.REGISTRY.ATTRIBUTE_RANGED_STRENGTH.get(), new AttributeModifier("Default", 0.15, AttributeModifier.Operation.MULTIPLY_BASE))
                .build();
        RANK_ATTRIBUTES[3] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(ExoArmory.REGISTRY.ATTRIBUTE_RANGED_STRENGTH.get(), new AttributeModifier("Default", 0.20, AttributeModifier.Operation.MULTIPLY_BASE))
                .build();
        RANK_ATTRIBUTES[4] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(ExoArmory.REGISTRY.ATTRIBUTE_RANGED_STRENGTH.get(), new AttributeModifier("Default", 0.25, AttributeModifier.Operation.MULTIPLY_BASE))
                .build();
    }

    private static final ArmoryResource RESOURCE = new AethersEmbraceResource();


    public AethersEmbraceBow() { super(); }

    public List<ArmoryAbility> getAbilities(ItemStack itemStack) {
        return switch (getRank(itemStack)) {
            case 0, 1 -> List.of(ExoArmory.REGISTRY.ABILITY_AETHER_BARRAGE);
            default -> List.of(ExoArmory.REGISTRY.ABILITY_AETHER_BARRAGE, ExoArmory.REGISTRY.ABILITY_SPECTRAL_PIERCE);
        };
    }
    public ArmoryResource getResource() { return RESOURCE; }

    public boolean isTargeting(ItemStack itemStack, Level level) {
        if (level == null) return false;

        IAethersEmbraceStorage aetherStorage = itemStack.getCapability(AethersEmbraceProvider.AETHERS_EMBRACE).resolve().orElse(null);
        if (aetherStorage == null) return false;

        return aetherStorage.getExpire() > level.getGameTime();
    }

    @Override
    public ResourceLocation getActivateIcon() {
        return iconResourcePath("bow");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        boolean serverLevel = !level.isClientSide;
        ItemStack itemStack = player.getItemInHand(hand);
        if (isTargeting(itemStack, level)) {
            IAethersEmbraceStorage aetherStorage = itemStack.getCapability(AethersEmbraceProvider.AETHERS_EMBRACE).resolve().get();
            long currentTime = level.getGameTime();
            Entity target = serverLevel ? ((ServerLevel) level).getEntity(aetherStorage.getTarget()) : ExoArmory.DIST_HELPER.getEntity(aetherStorage.getTarget());
            if (target == null || !target.isAlive()) {
                if (serverLevel) {
                    UUID uuid = getUUID(itemStack);
                    aetherStorage.setExpire(currentTime);
                    if (player instanceof ServerPlayer serverPlayer) {
                        PacketHandler.sendToPlayer(new AethersEmbraceTargetPacket(uuid, player.getInventory().selected,
                                aetherStorage.getTarget(), 0), serverPlayer);
                    }
                } else {
                    aetherStorage.setExpire(currentTime);
                }
            }
        }
        return super.use(level,player,hand);
    }

    @Override
    public void addToHover(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag, int rank, ComponentUtils.DetailLevel detail) {
        components.add(Component.literal(""));

        //Ability Info
        for (ArmoryAbility ability: getAbilities(itemStack)) {
            components.addAll(ability.getTooltip(detail, rank));
        }

        components.add(Component.literal(""));

        //Resource Info
        components.addAll(RESOURCE.getTooltip(detail, rank, itemStack));
    }

    @Override
    public Multimap<Attribute, AttributeModifier>[] getAttributesForAllRanks() { return RANK_ATTRIBUTES; }

    //region IForgeItem Overrides
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new AethersEmbraceProvider();
    }
    //endregion

    //region Item Overrides
    @Override
    public int getUseDuration(ItemStack itemStack) {
        int rank = getRank(itemStack);
        if (isTargeting(itemStack, ExoArmory.DIST_HELPER.getDefaultLevel())) {
            return (int)ExoArmory.REGISTRY.ABILITY_AETHER_BARRAGE.getStatForRank(AetherBarrageAbility.Stats.INTERVAL, rank);
        }
        return 72000;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack itemStack, int ticksLeft) {
        if (isTargeting(itemStack, level) && ticksLeft > (int)ExoArmory.REGISTRY.ABILITY_AETHER_BARRAGE.getStatForRank(AetherBarrageAbility.Stats.INTERVAL, getRank(itemStack))) {
            entity.stopUsingItem();
        }
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int ticksLeft) {
        if (!isTargeting(itemStack, level)) { super.releaseUsing(itemStack, level, entity, ticksLeft); }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof ServerPlayer player && isTargeting(itemStack, level)) {
            IAethersEmbraceStorage aetherStorage = itemStack.getCapability(AethersEmbraceProvider.AETHERS_EMBRACE).resolve().get();
            ServerLevel serverLevel = (ServerLevel)level;
            Entity target = serverLevel.getEntity(aetherStorage.getTarget());
            if (target == null) return itemStack;

            int rank = getRank(itemStack);
            double pitch = Math.toRadians(ThreadLocalRandom.current().nextDouble(150.0) + 15.0);
            double yaw = Math.toRadians(ThreadLocalRandom.current().nextDouble(360.0));
            double radius = 6;

            double yRelative = Math.sin(pitch) * radius;
            double xRelative = Math.cos(yaw) * (Math.cos(pitch) * radius);
            double zRelative = Math.sin(yaw) * radius;
            double xTarget = target.getX();
            double yTarget = target.getY() + 1;
            double zTarget = target.getZ();

            Vec3 arrowPosData = new Vec3(xTarget + xRelative, yTarget + yRelative, zTarget + zRelative);
            Arrow skyArrow = new EphemeralArrow(EntityType.ARROW, serverLevel);
            skyArrow.setPos(arrowPosData.x, arrowPosData.y, arrowPosData.z);
            skyArrow.setDeltaMovement(arrowPosData.subtract(target.position()).add(0, -1, 0).normalize().multiply(-1.75, -1.75, -1.75));

            skyArrow.setOwner(player);
            skyArrow.setBaseDamage((int)ExoArmory.REGISTRY.ABILITY_AETHER_BARRAGE.getStatForRank(AetherBarrageAbility.Stats.DAMAGE, rank));
            skyArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, .8f, 1.5f);
            serverLevel.addFreshEntity(skyArrow);
        }
        return itemStack;
    }
    //endregion
}
