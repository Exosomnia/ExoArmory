package com.exosomnia.exoarmory.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ToolAction;

import java.util.UUID;

public class NetheriteAnchorItem extends SwordItem {

    private static final UUID ATTACK_KNOCKBACK_UUID = UUID.fromString("c1ca907b-adf6-4b00-b412-8343e8b3d39d");

    private static final Item.Properties PROPERTIES = new Item.Properties()
            .durability(1564)
            .rarity(Rarity.UNCOMMON);

    public NetheriteAnchorItem() {
        super(Tiers.DIAMOND, 8, -3.0F, PROPERTIES);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemStack) {
        return (slot == EquipmentSlot.MAINHAND) ? ImmutableMultimap.of(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 11.0, AttributeModifier.Operation.ADDITION),
                Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -3.0, AttributeModifier.Operation.ADDITION),
                Attributes.ATTACK_KNOCKBACK, new AttributeModifier(ATTACK_KNOCKBACK_UUID, "Default", 0.5, AttributeModifier.Operation.ADDITION)) : ImmutableMultimap.of();
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return false;
    }
}
