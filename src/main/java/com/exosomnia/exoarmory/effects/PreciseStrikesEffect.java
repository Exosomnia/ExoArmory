package com.exosomnia.exoarmory.effects;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class PreciseStrikesEffect extends MobEffect {

    private final static String PASSIVE_CRITICAL_UUID = "f5651884-8ad4-44f3-8b1c-62615bce2e1e";

    public PreciseStrikesEffect(MobEffectCategory typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);

        this.addAttributeModifier(ExoArmory.REGISTRY.ATTRIBUTE_PASSIVE_CRITICAL.get(),
                PASSIVE_CRITICAL_UUID, 0.05, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        return 0.05 + (modifier.getAmount() * (double)(amplifier + 1));
    }
}
