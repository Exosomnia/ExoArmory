package com.exosomnia.exoarmory.capabilities.armory.item.resource;

import com.exosomnia.exoarmory.capabilities.armory.item.ability.ArmoryAbilityStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

public class ArmoryResourceStorage extends ArmoryAbilityStorage implements INBTSerializable<CompoundTag> {

    private double charge = 0.0;

    public ArmoryResourceStorage(@Nullable CompoundTag tag) {
        super(tag);
    }

    public double getCharge() { return charge; }
    public void setCharge(double amount) { charge = amount; }

    public void addCharge(double amount, double max) {
        charge = Math.min(charge + amount, max);
    }

    public void removeCharge(double amount) {
        charge = Math.max(charge - amount, 0);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putDouble("Charge", charge);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        charge = nbt.getDouble("Charge");
    }
}
