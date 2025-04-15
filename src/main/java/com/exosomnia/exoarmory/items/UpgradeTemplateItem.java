package com.exosomnia.exoarmory.items;

import com.exosomnia.exoarmory.items.armory.ArmoryItem;
import com.exosomnia.exoarmory.utils.ArmoryUtils;
import com.exosomnia.exolib.utils.ComponentUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class UpgradeTemplateItem extends Item {

    public final int rankTo;
    public final int rankFrom;
    private static final Item[] rankUpMaterials = new Item[]{Items.DIAMOND, Items.NETHERITE_INGOT, Items.NETHER_STAR, Items.DRAGON_HEAD};

    public UpgradeTemplateItem(int rankTo, Properties properties) {
        super(properties);
        this.rankTo = rankTo;
        this.rankFrom = rankTo - 1;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("item.exoarmory.info.smithing_template.line.1").withStyle(ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
        components.add(Component.translatable("item.exoarmory.info.smithing_template.line.2").withStyle(ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
        components.add(ComponentUtils.formatLine(I18n.get("item.exoarmory.info.smithing_template.line.3", rankFrom + 1, rankTo + 1), ComponentUtils.Styles.DEFAULT_DESC.getStyle(),
                ArmoryUtils.getRankFormatting(rankFrom).withItalic(true), ArmoryUtils.getRankFormatting(rankTo).withItalic(true)));
        components.add(ComponentUtils.formatLine(I18n.get("item.exoarmory.info.smithing_template.line.4", rankUpMaterials[rankFrom].getDescription().getString()),
                ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.INFO_HEADER.getStyle(), ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
    }
}
