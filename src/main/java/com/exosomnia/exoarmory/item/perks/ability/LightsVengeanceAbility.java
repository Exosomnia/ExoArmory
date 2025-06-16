package com.exosomnia.exoarmory.item.perks.ability;

import com.exosomnia.exoarmory.item.perks.event.handlers.PerkHandler;
import com.exosomnia.exoarmory.item.perks.event.interfaces.LivingHurtPerk;
import com.exosomnia.exoarmory.utils.AbilityItemUtils;
import com.exosomnia.exoarmory.utils.ResourceItemUtils;
import com.exosomnia.exolib.utils.ComponentUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.ArrayList;
import java.util.List;

public class LightsVengeanceAbility extends ArmoryAbility implements LivingHurtPerk {

    public enum Stats implements AbilityStat {
        RATIO,
    }

    public LightsVengeanceAbility() {
        super("lights_vengeance", 0xFEFFEB);
    }

    //region ArmoryAbility Overrides
    public void buildStats() {
        RANK_STATS.put(Stats.RATIO, new double[] {
                0.20, 0.30, 0.40, 0.433, 0.467, 0.50
        });
    }

    @Override
    public List<MutableComponent> getTooltip(ComponentUtils.DetailLevel detail, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.lights_vengeance.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle(),
                        ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.lights_vengeance.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.lights_vengeance.line.1", 100.0 * getStatForRank(Stats.RATIO, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
    //endregion

    @Override
    public boolean livingHurtEvent(PerkHandler.Context<LivingHurtEvent> context) {
        LivingHurtEvent event = context.event();
        LivingEntity defender = event.getEntity();

        if (context.triggerEntity() != defender) return false;

        ItemStack itemStack = context.triggerStack();
        double charge = event.getAmount() * getStatForRank(Stats.RATIO, AbilityItemUtils.getAbilityRank(this, itemStack, defender));
        ResourceItemUtils.addResourceCharge(itemStack, charge);

        return true;
    }
}
