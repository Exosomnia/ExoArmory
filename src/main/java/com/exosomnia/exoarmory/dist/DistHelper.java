package com.exosomnia.exoarmory.dist;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public abstract class DistHelper {

    public abstract Level getDefaultLevel();

    public abstract Entity getEntity(UUID target);

    public abstract Player getDefaultPlayer();
}