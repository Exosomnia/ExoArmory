package com.exosomnia.exoarmory.mixin.mixins;

import com.exosomnia.exoarmory.mixin.interfaces.IItemMixin;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Redirect(method = "isSameItemSameTags", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;areCapsCompatible(Lnet/minecraftforge/common/capabilities/CapabilityProvider;)Z"))
    private static boolean redirectIsSame(ItemStack original, CapabilityProvider<ItemStack> other) {
        return ((IItemMixin)original.getItem()).ignoreCapabilitiesForMatch() || original.areCapsCompatible(other);
    }
}
