package com.exosomnia.exoarmory.items.abilities;

import com.exosomnia.exoarmory.utils.TooltipUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public class UmbralAssaultAbility extends ArmoryAbility {

    public enum Stats implements AbilityStat {
        COST,
        RADIUS,
        TARGETS,
        DAMAGE
    }

    public UmbralAssaultAbility() {
        super("umbral_assault", 0x705685);
    }

    //region ArmoryAbility Overrides
    public void buildRanks() {
        RANK_STATS.put(Stats.COST, new double[]{50.0, 50.0, 50.0, 50.0, 50.0});
        RANK_STATS.put(Stats.RADIUS, new double[]{6.0, 6.0, 6.0, 6.0, 6.0});
        RANK_STATS.put(Stats.TARGETS, new double[]{8.0, 8.0, 8.0, 8.0, 8.0});
        RANK_STATS.put(Stats.DAMAGE, new double[]{8.0, 8.0, 8.0, 8.0, 8.0});
    }

    @Override
    public List<MutableComponent> getTooltip(TooltipUtils.DetailLevel detail, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.desc.umbral_assault.line.1"), TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.desc.umbral_assault.line.2"), TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.desc.umbral_assault.line.3"), TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.stat.umbral_assault.line.1", getStatForRank(Stats.COST, rank)),
                        TooltipUtils.Styles.DEFAULT_DESC.getStyle(), TooltipUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.stat.umbral_assault.line.2", getStatForRank(Stats.RADIUS, rank)),
                        TooltipUtils.Styles.DEFAULT_DESC.getStyle(), TooltipUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.stat.umbral_assault.line.3", (int)getStatForRank(Stats.TARGETS, rank)),
                        TooltipUtils.Styles.DEFAULT_DESC.getStyle(), TooltipUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.stat.umbral_assault.line.4", getStatForRank(Stats.DAMAGE, rank)),
                        TooltipUtils.Styles.DEFAULT_DESC.getStyle(), TooltipUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
    //endregion
}
