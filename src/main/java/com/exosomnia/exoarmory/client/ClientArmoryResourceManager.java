package com.exosomnia.exoarmory.client;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ExoArmory.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientArmoryResourceManager {

    public static HashMap<UUID, Double> resourceMap = new HashMap<>();

    public void setResource(UUID uuid, double amount) {
        resourceMap.put(uuid, amount);
    }

    public double getResource(UUID uuid) {
        return resourceMap.getOrDefault(uuid, 0.0);
    }

    @SubscribeEvent
    public static void playerLogOutClient(ClientPlayerNetworkEvent.LoggingOut event) {
        resourceMap.clear();
    }
}
