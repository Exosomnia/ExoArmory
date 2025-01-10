package com.exosomnia.exoarmory.items.abilities;

import com.exosomnia.exoarmory.utils.TooltipUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public class ShadowStrikeAbility extends ArmoryAbility {

    public enum Stats implements AbilityStat {
        DAMAGE,
        DURATION
    }

    public ShadowStrikeAbility() {
        super("shadow_strike", 0x686673);
    }

    //region ArmoryAbility Overrides
    public void buildRanks() {
        RANK_STATS.put(Stats.DAMAGE, new double[]{0.0, 0.0, 0.0, 1.0, 2.0});
        RANK_STATS.put(Stats.DURATION, new double[]{0.0, 0.0, 0.0, 10.0, 10.0});
    }

    @Override
    public List<MutableComponent> getTooltip(TooltipUtils.DetailLevel detail, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.desc.shadow_strike.line.1"), TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.desc.shadow_strike.line.2"), TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.desc.shadow_strike.line.3"), TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.stat.shadow_strike.line.1", (int)getStatForRank(Stats.DAMAGE, rank)),
                        TooltipUtils.Styles.DEFAULT_DESC.getStyle(), TooltipUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.stat.shadow_strike.line.2", (int)getStatForRank(Stats.DURATION, rank)),
                        TooltipUtils.Styles.DEFAULT_DESC.getStyle(), TooltipUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
    //endregion
}
