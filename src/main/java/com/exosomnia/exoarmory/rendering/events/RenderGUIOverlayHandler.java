package com.exosomnia.exoarmory.rendering.events;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.items.abilities.ArmoryAbility;
import com.exosomnia.exoarmory.rendering.client.managers.RenderingManager;
import com.exosomnia.exoarmory.items.armory.AbilityItem;
import com.exosomnia.exoarmory.items.armory.ResourcedItem;
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
        RenderingManager renderingManager = ExoArmory.DISPLAY_MANAGER;
        ItemStack itemStack = player.getMainHandItem();
        Item item = itemStack.getItem();

        int baseHeight = mc.getWindow().getGuiScaledHeight() - 61; //Represents the y value to draw from to avoid other HUD elements like armor or health
        int baseWidth = mc.getWindow().getGuiScaledWidth() / 2; //Represents the x value to draw from, the middle of the screen

        RenderSystem.defaultBlendFunc();

        float visibility;
        if (item instanceof ResourcedItem resourceItem) {
            visibility = (float) renderingManager.getResourceVisibility();
            if (visibility > 0) {
                RenderSystem.enableBlend();
                double chargeAmount = resourceItem.getResource(itemStack) / resourceItem.getResourceMax();

                int filledWidth = (int) (barWidth * chargeAmount);
                float[] rgb = resourceItem.getBarRGB();

                RenderSystem.setShaderColor(rgb[0], rgb[1], rgb[2], visibility);
                gui.blit(RESOURCE_BAR, baseWidth - barWidth / 2, baseHeight - 5, 0, 5, barWidth, 5, barWidth, 10);
                gui.blit(RESOURCE_BAR, baseWidth - barWidth / 2, baseHeight - 5, 0, 0, filledWidth, 5, barWidth, 10);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

                RenderSystem.disableBlend();
            }
        }

        if (item instanceof AbilityItem abilityItem) {
            visibility = (float) renderingManager.getAbilityVisibility();
            if (visibility > 0) {
                ArmoryAbility[] abilities = abilityItem.getAbilities();

                //Because we pad two pixels between each ability when drawing, start at -2 width
                int abilitiesWidth = -2;
                //Records that store the starting x draw location for each ability, avoids having to calculate them again after calculating total width
                int[] drawSections = new int[abilities.length];
                int index = 0; //Keeps track of the array index

                /*
                    Logic:  1. Apply padding
                            2. Store the current width (Will be used to draw frame and icon)
                            3. Calculate text width and add to total width
                 */
                for (ArmoryAbility ability : abilities) {
                    //Begin by applying padding and store the current width
                    abilitiesWidth += 2;
                    drawSections[index++] = abilitiesWidth;

                    String name = ability.getDisplayName();
                    int nameWidth = mc.font.width(name);
                    abilitiesWidth += frameWidth + nameWidth + 13;
                }
                index = 0; //Reset this back to 0, as we will use it for the actual drawing

                //Calculate initial x and...
                int initialX = baseWidth - (abilitiesWidth / 2);

                //Begin drawing
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, visibility);
                for (ArmoryAbility ability : abilities) {
                    RenderSystem.enableBlend(); //We have to do this because the last step, drawing the text, disables blending after it finishes
                    ResourceLocation icon = ability.getIcon();
                    String name = ability.getDisplayName();
                    int nameWidth = mc.font.width(name);
                    int rgb = ability.getRgb();

                    //The x value to base our drawing off of for this ability based on the initial X and the widths previous calculated
                    int thisDrawX = initialX + drawSections[index];

                    gui.blit(icon, thisDrawX + 4, baseHeight - 24, 0, 0, iconWidth, iconWidth, iconWidth, iconWidth);
                    gui.blit(ICON_FRAME, thisDrawX, baseHeight - 28, 0, 0, frameWidth, frameWidth, frameWidth, frameWidth);

                    gui.blitNineSlicedSized(NINE_SLICE, thisDrawX + 21, baseHeight - 27, nameWidth + 14, 20, 11, 11, 40, 11, 62, 62, 0, 0, 62, 62);

                    int color = (255 << 24) | rgb;
                    gui.drawString(mc.font, name, thisDrawX + 28, baseHeight - 21, color);

                    index++;
                }
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
