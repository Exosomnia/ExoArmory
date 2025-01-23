package com.exosomnia.exoarmory.capabilities.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public interface IArmoryArrowStorage extends INBTSerializable<CompoundTag> {

    void setStrength(double amount);
    double getStrength();

    void setItemUUID(UUID itemUUID);
    UUID getItemUUID();

    void setArrowType(byte type);
    byte getArrowType();

    void setArrowRank(int rank);
    int getArrowRank();

    void setEphemeral(boolean ephemeral);
    boolean isEphemeral();

    enum ArmoryArrowType {
        NORMAL(0),
        AETHER(1);

        private byte type;
        ArmoryArrowType(int type) { this.type = (byte)type; }
        public byte getType() { return type; }
    }
}
