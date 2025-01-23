package com.exosomnia.exoarmory;

import com.exosomnia.exoarmory.capabilities.aethersembrace.IAethersEmbraceStorage;
import com.exosomnia.exoarmory.capabilities.projectile.ArmoryArrowProvider;
import com.exosomnia.exoarmory.capabilities.projectile.IArmoryArrowStorage;
import com.exosomnia.exoarmory.capabilities.resource.IArmoryResourceStorage;
import com.exosomnia.exoarmory.effects.BlightedEffect;
import com.exosomnia.exoarmory.effects.FrostedEffect;
import com.exosomnia.exoarmory.effects.StellarInfusionEffect;
import com.exosomnia.exoarmory.effects.VulnerableEffect;
import com.exosomnia.exoarmory.entities.projectiles.GenericProjectile;
import com.exosomnia.exoarmory.items.UpgradeTemplateItem;
import com.exosomnia.exoarmory.items.abilities.*;
import com.exosomnia.exoarmory.items.armory.ArmoryItem;
import com.exosomnia.exoarmory.items.armory.bows.AethersEmbraceBow;
import com.exosomnia.exoarmory.items.armory.swords.*;
import com.exosomnia.exoarmory.items.resource.FrostbiteResource;
import com.exosomnia.exoarmory.items.resource.ShadowsEdgeResource;
import com.exosomnia.exoarmory.managers.ProjectileManager;
import com.exosomnia.exoarmory.networking.PacketHandler;
import com.exosomnia.exoarmory.recipes.smithing.SmithingUpgradeRecipe;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.lwjgl.glfw.GLFW;

public class Registry {

