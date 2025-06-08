package com.exosomnia.exoarmory.mixin.mixins;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.utils.AttributeUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolActions;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @ModifyConstant(method = "isBlocking", constant = @Constant(intValue = 5))
    private int isCurrentlyBlocking(int warmup) {
        return 0;
    }

    @Inject(method = "hurt", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;invulnerableTime:I", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void stopBlockingInvuln(DamageSource source, float damage, CallbackInfoReturnable<Boolean> ci, float f, boolean flag) {
        if (flag) { this.invulnerableTime = 10; }
    }

    @Inject(method = "stopUsingItem", at = @At("INVOKE"))
    private void blockingCooldown(CallbackInfo ci) {
        LivingEntity defender = (LivingEntity)(Object)this;
        ItemStack itemStack = defender.getUseItem();
        if (!(defender instanceof Player player) || !itemStack.canPerformAction(ToolActions.SHIELD_BLOCK)) { return; }
        InteractionHand hand = defender.getUsedItemHand();
        int stabilityTicks = (int) (AttributeUtils.getAttributeValueOfItemStack(ExoArmory.REGISTRY.ATTRIBUTE_SHIELD_STABILITY.get(),
                hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND, itemStack,
                player.getAttributeBaseValue(ExoArmory.REGISTRY.ATTRIBUTE_SHIELD_STABILITY.get())) * 20.0);
        ItemCooldowns cooldowns = player.getCooldowns();
        for (Item shieldItem : ExoArmory.REGISTRY.SHIELDING_ITEMS) {
            cooldowns.addCooldown(shieldItem, Mth.clamp((player.getUseItem().getUseDuration() - player.getUseItemRemainingTicks()), 10, Math.min(stabilityTicks, 100)));
        }
    }
}
