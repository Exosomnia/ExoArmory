package com.exosomnia.exoarmory.item.perks.ability;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.item.perks.ItemPerk;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.exosomnia.exolib.utils.ComponentUtils.DetailLevel;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public abstract class ArmoryAbility implements ItemPerk {
    public record Entry(ArmoryAbility ability, int rank){}

    private final static String NAME_TRANSLATION_FORMAT = "ability.exoarmory.name.%s";

    protected final Object2ObjectOpenHashMap<AbilityStat, double[]> RANK_STATS = new Object2ObjectOpenHashMap<>();

    private ResourceLocation icon;
    private String name;
    private int rgb;
    private MutableComponent defaultTooltip;

    public ArmoryAbility(String name, int rgb) {
        this.name = name;
        this.rgb = rgb;
        this.icon = ResourceLocation.fromNamespaceAndPath(ExoArmory.MODID, String.format("textures/gui/icon/%s.png", name));

        this.defaultTooltip = Component.translatable("item.exoarmory.info.ability").withStyle(ComponentUtils.Styles.INFO_HEADER.getStyle())
                .append(Component.literal(": ").withStyle(ComponentUtils.Styles.INFO_HEADER.getStyle().withUnderlined(false)))
                .append(getNameAsComponent(true));

        buildStats();
    }

    public ResourceLocation getIcon() { return icon; }

    public String getInternalName() { return name; }
    public String getDisplayName() { return I18n.get(String.format(NAME_TRANSLATION_FORMAT, name)); }
    public MutableComponent getNameAsComponent(boolean format) {
        MutableComponent component = Component.translatable(String.format(NAME_TRANSLATION_FORMAT, name));
        return !format ? component : component.withStyle(ComponentUtils.Styles.BLANK.getStyle().withColor(TextColor.fromRgb(rgb)));
    }

    public int getRGB() { return rgb; }

    public List<MutableComponent> getTooltip(DetailLevel detail, int rank) { return List.of(getDefaultTooltip()); }
    protected MutableComponent getDefaultTooltip() { return defaultTooltip; }

    public abstract void buildStats();

    public double getStatForRank(AbilityStat stat, int rank) {
        double[] stats = RANK_STATS.get(stat);
        return (stats == null) ? 0.0 : stats[Math.min(Math.max(rank, 0), stats.length - 1)];
    }

    public interface AbilityStat {}
}
