package com.exosomnia.exoarmory.items.armory.swords;

import com.exosomnia.exoarmory.capabilities.resource.ArmoryResourceProvider;
import com.exosomnia.exoarmory.items.abilities.ArmoryAbility;
import com.exosomnia.exoarmory.items.resource.ArmoryResource;
import com.exosomnia.exoarmory.items.resource.LuminisEdgeResource;
import com.exosomnia.exoarmory.items.resource.ResourcedItem;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.google.common.collect.ImmutableMultimap;
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

public class LuminisEdgeSword extends SwordArmoryItem implements ResourcedItem {

    private static final ArmoryResource RESOURCE = new LuminisEdgeResource();

    private static final Properties itemProperties = new Properties()
            .durability(782)
            .rarity(Rarity.UNCOMMON);


    public LuminisEdgeSword() {
        super(itemProperties);
    }

    public List<ArmoryAbility> getAbilities(ItemStack itemStack) {
        return switch (getRank(itemStack)) {
            case 0 -> List.of();
            default -> List.of();
        };
    }
    public ArmoryResource getResource() { return RESOURCE; }

    public void buildRanks() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder;

        //Rank 1
        builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 5.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4, AttributeModifier.Operation.ADDITION));
        this.RANK_ATTRIBUTES[0] = builder.build();

        //Rank 2
        builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 6.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4, AttributeModifier.Operation.ADDITION));
        this.RANK_ATTRIBUTES[1] = builder.build();

        //Rank 3
        builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 7.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.3, AttributeModifier.Operation.ADDITION));
        this.RANK_ATTRIBUTES[2] = builder.build();

        //Rank 4
        builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 8.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.3, AttributeModifier.Operation.ADDITION));
        this.RANK_ATTRIBUTES[3] = builder.build();

        //Rank 5
        builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 9.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.2, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(BASE_MOVEMENT_SPEED_UUID, "Weapon modifier", 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
        this.RANK_ATTRIBUTES[4] = builder.build();
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag, int rank, ComponentUtils.DetailLevel detail) {
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
