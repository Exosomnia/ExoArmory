package com.exosomnia.exoarmory.item.perks.ability;

import com.exosomnia.exolib.utils.ComponentUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class UmbralAssaultAbility extends ArmoryAbility {

    public enum Stats implements AbilityStat {
        COST,
        DURATION,
        RADIUS,
        MAX_TARGETS,
        DAMAGE
    }

    public UmbralAssaultAbility() {
        super("umbral_assault", 0x705685);
    }

    //region ArmoryAbility Overrides
    public void buildStats() {
        RANK_STATS.put(Stats.COST, new double[]{
                50.0, 50.0, 50.0, 50.0, 50.0, 50.0
        });
        RANK_STATS.put(Stats.DURATION, new double[]{
                8.0, 10.0, 12.0, 13.0, 14.0, 15.0
        });
        RANK_STATS.put(Stats.RADIUS, new double[]{
                4.0, 4.5, 5.0, 5.33, 5.67, 6.0
        });
        RANK_STATS.put(Stats.MAX_TARGETS, new double[]{
                1.0, 1.0, 2.0, 2.0, 2.0, 3.0
        });
        RANK_STATS.put(Stats.DAMAGE, new double[]{
                2.5, 3.0, 3.5, 4.0, 4.5, 5.0
        });
    }

    @Override
    public List<MutableComponent> getTooltip(ComponentUtils.DetailLevel detail, ItemStack itemStack, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, itemStack, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.umbral_assault.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.umbral_assault.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.umbral_assault.line.3"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.umbral_assault.line.1", getStatForRank(Stats.COST, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.umbral_assault.line.2", getStatForRank(Stats.RADIUS, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.umbral_assault.line.3", (int)getStatForRank(Stats.MAX_TARGETS, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.umbral_assault.line.4", getStatForRank(Stats.DAMAGE, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
    //endregion
}
