package com.exosomnia.exoarmory.utils;

import com.exosomnia.exoarmory.capabilities.armory.item.ArmoryItemProvider;
import com.exosomnia.exoarmory.capabilities.armory.item.ArmoryItemStorage;
import com.exosomnia.exoarmory.capabilities.armory.item.resource.ArmoryResourceProvider;
import com.exosomnia.exoarmory.item.armory.ArmoryItem;
import com.exosomnia.exolib.utils.ComponentUtils.Styles;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ArmoryItemUtils {

    private record AttributeRecord(EquipmentSlot slot, Attribute attribute, AttributeModifier.Operation operation) {}
    private static final Map<AttributeRecord, UUID> ARMORY_ATTRIBUTE_UUIDS = new HashMap<>();
    static {
        ARMORY_ATTRIBUTE_UUIDS.putAll(Map.of(
                new AttributeRecord(EquipmentSlot.MAINHAND, Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADDITION),
                UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF"),
                new AttributeRecord(EquipmentSlot.MAINHAND, Attributes.ATTACK_SPEED, AttributeModifier.Operation.ADDITION),
                UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3"))
        );
    }

    public static UUID getDefaultAttributeUUID(EquipmentSlot slot, Attribute attribute, AttributeModifier.Operation operation) {
        AttributeRecord record = new AttributeRecord(slot, attribute, operation);
        UUID uuid = ARMORY_ATTRIBUTE_UUIDS.get(record);
        if (uuid == null) {
            uuid = UUID.randomUUID();
            ARMORY_ATTRIBUTE_UUIDS.put(record, uuid);
        }
        return uuid;
    }

    public static int getRank(ItemStack itemStack) {
        Optional<ArmoryItemStorage> itemCapability = itemStack.getCapability(ArmoryItemProvider.ARMORY_ITEM).resolve();
        return itemCapability.isPresent() ? itemCapability.get().getRank() : 0;
    }

    public static UUID getUUID(ItemStack itemStack) {
        Optional<ArmoryItemStorage> itemCapability = itemStack.getCapability(ArmoryItemProvider.ARMORY_ITEM).resolve();
        return itemCapability.isPresent() ? itemCapability.get().getUUID() : null;
    }

    public static void setRank(ItemStack itemStack, int rank) {
        itemStack.getCapability(ArmoryItemProvider.ARMORY_ITEM).resolve().ifPresent(armoryCapability ->
                armoryCapability.setRank(rank));
    }

    public static Style getRankFormatting(int rank) {
        return switch (rank) {
            case 0 -> Styles.BLANK.getStyle().withColor(ChatFormatting.GRAY);
            case 1 -> Styles.BLANK.getStyle().withColor(ChatFormatting.GREEN);
            case 2 -> Styles.BLANK.getStyle().withColor(ChatFormatting.AQUA);
            case 3 -> Styles.BLANK.getStyle().withColor(ChatFormatting.LIGHT_PURPLE);
            case 4 -> Styles.BLANK.getStyle().withColor(ChatFormatting.RED);
            default -> Styles.BLANK.getStyle().withColor(ChatFormatting.DARK_RED).withBold(true).withObfuscated(true);
        };
    }
}
