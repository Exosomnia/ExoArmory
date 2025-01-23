package com.exosomnia.exoarmory.mixins;

import com.exosomnia.exoarmory.accessors.EntityAccessor;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityAccessor {

    private boolean clientGlowing = false;

    public void setClientGlowing(boolean flag) { clientGlowing = flag; }

    @Inject(method = "isCurrentlyGlowing", at = @At("RETURN"), cancellable = true)
    private void isCurrentlyGlowing(CallbackInfoReturnable<Boolean> ci) {
        ci.setReturnValue(ci.getReturnValue() || this.clientGlowing);
    }
}
