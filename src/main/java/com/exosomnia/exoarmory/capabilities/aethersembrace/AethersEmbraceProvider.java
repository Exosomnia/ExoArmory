package com.exosomnia.exoarmory.capabilities.aethersembrace;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;

public class AethersEmbraceProvider implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<IAethersEmbraceStorage> AETHERS_EMBRACE = CapabilityManager.get(new CapabilityToken<>(){});
    private final LazyOptional<IAethersEmbraceStorage> instance = LazyOptional.of(AethersEmbraceStorage::new);

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == AETHERS_EMBRACE ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() { return instance.resolve().get().serializeNBT(); }

    @Override
    public void deserializeNBT(CompoundTag nbt) { instance.resolve().get().deserializeNBT(nbt); }
}
