package com.exosomnia.exoarmory.networking.events.client;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExoArmory.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientNetworkEventHandler {

    @SubscribeEvent
    public static void playerLogInClient(ClientPlayerNetworkEvent.LoggingOut event) {
        ExoArmory.ABILITY_MANAGER.clear();
    }
}
