package com.exosomnia.exoarmory.mixins;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Shadow
    private BlockPos destroyBlockPos;
    @Shadow
    private ItemStack destroyingItem;

    @Inject(method="sameDestroyTarget", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void modSameDestroyTarget(BlockPos p_105282_, CallbackInfoReturnable<Boolean> ci, ItemStack itemstack) {
        if (!ci.getReturnValueZ()) {
            ci.setReturnValue(p_105282_.equals(this.destroyBlockPos) && (ItemStack.isSameItemSameTags(itemstack, this.destroyingItem) || !destroyingItem.shouldCauseBlockBreakReset(itemstack)));
        }
    }
}
