package com.exosomnia.exoarmory.capabilities.armory.item.resource;

import com.exosomnia.exoarmory.capabilities.armory.item.ArmoryItemProvider;
import com.exosomnia.exoarmory.capabilities.armory.item.ArmoryItemStorage;
import com.exosomnia.exoarmory.capabilities.armory.item.ability.ArmoryAbilityProvider;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArmoryResourceProvider implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<ArmoryResourceStorage> ARMORY_RESOURCE = CapabilityManager.get(new CapabilityToken<>(){});
    private final LazyOptional<ArmoryResourceStorage> instance;

    public ArmoryResourceProvider() {
        this(null);
    }

    public ArmoryResourceProvider(@Nullable CompoundTag tag) {
        instance = LazyOptional.of(() -> new ArmoryResourceStorage(tag));
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return (cap == ARMORY_RESOURCE ||
                cap == ArmoryAbilityProvider.ARMORY_ABILITY ||
                cap == ArmoryItemProvider.ARMORY_ITEM) ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() { return instance.resolve().get().serializeNBT(); }

    @Override
    public void deserializeNBT(CompoundTag nbt) { instance.resolve().get().deserializeNBT(nbt); }
}
