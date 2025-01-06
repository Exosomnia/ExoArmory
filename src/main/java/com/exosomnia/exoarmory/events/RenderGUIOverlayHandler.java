package com.exosomnia.exoarmory.events;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.client.managers.DisplayManager;
import com.exosomnia.exoarmory.items.AbilityItem;
import com.exosomnia.exoarmory.items.ResourcedItem;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExoArmory.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RenderGUIOverlayHandler {

    private static final ResourceLocation RESOURCE_BAR = new ResourceLocation(ExoArmory.MODID, "textures/gui/resource_bar.png");
    private static final ResourceLocation ICON_FRAME = new ResourceLocation(ExoArmory.MODID, "textures/gui/icon/icon_frame.png");
    private static final ResourceLocation NINE_SLICE = new ResourceLocation(ExoArmory.MODID, "textures/gui/nineslice_accent1.png");

    private static final int barWidth = 182; //Width of bar empty and filled textures

    private static final int iconWidth = 14; //Width of ability icon
    private static final int frameWidth = 22; //Width of ability frame

    @SubscribeEvent
    public static void renderGUIEvent(RenderGuiEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        GuiGraphics gui = event.getGuiGraphics();
        DisplayManager displayManager = ExoArmory.DISPLAY_MANAGER;
        ItemStack itemStack = player.getMainHandItem();
        Item item = itemStack.getItem();

        int baseHeight = mc.getWindow().getGuiScaledHeight() - 61; //Represents the y value to draw from to avoid other HUD elements like armor or health
        int baseWidth = mc.getWindow().getGuiScaledWidth() / 2; //Represents the x value to draw from, the middle of the screen

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        float visibility;
        if (item instanceof ResourcedItem resourceItem) {
            visibility = (float)displayManager.getResourceVisibility();
            if (visibility > 0) {
                double chargeAmount = resourceItem.getResource(itemStack) / resourceItem.getResourceMax();

                int filledWidth = (int) (barWidth * chargeAmount);
                float[] rgb = resourceItem.getBarRGB();

                RenderSystem.setShaderColor(rgb[0], rgb[1], rgb[2], visibility);
                gui.blit(RESOURCE_BAR, baseWidth - barWidth / 2, baseHeight - 5, 0, 5, barWidth, 5, barWidth, 10);
                gui.blit(RESOURCE_BAR, baseWidth - barWidth / 2, baseHeight - 5, 0, 0, filledWidth, 5, barWidth, 10);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }

        if (item instanceof AbilityItem abilityItem) {
            visibility = (float)displayManager.getAbilityVisibility();
            if (visibility > 0) {
                ResourceLocation icon = abilityItem.getAbilityIcon();
                String name = abilityItem.getAbilityName().getString();

                int abilityFontWidth = mc.font.width(name);
                int rgb = abilityItem.getAbilityRGB();
                int abilityGuiWidth = frameWidth + abilityFontWidth + 13; //14 is the padding used for the text, but since we draw 1 pixel into the frame, we pad 13
                int abilityBaseWidth = baseWidth - (abilityGuiWidth / 2);

                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, visibility);

                gui.blit(icon, abilityBaseWidth + 4, baseHeight - 24, 0, 0, iconWidth, iconWidth, iconWidth, iconWidth);
                gui.blit(ICON_FRAME, abilityBaseWidth, baseHeight - 28, 0, 0, frameWidth, frameWidth, frameWidth, frameWidth);

                gui.blitNineSlicedSized(NINE_SLICE, abilityBaseWidth + 21, baseHeight - 27, abilityFontWidth + 14, 20, 11, 11, 40, 11, 62, 62, 0, 0, 62, 62);

                int color = (255 << 24) | rgb;
                gui.drawString(mc.font, name, abilityBaseWidth + 28, baseHeight - 21, color);

                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }

        RenderSystem.disableBlend();
    }
}
