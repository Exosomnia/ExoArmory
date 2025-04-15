package com.exosomnia.exoarmory.effects;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class CourageEffect extends MobEffect {

    private final static String DAMAGE_UUID = "edfb14a9-6890-43b4-bbe7-71e705b8a661";
    private final static String ATTACK_SPEED_UUID = "ba2a0902-a751-42fb-9e10-4adbac5df9ed";
    private final static String MOVEMENT_UUID = "32fb77f7-3395-4851-8cd9-9431438dc33e";
    private final static String HEALING_UUID = "00d53f61-da85-46fb-ae83-6bc8084bb4b5";

    public CourageEffect(MobEffectCategory typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);

        this.addAttributeModifier(Attributes.ATTACK_DAMAGE,
                DAMAGE_UUID, 0.025, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED,
                ATTACK_SPEED_UUID, 0.025, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                MOVEMENT_UUID, 0.025, AttributeModifier.Operation.MULTIPLY_BASE);
        this.addAttributeModifier(ExoArmory.REGISTRY.ATTRIBUTE_HEALING_RECEIVED.get(),
                HEALING_UUID, 0.025, AttributeModifier.Operation.MULTIPLY_BASE);
    }
}
