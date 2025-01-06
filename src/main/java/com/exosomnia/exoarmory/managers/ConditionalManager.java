package com.exosomnia.exoarmory.managers;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = ExoArmory.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ConditionalManager {

    public static boolean SOLAR_TICK = false;
    public static HashMap<Player, Boolean> SOLAR_CONDITIONS = new HashMap<>();

    @SubscribeEvent
    public static void playerTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.side.equals(LogicalSide.SERVER) && event.phase.equals(TickEvent.Phase.START)) {
            SOLAR_TICK = false;
            Player eventPlayer = event.player;
            Level eventLevel = eventPlayer.level();
            if (eventLevel.getGameTime() % 10 == 0) {
                SOLAR_TICK = true;
                if (eventPlayer.isOnFire() || (!eventLevel.isRaining() && eventLevel.isDay() && eventLevel.canSeeSky(BlockPos.containing(eventPlayer.getEyePosition())))) {
                    SOLAR_CONDITIONS.put(eventPlayer, true);
                    return;
                }
                SOLAR_CONDITIONS.put(eventPlayer, false);
            }
        }
    }
}
