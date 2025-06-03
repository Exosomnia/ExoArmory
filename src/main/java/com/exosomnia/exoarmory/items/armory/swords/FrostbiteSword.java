package com.exosomnia.exoarmory.items.armory.swords;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.actions.FrigidFlurryAction;
import com.exosomnia.exoarmory.capabilities.resource.ArmoryResourceProvider;
import com.exosomnia.exoarmory.entities.projectiles.GenericProjectile;
import com.exosomnia.exoarmory.items.ActivatableItem;
import com.exosomnia.exoarmory.items.abilities.ArmoryAbility;
import com.exosomnia.exoarmory.items.abilities.FrigidFlurryAbility;
import com.exosomnia.exoarmory.items.resource.ArmoryResource;
import com.exosomnia.exoarmory.items.resource.FrostbiteResource;
import com.exosomnia.exoarmory.items.resource.ResourcedItem;
import com.exosomnia.exoarmory.networking.PacketHandler;
import com.exosomnia.exoarmory.networking.packets.ArmoryResourcePacket;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FrostbiteSword extends ArmorySwordItem implements ResourcedItem, ActivatableItem {

    private static final Multimap<Attribute, AttributeModifier>[] RANK_ATTRIBUTES = new Multimap[5];
    static {
        RANK_ATTRIBUTES[0] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 5.0, Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.4, Operation.ADDITION))
                .build();
        RANK_ATTRIBUTES[1] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 6.0, Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.4, Operation.ADDITION))
                .build();
        RANK_ATTRIBUTES[2] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 7.0, Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.4, Operation.ADDITION))
                .build();
        RANK_ATTRIBUTES[3] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 8.0, Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.4, Operation.ADDITION))
                .build();
        RANK_ATTRIBUTES[4] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 9.0, Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.4, Operation.ADDITION))
                .build();
    }

    private static final ArmoryResource RESOURCE = new FrostbiteResource();


    public FrostbiteSword() {
        super();
    }

    public List<ArmoryAbility> getAbilities(ItemStack itemStack) {
        return switch (getRank(itemStack)) {
            case 0, 1 -> List.of(ExoArmory.REGISTRY.ABILITY_FRIGID_FLURRY);
            default -> List.of(ExoArmory.REGISTRY.ABILITY_FRIGID_FLURRY, ExoArmory.REGISTRY.ABILITY_COLD_SNAP);
        };
    }
    public ArmoryResource getResource() { return RESOURCE; }
    @Override
    public ResourceLocation getActivateIcon() {
        return iconResourcePath("sword");
    }

    public Multimap<Attribute, AttributeModifier>[] getAttributesForAllRanks() { return RANK_ATTRIBUTES; }

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

    //region IForgeItem Overrides
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        ArmoryResourceProvider resourceProvider = new ArmoryResourceProvider();
        if (nbt == null) return resourceProvider;
        resourceProvider.deserializeNBT(nbt);
        return resourceProvider;
    }

    //TODO: DEBUGGING
    @Nullable
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag shareTag = stack.getTag();
        shareTag.putDouble("ArmoryResource", getResource().getResource(stack));
        return shareTag;
    }
    //endregion

    //region Item Overrides
    @Override
    public int getUseDuration(ItemStack itemStack) { return 72000; }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (ExoArmory.ABILITY_MANAGER.isPlayerActive(player) && RESOURCE.getResource(itemStack) >=
                ExoArmory.REGISTRY.ABILITY_FRIGID_FLURRY.getStatForRank(FrigidFlurryAbility.Stats.COST, getRank(itemStack))) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemStack);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        GenericProjectile projectile = new GenericProjectile(player, level, 0.0);
        projectile.setItem(new ItemStack(Items.PACKED_ICE));
        projectile.getPersistentData().putBoolean("FROSTBITE", true);
        Vec3 looking = player.getLookAngle();
        projectile.shoot(looking.x, looking.y, looking.z, 2.0F, 1.0F);
        level.addFreshEntity(projectile);

        player.getCooldowns().addCooldown(this, (int)ExoArmory.REGISTRY.ABILITY_FRIGID_FLURRY.getStatForRank(FrigidFlurryAbility.Stats.COOLDOWN,
                getRank(itemStack)) * 20);

        if (player instanceof ServerPlayer serverPlayer) {
            PacketHandler.sendToPlayer(new ArmoryResourcePacket(getUUID(itemStack), RESOURCE.getResource(itemStack)), serverPlayer);
        }

        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int ticksLeft) {
        if (entity instanceof ServerPlayer player && ticksLeft <= 71980 && RESOURCE.getResource(itemStack) >=
                ExoArmory.REGISTRY.ABILITY_FRIGID_FLURRY.getStatForRank(FrigidFlurryAbility.Stats.COST, getRank(itemStack))) {
            ExoArmory.ACTION_MANAGER.scheduleAction(new FrigidFlurryAction(ExoArmory.ACTION_MANAGER, entity, 40, ExoArmory.REGISTRY.ABILITY_FRIGID_FLURRY.getStatForRank(FrigidFlurryAbility.Stats.DAMAGE, getRank(itemStack))), 1);
            player.getCooldowns().addCooldown(this, (int)ExoArmory.REGISTRY.ABILITY_FRIGID_FLURRY.getStatForRank(FrigidFlurryAbility.Stats.COOLDOWN,
                    getRank(itemStack)) * 20);

            RESOURCE.removeResource(itemStack, ExoArmory.REGISTRY.ABILITY_FRIGID_FLURRY.getStatForRank(FrigidFlurryAbility.Stats.COST, getRank(itemStack)));
            PacketHandler.sendToPlayer(new ArmoryResourcePacket(getUUID(itemStack), RESOURCE.getResource(itemStack)), player);
        }
    }
    //endregion
}
