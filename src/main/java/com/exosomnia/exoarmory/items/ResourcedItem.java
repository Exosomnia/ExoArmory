package com.exosomnia.exoarmory.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public interface ResourcedItem extends AbilityItem {

    public float[] getBarRGB();
    public double getResource(ItemStack itemStack);
    public double getResourceMax();

    public default MutableComponent getResourceName() {
        return Component.translatable(String.format("resource.exoarmory.name.%s", getItemName()));
    }

    public default MutableComponent getResourceDesc() {
        return Component.translatable(String.format("resource.exoarmory.desc.%s", getItemName()));
    }
}
