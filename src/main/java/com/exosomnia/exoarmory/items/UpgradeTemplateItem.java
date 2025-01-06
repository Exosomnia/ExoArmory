package com.exosomnia.exoarmory.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class UpgradeTemplateItem extends Item {

    public final int rankTo;
    public final int rankFrom;
    public final Style INFO_STYLE = Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true);

    public UpgradeTemplateItem(int rankTo, Properties properties) {
        super(properties);
        this.rankTo = rankTo;
        this.rankFrom = rankTo - 1;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("item.exoarmory.info.smithing_template.line.1").withStyle(INFO_STYLE));
        components.add(Component.translatable("item.exoarmory.info.smithing_template.line.2").withStyle(INFO_STYLE));
        components.add(Component.translatable("item.exoarmory.info.smithing_template.line.3.1").withStyle(INFO_STYLE)
                //+1s to rank display because indexes start at 0
                .append(Component.literal(String.valueOf(rankFrom + 1)).withStyle(ArmoryItem.getRankFormatting(rankFrom).withItalic(true)))
                .append(Component.translatable("item.exoarmory.info.smithing_template.line.3.2").withStyle(INFO_STYLE))
                .append(Component.literal(String.valueOf(rankTo + 1)).withStyle(ArmoryItem.getRankFormatting(rankTo).withItalic(true))));
    }
}
