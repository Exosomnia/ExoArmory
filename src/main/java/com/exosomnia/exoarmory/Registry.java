package com.exosomnia.exoarmory;

import com.exosomnia.exoarmory.capabilities.resource.IArmoryResourceStorage;
import com.exosomnia.exoarmory.effects.StellarInfusionEffect;
import com.exosomnia.exoarmory.items.ArmoryItem;
import com.exosomnia.exoarmory.items.UpgradeTemplateItem;
import com.exosomnia.exoarmory.items.swords.GigaSword;
import com.exosomnia.exoarmory.items.swords.SolarSword;
import com.exosomnia.exoarmory.networking.PacketHandler;
import com.exosomnia.exoarmory.recipes.smithing.SmithingUpgradeRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registry {

    public final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS,
            ExoArmory.MODID);

    public final RegistryObject<MobEffect> EFFECT_STELLAR_INFUSION = MOB_EFFECTS.register("sunfire_surge",
            () -> new StellarInfusionEffect(MobEffectCategory.BENEFICIAL, 0xff5025) );


    public final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ExoArmory.MODID);
    public final RegistryObject<SoundEvent> SOUND_FIERY_EXPLOSION = SOUNDS.register("fiery_explosion", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ExoArmory.MODID, "fiery_explosion")));
    public final RegistryObject<SoundEvent> SOUND_FIERY_EFFECT = SOUNDS.register("fiery_effect", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ExoArmory.MODID, "fiery_effect")));


    public final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS,
            ExoArmory.MODID);

    public final RegistryObject<RecipeSerializer<SmithingUpgradeRecipe>> RECIPE_SMITHING_UPGRADE = RECIPE_SERIALIZERS.register("smithing_upgrade",
            SmithingUpgradeRecipe.Serializer::new);


    public final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExoArmory.MODID);
    public final RegistryObject<Item> ITEM_GIGA_SWORD = ITEMS.register("giga_sword", GigaSword::new);
    public final RegistryObject<Item> ITEM_SOLAR_SWORD = ITEMS.register("solar_sword", SolarSword::new);

    public final RegistryObject<Item> ITEM_TIER_2_TEMPLATE = ITEMS.register("tier_2_smithing_template", () -> new UpgradeTemplateItem(1, new Item.Properties()));
    public final RegistryObject<Item> ITEM_TIER_3_TEMPLATE = ITEMS.register("tier_3_smithing_template", () -> new UpgradeTemplateItem(2, new Item.Properties()));
    public final RegistryObject<Item> ITEM_TIER_4_TEMPLATE = ITEMS.register("tier_4_smithing_template", () -> new UpgradeTemplateItem(3, new Item.Properties()));
    public final RegistryObject<Item> ITEM_TIER_5_TEMPLATE = ITEMS.register("tier_5_smithing_template", () -> new UpgradeTemplateItem(4, new Item.Properties()));


    public final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExoArmory.MODID);
    public final RegistryObject<CreativeModeTab> TAB_ARMORY_EQUIPMENT = CREATIVE_TABS.register("armory_equipment", () -> CreativeModeTab.builder()
            .title(Component.translatable("tab.exoarmory.equipment"))
            .icon(() -> new ItemStack(ITEM_GIGA_SWORD.get()))
            .displayItems((parameters, output) -> {
                for (RegistryObject<Item> item : ITEMS.getEntries()) {
                    Item loopItem = item.get();
                    if (loopItem instanceof ArmoryItem) { output.accept(loopItem); }
                }
            })
            .build());
    public final RegistryObject<CreativeModeTab> TAB_ARMORY_ITEMS = CREATIVE_TABS.register("armory_items", () -> CreativeModeTab.builder()
            .title(Component.translatable("tab.exoarmory.items"))
            .icon(() -> new ItemStack(ITEM_TIER_2_TEMPLATE.get()))
            .displayItems((parameters, output) -> {
                for (RegistryObject<Item> item : ITEMS.getEntries()) {
                    Item loopItem = item.get();
                    if (!(loopItem instanceof ArmoryItem)) { output.accept(loopItem); }
                }
            })
            .build());


    public void registerCommon() {
        PacketHandler.register();   //Register our packets
        MinecraftForge.EVENT_BUS.addListener(this::registerCapabilities);
    }

    public void registerObjects(IEventBus eventBus) {
        SOUNDS.register(eventBus);
        MOB_EFFECTS.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        ITEMS.register(eventBus);
        CREATIVE_TABS.register(eventBus);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IArmoryResourceStorage.class);
    }
}
