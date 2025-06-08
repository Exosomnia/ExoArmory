package com.exosomnia.exoarmory.item.ability.event.handlers;

import com.exosomnia.exoarmory.item.ability.AbilityItem;
import com.exosomnia.exoarmory.item.ability.ArmoryAbility;
import com.exosomnia.exoarmory.item.ability.event.interfaces.AbilityEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

import java.util.HashSet;

public abstract class AbilityHandler<T extends Event> {

    public record Context<T extends Event>(T event, EquipmentSlot slot, LivingEntity triggerEntity, ItemStack triggerStack, HashSet<ArmoryAbility> handledAbilities){}

    protected void handleEvent(T event, LivingEntity triggerEntity) {
        HashSet<ArmoryAbility> handledAbilities = new HashSet<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {

            ItemStack triggerStack = triggerEntity.getItemBySlot(slot);
            if (triggerStack.isEmpty() || !(triggerStack.getItem() instanceof AbilityItem triggerAbilityItem)) continue;

            Context<T> context = new Context<>(event, slot, triggerEntity, triggerStack, handledAbilities);
            for (ArmoryAbility ability : triggerAbilityItem.getAbilities(triggerStack, triggerEntity)) {
                Class<? extends AbilityEvent<T>> eventInterface = getInterface();
                if (!(eventInterface.isInstance(ability))) continue;

                if (eventInterface.cast(ability).handle(context)) handledAbilities.add(ability);
            }
        }
    }

    protected abstract Class<? extends AbilityEvent<T>> getInterface();
}
