package com.exosomnia.exoarmory.effects;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class StellarInfusionEffect extends MobEffect {

    private final static String MOVEMENT_UUID = "f933d6c0-8db2-4302-a9aa-7ae15d68d8fc";
    private final static String ATTACK_UUID = "38af1d20-e013-46b8-b0a5-7c598c5fb0ba";

    public StellarInfusionEffect(MobEffectCategory typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);

        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                MOVEMENT_UUID, 0.4, AttributeModifier.Operation.MULTIPLY_BASE);
        this.addAttributeModifier(Attributes.ATTACK_SPEED,
                ATTACK_UUID, 0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
