package com.exosomnia.exoarmory.rendering.client.managers;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.items.armory.AbilityItem;
import com.exosomnia.exoarmory.items.armory.ArmoryItem;
import com.exosomnia.exoarmory.items.armory.ResourcedItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExoArmory.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RenderingManager {

    private final static double secondsVisible = 2.0;
    private final static double secondDeltaTotal = 20.0; //delta time total for one second
    private final static double deltaVisible = secondsVisible * secondDeltaTotal; //Total delta time to make displays visible
    private final static Minecraft mc = Minecraft.getInstance();

    ItemStack previousItem;
    double previousResource = -1.0;

    public double abilityVisibileTime = 0.0;
    public double resourceVisibleTime = 0.0;

    public void tick() {
        LocalPlayer player = mc.player;
        if (player == null) {
            previousItem = null;
            previousResource = -1.0;
            return;
        }

        abilityVisibileTime = Math.max(0.0, abilityVisibileTime - mc.getDeltaFrameTime());
        resourceVisibleTime = Math.max(0.0, resourceVisibleTime - mc.getDeltaFrameTime());
        ItemStack currentItem = player.getMainHandItem();

        boolean sameItem = currentItem == previousItem;
        boolean sameUUID = sameUUID(currentItem, previousItem);

        if (!sameItem && !sameUUID && (currentItem.getItem() instanceof AbilityItem)) { resetAbilityVisible(); }
        if (currentItem.getItem() instanceof ResourcedItem currentResourceItem) {
            if ( (!sameItem && !sameUUID) || (sameUUID && currentResourceItem.getResource(currentItem) != previousResource) ) {
                resetResourceVisible();
            }
            previousResource = currentResourceItem.getResource(currentItem);
        }
        previousItem = currentItem;
    }

    private boolean sameUUID(ItemStack currentItemStack, ItemStack previousItemStack) {
        if (previousItemStack == null) return false;
        else if ( (currentItemStack.getItem() instanceof ArmoryItem leftArmoryItem) && (previousItemStack.getItem() instanceof ArmoryItem rightArmoryItem) ) {
            return leftArmoryItem.getUUID(currentItemStack).equals(rightArmoryItem.getUUID(previousItemStack));
        }
        return false;
    }

    public void resetResourceVisible() {
        resourceVisibleTime = deltaVisible;
    }

    public void resetAbilityVisible() {
        abilityVisibileTime = deltaVisible;
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
}
