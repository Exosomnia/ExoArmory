package com.exosomnia.exoarmory.item.perks.event.handlers;

import com.exosomnia.exoarmory.item.perks.ItemPerk;
import com.exosomnia.exoarmory.item.perks.ability.AbilityItem;
import com.exosomnia.exoarmory.item.perks.resource.ResourceItem;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

import java.util.HashSet;
import java.util.function.BiFunction;

/**                         ---PERK SYSTEM 101---
 *  The Perk System has 3 main parts, ItemPerks, PerkEvents, and PerkHandlers
 * <p>
 *  ItemPerks:
 *      These represent basically any special effects an item may have, all
 *      ArmoryAbility and ArmoryResource are considered ItemPerks. ItemPerks
 *      may want to perform some action when a certain event occurs, to do
 *      so they implement PerkEvent interfaces.
 * <p>
 *  PerkEvents:
 *      An interface for a single, specific event a perk may want to take
 *      action on. For example, an ItemPerk may want to take action when
 *      the LivingHurtEvent is called. a PerkEvent interface has a single
 *      method that the ItemPerk will implement that defines the logic to
 *      be performed when the action happens.
 * <p>
 *  PerkHandlers:
 *      Actual objects that subscribe to events the PerkEvents run on. A
 *      Perk handler is responsible for determining the triggering LivingEntity
 *      in the fired event (if one exists), gathering all active ItemPerks,
 *      building the context of the event, and then dispatching that context
 *      to the specific PerkEvent it handles.
 * <p>
 *  EXAMPLE:
 * <p>
 *  A LivingHurtEvent is fired when a LivingEntity takes damage, the
 *  LivingHurtPerkHandler receives the event. It parses the event to
 *  determine the attacker and defender as involvedEntities. It then
 *  loops through all equipment of the involved entities then loops
 *  through all perks on each of the items. If the perk implements
 *  the PerkEvent interface for this event, the created context is
 *  passed to the perk to modify the event.
 */

public abstract class PerkHandler<E extends Event, P> {

    Class<P> perkInterface;
    BiFunction<P, Context<E>, Boolean> dispatchEvent;

    public PerkHandler(Class<P> perkInterface, BiFunction<P, Context<E>, Boolean> dispatchEvent) {
        this.perkInterface = perkInterface;
        this.dispatchEvent = dispatchEvent;
    }

    public record Context<E extends Event>(E event, EquipmentSlot slot, LivingEntity triggerEntity, ItemStack triggerStack, HashSet<ItemPerk> handledPerks) {}

    protected void handleEvent(E event, HashSet<LivingEntity> involvedEntities) {
        for (LivingEntity triggerEntity : involvedEntities) {
            if(triggerEntity.level().isClientSide) return;

            HashSet<ItemPerk> handledPerks = new HashSet<>();
            for (EquipmentSlot slot : EquipmentSlot.values()) {

                ItemStack triggerStack = triggerEntity.getItemBySlot(slot);
                if (triggerStack.isEmpty() || !(triggerStack.getItem() instanceof AbilityItem abilityItem)) continue;

                Context<E> context = new Context<>(event, slot, triggerEntity, triggerStack, handledPerks);
                ObjectArraySet<ItemPerk> perks = new ObjectArraySet<>();
                if (abilityItem instanceof ResourceItem resourceItem) perks.add(resourceItem.getResource());
                perks.addAll(abilityItem.getAbilities(triggerStack, triggerEntity).keySet());

                for (ItemPerk perk : perks) {
                    if (!(perkInterface.isInstance(perk))) continue;

                    if (dispatchEvent.apply(perkInterface.cast(perk), context)) handledPerks.add(perk);
                }
            }
        }
    }
}
