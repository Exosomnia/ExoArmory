package com.exosomnia.exoarmory.item.perks.event.handlers;

import com.exosomnia.exoarmory.item.perks.ItemPerk;
import com.exosomnia.exoarmory.item.perks.ability.AbilityItem;
import com.exosomnia.exoarmory.item.perks.event.interfaces.PerkEvent;
import com.exosomnia.exoarmory.item.perks.resource.ResourceItem;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

import java.util.HashSet;

public abstract class PerkHandler<T extends Event> {

    public record Context<T extends Event>(T event, EquipmentSlot slot, LivingEntity triggerEntity, ItemStack triggerStack, HashSet<ItemPerk> handledPerks){}

    protected void handleEvent(T event, LivingEntity triggerEntity) {
        HashSet<ItemPerk> handledPerks = new HashSet<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {

            ItemStack triggerStack = triggerEntity.getItemBySlot(slot);
            if (triggerStack.isEmpty() || !(triggerStack.getItem() instanceof AbilityItem abilityItem)) continue;

            Context<T> context = new Context<>(event, slot, triggerEntity, triggerStack, handledPerks);
            ObjectArraySet<ItemPerk> perks = new ObjectArraySet<ItemPerk>(abilityItem.getAbilities(triggerStack, triggerEntity).keySet());
            if (abilityItem instanceof ResourceItem resourceItem) perks.add(resourceItem.getResource());

            for (ItemPerk perk : perks) {
                Class<? extends PerkEvent<T>> eventInterface = getInterface();
                if (!(eventInterface.isInstance(perk))) continue;

                if (eventInterface.cast(perk).handle(context)) handledPerks.add(perk);
            }
        }
    }

    protected abstract Class<? extends PerkEvent<T>> getInterface();
}
