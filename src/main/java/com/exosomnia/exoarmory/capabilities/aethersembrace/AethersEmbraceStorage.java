package com.exosomnia.exoarmory.capabilities.aethersembrace;

import com.exosomnia.exoarmory.capabilities.resource.IArmoryResourceStorage;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class AethersEmbraceStorage implements IAethersEmbraceStorage, IArmoryResourceStorage {

    private static final UUID DEFAULT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private double charge;
    private UUID target;
    private long expire;

    public AethersEmbraceStorage() {
        charge = 0.0;
        target = DEFAULT_UUID;
        expire = 0;
    }

    @Override
    public void setTarget(UUID target) { this.target = target; }

    @Override
    public UUID getTarget() { return target; }

    @Override
    public void setExpire(long expire) { this.expire = expire; }

    @Override
    public long getExpire() { return expire; }

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
        tag.putUUID("Target", target);
        tag.putLong("Expire", expire);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        charge = nbt.getDouble("Charge");
        target = nbt.getUUID("Target");
        expire = nbt.getLong("Expire");
    }
}
