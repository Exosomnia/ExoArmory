package com.exosomnia.exoarmory.utils;

import com.exosomnia.exoarmory.capabilities.armory.item.resource.ArmoryResourceProvider;
import com.exosomnia.exoarmory.capabilities.armory.item.resource.ArmoryResourceStorage;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ResourceItemUtils {

    /**
     * Gets the current resource charge on the provided ItemStack.
     * @param itemStack the ItemStack to get the resource charge from
     * @return The resource charge on the ItemStack, or 0 if resource data doesn't exist
     */
    public static double getResourceCharge(ItemStack itemStack) {
        Optional<ArmoryResourceStorage> resourceCapability = itemStack.getCapability(ArmoryResourceProvider.ARMORY_RESOURCE).resolve();
        return resourceCapability.isPresent() ? resourceCapability.get().getCharge() : 0.0;
    }

    /**
     * Removes the specified amount of resource charge on the provided ItemStack, to a minimum of 0.
     * If the item does not have resource data, no action is taken.
     * @param itemStack the ItemStack to get the resource charge from
     * @param amount the amount of charge cost to remove
     */
    public static void removeResourceCharge(ItemStack itemStack, double amount) {
        itemStack.getCapability(ArmoryResourceProvider.ARMORY_RESOURCE).resolve().ifPresent(resourceCapability ->
                resourceCapability.removeCharge(amount));
    }

    /**
     * Checks if the provided ItemStack has at least the specified resource charge.
     * If so, reduce the charge on the ItemStack and execute the result.
     * @param itemStack the ItemStack to get the resource charge from
     * @param cost the charge cost to spend
     * @param result the Runnable to execute if enough charge exists
     * @return true if the provided ItemStack has enough charge, false otherwise.
     */
    public static boolean spendChargeOn(ItemStack itemStack, double cost, Runnable result) {
        double itemCharge = getResourceCharge(itemStack);

        if (itemCharge >= cost) {
            result.run();
            removeResourceCharge(itemStack, cost);
            return true;
        }

        return false;
    }
}
