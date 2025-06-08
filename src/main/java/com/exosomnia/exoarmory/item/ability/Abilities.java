package com.exosomnia.exoarmory.item.ability;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import javax.annotation.Nullable;

public class Abilities {
    private final static Object2ObjectOpenHashMap<String, ArmoryAbility> REGISTERED_ABILITIES = new Object2ObjectOpenHashMap<>();

    public final static SolarFlareAbility SOLAR_FLARE = registerAbility(new SolarFlareAbility());
    public final static SunfireSurgeAbility SUNFIRE_SURGE = registerAbility(new SunfireSurgeAbility());

    public final static UmbralAssaultAbility UMBRAL_ASSAULT = registerAbility(new UmbralAssaultAbility());
    public final static VeilOfDarknessAbility VEIL_OF_DARKNESS = registerAbility(new VeilOfDarknessAbility());
    public final static ShadowStrikeAbility SHADOW_STRIKE = registerAbility(new ShadowStrikeAbility());

    public final static FrigidFlurryAbility FRIGID_FLURRY = registerAbility(new FrigidFlurryAbility());
    public final static ColdSnapAbility COLD_SNAP = registerAbility(new ColdSnapAbility());

    public final static AetherBarrageAbility AETHER_BARRAGE = registerAbility(new AetherBarrageAbility());
    public final static SpectralPierceAbility SPECTRAL_PIERCE = registerAbility(new SpectralPierceAbility());

    public final static HerosCourageAbility HEROS_COURAGE = registerAbility(new HerosCourageAbility());
    public final static HerosFortitudeAbility HEROS_FORTITUDE = registerAbility(new HerosFortitudeAbility());
    public final static HerosWillAbility HEROS_WILL = registerAbility(new HerosWillAbility());

    private static <T extends ArmoryAbility> T registerAbility(T ability) {
        REGISTERED_ABILITIES.put(ability.getInternalName(), ability);
        return ability;
    }

    public static @Nullable ArmoryAbility getAbility(String name) {
        return REGISTERED_ABILITIES.get(name);
    }
}
