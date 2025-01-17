package com.exosomnia.exoarmory.items.armory.swords;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.actions.FrigidFlurryAction;
import com.exosomnia.exoarmory.capabilities.resource.ArmoryResourceProvider;
import com.exosomnia.exoarmory.entities.GenericProjectile;
import com.exosomnia.exoarmory.items.abilities.ArmoryAbility;
import com.exosomnia.exoarmory.items.abilities.FrigidFlurryAbility;
import com.exosomnia.exoarmory.items.resource.ArmoryResource;
import com.exosomnia.exoarmory.items.resource.FrostbiteResource;
import com.exosomnia.exoarmory.items.resource.ResourcedItem;
import com.exosomnia.exoarmory.networking.PacketHandler;
import com.exosomnia.exoarmory.networking.packets.ArmoryResourcePacket;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FrostbiteSword extends SwordArmoryItem implements ResourcedItem {

    private static final ArmoryResource RESOURCE = new FrostbiteResource();

    private static final Item.Properties itemProperties = new Item.Properties()
            .durability(782)
            .rarity(Rarity.UNCOMMON);


    public FrostbiteSword() {
        super(itemProperties);
    }

    public List<ArmoryAbility> getAbilities(ItemStack itemStack) {
        return switch (getRank(itemStack)) {
            case 0, 1 -> List.of(ExoArmory.REGISTRY.ABILITY_FRIGID_FLURRY);
            default -> List.of(ExoArmory.REGISTRY.ABILITY_FRIGID_FLURRY, ExoArmory.REGISTRY.ABILITY_COLD_SNAP);
        };
    }
    public ArmoryResource getResource() { return RESOURCE; }

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

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag, int rank, ComponentUtils.DetailLevel detail) {
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
        if (Screen.hasAltDown() && RESOURCE.getResource(itemStack) >=
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

        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int ticksLeft) {
        if (!level.isClientSide && entity instanceof ServerPlayer player && ticksLeft <= 71980) {
            ExoArmory.ACTION_MANAGER.scheduleAction(new FrigidFlurryAction(ExoArmory.ACTION_MANAGER, entity, 40, ExoArmory.REGISTRY.ABILITY_FRIGID_FLURRY.getStatForRank(FrigidFlurryAbility.Stats.DAMAGE, getRank(itemStack))), 1);
            player.getCooldowns().addCooldown(this, (int)ExoArmory.REGISTRY.ABILITY_FRIGID_FLURRY.getStatForRank(FrigidFlurryAbility.Stats.COOLDOWN,
                    getRank(itemStack)) * 20);

            RESOURCE.removeResource(itemStack, ExoArmory.REGISTRY.ABILITY_FRIGID_FLURRY.getStatForRank(FrigidFlurryAbility.Stats.COST, getRank(itemStack)));
            PacketHandler.sendToPlayer(new ArmoryResourcePacket(getUUID(itemStack),
                    player.getInventory().selected, RESOURCE.getResource(itemStack)), player);
        }
    }
    //endregion
}
