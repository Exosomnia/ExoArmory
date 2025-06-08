package com.exosomnia.exoarmory.mixin.mixins;

import com.exosomnia.exoarmory.mixin.interfaces.IItemMixin;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {

    @Redirect(method = "synchronizeSlotToRemote", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean redirectShouldNotSynchronizeAtMatches(ItemStack original, ItemStack other) {
        return (ItemStack.matches(original, other) && !((IItemMixin)original.getItem()).shouldResendData(original, other));
    }

    @Redirect(method = "synchronizeSlotToRemote", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;equals(Lnet/minecraft/world/item/ItemStack;Z)Z"))
    public boolean redirectShouldNotSynchronizeAtEquals(ItemStack original, ItemStack other, boolean limitTags) {
        return (original.equals(other, limitTags) && !((IItemMixin)original.getItem()).shouldResendData(original, other));
    }
}
