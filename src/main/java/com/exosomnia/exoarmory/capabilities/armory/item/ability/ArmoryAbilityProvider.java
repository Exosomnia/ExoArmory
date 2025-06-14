package com.exosomnia.exoarmory.capabilities.armory.item.ability;

import com.exosomnia.exoarmory.capabilities.armory.item.ArmoryItemProvider;
import com.exosomnia.exoarmory.capabilities.armory.item.ArmoryItemStorage;
import com.exosomnia.exoarmory.capabilities.armory.item.aethersembrace.AethersEmbraceStorage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArmoryAbilityProvider implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<ArmoryAbilityStorage> ARMORY_ABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    private final LazyOptional<ArmoryAbilityStorage> instance;

    public ArmoryAbilityProvider() {
        this(null);
    }

    public ArmoryAbilityProvider(@Nullable CompoundTag tag) {
        instance = LazyOptional.of(() -> new ArmoryAbilityStorage(tag));
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return (cap == ARMORY_ABILITY ||
                cap == ArmoryItemProvider.ARMORY_ITEM) ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() { return instance.resolve().get().serializeNBT(); }

    @Override
    public void deserializeNBT(CompoundTag nbt) { instance.resolve().get().deserializeNBT(nbt); }
}
