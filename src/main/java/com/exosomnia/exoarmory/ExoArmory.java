package com.exosomnia.exoarmory;

import com.exosomnia.exoarmory.actions.ActionManager;
import com.exosomnia.exoarmory.managers.ConditionalManager;
import com.exosomnia.exoarmory.managers.DataManager;
import com.exosomnia.exoarmory.rendering.client.RenderingManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExoArmory.MODID)
public class ExoArmory
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "exoarmory";
    public static final Registry REGISTRY = new Registry();

    @OnlyIn(Dist.CLIENT)
    public static RenderingManager RENDERING_MANAGER;

    public static DataManager DATA_MANAGER;
    public static ActionManager ACTION_MANAGER;
    public static ConditionalManager CONDITIONAL_MANAGER;

    public ExoArmory() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setupCommon);

        REGISTRY.registerCommon();
        REGISTRY.registerObjects(modBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modBus.addListener(this::setupClient) );
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modBus.addListener(this::registerEntityRenders) );
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.addListener(this::clientTick) );

        MinecraftForge.EVENT_BUS.addListener(this::serverTick);
    }

    public void setupCommon(FMLCommonSetupEvent event) {
        DATA_MANAGER = new DataManager();
        ACTION_MANAGER = new ActionManager();
        CONDITIONAL_MANAGER = new ConditionalManager();
    }

    @OnlyIn(Dist.CLIENT)
    public void setupClient(FMLClientSetupEvent event) {
        RENDERING_MANAGER = new RenderingManager();
    }

    @OnlyIn(Dist.CLIENT)
    public void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(REGISTRY.ENTITY_GENERIC_PROJECTILE.get(), ThrownItemRenderer::new);
    }

    @OnlyIn(Dist.CLIENT)
    public void clientTick(TickEvent.ClientTickEvent event) {
        RENDERING_MANAGER.tick();
    }

    public void serverTick(TickEvent.ServerTickEvent event) {
        ACTION_MANAGER.tick();
    }
}
