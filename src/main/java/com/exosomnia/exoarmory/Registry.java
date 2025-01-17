package com.exosomnia.exoarmory;

import com.exosomnia.exoarmory.capabilities.resource.IArmoryResourceStorage;
import com.exosomnia.exoarmory.effects.FrostedEffect;
import com.exosomnia.exoarmory.effects.StellarInfusionEffect;
import com.exosomnia.exoarmory.entities.GenericProjectile;
import com.exosomnia.exoarmory.items.UpgradeTemplateItem;
import com.exosomnia.exoarmory.items.abilities.*;
import com.exosomnia.exoarmory.items.armory.ArmoryItem;
import com.exosomnia.exoarmory.items.armory.swords.*;
import com.exosomnia.exoarmory.items.resource.FrostbiteResource;
import com.exosomnia.exoarmory.items.resource.ShadowsEdgeResource;
import com.exosomnia.exoarmory.networking.PacketHandler;
import com.exosomnia.exoarmory.recipes.smithing.SmithingUpgradeRecipe;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registry {

    public final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,
            ExoArmory.MODID);
    public final RegistryObject<EntityType<GenericProjectile>> ENTITY_GENERIC_PROJECTILE = ENTITIES.register("generic_projectile",
            () -> EntityType.Builder.<GenericProjectile>of(GenericProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F)
                    .clientTrackingRange(4).updateInterval(20).build("generic_projectile"));

    public final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS,
            ExoArmory.MODID);

    public final RegistryObject<MobEffect> EFFECT_STELLAR_INFUSION = MOB_EFFECTS.register("sunfire_surge",
            () -> new StellarInfusionEffect(MobEffectCategory.BENEFICIAL, 0xff5025) );
    public final RegistryObject<MobEffect> EFFECT_FROSTED = MOB_EFFECTS.register("frosted",
            () -> new FrostedEffect(MobEffectCategory.HARMFUL, 0x719BDE) );


    public final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ExoArmory.MODID);
    public final RegistryObject<SoundEvent> SOUND_FIERY_EXPLOSION = SOUNDS.register("fiery_explosion", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ExoArmory.MODID, "fiery_explosion")));
    public final RegistryObject<SoundEvent> SOUND_FIERY_EFFECT = SOUNDS.register("fiery_effect", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ExoArmory.MODID, "fiery_effect")));

    public final RegistryObject<SoundEvent> SOUND_DARK_AMBIENT_CHARGE = SOUNDS.register("dark_ambient_charge", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ExoArmory.MODID, "dark_ambient_charge")));
    public final RegistryObject<SoundEvent> SOUNG_MAGIC_CLASH = SOUNDS.register("magic_clash", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ExoArmory.MODID, "magic_clash")));
    public final RegistryObject<SoundEvent> SOUND_MAGIC_TELEPORT = SOUNDS.register("magic_teleport", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ExoArmory.MODID, "magic_teleport")));
    public final RegistryObject<SoundEvent> SOUND_MAGIC_ICE_CAST = SOUNDS.register("magic_ice_cast", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ExoArmory.MODID, "magic_ice_cast")));

    public final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS,
            ExoArmory.MODID);

    public final RegistryObject<RecipeSerializer<SmithingUpgradeRecipe>> RECIPE_SMITHING_UPGRADE = RECIPE_SERIALIZERS.register("smithing_upgrade",
            SmithingUpgradeRecipe.Serializer::new);


    public final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExoArmory.MODID);
    public final RegistryObject<Item> ITEM_GIGA_SWORD = ITEMS.register("giga_sword", GigaSword::new);
    public final RegistryObject<Item> ITEM_SOLAR_SWORD = ITEMS.register("solar_sword", SolarSword::new);
    public final RegistryObject<Item> ITEM_SHADOWS_EDGE = ITEMS.register("shadows_edge", ShadowsEdgeSword::new);
    public final RegistryObject<Item> ITEM_LUMINIS_EDGE = ITEMS.register("luminis_edge", LuminisEdgeSword::new);
    public final RegistryObject<Item> ITEM_FROSTBITE = ITEMS.register("frostbite", FrostbiteSword::new);
    public final RegistryObject<Item> ITEM_HEROS_TESTAMENT = ITEMS.register("heros_testament", HerosTestamentSword::new);

    public final RegistryObject<Item> ITEM_TIER_2_TEMPLATE = ITEMS.register("tier_2_smithing_template", () -> new UpgradeTemplateItem(1, new Item.Properties().rarity(Rarity.COMMON)));
    public final RegistryObject<Item> ITEM_TIER_3_TEMPLATE = ITEMS.register("tier_3_smithing_template", () -> new UpgradeTemplateItem(2, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public final RegistryObject<Item> ITEM_TIER_4_TEMPLATE = ITEMS.register("tier_4_smithing_template", () -> new UpgradeTemplateItem(3, new Item.Properties().rarity(Rarity.RARE)));
    public final RegistryObject<Item> ITEM_TIER_5_TEMPLATE = ITEMS.register("tier_5_smithing_template", () -> new UpgradeTemplateItem(4, new Item.Properties().rarity(Rarity.EPIC)));


    public final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExoArmory.MODID);
    public final RegistryObject<CreativeModeTab> TAB_ARMORY_EQUIPMENT = CREATIVE_TABS.register("armory_equipment", () -> CreativeModeTab.builder()
            .title(Component.translatable("tab.exoarmory.equipment"))
            .icon(() -> new ItemStack(ITEM_GIGA_SWORD.get()))
            .displayItems((parameters, output) -> {
                for (RegistryObject<Item> item : ITEMS.getEntries()) {
                    Item loopItem = item.get();
                    if (loopItem instanceof ArmoryItem armoryItem) {
                        for (var i = 0; i < 5; i++) {
                            ItemStack itemStack = new ItemStack(loopItem);
                            armoryItem.setRank(itemStack, i);
                            output.accept(itemStack);
                        }
                    }
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

    public final SolarFlareAbility ABILITY_SOLAR_FLARE = new SolarFlareAbility();
    public final SunfireSurgeAbility ABILITY_SUNFIRE_SURGE = new SunfireSurgeAbility();

    public final UmbralAssaultAbility ABILITY_UMBRAL_ASSAULT = new UmbralAssaultAbility();
    public final VeilOfDarknessAbility ABILITY_VEIL_OF_DARKNESS = new VeilOfDarknessAbility();
    public final ShadowStrikeAbility ABILITY_SHADOW_STRIKE = new ShadowStrikeAbility();

    public final FrigidFlurryAbility ABILITY_FRIGID_FLURRY = new FrigidFlurryAbility();
    public final ColdSnapAbility ABILITY_COLD_SNAP = new ColdSnapAbility();

    public void registerCommon() {
        PacketHandler.register();   //Register our packets
        MinecraftForge.EVENT_BUS.addListener(this::registerCapabilities);
        MinecraftForge.EVENT_BUS.addListener(ShadowsEdgeResource::livingAttackEvent);
        MinecraftForge.EVENT_BUS.addListener(FrostbiteResource::livingDeathEvent);
        MinecraftForge.EVENT_BUS.register(ABILITY_SOLAR_FLARE);
        MinecraftForge.EVENT_BUS.register(ABILITY_SUNFIRE_SURGE);
        MinecraftForge.EVENT_BUS.register(ABILITY_SHADOW_STRIKE);
        MinecraftForge.EVENT_BUS.register(ABILITY_COLD_SNAP);

        ItemProperties.registerGeneric(new ResourceLocation(ExoArmory.MODID, "using"),
                (itemStack, level, entity, data) -> entity != null && entity.isUsingItem() ? 1.0F : 0.0F);
        ItemProperties.registerGeneric(new ResourceLocation(ExoArmory.MODID, "use_time"),
                (itemStack, level, entity, data) -> entity != null && entity.isUsingItem() ? (float)(itemStack.getUseDuration() - entity.getUseItemRemainingTicks()) : 0.0F);
        ItemProperties.registerGeneric(new ResourceLocation(ExoArmory.MODID, "rank"),
                (itemStack, level, entity, data) -> ArmoryItem.getRank(itemStack));
    }

    public void registerObjects(IEventBus eventBus) {
        ENTITIES.register(eventBus);
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
