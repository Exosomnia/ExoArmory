package com.exosomnia.exoarmory.capabilities.projectile;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ArmoryArrowStorage implements IArmoryArrowStorage {

    private double strength;
    private UUID itemUUID;
    private byte type;
    private int rank;
    private boolean ephemeral;

    public ArmoryArrowStorage() {
        strength = 0.0;
        type = ArmoryArrowType.NORMAL.getType();
        rank = 0;
        ephemeral = false;
    }

    public void setStrength(double strength) { this.strength = strength; }

    public double getStrength() { return strength; }

    public void setItemUUID(UUID itemUUID) { this.itemUUID = itemUUID; }

    @Nullable
    public UUID getItemUUID() { return itemUUID; }

    public void setArrowType(byte type) { this.type = type; }

    public byte getArrowType() { return type; }

    public void setArrowRank(int rank) { this.rank = rank; }

    public int getArrowRank() { return rank; }

    public void setEphemeral(boolean ephemeral) { this.ephemeral = ephemeral; }

    public boolean isEphemeral() { return ephemeral; }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("Strength", strength);
        if (itemUUID != null) { tag.putUUID("ItemUUID", itemUUID); }
        tag.putByte("Type", type);
        tag.putInt("Rank", rank);
        tag.putBoolean("Ephemeral", ephemeral);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        strength = nbt.getDouble("Strength");
        if (nbt.hasUUID("ItemUUID")) { itemUUID = nbt.getUUID("ItemUUID"); }
        type = nbt.getByte("Type");
        rank = nbt.getByte("Rank");
        ephemeral = nbt.getBoolean("Ephemeral");
    }
}
