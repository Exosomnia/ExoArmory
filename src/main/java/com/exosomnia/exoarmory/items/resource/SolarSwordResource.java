package com.exosomnia.exoarmory.items.resource;

import com.exosomnia.exoarmory.utils.TooltipUtils;
import com.exosomnia.exoarmory.utils.TooltipUtils.DetailLevel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class SolarSwordResource extends ArmoryResource {

    public enum Stats implements ResourceStat {
        CHARGE
    }

    public SolarSwordResource() {
        super("solar_sword", 0xFF8000, 500.0);
    }

    public void buildRanks() {
        RANK_STATS.put(Stats.CHARGE, new double[]{0.5, 0.5, 0.75, 0.75, 1.0});
    }

    @Override
    public List<MutableComponent> getTooltip(DetailLevel detail, int rank, ItemStack itemStack) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank, itemStack));

        switch (detail) {
            case DESCRIPTION:
                description.add(TooltipUtils.formatLine(I18n.get("resource.exoarmory.desc.solar_sword.line.1"), TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(TooltipUtils.formatLine(I18n.get("resource.exoarmory.desc.solar_sword.line.2"), TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(TooltipUtils.formatLine(I18n.get("resource.exoarmory.stat.solar_sword.line.1", 2 * getStatForRank(Stats.CHARGE, rank)),
                        TooltipUtils.Styles.DEFAULT_DESC.getStyle(), TooltipUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
}
