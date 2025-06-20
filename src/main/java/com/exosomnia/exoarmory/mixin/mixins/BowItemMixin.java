package com.exosomnia.exoarmory.mixin.mixins;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.item.ReinforcedBowItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BowItem.class)
public abstract class BowItemMixin {

    @ModifyArg(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setBaseDamage(D)V"))
    private double powerNerf(double power) {
        return power - 0.5;
    }

    @Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void releaseUsingRecovery(ItemStack bow, Level level, LivingEntity shooter, int time, CallbackInfo ci, Player player, boolean flag, ItemStack itemstack, int i, float f, boolean flag1, ArrowItem arrowitem, AbstractArrow abstractArrow, int j, int k){
        double recoveryChance = (player.getAttributeValue(ExoArmory.REGISTRY.ATTRIBUTE_ARROW_RECOVERY.get()) - 1.0);
        if (level.random.nextDouble() < recoveryChance) abstractArrow.getPersistentData().putBoolean("Recovery", true);
    }

    @Redirect(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BowItem;getPowerForTime(I)F"))
    public float releaseUsingPowerForTime(int time) {
        return (((Object)this) instanceof ReinforcedBowItem reinforcedBowItem) ? ReinforcedBowItem.getPowerForTime(reinforcedBowItem, time) : BowItem.getPowerForTime(time);
    }
}
