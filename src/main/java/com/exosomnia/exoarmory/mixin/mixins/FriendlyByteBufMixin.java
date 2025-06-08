package com.exosomnia.exoarmory.mixin.mixins;

import com.exosomnia.exoarmory.mixin.interfaces.IItemMixin;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FriendlyByteBuf.class)
public abstract class FriendlyByteBufMixin {

    @Inject(method = "writeItemStack(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/network/FriendlyByteBuf;", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;writeNbt(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/network/FriendlyByteBuf;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void injectWriteItemStack(ItemStack itemStack, boolean limitedTag, CallbackInfoReturnable<FriendlyByteBuf> ci, Item item) {
        ((IItemMixin)item).writeItemNetworkData(itemStack, (FriendlyByteBuf)((Object)this));
    }

    @Inject(method = "readItem()Lnet/minecraft/world/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;readShareTag(Lnet/minecraft/nbt/CompoundTag;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void injectReadItemStack(CallbackInfoReturnable<ItemStack> ci, Item item, int i, ItemStack itemStack) {
        ((IItemMixin)item).readItemNetworkData(itemStack, (FriendlyByteBuf)((Object)this));
    }
}
