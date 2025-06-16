package com.exosomnia.exoarmory.item.perks.resource;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.item.armory.swords.ShadowsEdgeSword;
import com.exosomnia.exoarmory.item.perks.event.handlers.PerkHandler;
import com.exosomnia.exoarmory.item.perks.event.interfaces.LivingHurtPerk;
import com.exosomnia.exoarmory.utils.AbilityItemUtils;
import com.exosomnia.exoarmory.utils.ArmoryItemUtils;
import com.exosomnia.exoarmory.utils.ResourceItemUtils;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.exosomnia.exolib.utils.ComponentUtils.DetailLevel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class LuminisEdgeResource extends ArmoryResource implements LivingHurtPerk {

    public enum Stats implements ResourceStat {
        BONUS
    }

    public LuminisEdgeResource() {
        super("luminis_edge", 0xF5F564, 100.0);
    }

    public void buildRanks() {
        RANK_STATS.put(Stats.BONUS, new double[]{
                0.15, 0.15, 0.20, 0.20, 0.25
        });
    }

    @Override
    public List<MutableComponent> getTooltip(DetailLevel detail, int rank, ItemStack itemStack) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank, itemStack));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.desc.luminis_edge.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.desc.luminis_edge.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.desc.luminis_edge.line.3"),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.stat.luminis_edge.line.1", 100.0 * getStatForRank(Stats.BONUS, rank)),
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
        if (context.slot() != EquipmentSlot.MAINHAND) return false;
        if (!( source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK) )) return false;

        double glowingMod = event.getEntity().hasEffect(MobEffects.GLOWING) ? 1.0 + getStatForRank(Stats.BONUS, ArmoryItemUtils.getRank(context.triggerStack())) : 1.0;
        ResourceItemUtils.addResourceCharge(context.triggerStack(), Math.min(event.getEntity().getHealth(), event.getAmount()) * glowingMod);
        return true;
    }
}
