package com.exosomnia.exoarmory.item.perks.ability;

import com.exosomnia.exolib.utils.ComponentUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SpectralPierceAbility extends ArmoryAbility {

    public enum Stats implements AbilityStat {
        LEVEL,
    }

    public SpectralPierceAbility() {
        super("spectral_pierce", 0xFFFF5C);
    }

    //region ArmoryAbility Overrides
    public void buildStats() {
        RANK_STATS.put(Stats.LEVEL, new double[]{0.0, 1.0, 2.0});
    }

    @Override
    public List<MutableComponent> getTooltip(ComponentUtils.DetailLevel detail, ItemStack itemStack, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, itemStack, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.spectral_pierce.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle(),
                        ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.spectral_pierce.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.spectral_pierce.line.1", (int)getStatForRank(Stats.LEVEL, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
    //endregion
}
