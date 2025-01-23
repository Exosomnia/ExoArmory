package com.exosomnia.exoarmory.networking.events.server;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExoArmory.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerNetworkEventHandler {

    @SubscribeEvent
    public static void playerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        ExoArmory.CONDITIONAL_MANAGER.addPlayer(event.getEntity());
        ExoArmory.ABILITY_MANAGER.addPlayer(event.getEntity());
    }

    @SubscribeEvent
    public static void playerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        ExoArmory.CONDITIONAL_MANAGER.removePlayer(event.getEntity());
        ExoArmory.ABILITY_MANAGER.removePlayer(event.getEntity());
    }
}
