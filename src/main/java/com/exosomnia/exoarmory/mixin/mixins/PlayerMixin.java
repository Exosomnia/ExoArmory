package com.exosomnia.exoarmory.mixin.mixins;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "destroyVanishingCursedItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;removeItemNoUpdate(I)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void soulboundVanishingCancel(CallbackInfo ci, int i, ItemStack itemStack) {
        if (itemStack.getEnchantmentLevel(ExoArmory.REGISTRY.ENCHANTMENT_SOULBOUND.get()) > 0) { ci.cancel(); }
    }
}
