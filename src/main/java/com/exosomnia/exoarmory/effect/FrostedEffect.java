package com.exosomnia.exoarmory.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FrostedEffect extends MobEffect {

    private final static String MOVEMENT_UUID = "522e5e84-ab1c-422c-bb2f-66a34a532d1c";
    private final static String ARMOR_UUID = "ae56f280-2f2e-4b63-9143-ac2a92d47624";
    private final static String ARMOR_TOUGHNESS_UUID = "098b8ac1-636d-4e5b-8dd1-75f2777c210a";

    public FrostedEffect(MobEffectCategory typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);

        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                MOVEMENT_UUID, -0.1, AttributeModifier.Operation.MULTIPLY_BASE);
        this.addAttributeModifier(Attributes.ARMOR,
                ARMOR_UUID, -0.20, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ARMOR_TOUGHNESS,
                ARMOR_TOUGHNESS_UUID, -0.20, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
