package com.exosomnia.exoarmory.items.resource;

import com.exosomnia.exoarmory.capabilities.resource.ArmoryResourceProvider;
import com.exosomnia.exoarmory.capabilities.resource.IArmoryResourceStorage;
import com.exosomnia.exoarmory.items.abilities.ArmoryAbility;
import com.exosomnia.exoarmory.utils.TooltipUtils;
import com.exosomnia.exoarmory.utils.TooltipUtils.DetailLevel;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ArmoryResource {
    private final static String NAME_TRANSLATION_FORMAT = "resource.exoarmory.name.%s";

    protected final Map<ResourceStat, double[]> RANK_STATS = new HashMap<>();

    private String id;
    private int rgb;
    private double max;

    public ArmoryResource(String id, int rgb, double max) {
        this.id = id;
        this.rgb = rgb;
        this.max = max;

        buildRanks();
    }

    public int getRGB() { return rgb; }

    public String getInternalID() { return id; }
    public String getDisplayName() { return I18n.get(String.format(NAME_TRANSLATION_FORMAT, id)); }

    public double getResourceMax() { return max; }
    private IArmoryResourceStorage getResourceStorage(ItemStack itemStack) { return itemStack.getCapability(ArmoryResourceProvider.ARMORY_RESOURCE).resolve().get(); }
    public double getResource(ItemStack itemStack) { return itemStack.getCapability(ArmoryResourceProvider.ARMORY_RESOURCE).resolve().get().getCharge(); }
    public void addResource(ItemStack itemStack, double amount) { getResourceStorage(itemStack).addCharge(amount, max); }
    public void removeResource(ItemStack itemStack, double amount) { getResourceStorage(itemStack).removeCharge(amount); }

    public List<MutableComponent> getTooltip(DetailLevel detail, int rank, ItemStack itemStack) { return List.of(getDefaultTooltip(itemStack)); }
    protected MutableComponent getDefaultTooltip(ItemStack itemStack) {
        return Component.translatable(String.format(NAME_TRANSLATION_FORMAT, id)).withStyle(TooltipUtils.Styles.INFO_HEADER.getStyle())
            .append(Component.literal(": ").withStyle(TooltipUtils.Styles.INFO_HEADER.getStyle().withUnderlined(false)))
            .append(Component.literal(String.valueOf((int)getResource(itemStack))).withStyle(TooltipUtils.Styles.BLANK.getStyle().withColor(ChatFormatting.AQUA)))
            .append(Component.literal("/").withStyle(TooltipUtils.Styles.BLANK.getStyle().withColor(ChatFormatting.GRAY)))
            .append(Component.literal(String.valueOf((int)max)).withStyle(TooltipUtils.Styles.BLANK.getStyle().withColor(ChatFormatting.AQUA)));
    }

    public abstract void buildRanks();
    public double getStatForRank(ResourceStat stat, int rank) {
        double[] stats = RANK_STATS.get(stat);
        if (stats == null) return 0.0;
        return stats[Math.min(Math.max(rank, 0), 4)];
    }

    public interface ResourceStat {}
}
