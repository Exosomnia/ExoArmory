package com.exosomnia.exoarmory.items;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ReinforcedBowItem extends BowItem {

    private static final UUID RANGED_STRENGTH_UUID = UUID.fromString("4477081e-59b2-4cba-9613-07ac68cd0d5b");
    private final double strength;
    private final double pierce;

    public ReinforcedBowItem(Item.Properties properties, double strength, double pierce) {
        super(new Item.Properties().durability(1152));
        this.strength = strength;
        this.pierce = pierce;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemStack) {
        return slot == EquipmentSlot.MAINHAND ? ImmutableMultimap.of(ExoArmory.REGISTRY.ATTRIBUTE_RANGED_STRENGTH.get(),
                new AttributeModifier(RANGED_STRENGTH_UUID, "Default", strength, AttributeModifier.Operation.MULTIPLY_BASE)) : ImmutableMultimap.of();
    }

    public double getPierceChance() { return pierce; }

    @Override
    public int getEnchantmentValue() {
        return 6;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (pierce > 0) {
            components.add(ComponentUtils.formatLine(I18n.get("item.exoarmory.reinforced_bow.info.1", (float)(pierce * 100.0)),
                    ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
        }
    }
}
