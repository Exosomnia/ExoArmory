package com.exosomnia.exoarmory.client.rendering;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.items.abilities.AbilityItem;
import com.exosomnia.exoarmory.items.armory.ArmoryItem;
import com.exosomnia.exoarmory.items.resource.ArmoryResource;
import com.exosomnia.exoarmory.items.resource.ResourcedItem;
import com.exosomnia.exoarmory.managers.AbilityManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;

public class RenderingManager {

    private final static double SECONDS_VISIBLE = 2.5;
    private final static double DELTA_PER_SECOND = 20.0; //delta time total for one second
    private final static double DELTA_VISIBLE = SECONDS_VISIBLE * DELTA_PER_SECOND; //Total delta time to make displays visible
    private final static Minecraft MC = Minecraft.getInstance();

    private ItemStack previousItem;
    private double previousResource = -1.0;

    private double abilityVisibileTime = 0.0;
    private double resourceVisibleTime = 0.0;

    private static int totalTicks = 0;

    public void tick() {
        LocalPlayer player = MC.player;
        if (player == null) {
            previousItem = null;
            previousResource = -1.0;
            return;
        }

        totalTicks++;

        //Resource and ability rendering logic
        abilityVisibileTime = Math.max(0.0, abilityVisibileTime - 1.0);
        resourceVisibleTime = Math.max(0.0, resourceVisibleTime - 1.0);
        ItemStack currentItem = player.getMainHandItem();

        boolean sameItem = currentItem == previousItem;
        boolean sameUUID = sameUUID(currentItem, previousItem);

        if (!sameItem && !sameUUID && (currentItem.getItem() instanceof AbilityItem)) { resetAbilityVisible(); }
        if (currentItem.getItem() instanceof ResourcedItem currentResourceItem) {
            ArmoryResource resource = currentResourceItem.getResource();
            if ( (!sameItem && !sameUUID) || (sameUUID && resource.getResource(currentItem) != previousResource) || ExoArmory.ABILITY_MANAGER.isPlayerActive(player) ) {
                resetResourceVisible();
            }
            previousResource = resource.getResource(currentItem);
        }
        previousItem = currentItem;
    }

    private boolean sameUUID(ItemStack currentItemStack, ItemStack previousItemStack) {
        if (previousItemStack == null) return false;
        else if ( (currentItemStack.getItem() instanceof ArmoryItem leftItem) && (previousItemStack.getItem() instanceof ArmoryItem rightItem) ) {
            return leftItem.getUUID(currentItemStack).equals(rightItem.getUUID(previousItemStack));
        }
        return false;
    }

    public void resetResourceVisible() {
        resourceVisibleTime = DELTA_VISIBLE;
    }

    public void resetAbilityVisible() {
        abilityVisibileTime = DELTA_VISIBLE;
    }

    /***
     * Gets the alpha visibility for the resource display
     * @return a double (1.0 - 0.0)
     */
    public double getResourceVisibility() {
        return Math.min(1.0, resourceVisibleTime / 20.0);
    }

    /***
     * Gets the alpha visibility for the ability display
     * @return a double (1.0 - 0.0)
     */
    public double getAbilityVisibility() {
        return Math.min(1.0, abilityVisibileTime / 20.0);
    }

    public int getTotalTicks() { return totalTicks; }
}
