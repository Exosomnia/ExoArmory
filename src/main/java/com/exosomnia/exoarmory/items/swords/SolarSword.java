package com.exosomnia.exoarmory.items.swords;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.items.ResourcedItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SolarSword extends ExoSwordItem implements ResourcedItem {

    private static final float[] BAR_RGB = {1.0F, 1.0F, 0.0F};
    private static final ResourceLocation BAR_ICON = new ResourceLocation(ExoArmory.MODID, "textures/gui/icon/icon_flare.png");
    //private static final double MAX_CHARGE = 1000;

    public SolarSword(Tier tier, int damageBonus, float attackSpeed, Properties properties) {
        super(tier, damageBonus, attackSpeed, properties);
    }

    @Override
    public float[] getBarRGB() {
        return BAR_RGB;
    }

    @Override
    public ResourceLocation getBarIcon() {
        return BAR_ICON;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);

        p_41423_.add(Component.literal(String.valueOf(p_41421_.getOrCreateTag().getInt("TEST_TAG"))));
    }
}
