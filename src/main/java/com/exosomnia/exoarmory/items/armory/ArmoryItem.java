package com.exosomnia.exoarmory.items.armory;

import com.exosomnia.exoarmory.Config;
import com.exosomnia.exoarmory.utils.TooltipUtils;
import com.exosomnia.exoarmory.utils.TooltipUtils.DetailLevel;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public abstract class ArmoryItem extends Item {

    protected static final UUID BASE_MOVEMENT_SPEED_UUID = UUID.fromString("6024bfa5-cd10-4ccb-bfa6-bc8255087ee4");

    protected final Multimap<Attribute, AttributeModifier>[] RANK_ATTRIBUTES = new Multimap[5];

    public ArmoryItem(Properties properties) {
        super(properties);
        buildRanks();
    }

    //region IForgeItem Overrides
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !ItemStack.isSameItem(oldStack, newStack);
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return !ItemStack.isSameItem(oldStack, newStack);
    }
    //endregion

    //region Item Overrides
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(itemStack, level, components, flag);

        DetailLevel detail = DetailLevel.BASIC;
        detail = Screen.hasShiftDown() ? DetailLevel.DESCRIPTION : detail;
        detail = Screen.hasControlDown() ? DetailLevel.STATISTICS : detail;

        //Add rank info
        int rank = getRank(itemStack);
        components.add(Component.translatable("item.exoarmory.info.rank")
                        .withStyle(TooltipUtils.Styles.INFO_HEADER.getStyle())
                .append(Component.literal(": ")
                        .withStyle(TooltipUtils.Styles.INFO_HEADER.getStyle().withUnderlined(false)))
                .append(Component.literal(String.valueOf(rank + 1))
                        .withStyle(getRankFormatting(rank))));

        appendTooltip(itemStack, level, components, flag, rank, detail);

        if (!Config.hideHelp && detail == DetailLevel.BASIC) {
            components.add(TooltipUtils.formatLine(I18n.get("item.exoarmory.info.help"),
                    TooltipUtils.Styles.DEFAULT_DESC.getStyle(), TooltipUtils.Styles.HIGHLIGHT_DESC.getStyle()));
        }
    }
    //endregion

    public abstract void appendTooltip(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag, int rank, DetailLevel detail);

    public abstract void buildRanks();

    public UUID getUUID(ItemStack itemStack) {
        UUID itemUUID;
        CompoundTag itemTags = itemStack.getTag();
        if (!itemTags.hasUUID("UUID")) { itemTags.putUUID("UUID", itemUUID = UUID.randomUUID()); }
        else { itemUUID = itemTags.getUUID("UUID"); }
        return itemUUID;
    }

    public int getRank(ItemStack itemStack) {
        return itemStack.getTag().getInt("Rank");
    }

    public void setRank(ItemStack itemStack, int rank) {
        itemStack.getTag().putInt("Rank", rank);
    }

    public static Style getRankFormatting(int rank) {
        return switch (rank) {
            case 0 -> TooltipUtils.Styles.BLANK.getStyle().withColor(ChatFormatting.GRAY);
            case 1 -> TooltipUtils.Styles.BLANK.getStyle().withColor(ChatFormatting.GREEN);
            case 2 -> TooltipUtils.Styles.BLANK.getStyle().withColor(ChatFormatting.AQUA);
            case 3 -> TooltipUtils.Styles.BLANK.getStyle().withColor(ChatFormatting.LIGHT_PURPLE);
            case 4 -> TooltipUtils.Styles.BLANK.getStyle().withColor(ChatFormatting.RED);
            default -> TooltipUtils.Styles.BLANK.getStyle().withColor(ChatFormatting.DARK_RED).withBold(true).withObfuscated(true);
        };
    }
}
