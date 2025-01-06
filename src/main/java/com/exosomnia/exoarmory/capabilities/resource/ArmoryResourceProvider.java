package com.exosomnia.exoarmory.capabilities.resource;

import net.minecraft.core.Direction;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class ArmoryResourceProvider implements ICapabilitySerializable<DoubleTag> {

    public static final Capability<IArmoryResourceStorage> ARMORY_RESOURCE = CapabilityManager.get(new CapabilityToken<>(){});
    private final LazyOptional<IArmoryResourceStorage> instance = LazyOptional.of(() -> new ArmoryResourceStorage(0.0));

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == ARMORY_RESOURCE ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public DoubleTag serializeNBT() { return instance.resolve().get().serializeNBT(); }

    @Override
    public void deserializeNBT(DoubleTag nbt) { instance.resolve().get().deserializeNBT(nbt); }
}
