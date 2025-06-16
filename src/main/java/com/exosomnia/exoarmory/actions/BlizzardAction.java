package com.exosomnia.exoarmory.actions;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.item.perks.ability.ColdSnapAbility;
import com.exosomnia.exoarmory.utils.ArmoryItemUtils;
import com.exosomnia.exoarmory.utils.TargetUtils;
import com.exosomnia.exolib.ExoLib;
import com.exosomnia.exolib.networking.packets.ParticleShapePacket;
import com.exosomnia.exolib.particles.options.RGBSParticleOptions;
import com.exosomnia.exolib.particles.shapes.ParticleShapeDome;
import com.exosomnia.exolib.particles.shapes.ParticleShapeOptions;
import com.exosomnia.exolib.particles.shapes.ParticleShapeRing;
import com.exosomnia.exolib.utils.ColorUtils;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BlizzardAction extends Action {

    private final LivingEntity owner;
    private final Level level;
    private final Vec3 origin;
    private final AABB area;
    private final double range;
    private int count;
    private final double damage;

    public BlizzardAction(ActionManager manager, LivingEntity owner, Vec3 origin, double range, int count, double damage) {
        super(manager);
        this.owner = owner;
        this.origin = origin;
        this.range = range;
        this.count = count;
        this.damage = damage;

        level = owner.level();
        area = new AABB(origin.x, origin.y, origin.z, origin.x, origin.y, origin.z).inflate(range);
    }

    @Override
    public boolean isValid() {
        return owner.isAlive();
    }

    @Override
    public void action() {
        if (count-- > 0) {
            DamageSource damageSource = owner.damageSources().freeze();

            List<LivingEntity> blizzardTargets = level.getEntitiesOfClass(LivingEntity.class, area, target ->
                    TargetUtils.validTarget(owner, target) && target.distanceToSqr(origin) <= range * range);
            for (LivingEntity target : blizzardTargets) {
                target.hurt(damageSource, (float)damage);
                target.addEffect(new MobEffectInstance(ExoArmory.REGISTRY.EFFECT_FROSTED.get(), 200, 0));
            }

            //Visual and audio effects
            level.playSound(null, origin.x, origin.y, origin.z,
                    ExoArmory.REGISTRY.SOUND_MAGIC_ICE_CAST.get(), SoundSource.PLAYERS, 0.25F, 1.5F);

            int ringCount = (int)Math.max(0, Math.ceil(range - 1.0));
            Vec3 particlePos = origin.add(0, .25, 0);
            float[] color = ColorUtils.intToFloats(0x8DDBEB);
            BlockParticleOption innerParticles = new BlockParticleOption(ParticleTypes.BLOCK,
                    Blocks.SNOW_BLOCK.defaultBlockState());

            ParticleShapePacket[] packets = new ParticleShapePacket[ringCount + 1];
            for (int i = 0; i < ringCount; i++) {
                packets[i] = new ParticleShapePacket(new ParticleShapeRing(innerParticles, particlePos,
                        new ParticleShapeOptions.Ring((float)(i + 1), (i + 1) * 16)));
            }
            packets[ringCount] = new ParticleShapePacket(new ParticleShapeDome(new RGBSParticleOptions(ExoLib.REGISTRY.SPIRAL_PARTICLE.get(), color[0], color[1], color[2], 0.1F),
                    particlePos, new ParticleShapeOptions.Dome((float)range, 128)));

            for (ServerPlayer player : ((ServerLevel)level).players()) {
                for (ParticleShapePacket packet : packets) {
                    com.exosomnia.exolib.networking.PacketHandler.sendToPlayer(packet, player);
                }
            }

            manager.scheduleAction(this, 20);
        }
    }
}
