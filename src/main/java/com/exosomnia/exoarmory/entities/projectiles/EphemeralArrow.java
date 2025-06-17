package com.exosomnia.exoarmory.entities.projectiles;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class EphemeralArrow extends Arrow {

    public EphemeralArrow(EntityType<? extends Arrow> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.inGroundTime >= 50) {
            playParticles(this.level(), this.position());
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        playParticles(this.level(), this.position());
        this.remove(RemovalReason.DISCARDED);
    }

    private void playParticles(Level level, Vec3 position) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        serverLevel.sendParticles(ParticleTypes.END_ROD, position.x, position.y, position.z, 4, 0, 0, 0, 0.02);
    }
}
