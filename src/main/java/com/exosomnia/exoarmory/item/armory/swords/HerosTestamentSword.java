package com.exosomnia.exoarmory.item.armory.swords;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.capabilities.armory.item.ability.ArmoryAbilityProvider;
import com.exosomnia.exoarmory.capabilities.armory.item.resource.ArmoryResourceProvider;
import com.exosomnia.exoarmory.item.perks.ability.Abilities;
import com.exosomnia.exoarmory.item.perks.ability.AbilityItem;
import com.exosomnia.exoarmory.item.perks.ability.ArmoryAbility;
import com.exosomnia.exoarmory.item.perks.ability.HerosWillAbility;
import com.exosomnia.exoarmory.utils.AbilityItemUtils;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HerosTestamentSword extends ArmorySwordItem implements AbilityItem {

    private static final Object2IntLinkedOpenHashMap<ArmoryAbility>[] RANK_ABILITIES = new Object2IntLinkedOpenHashMap[5];
    private static final Multimap<Attribute, AttributeModifier>[] RANK_ATTRIBUTES = new Multimap[5];
    static {
        RANK_ATTRIBUTES[0] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 7.5, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.8, AttributeModifier.Operation.ADDITION))
                .build();
        RANK_ATTRIBUTES[1] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 8.5, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.8, AttributeModifier.Operation.ADDITION))
                .build();
        RANK_ATTRIBUTES[2] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 9.5, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.8, AttributeModifier.Operation.ADDITION))
                .build();
        RANK_ATTRIBUTES[3] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 10.5, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.8, AttributeModifier.Operation.ADDITION))
                .put(ExoArmory.REGISTRY.ATTRIBUTE_CRITICAL_DAMAGE.get(), new AttributeModifier(BASE_CRITICAL_DAMAGE_UUID, "Default",
                        Abilities.HEROS_WILL.getStatForRank(HerosWillAbility.Stats.BONUS, 1), AttributeModifier.Operation.MULTIPLY_BASE))
                .put(ExoArmory.REGISTRY.ATTRIBUTE_HEALING_RECEIVED.get(), new AttributeModifier(BASE_HEALING_RECEIVED_UUID, "Default",
                        Abilities.HEROS_WILL.getStatForRank(HerosWillAbility.Stats.BONUS, 1), AttributeModifier.Operation.MULTIPLY_BASE))
                .build();
        RANK_ATTRIBUTES[4] = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Default", 11.5, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Default", -2.8, AttributeModifier.Operation.ADDITION))
                .put(ExoArmory.REGISTRY.ATTRIBUTE_CRITICAL_DAMAGE.get(), new AttributeModifier(BASE_CRITICAL_DAMAGE_UUID, "Default",
                        Abilities.HEROS_WILL.getStatForRank(HerosWillAbility.Stats.BONUS, 2), AttributeModifier.Operation.MULTIPLY_BASE))
                .put(ExoArmory.REGISTRY.ATTRIBUTE_HEALING_RECEIVED.get(), new AttributeModifier(BASE_HEALING_RECEIVED_UUID, "Default",
                        Abilities.HEROS_WILL.getStatForRank(HerosWillAbility.Stats.BONUS, 2), AttributeModifier.Operation.MULTIPLY_BASE))
                .build();

        RANK_ABILITIES[0] = AbilityItemUtils.rankBuilder()
                .addAbility(Abilities.HEROS_COURAGE, 0)
                .build();
        RANK_ABILITIES[1] = AbilityItemUtils.rankBuilder()
                .addAbility(Abilities.HEROS_COURAGE, 0)
                .addAbility(Abilities.HEROS_FORTITUDE, 0)
                .build();
        RANK_ABILITIES[2] = AbilityItemUtils.rankBuilder()
                .addAbility(Abilities.HEROS_COURAGE, 1)
                .addAbility(Abilities.HEROS_FORTITUDE, 1)
                .build();
        RANK_ABILITIES[3] = AbilityItemUtils.rankBuilder()
                .addAbility(Abilities.HEROS_COURAGE, 1)
                .addAbility(Abilities.HEROS_FORTITUDE, 1)
                .addAbility(Abilities.HEROS_WILL, 1)
                .build();
        RANK_ABILITIES[4] = AbilityItemUtils.rankBuilder()
                .addAbility(Abilities.HEROS_COURAGE, 2)
                .addAbility(Abilities.HEROS_FORTITUDE, 2)
                .addAbility(Abilities.HEROS_WILL, 2)
                .build();
    }

    public HerosTestamentSword() {
        super();
    }

    public Object2IntLinkedOpenHashMap<ArmoryAbility> getAbilities(ItemStack itemStack, LivingEntity wielder) {
        return RANK_ABILITIES[getRank(itemStack)];
    }

    public Multimap<Attribute, AttributeModifier>[] getAttributesForAllRanks() { return RANK_ATTRIBUTES; }

    public void addToHover(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag, int rank, ComponentUtils.DetailLevel detail) {
        components.add(Component.literal(""));

        //Ability Info
        Player player = ExoArmory.DIST_HELPER.getDefaultPlayer();
        for (ArmoryAbility ability : getAbilities(itemStack, ExoArmory.DIST_HELPER.getDefaultPlayer()).keySet()) {
            components.addAll(ability.getTooltip(detail, AbilityItemUtils.getAbilityRank(ability, itemStack, player)));
        }
    }

    //region IForgeItem Overrides
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ArmoryAbilityProvider(nbt);
    }
    //endregion
}
