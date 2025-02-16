package com.exosomnia.exoarmory;

import com.exosomnia.exoarmory.capabilities.aethersembrace.IAethersEmbraceStorage;
import com.exosomnia.exoarmory.capabilities.projectile.ArmoryArrowProvider;
import com.exosomnia.exoarmory.capabilities.projectile.IArmoryArrowStorage;
import com.exosomnia.exoarmory.capabilities.resource.IArmoryResourceStorage;
import com.exosomnia.exoarmory.effects.*;
import com.exosomnia.exoarmory.enchantment.FortifyingEnchantment;
import com.exosomnia.exoarmory.enchantment.RallyingEnchantment;
import com.exosomnia.exoarmory.enchantment.SoulboundEnchantment;
import com.exosomnia.exoarmory.entities.projectiles.GenericProjectile;
import com.exosomnia.exoarmory.items.ReinforcedBowItem;
import com.exosomnia.exoarmory.items.ReinforcedShieldItem;
import com.exosomnia.exoarmory.items.UpgradeTemplateItem;
import com.exosomnia.exoarmory.items.abilities.*;
import com.exosomnia.exoarmory.items.armory.ArmoryItem;
import com.exosomnia.exoarmory.items.armory.bows.AethersEmbraceBow;
import com.exosomnia.exoarmory.items.armory.swords.*;
import com.exosomnia.exoarmory.items.resource.AethersEmbraceResource;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.UUID;

public class Registry {

    private final ResourceLocation LEGACY_INFERNAL_SHIELD = new ResourceLocation("enigmaticlegacy", "infernal_shield");
    public List<Item> SHIELDING_ITEMS;

    public final UUID SHIELD_ARMOR_UUID = UUID.fromString("53af3a0a-a46e-42db-8c64-8e888f676d34");
    public final UUID OFF_HAND_SHIELD_ARMOR_UUID = UUID.fromString("4ced379e-2b80-43fd-b3b1-297175cfdf18");
    public final UUID PASSIVE_BLOCK_UUID = UUID.fromString("76a473b3-a898-470b-8968-af37fdd52a7d");
    public final UUID SHIELD_STABILITY_UUID = UUID.fromString("105cb502-61ce-4122-8c5b-bd271a5c85ca");
    public final UUID SHIELD_ATTACK_UUID = UUID.fromString("8dbb711e-0c9c-4b02-b002-dc1b9303c699");
    public final UUID OFF_HAND_SHIELD_ATTACK_UUID = UUID.fromString("db3f949d-eaf5-4e92-930b-ca431a064e80");

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
    public final RegistryObject<MobEffect> EFFECT_EAGLE_EYE = MOB_EFFECTS.register("eagle_eye",
            () -> new EagleEyeEffect(MobEffectCategory.BENEFICIAL, 0x81AB0F) );


