package com.exosomnia.exoarmory.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FrostedEffect extends MobEffect {

    private final static String MOVEMENT_UUID = "522e5e84-ab1c-422c-bb2f-66a34a532d1c";
    private final static String ARMOR_UUID = "ae56f280-2f2e-4b63-9143-ac2a92d47624";

    public FrostedEffect(MobEffectCategory typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);

        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                MOVEMENT_UUID, -0.1, AttributeModifier.Operation.MULTIPLY_BASE);
        this.addAttributeModifier(Attributes.ARMOR,
                ARMOR_UUID, -2.0, AttributeModifier.Operation.ADDITION);
    }
}
