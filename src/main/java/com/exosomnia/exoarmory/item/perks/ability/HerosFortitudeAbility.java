package com.exosomnia.exoarmory.item.perks.ability;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.item.perks.event.handlers.PerkHandler;
import com.exosomnia.exoarmory.item.perks.event.interfaces.LivingDeathPerk;
import com.exosomnia.exoarmory.utils.AbilityItemUtils;
import com.exosomnia.exolib.utils.ComponentUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.ArrayList;
import java.util.List;

public class HerosFortitudeAbility extends ArmoryAbility implements LivingDeathPerk {

    public enum Stats implements AbilityStat {
        CHANCE,
        AMOUNT
    }

    public HerosFortitudeAbility() {
        super("heros_fortitude", 0xD19356);
    }

    //region ArmoryAbility Overrides
    public void buildStats() {
        RANK_STATS.put(Stats.CHANCE, new double[]{0.10, 0.125, 0.25});
        RANK_STATS.put(Stats.AMOUNT, new double[]{0.0, 0.0, 0.0});
    }

    @Override
    public List<MutableComponent> getTooltip(ComponentUtils.DetailLevel detail, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.heros_fortitude.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.heros_fortitude.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.heros_fortitude.line.3"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.heros_fortitude.line.1", getStatForRank(Stats.CHANCE, rank) * 100.0),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.heros_fortitude.line.2", ((int)getStatForRank(Stats.AMOUNT, rank) + 1) * 4),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
    //endregion

    @Override
    public boolean livingDeathEvent(PerkHandler.Context<LivingDeathEvent> context) {
        LivingDeathEvent event = context.event();
        LivingEntity defender = event.getEntity();
        DamageSource source = event.getSource();

        if (source.getEntity() != context.triggerEntity()) return false;
        LivingEntity attacker = context.triggerEntity();

        if (context.slot() != EquipmentSlot.MAINHAND) return false;
        if (!source.is(DamageTypes.PLAYER_ATTACK) && !source.is(DamageTypes.MOB_ATTACK)) return false;
        if (!defender.getType().getCategory().equals(MobCategory.MONSTER)) return false;

        int rank = AbilityItemUtils.getAbilityRank(this, context.triggerStack(), attacker);
        if (attacker.getRandom().nextDouble() < getStatForRank(Stats.CHANCE, rank)) {
            attacker.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 400, (int)getStatForRank(Stats.AMOUNT, rank)));
            if (attacker instanceof ServerPlayer player) {
                player.playNotifySound(ExoArmory.REGISTRY.SOUND_MAGIC_CLASH.get(), SoundSource.PLAYERS, 0.333F, 0.667F);
            }
        }
        return true;
    }
}
