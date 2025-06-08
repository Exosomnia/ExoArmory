package com.exosomnia.exoarmory.capabilities.armory.item.ability;

import com.exosomnia.exoarmory.capabilities.armory.item.ArmoryItemStorage;
import com.exosomnia.exoarmory.item.ability.Abilities;
import com.exosomnia.exoarmory.item.ability.ArmoryAbility;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class ArmoryAbilityStorage extends ArmoryItemStorage implements INBTSerializable<CompoundTag> {

    private final HashMap<ArmoryAbility, Byte> abilityBonuses = new HashMap<>();
    private ArmoryAbility inheritedAbility = null;

    public ArmoryAbilityStorage(@Nullable CompoundTag tag) {
        super(tag);
    }

    public int getAbilityBonus(ArmoryAbility ability) {
        Byte rank = abilityBonuses.get(ability);
        return rank == null ? 0 : rank;
    }
    public ArmoryAbility getInheritedAbility() { return inheritedAbility; }

    public void setAbilityRank(ArmoryAbility ability, int rank) { abilityBonuses.put(ability, (byte)(rank & 255)); }
    public void setInheritedAbility(ArmoryAbility ability) { this.inheritedAbility = ability; }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();

        if (!abilityBonuses.isEmpty()) {
            ListTag abilityRanksTag = new ListTag();
            abilityBonuses.forEach((ability, rank) -> {
                CompoundTag abilityTag = new CompoundTag();
                abilityTag.putString("Ability", ability.getInternalName());
                abilityTag.putByte("Rank", rank);
            });
            tag.put("AbilityRanks", abilityRanksTag);
        }

        if (inheritedAbility != null) {
            tag.putString("InheritedAbility", inheritedAbility.getInternalName());
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        abilityBonuses.clear();

        Tag tag = nbt.get("AbilityRanks");
        if (tag instanceof ListTag abilityRanksTag) {
            abilityRanksTag.forEach(rankEntry -> {
                if (!(rankEntry instanceof CompoundTag abilityTag)) return;

                ArmoryAbility ability = Abilities.getAbility(abilityTag.getString("Ability"));
                if (ability == null) return;

                abilityBonuses.put(ability, abilityTag.getByte("Rank"));
            });
        }

        inheritedAbility = Abilities.getAbility(nbt.getString("InheritedAbility"));
    }
}
