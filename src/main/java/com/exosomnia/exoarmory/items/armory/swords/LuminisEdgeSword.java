package com.exosomnia.exoarmory.items.armory.swords;

import com.exosomnia.exoarmory.capabilities.resource.ArmoryResourceProvider;
import com.exosomnia.exoarmory.items.abilities.ArmoryAbility;
import com.exosomnia.exoarmory.items.resource.ArmoryResource;
import com.exosomnia.exoarmory.items.resource.LuminisEdgeResource;
import com.exosomnia.exoarmory.items.resource.ResourcedItem;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LuminisEdgeSword extends ArmorySwordItem implements ResourcedItem {

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

    private static final ArmoryResource RESOURCE = new LuminisEdgeResource();


    public LuminisEdgeSword() {
        super();
    }

    public List<ArmoryAbility> getAbilities(ItemStack itemStack) {
        return switch (getRank(itemStack)) {
            case 0 -> List.of();
            default -> List.of();
        };
    }
    public ArmoryResource getResource() { return RESOURCE; }

    public Multimap<Attribute, AttributeModifier>[] getAttributesForAllRanks() { return RANK_ATTRIBUTES; }

    public void addToHover(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag, int rank, ComponentUtils.DetailLevel detail) {
        components.add(Component.literal(""));

        //Ability Info
        for (ArmoryAbility ability: getAbilities(itemStack)) {
            components.addAll(ability.getTooltip(detail, rank));
        }

        components.add(Component.literal(""));

        //Resource Info
        components.addAll(RESOURCE.getTooltip(detail, rank, itemStack));
    }

    //region IForgeItem Overrides
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ArmoryResourceProvider();
    }
    //endregion
}
