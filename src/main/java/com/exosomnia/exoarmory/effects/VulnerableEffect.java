package com.exosomnia.exoarmory.effects;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class VulnerableEffect extends MobEffect {

    private final static String VULNERABLE_UUID = "a440070b-a7b9-4389-83ac-22cba5a76370";

    public VulnerableEffect(MobEffectCategory typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);

        this.addAttributeModifier(ExoArmory.REGISTRY.ATTRIBUTE_VULNERABILITY.get(),
                VULNERABLE_UUID, -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
