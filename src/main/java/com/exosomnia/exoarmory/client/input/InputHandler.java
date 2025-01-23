package com.exosomnia.exoarmory.client.input;

import com.exosomnia.exoarmory.Config;
import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.networking.PacketHandler;
import com.exosomnia.exoarmory.networking.packets.AbilityActivePacket;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExoArmory.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InputHandler {

    @SubscribeEvent
    public static void inputEvent(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.getKey() != ExoArmory.REGISTRY.KEY_ACTIVATE.getKey().getValue()) return;

        LocalPlayer player = mc.player;
        if (player == null) return;

        if (!mc.isPaused() && mc.screen == null && event.getAction() == InputConstants.PRESS) {
            AbilityActivePacket packet = new AbilityActivePacket(!Config.toggleActivation || !ExoArmory.ABILITY_MANAGER.isPlayerActive(player));
            ExoArmory.ABILITY_MANAGER.setPlayerActive(player, packet.active);
            PacketHandler.sendToServer(new AbilityActivePacket(packet.active));
        }
        else if (event.getAction() == InputConstants.RELEASE && !Config.toggleActivation) {
            ExoArmory.ABILITY_MANAGER.setPlayerActive(player, false);
            PacketHandler.sendToServer(new AbilityActivePacket(false));
        }
    }
}
