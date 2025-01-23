package com.exosomnia.exoarmory.items.armory.swords;

import com.exosomnia.exolib.utils.ComponentUtils.DetailLevel;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GigaSword extends ArmorySwordItem {

    private static final Multimap<Attribute, AttributeModifier>[] RANK_ATTRIBUTES = new Multimap[5];
    static {
        RANK_ATTRIBUTES[0] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 10.0, Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -3.0, Operation.ADDITION))
                .build();
        RANK_ATTRIBUTES[1] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 14.0, Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -3.1, Operation.ADDITION))
                .put(Attributes.MOVEMENT_SPEED, new AttributeModifier("Default", -0.025, Operation.MULTIPLY_TOTAL))
                .build();
        RANK_ATTRIBUTES[2] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 18.0, Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -3.2, Operation.ADDITION))
                .put(Attributes.MOVEMENT_SPEED, new AttributeModifier("Default", -0.075, Operation.MULTIPLY_TOTAL))
                .build();
        RANK_ATTRIBUTES[3] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 23.0, Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -3.3, Operation.ADDITION))
                .put(Attributes.MOVEMENT_SPEED, new AttributeModifier("Default", -0.15, Operation.MULTIPLY_TOTAL))
                .build();
        RANK_ATTRIBUTES[4] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 32.0, Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -3.4, Operation.ADDITION))
                .put(Attributes.MOVEMENT_SPEED, new AttributeModifier("Default", -0.25, Operation.MULTIPLY_TOTAL))
                .build();
    }


    public GigaSword() {
        super();
    }

    public Multimap<Attribute, AttributeModifier>[] getAttributesForAllRanks() { return RANK_ATTRIBUTES; }

    public void addToHover(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag, int rank, DetailLevel detail) {}
}
