package com.exosomnia.exoarmory.item.perks.ability;

import com.exosomnia.exoarmory.item.perks.event.handlers.PerkHandler;
import com.exosomnia.exoarmory.item.perks.event.interfaces.LivingDeathPerk;
import com.exosomnia.exoarmory.item.perks.event.interfaces.LivingHurtPerk;
import com.exosomnia.exoarmory.utils.AbilityItemUtils;
import com.exosomnia.exolib.utils.ComponentUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.ArrayList;
import java.util.List;

public class ShadowStrikeAbility extends ArmoryAbility implements LivingHurtPerk, LivingDeathPerk {

    public enum Stats implements AbilityStat {
        DAMAGE,
        DURATION
    }

    public ShadowStrikeAbility() {
        super("shadow_strike", 0x686673);
    }

    //region ArmoryAbility Overrides
    public void buildStats() {
        RANK_STATS.put(Stats.DAMAGE, new double[]{0.0, 0.0, 0.0, 1.0, 2.0});
        RANK_STATS.put(Stats.DURATION, new double[]{0.0, 0.0, 0.0, 10.0, 10.0});
    }

    @Override
    public List<MutableComponent> getTooltip(ComponentUtils.DetailLevel detail, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.shadow_strike.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.shadow_strike.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.shadow_strike.line.3"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.shadow_strike.line.1", getStatForRank(Stats.DAMAGE, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.shadow_strike.line.2", (int)getStatForRank(Stats.DURATION, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
    //endregion

    @Override
    public boolean livingHurtEvent(PerkHandler.Context<LivingHurtEvent> context) {
        LivingHurtEvent event = context.event();
        DamageSource source = event.getSource();

        if (source.getEntity() != context.triggerEntity()) return false;
        LivingEntity attacker = context.triggerEntity();

        if (context.slot() != EquipmentSlot.MAINHAND) return false;
        if (!source.is(DamageTypes.PLAYER_ATTACK) && !source.is(DamageTypes.MOB_ATTACK)) return false;
        if (!attacker.hasEffect(MobEffects.INVISIBILITY)) return false;

        event.setAmount(event.getAmount() + (float)getStatForRank(Stats.DAMAGE, AbilityItemUtils.getAbilityRank(this, context.triggerStack(), attacker)));
        return true;
    }

    @Override
    public boolean livingDeathEvent(PerkHandler.Context<LivingDeathEvent> context) {
        LivingDeathEvent event = context.event();
        LivingEntity attacker = context.triggerEntity();
        DamageSource source = event.getSource();

        if (!(attacker.level() instanceof ServerLevel)) return false;
        if (context.slot() != EquipmentSlot.MAINHAND) return false;
        if (!source.is(DamageTypes.PLAYER_ATTACK) && !source.is(DamageTypes.MOB_ATTACK)) return false;
        if (!attacker.hasEffect(MobEffects.INVISIBILITY)) return false;

        int rank = AbilityItemUtils.getAbilityRank(this, context.triggerStack(), attacker);
        MobEffectInstance currentEffect = attacker.getEffect(MobEffects.INVISIBILITY);
        attacker.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, (int) (currentEffect.getDuration() + getStatForRank(Stats.DURATION, rank) * 20.0),
                currentEffect.getAmplifier(), true, false, true));
        return true;
    }
}
