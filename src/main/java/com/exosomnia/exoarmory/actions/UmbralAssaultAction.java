package com.exosomnia.exoarmory.actions;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.utils.TargetUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class UmbralAssaultAction extends Action {

    private LivingEntity owner;
    private Level level;
    private Vec3 origin;
    private AABB area;
    private double range;
    private int count;
    private double damage;
    private List<LivingEntity> victims;

    public UmbralAssaultAction(ActionManager manager, LivingEntity owner, double range, int count, double damage) {
        super(manager);
        this.owner = owner;
        this.range = range;
        this.count = count;
        this.damage = damage;

        level = owner.level();
        origin = owner.position();
        area = new AABB(origin.x, origin.y, origin.z, origin.x, origin.y, origin.z).inflate(range);
        victims = new ArrayList<>();
    }

    public boolean isValid() { return !owner.isRemoved() && owner.isAlive(); }

    public void action() {
        if (count <= 0) {
            level.playSound(null, origin.x, origin.y, origin.z, ExoArmory.REGISTRY.SOUND_MAGIC_TELEPORT.get(),
                    SoundSource.PLAYERS, 0.67F, 1.0F);
            owner.teleportTo(origin.x, origin.y, origin.z);
            active = false;
            return;
        }

        LivingEntity target = null;
        boolean validTarget = false;
        int index = 0;

        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area);
        int targetCount = targets.size();

        while(index < targetCount && !validTarget) {
            target = targets.get(index++);
            validTarget = target.position().distanceTo(origin) <= range && TargetUtils.validTarget(owner, target) && !victims.contains(target);
        }

        if (!validTarget) {
            count = 0;
            manager.scheduleAction(this, 10);
            return;
        }
        victims.add(target);

        Vec3 teleportPos = target.getLookAngle().multiply(1.0, 0.0, 1.0).scale(-1.5).add(target.position());
        if (!level.getBlockState(BlockPos.containing(teleportPos.x, teleportPos.y, teleportPos.z)).getCollisionShape(level, BlockPos.containing(teleportPos.x, teleportPos.y, teleportPos.z)).isEmpty()) {
            teleportPos = target.position();
        }
        owner.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 4, 10));
        if (owner instanceof ServerPlayer player) { player.connection.teleport(teleportPos.x, teleportPos.y, teleportPos.z, target.yBodyRot, 0); }
        else {owner.teleportTo(teleportPos.x, teleportPos.y, teleportPos.z);}
        level.playSound(null, teleportPos.x, teleportPos.y, teleportPos.z, ExoArmory.REGISTRY.SOUND_MAGIC_TELEPORT.get(),
                SoundSource.PLAYERS, 0.67F, 1.0F);

        if (owner instanceof Player player) { target.hurt(owner.damageSources().playerAttack(player), (float)damage); }
        else { target.hurt(owner.damageSources().mobAttack(owner), (float)damage); }

        --count;
        manager.scheduleAction(this, 4);
    }
}
