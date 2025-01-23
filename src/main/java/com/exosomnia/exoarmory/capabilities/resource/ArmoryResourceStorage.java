package com.exosomnia.exoarmory.capabilities.resource;

import net.minecraft.nbt.CompoundTag;

public class ArmoryResourceStorage implements IArmoryResourceStorage {

    private double charge;

    public ArmoryResourceStorage(double charge) {
        setCharge(charge);
    }

    @Override
    public void setCharge(double amount) { charge = amount; }

    @Override
    public void addCharge(double amount, double max) { charge = Math.min(charge + amount, max); }

    @Override
    public void removeCharge(double amount) { charge = Math.max(charge - amount, 0); }

    @Override
    public double getCharge() { return charge; }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("Charge", charge);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        charge = nbt.getDouble("Charge");
    }
}
