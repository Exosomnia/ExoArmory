package com.exosomnia.exoarmory.item.armory.swords;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.capabilities.armory.item.resource.ArmoryResourceProvider;
import com.exosomnia.exoarmory.item.perks.ability.Abilities;
import com.exosomnia.exoarmory.item.perks.ability.ArmoryAbility;
import com.exosomnia.exoarmory.item.perks.ability.UmbralAssaultAbility;
import com.exosomnia.exoarmory.item.perks.resource.ArmoryResource;
import com.exosomnia.exoarmory.item.perks.resource.ResourceItem;
import com.exosomnia.exoarmory.item.perks.resource.ShadowsEdgeResource;
import com.exosomnia.exoarmory.managers.ConditionalManager;
import com.exosomnia.exoarmory.utils.AbilityItemUtils;
import com.exosomnia.exoarmory.utils.ResourceItemUtils;
import com.exosomnia.exolib.utils.ComponentUtils.DetailLevel;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShadowsEdgeSword extends ArmorySwordItem implements ResourceItem {

    private static final Object2IntLinkedOpenHashMap<ArmoryAbility>[] RANK_ABILITIES = new Object2IntLinkedOpenHashMap[5];
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

        RANK_ABILITIES[0] = AbilityItemUtils.rankBuilder()
                .addAbility(Abilities.UMBRAL_ASSAULT, 0)
                .build();
        RANK_ABILITIES[1] = AbilityItemUtils.rankBuilder()
                .addAbility(Abilities.UMBRAL_ASSAULT, 0)
                .addAbility(Abilities.VEIL_OF_DARKNESS, 0)
                .build();
        RANK_ABILITIES[2] = AbilityItemUtils.rankBuilder()
                .addAbility(Abilities.UMBRAL_ASSAULT, 1)
                .addAbility(Abilities.VEIL_OF_DARKNESS, 1)
                .build();
        RANK_ABILITIES[3] = AbilityItemUtils.rankBuilder()
                .addAbility(Abilities.UMBRAL_ASSAULT, 1)
                .addAbility(Abilities.VEIL_OF_DARKNESS, 1)
                .addAbility(Abilities.SHADOW_STRIKE, 1)
                .build();
        RANK_ABILITIES[4] = AbilityItemUtils.rankBuilder()
                .addAbility(Abilities.UMBRAL_ASSAULT, 2)
                .addAbility(Abilities.VEIL_OF_DARKNESS, 2)
                .addAbility(Abilities.SHADOW_STRIKE, 2)
                .build();
    }

    private static final ArmoryResource RESOURCE = new ShadowsEdgeResource();

    public ShadowsEdgeSword() { super(); }

    public Object2IntLinkedOpenHashMap<ArmoryAbility> getAbilities(ItemStack itemStack, LivingEntity wielder) {
        return RANK_ABILITIES[getRank(itemStack)];
    }

    public ArmoryResource getResource() { return RESOURCE; }

    public Multimap<Attribute, AttributeModifier>[] getAttributesForAllRanks() { return RANK_ATTRIBUTES; }

    public void addToHover(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag, int rank, DetailLevel detail) {
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
        return new ArmoryResourceProvider();
    }
    //endregion

    //region Item Overrides
    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotIndex, boolean selectedIndex) {
        if (level.isClientSide) { return; }

        if (level.getGameTime() % 10 == 0 && ExoArmory.CONDITIONAL_MANAGER.getPlayerCondition((Player)entity, ConditionalManager.Condition.SHADOWS_EDGE)) {
            LivingEntity owner = (LivingEntity) entity;
            if (getAbilities(itemStack, owner).containsKey(Abilities.VEIL_OF_DARKNESS)) {
                owner.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 50, 0, true, false, true));
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack itemStack) { return 30; }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        int rank = getRank(itemStack);

        if (Abilities.UMBRAL_ASSAULT.getStatForRank(UmbralAssaultAbility.Stats.COST, rank)
            <= getResource().getResource(itemStack)) {

            if (level.isClientSide) { player.playSound(ExoArmory.REGISTRY.SOUND_DARK_AMBIENT_CHARGE.get(),
                    0.34F, 1.0F); }

            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int duration) {
        if (level.isClientSide) {
            if (entity instanceof LocalPlayer) {
                Minecraft.getInstance().getSoundManager().stop(ExoArmory.REGISTRY.SOUND_DARK_AMBIENT_CHARGE.getId(), SoundSource.PLAYERS);
            }
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        if (!level.isClientSide) {
            if (entity instanceof ServerPlayer player) {
                level.playSound(null, player.blockPosition(), ExoArmory.REGISTRY.SOUND_MAGIC_CLASH.get(), SoundSource.PLAYERS, 0.34F, 1.0F);
                player.getCooldowns().addCooldown(itemStack.getItem(), 300);
            }

            UmbralAssaultAbility ability = Abilities.UMBRAL_ASSAULT;
            int rank = AbilityItemUtils.getAbilityRank(ability, itemStack, entity);
            ResourceItemUtils.spendChargeOn(itemStack,
                    ability.getStatForRank(UmbralAssaultAbility.Stats.COST, rank),
                    () -> entity.addEffect(new MobEffectInstance(ExoArmory.REGISTRY.EFFECT_UMBRAL_ASSAULT.get(),
                            (int)(ability.getStatForRank(UmbralAssaultAbility.Stats.DURATION, rank) * 20.0), rank)));
        }
        return itemStack;
    }
    //endregion
}
