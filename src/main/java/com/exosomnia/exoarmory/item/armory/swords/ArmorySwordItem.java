package com.exosomnia.exoarmory.item.armory.swords;

import com.exosomnia.exoarmory.Config;
import com.exosomnia.exoarmory.item.armory.ArmoryItem;
import com.exosomnia.exoarmory.item.resource.ResourcedItem;
import com.exosomnia.exoarmory.utils.ArmoryItemUtils;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public abstract class ArmorySwordItem extends SwordItem implements ArmoryItem {

    protected static final UUID BASE_CRITICAL_DAMAGE_UUID = UUID.fromString("edfb14a9-6890-43b4-bbe7-71e705b8a661");
    protected static final UUID BASE_HEALING_RECEIVED_UUID = UUID.fromString("c4b6ae00-7d2a-475c-8ea9-9f78fe143f06");

    private static final Item.Properties PROPERTIES = new Item.Properties()
            .durability(1561)
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
        int rank = ArmoryItemUtils.getRank(itemStack);
        components.add(Component.translatable("item.exoarmory.info.rank")
                .withStyle(ComponentUtils.Styles.INFO_HEADER.getStyle())
                .append(Component.literal(": ")
                        .withStyle(ComponentUtils.Styles.INFO_HEADER.getStyle().withUnderlined(false)))
                .append(Component.literal(String.valueOf(rank + 1))
                        .withStyle(ArmoryItemUtils.getRankFormatting(rank))));

        addToHover(itemStack, level, components, flag, rank, detail);

        if (!Config.hideHelp && detail == ComponentUtils.DetailLevel.BASIC) {
            components.add(ComponentUtils.formatLine(I18n.get("item.exoarmory.info.help"),
                    ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
        }
    }

    @Override
    public void writeItemNetworkData(ItemStack itemStack, FriendlyByteBuf buffer) {
        if (itemStack.getItem() instanceof ResourcedItem resourcedItem) {
            buffer.writeDouble(resourcedItem.getResource().getResourceStorage(itemStack).getCharge());
        }
    }

    @Override
    public void readItemNetworkData(ItemStack itemStack, FriendlyByteBuf buffer) {
        if (itemStack.getItem() instanceof ResourcedItem resourcedItem) {
            double charge = buffer.readDouble();
            resourcedItem.getResource().getResourceStorage(itemStack).setCharge(charge);
        }
    }

    @Override
    public boolean shouldResendData(ItemStack itemStack, ItemStack otherStack) {
        if (itemStack.getItem() instanceof ResourcedItem thisResourceItem && otherStack.getItem() instanceof ResourcedItem otherResourceItem) {
            return thisResourceItem.getResource().getResource(itemStack) != otherResourceItem.getResource().getResource(otherStack);
        }
        else return false;
    }

    @Override
    public boolean ignoreCapabilitiesForMatch() {
        return true;
    }
    //endregion
}
