package com.exosomnia.exoarmory.capabilities.resource;

import net.minecraft.nbt.DoubleTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IArmoryResourceStorage extends INBTSerializable<DoubleTag> {

    /**
     * Sets the specified amount to the itemstack's resource charge.
     * @param amount the amount of charge to set.
     */
    void setCharge(double amount);

    /**
     * Adds the specified amount to the itemstack's resource charge.
     * @param amount the amount of charge to add.
     * @param max the charge to cap at when adding.
     */
    void addCharge(double amount, double max);

    /**
     * Gets the current charge level of the itemstack.
     * @return the itemstack's current charge level.
     */
    double getCharge();
}
