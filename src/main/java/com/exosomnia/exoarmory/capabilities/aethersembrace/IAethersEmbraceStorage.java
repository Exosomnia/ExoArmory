package com.exosomnia.exoarmory.capabilities.aethersembrace;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public interface IAethersEmbraceStorage extends INBTSerializable<CompoundTag> {

    void setTarget(UUID target);
    UUID getTarget();

    void setExpire(long expire);
    long getExpire();
}
