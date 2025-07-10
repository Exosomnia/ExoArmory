package com.exosomnia.exoarmory.item.armory.swords;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.actions.FrigidFlurryAction;
import com.exosomnia.exoarmory.capabilities.armory.item.resource.ArmoryResourceProvider;
import com.exosomnia.exoarmory.entities.projectiles.GenericProjectile;
import com.exosomnia.exoarmory.item.ActivatableItem;
import com.exosomnia.exoarmory.item.perks.ability.Abilities;
import com.exosomnia.exoarmory.item.perks.ability.ArmoryAbility;
import com.exosomnia.exoarmory.item.perks.ability.FrigidFlurryAbility;
import com.exosomnia.exoarmory.item.perks.resource.ArmoryResource;
import com.exosomnia.exoarmory.item.perks.resource.FrostbiteResource;
import com.exosomnia.exoarmory.item.perks.resource.ResourceItem;
import com.exosomnia.exoarmory.utils.AbilityItemUtils;
import com.exosomnia.exoarmory.utils.ArmoryItemUtils;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FrostbiteSword extends ArmorySwordItem implements ResourceItem, ActivatableItem {

    private static final Object2IntLinkedOpenHashMap<ArmoryAbility>[] RANK_ABILITIES = new Object2IntLinkedOpenHashMap[5];
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

        RANK_ABILITIES[0] = AbilityItemUtils.rankBuilder()
                .addAbility(Abilities.FRIGID_FLURRY, 0)
                .build();
        RANK_ABILITIES[1] = AbilityItemUtils.rankBuilder()
                .addAbility(Abilities.FRIGID_FLURRY, 0)
                .addAbility(Abilities.COLD_SNAP, 0)
                .build();
        RANK_ABILITIES[2] = AbilityItemUtils.rankBuilder()
                .addAbility(Abilities.FRIGID_FLURRY, 1)
                .addAbility(Abilities.COLD_SNAP, 1)
                .build();
        RANK_ABILITIES[3] = AbilityItemUtils.rankBuilder()
                .addAbility(Abilities.FRIGID_FLURRY, 1)
                .addAbility(Abilities.COLD_SNAP, 1)
                .addAbility(Abilities.BLIZZARD, 1)
                .build();
        RANK_ABILITIES[4] = AbilityItemUtils.rankBuilder()
                .addAbility(Abilities.FRIGID_FLURRY, 2)
                .addAbility(Abilities.COLD_SNAP, 2)
                .addAbility(Abilities.BLIZZARD, 2)
                .build();
    }

    private static final ArmoryResource RESOURCE = new FrostbiteResource();


    public FrostbiteSword() {
        super();
    }

    public Object2IntLinkedOpenHashMap<ArmoryAbility> getAbilities(ItemStack itemStack, LivingEntity wielder) {
        return RANK_ABILITIES[ArmoryItemUtils.getRank(itemStack)];
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
        Player player = ExoArmory.DIST_HELPER.getDefaultPlayer();
        for (ArmoryAbility ability : getAbilities(itemStack, ExoArmory.DIST_HELPER.getDefaultPlayer()).keySet()) {
            components.addAll(ability.getTooltip(detail, itemStack, AbilityItemUtils.getAbilityRank(ability, itemStack, player)));
        }

        components.add(Component.literal(""));

        //Resource Info
        components.addAll(RESOURCE.getTooltip(detail, rank, itemStack));
    }

    //region IForgeItem Overrides
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ArmoryResourceProvider(nbt);
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
                Abilities.FRIGID_FLURRY.getStatForRank(FrigidFlurryAbility.Stats.COST, getRank(itemStack))) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemStack);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        GenericProjectile projectile = new GenericProjectile(player, level, 0.0);
        projectile.setItem(new ItemStack(Items.PACKED_ICE));
        projectile.getPersistentData().putBoolean("FROSTBITE", true);
        Vec3 looking = player.getLookAngle();
        projectile.shoot(looking.x, looking.y, looking.z, 2.5F, 1.0F);
        level.addFreshEntity(projectile);

        player.getCooldowns().addCooldown(this, (int)Abilities.FRIGID_FLURRY.getStatForRank(FrigidFlurryAbility.Stats.COOLDOWN,
                getRank(itemStack)) * 20);

        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int ticksLeft) {
        if (entity instanceof ServerPlayer player && ticksLeft <= 71980 && RESOURCE.getResource(itemStack) >=
                Abilities.FRIGID_FLURRY.getStatForRank(FrigidFlurryAbility.Stats.COST, getRank(itemStack))) {
            ExoArmory.ACTION_MANAGER.scheduleAction(new FrigidFlurryAction(ExoArmory.ACTION_MANAGER, entity, 40, Abilities.FRIGID_FLURRY.getStatForRank(FrigidFlurryAbility.Stats.DAMAGE, getRank(itemStack))), 1);
            player.getCooldowns().addCooldown(this, (int)Abilities.FRIGID_FLURRY.getStatForRank(FrigidFlurryAbility.Stats.COOLDOWN,
                    getRank(itemStack)) * 20);

            RESOURCE.removeResource(itemStack, Abilities.FRIGID_FLURRY.getStatForRank(FrigidFlurryAbility.Stats.COST, getRank(itemStack)));
        }
    }
    //endregion
}
