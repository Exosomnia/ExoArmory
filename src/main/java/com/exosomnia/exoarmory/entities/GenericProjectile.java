package com.exosomnia.exoarmory.entities;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class GenericProjectile extends ThrowableItemProjectile {

    public double damage;

    public GenericProjectile(EntityType<? extends GenericProjectile> projectile, Level level) {
        super(projectile, level);
        this.damage = 0.0;
    }

    public GenericProjectile(LivingEntity owner, Level level, double damage) {
        super(ExoArmory.REGISTRY.ENTITY_GENERIC_PROJECTILE.get(), owner, level);
        this.damage = damage;
    }

    @Override
    protected Item getDefaultItem() {
        return Items.DIRT;
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        if (hitResult.getEntity() instanceof LivingEntity entity) {
            if (this.getPersistentData().getBoolean("FROSTBITE")) {
                entity.addEffect(new MobEffectInstance(ExoArmory.REGISTRY.EFFECT_FROSTED.get(), 200, 0));
            }
            entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float) damage);
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }
}
