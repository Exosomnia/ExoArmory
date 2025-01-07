package com.exosomnia.exoarmory.items.armory;

import net.minecraft.world.item.ItemStack;

public interface ResourcedItem extends AbilityItem {

    public float[] getBarRGB();
    public double getResource(ItemStack itemStack);
    public double getResourceMax();
}
