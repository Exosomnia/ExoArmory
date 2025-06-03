package com.exosomnia.exoarmory;

import com.exosomnia.exoarmory.actions.ActionManager;
import com.exosomnia.exoarmory.client.ClientArmoryResourceManager;
import com.exosomnia.exoarmory.client.rendering.RenderingManager;
import com.exosomnia.exoarmory.dist.ClientDistHelper;
import com.exosomnia.exoarmory.dist.DistHelper;
import com.exosomnia.exoarmory.dist.ServerDistHelper;
import com.exosomnia.exoarmory.items.armory.ArmoryItem;
import com.exosomnia.exoarmory.items.armory.bows.AethersEmbraceBow;
import com.exosomnia.exoarmory.managers.AbilityManager;
import com.exosomnia.exoarmory.managers.ConditionalManager;
import com.exosomnia.exolib.recipes.brewing.BrewingRecipeHelper;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExoArmory.MODID)
public class ExoArmory
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "exoarmory";
    public static final Registry REGISTRY = new Registry();
    public static DistHelper DIST_HELPER;

    @OnlyIn(Dist.CLIENT)
    public static RenderingManager RENDERING_MANAGER;
    @OnlyIn(Dist.CLIENT)
    public static ClientArmoryResourceManager RESOURCE_MANAGER;

    public static ActionManager ACTION_MANAGER;
    public static ConditionalManager CONDITIONAL_MANAGER;
    public static AbilityManager ABILITY_MANAGER;

    public ExoArmory() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setupCommon);

        REGISTRY.registerCommon();
        REGISTRY.registerObjects(modBus);
        DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> modBus.addListener(this::setupServer) );

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> REGISTRY::registerClient);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modBus.addListener(this::setupClient) );
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modBus.addListener(this::registerEntityRenders) );
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.addListener(this::clientTick) );

        MinecraftForge.EVENT_BUS.addListener(this::serverTick);
    }

    public void setupCommon(FMLCommonSetupEvent event) {
        REGISTRY.populateAfterRegistration();

        ACTION_MANAGER = new ActionManager();
        CONDITIONAL_MANAGER = new ConditionalManager();
        ABILITY_MANAGER = new AbilityManager();

        REGISTRY.SHIELDING_ITEMS = ForgeRegistries.ITEMS.getValues().stream().filter(item -> item.canPerformAction(new ItemStack(item), ToolActions.SHIELD_BLOCK)).toList();

        event.enqueueWork(() -> {
            Potion baseEagleEye = REGISTRY.POTION_EAGLE_EYE.get();
            BrewingRecipeHelper.addSimplePotionRecipe(Potions.AWKWARD, Items.SKELETON_SKULL, baseEagleEye);
            BrewingRecipeHelper.addSimplePotionRecipe(baseEagleEye, Items.GLOWSTONE_DUST, REGISTRY.POTION_EAGLE_EYE_STRONG.get());
            BrewingRecipeHelper.addSimplePotionRecipe(baseEagleEye, Items.REDSTONE, REGISTRY.POTION_EAGLE_EYE_EXTENDED.get());
        });
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public void setupServer(FMLDedicatedServerSetupEvent event) {
        DIST_HELPER = new ServerDistHelper();
    }

    @OnlyIn(Dist.CLIENT)
    public void setupClient(FMLClientSetupEvent event) {
        DIST_HELPER = new ClientDistHelper();
        RENDERING_MANAGER = new RenderingManager();
        RESOURCE_MANAGER = new ClientArmoryResourceManager();

        ItemProperties.registerGeneric(ResourceLocation.fromNamespaceAndPath(ExoArmory.MODID, "using"),
                (itemStack, level, entity, data) -> entity != null && entity.isUsingItem() && entity.getUseItem() == itemStack ? 1.0F : 0.0F);
        ItemProperties.registerGeneric(ResourceLocation.fromNamespaceAndPath(ExoArmory.MODID, "use_time"),
                (itemStack, level, entity, data) -> entity != null && entity.isUsingItem() ? (float)(itemStack.getUseDuration() - entity.getUseItemRemainingTicks()) : 0.0F);
        ItemProperties.registerGeneric(ResourceLocation.fromNamespaceAndPath(ExoArmory.MODID, "rank"),
                (itemStack, level, entity, data) -> {
                    if (itemStack.getItem() instanceof ArmoryItem armoryItem) {
                        return (float)armoryItem.getRank(itemStack);
                    }
                    return 0.0F;
                });
        ItemProperties.register(REGISTRY.ITEM_AETHERS_EMBRACE.get(), ResourceLocation.fromNamespaceAndPath(ExoArmory.MODID, "aether"),
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
