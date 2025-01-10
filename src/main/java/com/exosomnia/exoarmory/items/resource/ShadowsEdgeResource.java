package com.exosomnia.exoarmory.items.resource;

import com.exosomnia.exoarmory.utils.TooltipUtils;
import com.exosomnia.exoarmory.utils.TooltipUtils.DetailLevel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShadowsEdgeResource extends ArmoryResource {

    public enum Stats implements ResourceStat {
        CHARGE
    }

    public ShadowsEdgeResource() {
        super("shadows_edge", 0x705685, 25.0);
    }

    public void buildRanks() {
        RANK_STATS.put(Stats.CHARGE, new double[]{1.0, 1.0, 1.0, 1.0, 1.0});
    }

    @Override
    public List<MutableComponent> getTooltip(DetailLevel detail, int rank, ItemStack itemStack) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank, itemStack));

        switch (detail) {
            case DESCRIPTION:
                description.add(TooltipUtils.formatLine(I18n.get("resource.exoarmory.desc.shadows_edge.line.1"), TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(TooltipUtils.formatLine(I18n.get("resource.exoarmory.desc.shadows_edge.line.2"), TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(TooltipUtils.formatLine(I18n.get("resource.exoarmory.stat.shadows_edge.line.1", getStatForRank(Stats.CHARGE, rank)),
                        TooltipUtils.Styles.DEFAULT_DESC.getStyle(), TooltipUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
}
