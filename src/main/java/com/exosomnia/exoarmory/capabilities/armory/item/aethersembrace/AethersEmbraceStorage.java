package com.exosomnia.exoarmory.capabilities.armory.item.aethersembrace;

import com.exosomnia.exoarmory.capabilities.armory.item.resource.ArmoryResourceStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class AethersEmbraceStorage extends ArmoryResourceStorage implements INBTSerializable<CompoundTag> {

    private static final UUID DEFAULT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private UUID target = DEFAULT_UUID;
    private long expire = 0;

    public AethersEmbraceStorage(@Nullable CompoundTag tag) {
        super(tag);
    }

    public void setTarget(UUID target) { this.target = target; }
    public void setExpire(long expire) { this.expire = expire; }

    public UUID getTarget() { return target; }
    public long getExpire() { return expire; }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();

        tag.putUUID("Target", target);
        tag.putLong("Expire", expire);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);

        target = nbt.getUUID("Target");
        expire = nbt.getLong("Expire");
    }
}
