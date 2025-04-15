package com.exosomnia.exoarmory;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = ExoArmory.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue HIDE_HELP = BUILDER
            .comment("Hides the \"Press Shift/Ctrl for more info\" tip in item tooltips.")
            .define("hideHelp", false);

    private static final ForgeConfigSpec.BooleanValue SHOW_RESOURCE_AMOUNT = BUILDER
            .comment("Shows/hides the numerical resource amount when displaying the resource bar.")
            .define("showResourceAmount", true);

    private static final ForgeConfigSpec.BooleanValue ABILITY_ACTIVE_TOGGLE = BUILDER
            .comment("Determines if activating abilities are done via toggle or being held.")
            .define("toggleActivation", false);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean hideHelp;
    public static boolean showResourceAmount;
    public static boolean toggleActivation;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        hideHelp = HIDE_HELP.get();
        showResourceAmount = SHOW_RESOURCE_AMOUNT.get();
        toggleActivation = ABILITY_ACTIVE_TOGGLE.get();
    }
}
