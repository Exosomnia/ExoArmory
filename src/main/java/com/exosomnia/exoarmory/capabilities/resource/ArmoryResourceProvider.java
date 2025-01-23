package com.exosomnia.exoarmory.capabilities.resource;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class ArmoryResourceProvider implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<IArmoryResourceStorage> ARMORY_RESOURCE = CapabilityManager.get(new CapabilityToken<>(){});
    private final LazyOptional<IArmoryResourceStorage> instance = LazyOptional.of(() -> new ArmoryResourceStorage(0.0));

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == ARMORY_RESOURCE ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() { return instance.resolve().get().serializeNBT(); }

    @Override
    public void deserializeNBT(CompoundTag nbt) { instance.resolve().get().deserializeNBT(nbt); }
}
