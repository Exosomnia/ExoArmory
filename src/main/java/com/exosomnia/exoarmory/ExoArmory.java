package com.exosomnia.exoarmory;

import com.exosomnia.exoarmory.client.managers.DisplayManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExoArmory.MODID)
public class ExoArmory
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "exoarmory";
    public static final Registry REGISTRY = new Registry();

    @OnlyIn(Dist.CLIENT)
    public static DisplayManager DISPLAY_MANAGER;

    public ExoArmory()
    {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        REGISTRY.registerCommon();
        REGISTRY.registerObjects(modBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modBus.addListener(this::setupClient) );
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.addListener(this::clientTick) );
    }

    @OnlyIn(Dist.CLIENT)
    public void setupClient(FMLClientSetupEvent event) {
        DISPLAY_MANAGER = new DisplayManager();
    }

    @OnlyIn(Dist.CLIENT)
    public void clientTick(TickEvent.ClientTickEvent event) {
        DISPLAY_MANAGER.tick();
    }
}
