package com.exosomnia.exoarmory.items.armory.swords;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.actions.UmbralAssaultAction;
import com.exosomnia.exoarmory.capabilities.resource.ArmoryResourceProvider;
import com.exosomnia.exoarmory.items.abilities.*;
import com.exosomnia.exoarmory.items.resource.ArmoryResource;
import com.exosomnia.exoarmory.items.resource.ResourcedItem;
import com.exosomnia.exoarmory.items.resource.ShadowsEdgeResource;
import com.exosomnia.exoarmory.managers.ConditionalManager;
import com.exosomnia.exoarmory.networking.PacketHandler;
import com.exosomnia.exoarmory.networking.packets.ArmoryResourcePacket;
import com.exosomnia.exolib.utils.ComponentUtils.DetailLevel;
import com.exosomnia.exolib.ExoLib;
import com.exosomnia.exolib.particles.options.RGBSParticleOptions;
import com.exosomnia.exolib.particles.shapes.ParticleShapeDome;
import com.exosomnia.exolib.particles.shapes.ParticleShapeOptions;
import com.exosomnia.exolib.utils.ColorUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
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
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShadowsEdgeSword extends ArmorySwordItem implements ResourcedItem {

    private static final ArmoryResource RESOURCE = new ShadowsEdgeResource();
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

    public ShadowsEdgeSword() { super(); }

    public List<ArmoryAbility> getAbilities(ItemStack itemStack) {
        return switch (getRank(itemStack)) {
            case 0 -> List.of(ExoArmory.REGISTRY.ABILITY_UMBRAL_ASSAULT);
            case 1, 2 -> List.of(ExoArmory.REGISTRY.ABILITY_UMBRAL_ASSAULT, ExoArmory.REGISTRY.ABILITY_VEIL_OF_DARKNESS);
            default -> List.of(ExoArmory.REGISTRY.ABILITY_UMBRAL_ASSAULT, ExoArmory.REGISTRY.ABILITY_VEIL_OF_DARKNESS, ExoArmory.REGISTRY.ABILITY_SHADOW_STRIKE);
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
    //endregion

    //region Item Overrides
    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotIndex, boolean selectedIndex) {
        if (level.isClientSide) { return; }

        if (level.getGameTime() % 10 == 0 && ExoArmory.CONDITIONAL_MANAGER.getPlayerCondition((Player)entity, ConditionalManager.Condition.SHADOWS_EDGE)) {
            LivingEntity owner = (LivingEntity) entity;
            VeilOfDarknessAbility ability = getAbility(ExoArmory.REGISTRY.ABILITY_VEIL_OF_DARKNESS, itemStack, getRank(itemStack));
            if (ability != null) {
                owner.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 50, 0, true, false, true));
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack itemStack) { return 50; }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        int rank = getRank(itemStack);

        if (ExoArmory.REGISTRY.ABILITY_UMBRAL_ASSAULT.getStatForRank(UmbralAssaultAbility.Stats.COST, rank)
            <= getResource().getResource(itemStack)) {

            if (level.isClientSide) { player.playSound(ExoArmory.REGISTRY.SOUND_DARK_AMBIENT_CHARGE.get(),
                    0.34F, 1.0F); }

            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack itemStack, int duration) {
        if (!level.isClientSide) { return; }
        if (duration % 10 == 0) {
            if (entity instanceof LocalPlayer player) {
                float[] colors = ColorUtils.intToFloats(0x705685);
                new ParticleShapeDome(new RGBSParticleOptions(ExoLib.REGISTRY.TWINKLE_PARTICLE.get(), colors[0], colors[1], colors[2], 0.15F), player.position(),
                        new ParticleShapeOptions.Dome(5.0F, 320)).playOnClient((ClientLevel) level);
            }
        }
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
        if (!level.isClientSide && entity instanceof ServerPlayer player) {
            level.playSound(null, player.blockPosition(), ExoArmory.REGISTRY.SOUND_MAGIC_CLASH.get(), SoundSource.PLAYERS, 0.34F, 1.0F);
            ExoArmory.ACTION_MANAGER.scheduleAction(new UmbralAssaultAction(ExoArmory.ACTION_MANAGER, player, 6.0, 10, 8.0), 1);
            player.getCooldowns().addCooldown(itemStack.getItem(), 600);

            int rank = getRank(itemStack);
            RESOURCE.removeResource(itemStack, ExoArmory.REGISTRY.ABILITY_UMBRAL_ASSAULT.getStatForRank(
                    UmbralAssaultAbility.Stats.COST, rank));
            PacketHandler.sendToPlayer(new ArmoryResourcePacket(getUUID(itemStack),
                    player.getInventory().selected, RESOURCE.getResource(itemStack)), player);
        }
        return itemStack;
    }
    //endregion
}
