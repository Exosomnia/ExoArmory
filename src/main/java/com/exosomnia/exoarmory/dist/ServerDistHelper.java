package com.exosomnia.exoarmory.dist;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;

public class ServerDistHelper extends DistHelper {

    @Override
    public Level getDefaultLevel() {
        return ServerLifecycleHooks.getCurrentServer().overworld();
    }

    @Override
    public Entity getEntity(UUID target) {
        return null;
    }

    @Override
    public Player getDefaultPlayer() {
        return null;
    }
}
