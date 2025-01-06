package com.exosomnia.exoarmory.items;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public interface AbilityItem {

    public String getItemName();
    public int getAbilityRGB();

    public default MutableComponent getAbilityName() {
        return Component.translatable(String.format("ability.exoarmory.name.%s", getItemName()));
    }

    public default MutableComponent getAbilityDesc() {
        return Component.translatable(String.format("ability.exoarmory.desc.%s", getItemName()));
    }

    public default ResourceLocation getAbilityIcon(){
        return new ResourceLocation(ExoArmory.MODID, String.format("textures/gui/icon/%s.png", getItemName()));
    }

}
