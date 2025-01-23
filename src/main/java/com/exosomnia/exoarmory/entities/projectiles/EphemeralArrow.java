package com.exosomnia.exoarmory.entities.projectiles;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class EphemeralArrow extends Arrow {

    public EphemeralArrow(EntityType<? extends Arrow> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.inGroundTime >= 50) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        this.remove(RemovalReason.DISCARDED);
    }
}
