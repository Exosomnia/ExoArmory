package com.exosomnia.exoarmory.item.perks.ability;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.item.perks.event.handlers.PerkHandler;
import com.exosomnia.exoarmory.item.perks.event.interfaces.LivingHurtPerk;
import com.exosomnia.exoarmory.utils.AbilityItemUtils;
import com.exosomnia.exoarmory.utils.ResourceItemUtils;
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
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.ArrayList;
import java.util.List;

public class RadiantSmiteAbility extends ArmoryAbility implements LivingHurtPerk {

    public enum Stats implements AbilityStat {
        COST,
        DAMAGE,
        DURATION
    }

    public RadiantSmiteAbility() {
        super("radiant_smite", 0xF7E8BE);
    }

    //region ArmoryAbility Overrides
    public void buildStats() {
        RANK_STATS.put(Stats.COST, new double[]{
                100.0, 100.0, 100.0, 100.0, 100.0, 100.0
        });
        RANK_STATS.put(Stats.DAMAGE, new double[]{
                1.0, 1.5, 2.0, 2.33, 2.66, 3.0
        });
        RANK_STATS.put(Stats.DURATION, new double[]{
                4.0, 4.5, 5.0, 5.33, 5.66, 6.0
        });
    }

    @Override
    public List<MutableComponent> getTooltip(ComponentUtils.DetailLevel detail, ItemStack itemStack, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, itemStack, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.radiant_smite.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle(),
                        ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.radiant_smite.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.radiant_smite.line.3"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.radiant_smite.line.4"), ComponentUtils.Styles.DEFAULT_DESC.getStyle(),
                        ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.radiant_smite.line.1", (int)getStatForRank(Stats.COST, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.radiant_smite.line.2", getStatForRank(Stats.DAMAGE, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.radiant_smite.line.3", getStatForRank(Stats.DURATION, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
    //endregion

    public void radiantSmite(LivingEntity target, int rank) {
        Level level = target.level();
        if (level.isClientSide) return;

        int duration = (int)(getStatForRank(Stats.DURATION, rank) * 20.0);
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, duration, 0));
        target.addEffect(new MobEffectInstance(ExoArmory.REGISTRY.EFFECT_VULNERABLE.get(), duration, 0));

        //Visual and audio effects
        ServerLevel serverLevel = (ServerLevel)level;
        Vec3 position = target.position();
        serverLevel.playSound(null, target.blockPosition(), ExoArmory.REGISTRY.SOUND_FIERY_EXPLOSION.get(), SoundSource.NEUTRAL, 0.5F, 2.0F);
        serverLevel.sendParticles(ParticleTypes.END_ROD, position.x, position.y+(target.getEyeHeight()/2), position.z, 25, 0, 0, 0, 0.075);
    }

    @Override
    public boolean livingHurtEvent(PerkHandler.Context<LivingHurtEvent> context) {
        LivingHurtEvent event = context.event();
        LivingEntity defender = event.getEntity();
        DamageSource source = event.getSource();

        if (source.getEntity() != context.triggerEntity()) return false;
        LivingEntity attacker = context.triggerEntity();
        if (attacker instanceof ServerPlayer player && !ExoArmory.ABILITY_MANAGER.isPlayerActive(player)) return false;

        if (context.slot() != EquipmentSlot.MAINHAND) return false;
        if (!source.is(DamageTypes.PLAYER_ATTACK) && !source.is(DamageTypes.MOB_ATTACK)) return false;

        ItemStack attackerItem = context.triggerStack();
        int rank = AbilityItemUtils.getAbilityRank(this, attackerItem, attacker);
        return ResourceItemUtils.spendChargeOn(attackerItem, getStatForRank(Stats.COST, rank), () -> {
            event.setAmount(event.getAmount() + (float)getStatForRank(Stats.DAMAGE, rank));
            radiantSmite(defender, rank);
        });
    }
}
