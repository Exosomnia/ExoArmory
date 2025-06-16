package com.exosomnia.exoarmory.item.perks.ability;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.actions.ActionManager;
import com.exosomnia.exoarmory.actions.BlizzardAction;
import com.exosomnia.exoarmory.item.perks.event.handlers.PerkHandler;
import com.exosomnia.exoarmory.item.perks.event.interfaces.LivingDeathPerk;
import com.exosomnia.exoarmory.utils.AbilityItemUtils;
import com.exosomnia.exoarmory.utils.ArmoryItemUtils;
import com.exosomnia.exoarmory.utils.TargetUtils;
import com.exosomnia.exolib.ExoLib;
import com.exosomnia.exolib.networking.packets.ParticleShapePacket;
import com.exosomnia.exolib.particles.options.RGBSParticleOptions;
import com.exosomnia.exolib.particles.shapes.ParticleShapeOptions;
import com.exosomnia.exolib.particles.shapes.ParticleShapeRing;
import com.exosomnia.exolib.utils.ColorUtils;
import com.exosomnia.exolib.utils.ComponentUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.ArrayList;
import java.util.List;

public class BlizzardAbility extends ArmoryAbility implements LivingDeathPerk {

    public enum Stats implements AbilityStat {
        RADIUS,
        DAMAGE,
        DURATION
    }

    public BlizzardAbility() {
        super("blizzard", 0x8DDBEB);
    }

    //region ArmoryAbility Overrides
    public void buildStats() {
        RANK_STATS.put(Stats.RADIUS, new double[]{
                2.5, 3.0, 3.5, 3.83, 4.16, 4.5
        });
        RANK_STATS.put(Stats.DAMAGE, new double[]{
                2.5, 3.0, 3.5, 3.75, 4.0, 4.25
        });
        RANK_STATS.put(Stats.DURATION, new double[]{
                2.0, 2.0, 3.0, 3.0, 3.0, 4.0
        });
    }

    @Override
    public List<MutableComponent> getTooltip(ComponentUtils.DetailLevel detail, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.blizzard.line.1"),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.blizzard.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.blizzard.line.3"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.blizzard.line.1", getStatForRank(Stats.RADIUS, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.blizzard.line.2", getStatForRank(Stats.DAMAGE, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.blizzard.line.3", (int)getStatForRank(Stats.DURATION, rank)),
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
        if (!defender.hasEffect(ExoArmory.REGISTRY.EFFECT_FROSTED.get())) return false;

        if (defender.hasEffect(ExoArmory.REGISTRY.EFFECT_FROSTED.get())) {
            int rank = AbilityItemUtils.getAbilityRank(this, context.triggerStack(), attacker);
            ExoArmory.ACTION_MANAGER.scheduleAction(new BlizzardAction(ExoArmory.ACTION_MANAGER, attacker, defender.position(),
                    getStatForRank(Stats.RADIUS, rank), (int)getStatForRank(Stats.DURATION, rank), getStatForRank(Stats.DAMAGE, rank)), 10);
            return true;
        }

        return false;
    }
}