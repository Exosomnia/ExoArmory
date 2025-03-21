package com.exosomnia.exoarmory.items.armory.swords;

import com.exosomnia.exoarmory.Config;
import com.exosomnia.exoarmory.items.armory.ArmoryItem;
import com.exosomnia.exoarmory.utils.ArmoryUtils;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ArmorySwordItem extends SwordItem implements ArmoryItem {

    private static final Item.Properties PROPERTIES = new Item.Properties()
            .durability(782)
            .rarity(Rarity.UNCOMMON);

    public ArmorySwordItem() {
        super(Tiers.DIAMOND, 4, -2.4F, PROPERTIES);
    }

    //region IForgeItem Overrides
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemStack) {
        return slot == EquipmentSlot.MAINHAND ? getAttributesForRank(getRank(itemStack)) : ImmutableMultimap.of();
    }
    //endregion

    //region SwordItem Overrides
    @Override
    public float getDamage() {
        float damage = 0.0F;
        float multiply = 1.0F;
        for (AttributeModifier mod : this.getAttributesForRank(0).get(Attributes.ATTACK_DAMAGE)) {
            if (mod.getOperation().equals(AttributeModifier.Operation.ADDITION)) { damage += (float)mod.getAmount(); }
            else { multiply += (float)mod.getAmount(); }
        }
        return damage * multiply;
    }
    //endregion

    //region Item Overrides
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        ComponentUtils.DetailLevel detail = ComponentUtils.DetailLevel.BASIC;
        detail = Screen.hasShiftDown() ? ComponentUtils.DetailLevel.DESCRIPTION : detail;
        detail = Screen.hasControlDown() ? ComponentUtils.DetailLevel.STATISTICS : detail;

        //Add rank info
        int rank = getRank(itemStack);
        components.add(Component.translatable("item.exoarmory.info.rank")
                .withStyle(ComponentUtils.Styles.INFO_HEADER.getStyle())
                .append(Component.literal(": ")
                        .withStyle(ComponentUtils.Styles.INFO_HEADER.getStyle().withUnderlined(false)))
                .append(Component.literal(String.valueOf(rank + 1))
                        .withStyle(ArmoryUtils.getRankFormatting(rank))));

        addToHover(itemStack, level, components, flag, rank, detail);

        if (!Config.hideHelp && detail == ComponentUtils.DetailLevel.BASIC) {
            components.add(ComponentUtils.formatLine(I18n.get("item.exoarmory.info.help"),
                    ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
        }
    }
    //endregion
}
