package com.exosomnia.exoarmory.capabilities.armory.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.UUID;

public class ArmoryItemStorage implements INBTSerializable<CompoundTag> {

    private UUID uuid;
    private byte rank;

    public UUID getUUID() { return uuid; }
    public int getRank() { return rank; }

    public void setUUID(UUID uuid) { this.uuid = uuid; }
    public void setRank(int rank) { this.rank = (byte)(rank & 255); }

    public ArmoryItemStorage(@Nullable CompoundTag tag) {
        uuid = (tag != null && tag.get("Parent") instanceof CompoundTag parent) ? parent.getUUID("UUID") : UUID.randomUUID();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("UUID", uuid);
        tag.putByte("Rank", rank);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        uuid = nbt.getUUID("UUID");
        rank = nbt.getByte("Rank");
    }
}
