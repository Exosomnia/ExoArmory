package com.exosomnia.exoarmory.item.perks.ability;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.item.perks.event.handlers.PerkHandler;
import com.exosomnia.exoarmory.item.perks.event.interfaces.LivingHurtPerk;
import com.exosomnia.exoarmory.utils.AbilityItemUtils;
import com.exosomnia.exoarmory.utils.TargetUtils;
import com.exosomnia.exolib.ExoLib;
import com.exosomnia.exolib.networking.packets.ParticleShapePacket;
import com.exosomnia.exolib.particles.options.RGBSParticleOptions;
import com.exosomnia.exolib.particles.shapes.ParticleShapeLine;
import com.exosomnia.exolib.particles.shapes.ParticleShapeOptions;
import com.exosomnia.exolib.utils.ColorUtils;
import com.exosomnia.exolib.utils.ComponentUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ColdSnapAbility extends ArmoryAbility implements LivingHurtPerk {

    public enum Stats implements AbilityStat {
        RADIUS,
        MAX_TARGETS,
        DAMAGE
    }

    public ColdSnapAbility() {
        super("cold_snap", 0x1695A6);
    }

    //region ArmoryAbility Overrides
    public void buildStats() {
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
    public List<MutableComponent> getTooltip(ComponentUtils.DetailLevel detail, ItemStack itemStack, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, itemStack, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.cold_snap.line.1"),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.cold_snap.line.2"),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.cold_snap.line.1", getStatForRank(Stats.RADIUS, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.cold_snap.line.2", (int)getStatForRank(Stats.MAX_TARGETS, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.cold_snap.line.3", getStatForRank(Stats.DAMAGE, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
    //endregion

    //region Ability Logic
    public void coldSnapEffect(ServerLevel level, int rank, @Nullable LivingEntity attacker, LivingEntity defender) {
        double radius = getStatForRank(Stats.RADIUS, rank);
        int spreadCount = (int)getStatForRank(Stats.MAX_TARGETS, rank);
        float damage = (float)getStatForRank(Stats.DAMAGE, rank);

        DamageSource source = attacker == null ? level.damageSources().freeze() :
                attacker.damageSources().freeze();

        //AoE logic, damage nearby burning entities
        Vec3 position = defender.getEyePosition();
        Set<ParticleShapePacket> packetSet = new HashSet<>();
        List<LivingEntity> nearbyEntities = defender.level().getEntitiesOfClass(LivingEntity.class, new AABB(position, position).inflate(radius), target ->
                target.hasEffect(ExoArmory.REGISTRY.EFFECT_FROSTED.get()) && target != defender &&
                TargetUtils.validTarget(attacker, target) && target.distanceToSqr(position) <= radius * radius);

        nearbyEntities.sort((entityL, entityR) -> Double.compare(entityL.position().distanceToSqr(position), entityR.distanceToSqr(position)));
        spreadCount = Math.min(spreadCount, nearbyEntities.size());

        if (nearbyEntities.isEmpty()) return;

        float[] color = ColorUtils.intToFloats(0x1695A6);
        for (int i = 0; i < spreadCount; i++) {
            LivingEntity coldEntity = nearbyEntities.get(i);
            coldEntity.hurt(source, damage);
            Vec3 launchVec = coldEntity.position().subtract(position).normalize().multiply(0.333, 0.333, 0.333);
            coldEntity.push(launchVec.x, launchVec.y + 0.333, launchVec.z);

            packetSet.add(new ParticleShapePacket(
                    new ParticleShapeLine(new RGBSParticleOptions(ExoLib.REGISTRY.SPARKLE_PARTICLE.get(), color[0], color[1], color[2], 0.1F), position,
                            new ParticleShapeOptions.Line(coldEntity.getEyePosition().subtract(0, coldEntity.getEyeHeight() / 3.0, 0), 12))));
        }

        //Perform visual and audio effects
        level.playSeededSound(null, position.x, position.y, position.z, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS,
                0.5F, 1.5F, 0);
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
        if (!defender.hasEffect(ExoArmory.REGISTRY.EFFECT_FROSTED.get())) return false;

        ItemStack attackerItem = context.triggerStack();
        int rank = AbilityItemUtils.getAbilityRank(this, attackerItem, attacker);
        coldSnapEffect((ServerLevel)defender.level(), rank, attacker, defender);

        return true;
    }
    //endregion
}