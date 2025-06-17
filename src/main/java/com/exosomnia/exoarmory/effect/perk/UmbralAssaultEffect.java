package com.exosomnia.exoarmory.effect.perk;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.item.perks.ability.Abilities;
import com.exosomnia.exoarmory.item.perks.ability.UmbralAssaultAbility;
import com.exosomnia.exoarmory.utils.TargetUtils;
import com.exosomnia.exolib.ExoLib;
import com.exosomnia.exolib.networking.packets.ParticleShapePacket;
import com.exosomnia.exolib.particles.options.RGBSParticleOptions;
import com.exosomnia.exolib.particles.shapes.ParticleShapeDome;
import com.exosomnia.exolib.particles.shapes.ParticleShapeLine;
import com.exosomnia.exolib.particles.shapes.ParticleShapeOptions;
import com.exosomnia.exolib.particles.shapes.ParticleShapeRing;
import com.exosomnia.exolib.utils.ColorUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UmbralAssaultEffect extends MobEffect {

    public UmbralAssaultEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x705685);
    }

    @Override
    public boolean isDurationEffectTick(int tick, int amplifier) {
        return tick % 10 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity effectEntity, int amplifier) {
        if (effectEntity.level().isClientSide) return;

        double radius = Abilities.UMBRAL_ASSAULT.getStatForRank(UmbralAssaultAbility.Stats.RADIUS, amplifier);
        int spreadCount = (int)Abilities.UMBRAL_ASSAULT.getStatForRank(UmbralAssaultAbility.Stats.MAX_TARGETS, amplifier);
        float damage = (float)Abilities.UMBRAL_ASSAULT.getStatForRank(UmbralAssaultAbility.Stats.DAMAGE, amplifier);

        boolean playerEffect = (effectEntity instanceof ServerPlayer);
        ServerPlayer serverPlayer = playerEffect ? (ServerPlayer)effectEntity : null;
        Vec3 position = effectEntity.position().add(0.0, effectEntity.getEyeHeight() / 3.0, 0.0);
        ServerLevel level = (ServerLevel)effectEntity.level();
        DamageSource source = effectEntity.damageSources().indirectMagic(null, effectEntity);

        if (playerEffect) {
            float[] colors = ColorUtils.intToFloats(0x705685);
            new ParticleShapeRing(new RGBSParticleOptions(ExoLib.REGISTRY.TWINKLE_PARTICLE.get(), colors[0], colors[1], colors[2], 0.15F), position,
                    new ParticleShapeOptions.Ring((float)radius, 64)).sendToPlayers(serverPlayer.serverLevel(), List.of(serverPlayer));
        }

        //AoE logic, damage nearby burning entities
        Set<ParticleShapePacket> packetSet = new HashSet<>();
        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, new AABB(position, position).inflate(radius), livingEntity ->
                TargetUtils.validTarget(effectEntity, livingEntity) &&
                livingEntity.position().distanceToSqr(position) <= radius * radius);

        nearbyEntities.sort((entityL, entityR) -> Double.compare(entityL.position().distanceToSqr(position), entityR.distanceToSqr(position)));
        spreadCount = Math.min(spreadCount, nearbyEntities.size());

        for (int i = 0; i < spreadCount; i++) {
            LivingEntity assaultEntity = nearbyEntities.get(i);
            assaultEntity.hurt(source, damage);

            packetSet.add(new ParticleShapePacket(new ParticleShapeLine(ParticleTypes.ENCHANTED_HIT, position,
                            new ParticleShapeOptions.Line(assaultEntity.getEyePosition().subtract(0, assaultEntity.getEyeHeight() / 3.0, 0), 12))));
        }

        if (nearbyEntities.isEmpty()) return;

        //Perform visual and audio effects
        level.playSeededSound(null, position.x, position.y, position.z, ExoArmory.REGISTRY.SOUND_MAGIC_CLASH.get(), SoundSource.PLAYERS,
                0.34F, 1.75F, 0);
        if(playerEffect) {
            packetSet.forEach(packet -> com.exosomnia.exolib.networking.PacketHandler.sendToPlayer(packet, serverPlayer));
        }
    }
}
