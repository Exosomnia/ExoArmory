package com.exosomnia.exoarmory.managers;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class DataManager {

    public DataManager() {
        IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(this::playerLogIn);
        bus.addListener(this::playerLogOut);
    }

    public void playerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        ExoArmory.CONDITIONAL_MANAGER.addPlayer(event.getEntity());
    }

    public void playerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        ExoArmory.CONDITIONAL_MANAGER.removePlayer(event.getEntity());
    }
}
