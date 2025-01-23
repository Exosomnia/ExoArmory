package com.exosomnia.exoarmory;

import com.exosomnia.exoarmory.actions.ActionManager;
import com.exosomnia.exoarmory.client.rendering.RenderingManager;
import com.exosomnia.exoarmory.items.armory.ArmoryItem;
import com.exosomnia.exoarmory.items.armory.bows.AethersEmbraceBow;
import com.exosomnia.exoarmory.managers.AbilityManager;
import com.exosomnia.exoarmory.managers.ConditionalManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
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

    public static ActionManager ACTION_MANAGER;
    public static ConditionalManager CONDITIONAL_MANAGER;
    public static AbilityManager ABILITY_MANAGER;

    public ExoArmory() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setupCommon);

        REGISTRY.registerCommon();
        REGISTRY.registerObjects(modBus);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> REGISTRY::registerClient);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modBus.addListener(this::setupClient) );
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modBus.addListener(this::registerEntityRenders) );
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.addListener(this::clientTick) );

        MinecraftForge.EVENT_BUS.addListener(this::serverTick);
    }

    public void setupCommon(FMLCommonSetupEvent event) {
        ACTION_MANAGER = new ActionManager();
        CONDITIONAL_MANAGER = new ConditionalManager();
        ABILITY_MANAGER = new AbilityManager();
    }

    @OnlyIn(Dist.CLIENT)
    public void setupClient(FMLClientSetupEvent event) {
        RENDERING_MANAGER = new RenderingManager();

        ItemProperties.registerGeneric(new ResourceLocation(ExoArmory.MODID, "using"),
                (itemStack, level, entity, data) -> entity != null && entity.isUsingItem() && entity.getUseItem() == itemStack ? 1.0F : 0.0F);
        ItemProperties.registerGeneric(new ResourceLocation(ExoArmory.MODID, "use_time"),
                (itemStack, level, entity, data) -> entity != null && entity.isUsingItem() ? (float)(itemStack.getUseDuration() - entity.getUseItemRemainingTicks()) : 0.0F);
        ItemProperties.registerGeneric(new ResourceLocation(ExoArmory.MODID, "rank"),
                (itemStack, level, entity, data) -> {
                    if (itemStack.getItem() instanceof ArmoryItem armoryItem) {
                        return (float)armoryItem.getRank(itemStack);
                    }
                    return 0.0F;
                });
        ItemProperties.register(REGISTRY.ITEM_AETHERS_EMBRACE.get(), new ResourceLocation(ExoArmory.MODID, "aether"),
                (itemStack, level, entity, data) -> entity != null && ((AethersEmbraceBow)itemStack.getItem()).isTargeting(itemStack, level) ? 1.0F : 0.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(REGISTRY.ENTITY_GENERIC_PROJECTILE.get(), ThrownItemRenderer::new);
    }

    @OnlyIn(Dist.CLIENT)
    public void clientTick(TickEvent.ClientTickEvent event) {
        if(event.phase.equals(TickEvent.Phase.END)) { RENDERING_MANAGER.tick(); }
    }

    public void serverTick(TickEvent.ServerTickEvent event) {
        if(event.phase.equals(TickEvent.Phase.END)) {  ACTION_MANAGER.tick(); }
    }
}
