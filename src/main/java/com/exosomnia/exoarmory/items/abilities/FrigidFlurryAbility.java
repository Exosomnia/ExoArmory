package com.exosomnia.exoarmory.items.abilities;

import com.exosomnia.exolib.utils.ComponentUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public class FrigidFlurryAbility extends ArmoryAbility {

    public enum Stats implements AbilityStat {
        COOLDOWN,
        ARMOR,
        FROST_DURATION,
        COST,
        PROJECTILES,
        DAMAGE
    }

    public FrigidFlurryAbility() {
        super("frigid_flurry", 0x719BDE);
    }

    //region ArmoryAbility Overrides
    public void buildRanks() {
        RANK_STATS.put(Stats.COOLDOWN, new double[]{15.0, 15.0, 15.0, 15.0, 15.0});
        RANK_STATS.put(Stats.ARMOR, new double[]{2.0, 2.0, 2.0, 2.0, 2.0});
        RANK_STATS.put(Stats.FROST_DURATION, new double[]{10.0, 10.0, 10.0, 10.0, 10.0});
        RANK_STATS.put(Stats.COST, new double[]{10.0, 10.0, 10.0, 10.0, 10.0});
        RANK_STATS.put(Stats.PROJECTILES, new double[]{20.0, 25.0, 30.0, 35.0, 40.0});
        RANK_STATS.put(Stats.DAMAGE, new double[]{1.0, 1.5, 2.0, 2.5, 3.0});
    }

    @Override
    public List<MutableComponent> getTooltip(ComponentUtils.DetailLevel detail, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.frigid_flurry.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.frigid_flurry.line.2"),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.frigid_flurry.line.3"),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.frigid_flurry.line.4"),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.frigid_flurry.line.5"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.frigid_flurry.line.6"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.frigid_flurry.line.1", (int)getStatForRank(Stats.COOLDOWN, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.frigid_flurry.line.2", (int)getStatForRank(Stats.FROST_DURATION, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.frigid_flurry.line.3", (int)getStatForRank(Stats.ARMOR, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.frigid_flurry.line.4"), ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.frigid_flurry.line.5", getStatForRank(Stats.COST, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.frigid_flurry.line.6", (int)getStatForRank(Stats.PROJECTILES, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.frigid_flurry.line.7", getStatForRank(Stats.DAMAGE, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
    //endregion
}