    public final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS,
            ExoArmory.MODID);

    public final RegistryObject<Potion> POTION_EAGLE_EYE = POTIONS.register("eagle_eye",
            () -> new Potion(new MobEffectInstance(EFFECT_EAGLE_EYE.get(), 3600, 0)));
    public final RegistryObject<Potion> POTION_EAGLE_EYE_STRONG = POTIONS.register("eagle_eye_strong",
            () -> new Potion(new MobEffectInstance(EFFECT_EAGLE_EYE.get(), 1800, 1)));
    public final RegistryObject<Potion> POTION_EAGLE_EYE_EXTENDED = POTIONS.register("eagle_eye_extended",
            () -> new Potion(new MobEffectInstance(EFFECT_EAGLE_EYE.get(), 9600, 0)));


    public final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ExoArmory.MODID);

    public final RegistryObject<Enchantment> ENCHANTMENT_SOULBOUND = ENCHANTMENTS.register("soulbound", SoulboundEnchantment::new);
    public final RegistryObject<Enchantment> ENCHANTMENT_FORTIFYING = ENCHANTMENTS.register("fortifying", FortifyingEnchantment::new);
    public final RegistryObject<Enchantment> ENCHANTMENT_RALLYING = ENCHANTMENTS.register("rallying", RallyingEnchantment::new);

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

    public final RegistryObject<Item> ITEM_COPPER_SHIELD = ITEMS.register("copper_shield", () -> new ReinforcedShieldItem(420, 1.25, 0.05, 1)); //336 Default (1.25 mod)
    public final RegistryObject<Item> ITEM_IRON_SHIELD = ITEMS.register("iron_shield", () -> new ReinforcedShieldItem(588, 1.5, 0.075, 1));
    public final RegistryObject<Item> ITEM_GOLD_SHIELD = ITEMS.register("gold_shield", () -> new ReinforcedShieldItem(196, 3.0, 0.05, 0));
    public final RegistryObject<Item> ITEM_DIAMOND_SHIELD = ITEMS.register("diamond_shield", () -> new ReinforcedShieldItem(840, 1.75, 0.075, 2));
    public final RegistryObject<Item> ITEM_NETHERITE_SHIELD = ITEMS.register("netherite_shield", () -> new ReinforcedShieldItem(1176, 2.0, 0.1, 2));

    public final RegistryObject<Item> ITEM_DRAGON_BOW = ITEMS.register("dragon_bow", () -> new ReinforcedBowItem(new Item.Properties().durability(960), 0.1, 0.0));
    public final RegistryObject<Item> ITEM_ETHERIUM_BOW = ITEMS.register("etherium_bow", () -> new ReinforcedBowItem(new Item.Properties().durability(1152), 0.15, 0.333));

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

    public final RegistryObject<Attribute> ATTRIBUTE_SHIELD_STABILITY = ATTRIBUTES.register("shield_stability",
            () -> new RangedAttribute("attribute.exoarmory.shield_stability",
                    1.0,
                    0.0,
                    72000.0).setSyncable(true));

    public final RegistryObject<Attribute> ATTRIBUTE_PASSIVE_BLOCK = ATTRIBUTES.register("passive_block",
            () -> new RangedAttribute("attribute.exoarmory.passive_block",
                    1.0,
                    1.0,
                    2.0));

    public KeyMapping KEY_ACTIVATE;

    public final SolarFlareAbility ABILITY_SOLAR_FLARE = new SolarFlareAbility();
    public final SunfireSurgeAbility ABILITY_SUNFIRE_SURGE = new SunfireSurgeAbility();

    public final UmbralAssaultAbility ABILITY_UMBRAL_ASSAULT = new UmbralAssaultAbility();
    public final VeilOfDarknessAbility ABILITY_VEIL_OF_DARKNESS = new VeilOfDarknessAbility();
    public final ShadowStrikeAbility ABILITY_SHADOW_STRIKE = new ShadowStrikeAbility();

    public final FrigidFlurryAbility ABILITY_FRIGID_FLURRY = new FrigidFlurryAbility();
    public final ColdSnapAbility ABILITY_COLD_SNAP = new ColdSnapAbility();

    //

    public void registerCommon() {
        PacketHandler.register();   //Register our packets
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::attributeModifyEvent);

        ProjectileManager manager = new ProjectileManager();
        MinecraftForge.EVENT_BUS.register(manager);

        MinecraftForge.EVENT_BUS.addListener(this::registerCapabilities);
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, this::attachCapabilities);
        MinecraftForge.EVENT_BUS.addListener(this::attributeItemModifyEvent);
        MinecraftForge.EVENT_BUS.addListener(ShadowsEdgeResource::livingAttackEvent);
        MinecraftForge.EVENT_BUS.addListener(FrostbiteResource::livingDeathEvent);
        MinecraftForge.EVENT_BUS.addListener(AethersEmbraceResource::arrowImpactLivingEvent);
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
        ENCHANTMENTS.register(eventBus);
        POTIONS.register(eventBus);
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
        event.add(EntityType.PLAYER, ATTRIBUTE_SHIELD_STABILITY.get());
        for (EntityType<? extends LivingEntity> entity : event.getTypes()) {
            event.add(entity, ATTRIBUTE_RANGED_STRENGTH.get());
            event.add(entity, ATTRIBUTE_HEALING_RECEIVED.get());
            event.add(entity, ATTRIBUTE_VULNERABILITY.get());
            event.add(entity, ATTRIBUTE_PASSIVE_BLOCK.get());
        }
    }

    public void attributeItemModifyEvent(final ItemAttributeModifierEvent event) {
        EquipmentSlot slot = event.getSlotType();
        if (!slot.equals(EquipmentSlot.OFFHAND) && !slot.equals(EquipmentSlot.MAINHAND)) { return; }
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        if (item.canPerformAction(stack, ToolActions.SHIELD_BLOCK)) {
            if (event.getOriginalModifiers().isEmpty()) {
                ResourceLocation location = ForgeRegistries.ITEMS.getKey(item);
                if (location.equals(LEGACY_INFERNAL_SHIELD)) {
                    event.addModifier(ATTRIBUTE_SHIELD_STABILITY.get(), new AttributeModifier(SHIELD_STABILITY_UUID, "Default", 2.25, AttributeModifier.Operation.ADDITION));
                    event.addModifier(ATTRIBUTE_PASSIVE_BLOCK.get(), new AttributeModifier(PASSIVE_BLOCK_UUID, "Default", 0.10, AttributeModifier.Operation.MULTIPLY_BASE));
                    event.addModifier(Attributes.ARMOR, new AttributeModifier(slot == EquipmentSlot.MAINHAND ? SHIELD_ARMOR_UUID : OFF_HAND_SHIELD_ARMOR_UUID, "Default", 3, AttributeModifier.Operation.ADDITION));
                } else {
                    event.addModifier(ATTRIBUTE_SHIELD_STABILITY.get(), new AttributeModifier(SHIELD_STABILITY_UUID, "Default", 1.0, AttributeModifier.Operation.ADDITION));
                    event.addModifier(ATTRIBUTE_PASSIVE_BLOCK.get(), new AttributeModifier(PASSIVE_BLOCK_UUID, "Default", 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
                    //event.addModifier(Attributes.ARMOR, new AttributeModifier(slot == EquipmentSlot.MAINHAND ? SHIELD_ARMOR_UUID : OFF_HAND_SHIELD_ARMOR_UUID, "Default", 0.0, AttributeModifier.Operation.ADDITION));
                }
            }
        }
    }
}
