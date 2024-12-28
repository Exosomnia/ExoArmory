package com.exosomnia.exoarmory;

import com.exosomnia.exoarmory.items.swords.ExoSwordItem;
import com.exosomnia.exoarmory.items.swords.SolarSword;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExoArmory.MODID)
public class ExoArmory
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "exoarmory";

    public ExoArmory()
    {
        DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
        ITEMS.register("giga_sword", () -> new ExoSwordItem(Tiers.IRON, 2, -2.8F, new Item.Properties()) );
        ITEMS.register("solar_sword", () -> new SolarSword(Tiers.IRON, 1, -2.2F, new Item.Properties()) );
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
