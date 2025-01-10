package com.exosomnia.exoarmory.managers;

import com.exosomnia.exoarmory.items.armory.swords.ShadowsEdgeSword;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConditionalManager {

    public enum Condition {
        SOLAR_SWORD,
        SHADOWS_EDGE
    }
    private final Map<UUID, Map<Condition, Boolean>> CONDITIONALS = new HashMap<>();

    public ConditionalManager() {
        MinecraftForge.EVENT_BUS.addListener(this::playerTickEvent);
    }

    public void addPlayer(Player player) {
        Map<Condition, Boolean> newPlayerMap = new HashMap<>();
        for (Condition condition : Condition.values()) {
            newPlayerMap.put(condition, false);
        }
        CONDITIONALS.put(player.getUUID(), newPlayerMap);
    }

    public void removePlayer(Player player) {
        CONDITIONALS.remove(player.getUUID());
    }

    public boolean getPlayerCondition(Player player, Condition condition) {
        return CONDITIONALS.get(player.getUUID()).get(condition);
    }

    public void setPlayerCondition(Player player, Condition condition, Boolean value) {
        CONDITIONALS.get(player.getUUID()).put(condition, value);
    }

    public void playerTickEvent(TickEvent.PlayerTickEvent event) {
        if (!event.side.equals(LogicalSide.SERVER) || !event.phase.equals(TickEvent.Phase.START)) { return; }

        Player player = event.player;
        ItemStack itemStack = player.getMainHandItem();
        Level level = player.level();
        if (level.getGameTime() % 10 == 0) {
            setPlayerCondition(player, Condition.SOLAR_SWORD,false);
            setPlayerCondition(player, Condition.SHADOWS_EDGE, false);
            if (player.isOnFire() || (!level.isRaining() && level.isDay() && level.canSeeSky(BlockPos.containing(player.getEyePosition())))) {
                setPlayerCondition(player, Condition.SOLAR_SWORD, true);
            }
            if (itemStack.getItem() instanceof ShadowsEdgeSword && level.getMaxLocalRawBrightness(player.blockPosition()) == 0) {
                setPlayerCondition(player, Condition.SHADOWS_EDGE, true);
            }
        }
    }
}
