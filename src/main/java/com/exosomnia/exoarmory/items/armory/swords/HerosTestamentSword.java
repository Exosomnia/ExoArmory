package com.exosomnia.exoarmory.items.armory.swords;

import com.exosomnia.exoarmory.items.abilities.AbilityItem;
import com.exosomnia.exoarmory.items.abilities.ArmoryAbility;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HerosTestamentSword extends ArmorySwordItem implements AbilityItem {

    private static final Multimap<Attribute, AttributeModifier>[] RANK_ATTRIBUTES = new Multimap[5];
    static {
        RANK_ATTRIBUTES[0] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 5.0, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.4, AttributeModifier.Operation.ADDITION))
                .build();
        RANK_ATTRIBUTES[1] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 6.0, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.4, AttributeModifier.Operation.ADDITION))
                .build();
        RANK_ATTRIBUTES[2] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 7.0, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.4, AttributeModifier.Operation.ADDITION))
                .build();
        RANK_ATTRIBUTES[3] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 8.0, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.4, AttributeModifier.Operation.ADDITION))
                .build();
        RANK_ATTRIBUTES[4] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 9.0, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.4, AttributeModifier.Operation.ADDITION))
                .build();
    }

    public HerosTestamentSword() {
        super();
    }

    public List<ArmoryAbility> getAbilities(ItemStack itemStack) {
        return switch (getRank(itemStack)) {
            case 0 -> List.of();
            default -> List.of();
        };
    }

    public Multimap<Attribute, AttributeModifier>[] getAttributesForAllRanks() { return RANK_ATTRIBUTES; }

    public void addToHover(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag, int rank, ComponentUtils.DetailLevel detail) {}

}
