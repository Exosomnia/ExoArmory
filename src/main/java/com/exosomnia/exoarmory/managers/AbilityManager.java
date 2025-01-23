package com.exosomnia.exoarmory.managers;

import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class AbilityManager {

    private final Map<UUID, Boolean> isActive = new HashMap<>();

    public void addPlayer(Player player) { isActive.put(player.getUUID(), false); }
    public void setPlayerActive(Player player, Boolean active) { isActive.put(player.getUUID(), active); }
    public Boolean isPlayerActive(Player player) { return isActive.get(player.getUUID()); }
    public void removePlayer(Player player) {
        isActive.remove(player.getUUID());
    }

    public void clear() { isActive.clear(); }
}
