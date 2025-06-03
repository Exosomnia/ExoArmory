package com.exosomnia.exoarmory.effects;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class PreciseStrikesEffect extends MobEffect {

    private final static String PASSIVE_CRITICAL_UUID = "f5651884-8ad4-44f3-8b1c-62615bce2e1e";
    private final static String ATTACK_SPEED_UUID = "04558cf8-c90a-4d7e-bdc3-4d2a6dbb5fd2";

    public PreciseStrikesEffect(MobEffectCategory typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);

        this.addAttributeModifier(ExoArmory.REGISTRY.ATTRIBUTE_PASSIVE_CRITICAL.get(),
                PASSIVE_CRITICAL_UUID, 0.05, AttributeModifier.Operation.MULTIPLY_BASE);
        this.addAttributeModifier(Attributes.ATTACK_SPEED,
                ATTACK_SPEED_UUID, 0.025, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        if (modifier.getId().equals(UUID.fromString(ATTACK_SPEED_UUID))) { return (modifier.getAmount() * amplifier); }
        return 0.1 + (modifier.getAmount() * (amplifier + 1));
    }
}
