package com.exosomnia.exoarmory.items;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraft.resources.ResourceLocation;

public interface ActivatableItem {

    ResourceLocation getActivateIcon();
    default ResourceLocation iconResourcePath(String name) {
        return ResourceLocation.fromNamespaceAndPath(ExoArmory.MODID, String.format("textures/gui/icon/active/%s.png", name));
    }
}
