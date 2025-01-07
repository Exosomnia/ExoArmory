package com.exosomnia.exoarmory.items.abilities;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.utils.TooltipUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public abstract class ArmoryAbility {
    private final static String NAME_TRANSLATION_FORMAT = "ability.exoarmory.name.%s";

    private ResourceLocation icon;
    private String name;
    private int rgb;
    private MutableComponent defaultTooltip;

    public ArmoryAbility(String name, int rgb) {
        this.name = name;
        this.rgb = rgb;
        this.icon = new ResourceLocation(ExoArmory.MODID, String.format("textures/gui/icon/%s.png", name));

        this.defaultTooltip = Component.translatable("item.exoarmory.info.ability").withStyle(TooltipUtils.Styles.INFO_HEADER.getStyle())
                .append(Component.literal(": ").withStyle(TooltipUtils.Styles.INFO_HEADER.getStyle().withUnderlined(false)))
                .append(getNameAsComponent(true));
    }

    public ResourceLocation getIcon() { return icon; }

    public String getInternalName() { return name; }
    public String getDisplayName() { return I18n.get(String.format(NAME_TRANSLATION_FORMAT, name)); }
    public MutableComponent getNameAsComponent(boolean format) {
        MutableComponent component = Component.translatable(String.format(NAME_TRANSLATION_FORMAT, name));
        return !format ? component : component.withStyle(TooltipUtils.Styles.BLANK.getStyle().withColor(TextColor.fromRgb(rgb)));
    }

    public int getRgb() { return rgb; }

    public List<MutableComponent> getTooltip(boolean detailed, int rank) { return List.of(getDefaultTooltip()); }
    protected MutableComponent getDefaultTooltip() { return defaultTooltip; }
}
