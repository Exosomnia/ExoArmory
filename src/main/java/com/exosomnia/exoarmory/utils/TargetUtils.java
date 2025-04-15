package com.exosomnia.exoarmory.utils;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;

public class TargetUtils {

    public static boolean validTarget(LivingEntity attacker, LivingEntity defender) {
        return  (defender.isAlive() &&
                !defender.isRemoved() &&
                attacker != defender &&
                (!(defender instanceof TamableAnimal tamableAnimal) || !tamableAnimal.isTame()) &&
                (!(defender instanceof NeutralMob neutralMob) || (attacker != null && !neutralMob.isAngryAt(attacker))));
    }
}