    public final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,
            ExoArmory.MODID);
    public final RegistryObject<EntityType<GenericProjectile>> ENTITY_GENERIC_PROJECTILE = ENTITIES.register("generic_projectile",
            () -> EntityType.Builder.<GenericProjectile>of(GenericProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F)
                    .clientTrackingRange(4).updateInterval(20).build("generic_projectile"));

    public final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS,
            ExoArmory.MODID);

    public final RegistryObject<MobEffect> EFFECT_SUNFIRE_SURGE = MOB_EFFECTS.register("sunfire_surge",
            () -> new StellarInfusionEffect(MobEffectCategory.BENEFICIAL, 0xff5025) );
    public final RegistryObject<MobEffect> EFFECT_FROSTED = MOB_EFFECTS.register("frosted",
            () -> new FrostedEffect(MobEffectCategory.HARMFUL, 0x719BDE) );
    public final RegistryObject<MobEffect> EFFECT_BLIGHTED = MOB_EFFECTS.register("blighted",
            () -> new BlightedEffect(MobEffectCategory.HARMFUL, 0x36040A) );
    public final RegistryObject<MobEffect> EFFECT_VULNERABLE = MOB_EFFECTS.register("vulnerable",
            () -> new VulnerableEffect(MobEffectCategory.HARMFUL, 0x3D2F54) );


    public final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ExoArmory.MODID);
    public final RegistryObject<SoundEvent> SOUND_FIERY_EXPLOSION = SOUNDS.register("fiery_explosion", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ExoArmory.MODID, "fiery_explosion")));
    public final RegistryObject<SoundEvent> SOUND_FIERY_EFFECT = SOUNDS.register("fiery_effect", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ExoArmory.MODID, "fiery_effect")));

    public final RegistryObject<SoundEvent> SOUND_DARK_AMBIENT_CHARGE = SOUNDS.register("dark_ambient_charge", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ExoArmory.MODID, "dark_ambient_charge")));
    public final RegistryObject<SoundEvent> SOUND_MAGIC_CLASH = SOUNDS.register("magic_clash", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ExoArmory.MODID, "magic_clash")));
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
    public final RegistryObject<Item> ITEM_AETHERS_EMBRACE = ITEMS.register("aethers_embrace", AethersEmbraceBow::new);

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

    //Attributes Registry
    public final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, ExoArmory.MODID);
    public final RegistryObject<Attribute> ATTRIBUTE_RANGED_STRENGTH = ATTRIBUTES.register("ranged_strength",
            () -> new RangedAttribute("attribute.exoarmory.ranged_strength",
                    1.0,
                    0.0,
                    1024.0));

    public final RegistryObject<Attribute> ATTRIBUTE_HEALING_RECEIVED = ATTRIBUTES.register("healing_received",
            () -> new RangedAttribute("attribute.exoarmory.healing_received",
                    1.0,
                    0.0,
                    128.0));

    public final RegistryObject<Attribute> ATTRIBUTE_VULNERABILITY = ATTRIBUTES.register("vulnerability",
            () -> new RangedAttribute("attribute.exoarmory.vulnerability",
                    1.0,
                    -127.0,
                    2.0));

    public KeyMapping KEY_ACTIVATE;

    public final SolarFlareAbility ABILITY_SOLAR_FLARE = new SolarFlareAbility();
    public final SunfireSurgeAbility ABILITY_SUNFIRE_SURGE = new SunfireSurgeAbility();

    public final UmbralAssaultAbility ABILITY_UMBRAL_ASSAULT = new UmbralAssaultAbility();
    public final VeilOfDarknessAbility ABILITY_VEIL_OF_DARKNESS = new VeilOfDarknessAbility();
    public final ShadowStrikeAbility ABILITY_SHADOW_STRIKE = new ShadowStrikeAbility();

    public final FrigidFlurryAbility ABILITY_FRIGID_FLURRY = new FrigidFlurryAbility();
    public final ColdSnapAbility ABILITY_COLD_SNAP = new ColdSnapAbility();

    public void registerCommon() {
        PacketHandler.register();   //Register our packets
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::attributeModifyEvent);

        ProjectileManager manager = new ProjectileManager();
        MinecraftForge.EVENT_BUS.register(manager);

        MinecraftForge.EVENT_BUS.addListener(this::registerCapabilities);
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, this::attachCapabilities);
        MinecraftForge.EVENT_BUS.addListener(ShadowsEdgeResource::livingAttackEvent);
        MinecraftForge.EVENT_BUS.addListener(FrostbiteResource::livingDeathEvent);
        MinecraftForge.EVENT_BUS.register(ABILITY_SOLAR_FLARE);
        MinecraftForge.EVENT_BUS.register(ABILITY_SUNFIRE_SURGE);
        MinecraftForge.EVENT_BUS.register(ABILITY_SHADOW_STRIKE);
        MinecraftForge.EVENT_BUS.register(ABILITY_COLD_SNAP);
    }

    public void registerObjects(IEventBus eventBus) {
        ENTITIES.register(eventBus);
        SOUNDS.register(eventBus);
        MOB_EFFECTS.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        ITEMS.register(eventBus);
        CREATIVE_TABS.register(eventBus);
        ATTRIBUTES.register(eventBus);
    }

    public void registerClient() {
        KEY_ACTIVATE = new KeyMapping("key.exoarmory.activate", KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.categories.exoarmory");

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerKeyMappings);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IArmoryResourceStorage.class);
        event.register(IArmoryArrowStorage.class);
        event.register(IAethersEmbraceStorage.class);
    }

    public void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(KEY_ACTIVATE);
    }

    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof AbstractArrow) {
            event.addCapability(new ResourceLocation(ExoArmory.MODID, "armory_arrow_storage"), new ArmoryArrowProvider());
        }
    }

    public void attributeModifyEvent(final EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, ATTRIBUTE_RANGED_STRENGTH.get());
        event.add(EntityType.SKELETON, ATTRIBUTE_RANGED_STRENGTH.get());
        for (EntityType<? extends LivingEntity> entity : event.getTypes()) {
            event.add(entity, ATTRIBUTE_HEALING_RECEIVED.get());
            event.add(entity, ATTRIBUTE_VULNERABILITY.get());
        }
    }
}
