package com.exosomnia.exoarmory;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = ExoArmory.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue HIDE_HELP = BUILDER
            .comment("Hides the \"Press Shift/Ctrl for more info\" tip in item tooltips.")
            .define("hideHelp", false);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean hideHelp;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        hideHelp = HIDE_HELP.get();
    }
}
