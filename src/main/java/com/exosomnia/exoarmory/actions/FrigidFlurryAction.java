package com.exosomnia.exoarmory.actions;

import com.exosomnia.exoarmory.entities.projectiles.GenericProjectile;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FrigidFlurryAction extends Action {

    private LivingEntity owner;
    private Level level;
    private int count;
    private double damage;

    public FrigidFlurryAction(ActionManager manager, LivingEntity owner, int count, double damage) {
        super(manager);
        this.owner = owner;
        this.count = count;
        this.damage = damage;

        level = owner.level();
    }

    public boolean isValid() { return count > 0 && !owner.isRemoved() && owner.isAlive(); }

    @Override
    public void action() {
        level.playSound(null, owner.getX(), owner.getY(), owner.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        GenericProjectile projectile = new GenericProjectile(owner, level, damage);
        projectile.setItem(new ItemStack(Items.PACKED_ICE));
        projectile.getPersistentData().putBoolean("FROSTBITE", true);
        Vec3 looking = owner.getLookAngle();
        projectile.shoot(looking.x, looking.y, looking.z, 2.0F, 5.0F);
        level.addFreshEntity(projectile);

        if (count % 2 == 0) {
            Vec3 pushVec = new Vec3(owner.getViewVector(1.0F).x, 0.025, owner.getViewVector(1.0F).z)
                    .multiply(-.2f, 1.0f, -.2f);
            owner.setDeltaMovement(pushVec);
            owner.hurtMarked = true;
        }

        count--;
        manager.scheduleAction(this, 4);
    }
}
