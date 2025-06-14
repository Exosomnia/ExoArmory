package com.exosomnia.exoarmory.effect;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class EagleEyeEffect extends MobEffect {

    private final static String PROJECTILE_UUID = "b143c40c-14b3-4106-88bb-cdd0a20359a8";

    public EagleEyeEffect(MobEffectCategory typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);

        this.addAttributeModifier(ExoArmory.REGISTRY.ATTRIBUTE_RANGED_STRENGTH.get(),
                PROJECTILE_UUID, 0.125, AttributeModifier.Operation.MULTIPLY_BASE);
    }
}
