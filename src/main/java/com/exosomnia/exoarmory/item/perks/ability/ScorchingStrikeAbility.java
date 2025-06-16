package com.exosomnia.exoarmory.item.perks.ability;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.item.perks.event.handlers.PerkHandler;
import com.exosomnia.exoarmory.item.perks.event.interfaces.CriticalHitPerk;
import com.exosomnia.exoarmory.item.perks.event.interfaces.LivingHurtPerk;
import com.exosomnia.exoarmory.utils.AbilityItemUtils;
import com.exosomnia.exoarmory.utils.ResourceItemUtils;
import com.exosomnia.exoarmory.utils.TargetUtils;
import com.exosomnia.exolib.ExoLib;
import com.exosomnia.exolib.networking.packets.ParticleShapePacket;
import com.exosomnia.exolib.particles.options.RGBSParticleOptions;
import com.exosomnia.exolib.particles.shapes.ParticleShape;
import com.exosomnia.exolib.particles.shapes.ParticleShapeDome;
import com.exosomnia.exolib.particles.shapes.ParticleShapeLine;
import com.exosomnia.exolib.particles.shapes.ParticleShapeOptions;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.exosomnia.exolib.utils.ComponentUtils.DetailLevel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nullable;
import java.util.*;

public class ScorchingStrikeAbility extends ArmoryAbility implements LivingHurtPerk {

    public enum Stats implements AbilityStat {
        DURATION,
        RADIUS,
        MAX_TARGETS,
        DAMAGE
    }

    public ScorchingStrikeAbility() {
        super("scorching_strike", 0xFF9B30);
    }

    //region ArmoryAbility Overrides
    public void buildStats() {
        RANK_STATS.put(Stats.DURATION, new double[]{
                3.5, 4.5, 5.5, 6.0, 6.5, 7.0
        });
        RANK_STATS.put(Stats.RADIUS, new double[]{
                3.0, 3.5, 4.0, 4.33, 4.67, 5.0
        });
        RANK_STATS.put(Stats.MAX_TARGETS, new double[]{
                1.0, 2.0, 2.0, 2.0, 2.0, 3.0
        });
        RANK_STATS.put(Stats.DAMAGE, new double[]{
                2.0, 2.5, 3.0, 3.33, 3.67, 4.0
        });
    }

    @Override
    public List<MutableComponent> getTooltip(DetailLevel detail, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.scorching_strike.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.scorching_strike.line.2"),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.scorching_strike.line.3"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.scorching_strike.line.1", getStatForRank(Stats.DURATION, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.scorching_strike.line.2", getStatForRank(Stats.RADIUS, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.scorching_strike.line.3", (int)getStatForRank(Stats.MAX_TARGETS, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.scorching_strike.line.4", getStatForRank(Stats.DAMAGE, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
    //endregion

    //region Ability Logic
    public void scorchingStrikeEffect(ServerLevel level, int rank, @Nullable LivingEntity attacker, LivingEntity defender) {
        double effectDuration = getStatForRank(Stats.DURATION, rank);
        double radius = getStatForRank(Stats.RADIUS, rank);
        int spreadCount = (int)getStatForRank(Stats.MAX_TARGETS, rank);
        float damage = (float)getStatForRank(Stats.DAMAGE, rank);

        DamageSource source = attacker == null ? level.damageSources().magic() :
                attacker.damageSources().indirectMagic(attacker, null);
        defender.addEffect(new MobEffectInstance(ExoArmory.REGISTRY.EFFECT_FIRE_VULNERABILITY.get(), (int)(effectDuration * 20), 0));

        //AoE logic, damage nearby burning entities
        Vec3 position = defender.getEyePosition();
        Set<ParticleShapePacket> packetSet = new HashSet<>();
        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, new AABB(position, position).inflate(radius), livingEntity ->
                livingEntity.isOnFire() && !livingEntity.hasEffect(MobEffects.FIRE_RESISTANCE) &&
                livingEntity != defender && TargetUtils.validTarget(attacker, livingEntity) &&
                livingEntity.position().distanceToSqr(position) <= radius * radius);

        nearbyEntities.sort((entityL, entityR) -> Double.compare(entityL.position().distanceToSqr(position), entityR.distanceToSqr(position)));
        spreadCount = Math.min(spreadCount, nearbyEntities.size());

        if (nearbyEntities.isEmpty()) return;

        for (int i = 0; i < spreadCount; i++) {
            LivingEntity flaredEntity = nearbyEntities.get(i);
            flaredEntity.hurt(source, damage);
            Vec3 launchVec = flaredEntity.position().subtract(position).normalize().multiply(0.333, 0.333, 0.333);
            flaredEntity.push(launchVec.x, launchVec.y + 0.333, launchVec.z);

            packetSet.add(new ParticleShapePacket(
                new ParticleShapeLine(ParticleTypes.FLAME, position,
                new ParticleShapeOptions.Line(flaredEntity.getEyePosition().subtract(0, flaredEntity.getEyeHeight() / 3.0, 0), 12))));
        }

        //Perform visual and audio effects
        level.playSeededSound(null, position.x, position.y, position.z, ExoArmory.REGISTRY.SOUND_FIERY_EXPLOSION.get(), SoundSource.PLAYERS,
                0.34F, 1.75F, 0);
        for(ServerPlayer player : level.players()) {
            packetSet.forEach(packet -> com.exosomnia.exolib.networking.PacketHandler.sendToPlayer(packet, player));
        }
    }

    @Override
    public boolean livingHurtEvent(PerkHandler.Context<LivingHurtEvent> context) {
        LivingHurtEvent event = context.event();
        LivingEntity defender = event.getEntity();
        DamageSource source = event.getSource();

        if (source.getEntity() != context.triggerEntity()) return false;
        LivingEntity attacker = context.triggerEntity();

        if (context.slot() != EquipmentSlot.MAINHAND) return false;
        if (!source.is(DamageTypes.PLAYER_ATTACK) && !source.is(DamageTypes.MOB_ATTACK)) return false;
        if (!defender.isOnFire()) return false;

        ItemStack attackerItem = context.triggerStack();
        int rank = AbilityItemUtils.getAbilityRank(Abilities.SCORCHING_STRIKE, attackerItem, attacker);
        scorchingStrikeEffect((ServerLevel)defender.level(), rank, attacker, defender);

        return true;
    }
    //endregion
}
