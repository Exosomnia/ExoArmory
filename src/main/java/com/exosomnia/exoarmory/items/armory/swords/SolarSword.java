package com.exosomnia.exoarmory.items.armory.swords;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.items.abilities.ArmoryAbility;
import com.exosomnia.exoarmory.capabilities.resource.ArmoryResourceProvider;
import com.exosomnia.exoarmory.items.armory.ResourcedItem;
import com.exosomnia.exoarmory.managers.ConditionalManager;
import com.exosomnia.exoarmory.networking.PacketHandler;
import com.exosomnia.exoarmory.networking.packets.ArmoryResourcePacket;
import com.exosomnia.exoarmory.utils.TooltipUtils;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
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
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mod.EventBusSubscriber(modid = ExoArmory.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SolarSword extends SwordArmoryItem implements ResourcedItem {

    private static final ArmoryAbility[] ABILITIES = {ExoArmory.REGISTRY.ABILITY_SOLAR_FLARE,
            ExoArmory.REGISTRY.ABILITY_SUNFIRE_SURGE};

    private static final float[] BAR_RGB = {1.0F, 0.5F, 0.0F};
    private static final double RESOURCE_MAX = 500.0;

    private static final Item.Properties itemProperties = new Item.Properties()
            .durability(782)
            .rarity(Rarity.UNCOMMON);


    public SolarSword() {
        super(itemProperties);
    }

    public float[] getBarRGB() {
        return BAR_RGB;
    }
    public double getResource(ItemStack itemStack) { return itemStack.getCapability(ArmoryResourceProvider.ARMORY_RESOURCE).resolve().get().getCharge(); }
    public double getResourceMax() { return RESOURCE_MAX; }

    public ArmoryAbility[] getAbilities() { return ABILITIES; }

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
        boolean detailed = Screen.hasShiftDown();
        components.add(Component.literal(""));

        //Ability Info
        for (ArmoryAbility ability: ABILITIES) {
            components.addAll(ability.getTooltip(detailed, getRank(itemStack)));
        }

        components.add(Component.literal(""));

        //Resource Info
        components.add(Component.translatable("resource.exoarmory.name.solar_sword")
                        .withStyle(TooltipUtils.Styles.INFO_HEADER.getStyle())
                .append(Component.literal(": ")
                        .withStyle(TooltipUtils.Styles.INFO_HEADER.getStyle().withUnderlined(false)))
                .append(Component.literal(String.valueOf((int)getResource(itemStack)))
                        .withStyle(TooltipUtils.Styles.BLANK.getStyle().withColor(ChatFormatting.AQUA)))
                .append(Component.literal("/")
                        .withStyle(TooltipUtils.Styles.BLANK.getStyle().withColor(ChatFormatting.GRAY)))
                .append(Component.literal(String.valueOf((int)RESOURCE_MAX))
                        .withStyle(TooltipUtils.Styles.BLANK.getStyle().withColor(ChatFormatting.AQUA))));

        if (Screen.hasShiftDown()) {
            //components.add(7, getResourceDesc().withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        }

    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotIndex, boolean selectedIndex) {
        if (level.isClientSide) { return; }

        if (level.getGameTime() % 10 == 0 && ConditionalManager.SOLAR_CONDITIONS.get((Player)entity)) {
            itemStack.getCapability(ArmoryResourceProvider.ARMORY_RESOURCE).ifPresent(iArmoryResourceStorage -> {
                iArmoryResourceStorage.addCharge(0.5, RESOURCE_MAX);
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
}

