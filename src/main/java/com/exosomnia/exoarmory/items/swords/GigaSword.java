package com.exosomnia.exoarmory.items.swords;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class GigaSword extends SwordArmoryItem {

    private static final Item.Properties itemProperties = new Item.Properties()
            .durability(5)
            .rarity(Rarity.UNCOMMON);

    public GigaSword() {
        super(itemProperties);
//
//        //Rank 2
//        builder = ImmutableMultimap.builder();
//        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", getDamage(), AttributeModifier.Operation.ADDITION));
//        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
//        this.RANK_ATTRIBUTES[0] = builder.build();
    }

    @Override
    public void buildRanks() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder;

        //Rank 1
        builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 5.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.8, AttributeModifier.Operation.ADDITION));
        this.RANK_ATTRIBUTES[0] = builder.build();
    }
}
