package com.exosomnia.exoarmory.events;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.items.ResourcedItem;
import com.exosomnia.exoarmory.items.swords.ExoSwordItem;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber(modid = ExoArmory.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RenderGUIOverlayHandler {

    private static final ResourceLocation RESOURCE_BAR_EMPTY = new ResourceLocation(ExoArmory.MODID, "textures/gui/resource_bar_empty.png");
    private static final ResourceLocation RESOURCE_BAR_FILLED = new ResourceLocation(ExoArmory.MODID, "textures/gui/resource_bar_filled.png");

    private static final ResourceLocation ICON_FRAME = new ResourceLocation(ExoArmory.MODID, "textures/gui/icon/icon_frame.png");

    private static final int barWidth = 182; //Width of bar empty and filled textures
    private static final int iconWidth = 24; //Width of on/off icon

    @SubscribeEvent
    public static void renderGUIEvent(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        Item item = player.getMainHandItem().getItem();
        if ( (item instanceof ResourcedItem) ) {
            ResourcedItem resourceItem = (ResourcedItem)item;
            GuiGraphics gui = event.getGuiGraphics();

            double chargeAmount = 0.5;
            //double chargeAmount = data.getCharge() / data.MAX_CHARGE;
            //boolean toggle = RegistrationHandler.ACTIVATE.isDown();

            int scaledHeight = mc.getWindow().getGuiScaledHeight();
            int scaledWidth = mc.getWindow().getGuiScaledWidth() / 2;
            int filledWidth = (int) (barWidth * chargeAmount);
            float[] rgb = resourceItem.getBarRGB();
            ResourceLocation icon = resourceItem.getBarIcon();

            RenderSystem.setShaderColor(rgb[0], rgb[1], rgb[2], 1.0F);
            gui.blit(RESOURCE_BAR_EMPTY, scaledWidth - barWidth / 2, scaledHeight - 56, 0, 0, barWidth, 5, barWidth, 5);
            gui.blit(RESOURCE_BAR_FILLED, scaledWidth - barWidth / 2, scaledHeight - 56, 0, 0, filledWidth, 5, barWidth, 5);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            gui.blit(ICON_FRAME, scaledWidth - 24 / 2, scaledHeight - 78, 0, 0, 24, 24, 24, 24);
            gui.blit(icon, scaledWidth - 16 / 2, scaledHeight - 74, 0, 0, 16, 16, 16, 16);

            gui.drawString(mc.font, "Solar Flare", (scaledWidth - 16 / 2) + 14, scaledHeight - 74, 0xFF8000);
            //gui.blit(toggle ? CHARGE_TOGGLE_ON : CHARGE_TOGGLE_OFF, (scaledWidth - iconWidth / 2) + 4, scaledHeight - 61, 0, 0, 15, 15, 15, 15);


        }
    }
}
