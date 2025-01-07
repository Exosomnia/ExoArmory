package com.exosomnia.exoarmory.items;

import com.exosomnia.exoarmory.items.armory.ArmoryItem;
import com.exosomnia.exoarmory.utils.TooltipUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
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
        components.add(Component.translatable("item.exoarmory.info.smithing_template.line.1").withStyle(TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
        components.add(Component.translatable("item.exoarmory.info.smithing_template.line.2").withStyle(TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
        components.add(TooltipUtils.formatLine(I18n.get("item.exoarmory.info.smithing_template.line.3", rankFrom + 1, rankTo + 1), TooltipUtils.Styles.DEFAULT_DESC.getStyle(),
                ArmoryItem.getRankFormatting(rankFrom).withItalic(true), ArmoryItem.getRankFormatting(rankTo).withItalic(true)));
    }
}
