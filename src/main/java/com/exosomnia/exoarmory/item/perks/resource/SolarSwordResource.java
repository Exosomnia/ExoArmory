package com.exosomnia.exoarmory.item.perks.resource;

import com.exosomnia.exoarmory.item.perks.event.handlers.PerkHandler;
import com.exosomnia.exoarmory.item.perks.event.interfaces.CriticalHitPerk;
import com.exosomnia.exoarmory.item.perks.event.interfaces.LivingHurtPerk;
import com.exosomnia.exoarmory.utils.ArmoryItemUtils;
import com.exosomnia.exoarmory.utils.ResourceItemUtils;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.exosomnia.exolib.utils.ComponentUtils.DetailLevel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

import java.util.ArrayList;
import java.util.List;

public class SolarSwordResource extends ArmoryResource implements LivingHurtPerk {

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
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.desc.solar_sword.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.desc.solar_sword.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.stat.solar_sword.line.1", 2 * getStatForRank(Stats.CHARGE, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.stat.solar_sword.line.2", getStatForRank(Stats.CHARGE, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }

    @Override
    public boolean livingHurtEvent(PerkHandler.Context<LivingHurtEvent> context) {
        LivingHurtEvent event = context.event();
        DamageSource source = event.getSource();

        if (source.getEntity() != context.triggerEntity()) return false;
        if (!event.getEntity().isOnFire()) return false;

        ItemStack triggerStack = context.triggerStack();
        ResourceItemUtils.addResourceCharge(triggerStack, getStatForRank(Stats.CHARGE, ArmoryItemUtils.getRank(triggerStack)));
        return true;
    }
}
