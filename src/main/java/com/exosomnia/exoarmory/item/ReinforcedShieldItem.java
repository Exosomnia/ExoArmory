package com.exosomnia.exoarmory.item;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.Registry;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;

public class ReinforcedShieldItem extends ShieldItem {

    private final double shieldStability;
    private final double passiveBlock;
    private final double armor;
    private final Item repairItem;

    public ReinforcedShieldItem(int durability, double shieldStability, double passiveBlock, int armor, Item repairItem) {
        super(new Item.Properties().durability(durability));
        this.shieldStability = shieldStability;
        this.passiveBlock = passiveBlock;
        this.armor = armor;
        this.repairItem = repairItem;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemStack) {
        Registry registry = ExoArmory.REGISTRY;
        return switch(slot) {
            case MAINHAND -> ImmutableMultimap.of(registry.ATTRIBUTE_SHIELD_STABILITY.get(), new AttributeModifier(registry.SHIELD_STABILITY_UUID, "Default", shieldStability, AttributeModifier.Operation.ADDITION),
                    registry.ATTRIBUTE_PASSIVE_BLOCK.get(), new AttributeModifier(registry.PASSIVE_BLOCK_UUID, "Default", passiveBlock, AttributeModifier.Operation.MULTIPLY_BASE),
                    Attributes.ARMOR, new AttributeModifier(registry.SHIELD_ARMOR_UUID, "Default", armor, AttributeModifier.Operation.ADDITION));
            case OFFHAND -> ImmutableMultimap.of(registry.ATTRIBUTE_SHIELD_STABILITY.get(), new AttributeModifier(registry.SHIELD_STABILITY_UUID, "Default", shieldStability, AttributeModifier.Operation.ADDITION),
                    registry.ATTRIBUTE_PASSIVE_BLOCK.get(), new AttributeModifier(registry.PASSIVE_BLOCK_UUID, "Default", passiveBlock, AttributeModifier.Operation.MULTIPLY_BASE),
                    Attributes.ARMOR, new AttributeModifier(registry.OFF_HAND_SHIELD_ARMOR_UUID, "Default", armor, AttributeModifier.Operation.ADDITION));
            default -> ImmutableMultimap.of();
        };
    }

    @Override
    public boolean isValidRepairItem(ItemStack itemStack, ItemStack repairStack) {
        return repairStack.is(repairItem) || super.isValidRepairItem(itemStack, repairStack);
    }
}
