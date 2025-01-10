package com.exosomnia.exoarmory.items.abilities;

import com.exosomnia.exoarmory.utils.TooltipUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public class VeilOfDarknessAbility extends ArmoryAbility {

    public VeilOfDarknessAbility() {
        super("veil_of_darkness", 0x1A0A7D);
    }

    //region ArmoryAbility Overrides
    public void buildRanks() {}

    @Override
    public List<MutableComponent> getTooltip(TooltipUtils.DetailLevel detail, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.desc.veil_of_darkness.line.1"), TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.desc.veil_of_darkness.line.2"), TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                break;
        }
        return description;
    }
    //endregion
}
