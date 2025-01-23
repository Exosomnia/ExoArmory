package com.exosomnia.exoarmory.effects;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class BlightedEffect extends MobEffect {

    private final static String HEALING_UUID = "9de80dbb-de38-446b-896e-6c0e0f75ebf9";

    public BlightedEffect(MobEffectCategory typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);

        this.addAttributeModifier(ExoArmory.REGISTRY.ATTRIBUTE_HEALING_RECEIVED.get(),
                HEALING_UUID, -0.2, AttributeModifier.Operation.MULTIPLY_BASE);
    }
}
