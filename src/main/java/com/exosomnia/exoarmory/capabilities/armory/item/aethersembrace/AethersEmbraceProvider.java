package com.exosomnia.exoarmory.capabilities.armory.item.aethersembrace;

import com.exosomnia.exoarmory.capabilities.armory.item.ArmoryItemProvider;
import com.exosomnia.exoarmory.capabilities.armory.item.ability.ArmoryAbilityProvider;
import com.exosomnia.exoarmory.capabilities.armory.item.resource.ArmoryResourceProvider;
import com.exosomnia.exoarmory.capabilities.armory.item.resource.ArmoryResourceStorage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class AethersEmbraceProvider implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<AethersEmbraceStorage> AETHERS_EMBRACE = CapabilityManager.get(new CapabilityToken<>(){});
    private final LazyOptional<AethersEmbraceStorage> instance = LazyOptional.of(() -> new AethersEmbraceStorage(null));

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return (cap == AETHERS_EMBRACE ||
                cap == ArmoryResourceProvider.ARMORY_RESOURCE ||
                cap == ArmoryAbilityProvider.ARMORY_ABILITY ||
                cap == ArmoryItemProvider.ARMORY_ITEM) ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() { return instance.resolve().get().serializeNBT(); }

    @Override
    public void deserializeNBT(CompoundTag nbt) { instance.resolve().get().deserializeNBT(nbt); }
}
