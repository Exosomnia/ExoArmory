package com.exosomnia.exoarmory.item.resource;

import com.exosomnia.exoarmory.capabilities.armory.item.aethersembrace.AethersEmbraceProvider;
import com.exosomnia.exoarmory.capabilities.armory.item.resource.ArmoryResourceStorage;
import com.exosomnia.exoarmory.capabilities.projectile.ArmoryArrowProvider;
import com.exosomnia.exoarmory.item.armory.bows.AethersEmbraceBow;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.exosomnia.exolib.utils.ComponentUtils.DetailLevel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AethersEmbraceResource extends ArmoryResource {

    public enum Stats implements ResourceStat {
        CHARGE
    }

    public AethersEmbraceResource() {
        super("aethers_embrace", 0xFFFF8C, 100.0);
    }

    public void buildRanks() {
        RANK_STATS.put(Stats.CHARGE, new double[]{1.0, 1.0, 1.0, 1.0, 1.5});
    }

    @Override
    public double getResource(ItemStack itemStack) { return ((ArmoryResourceStorage)itemStack.getCapability(AethersEmbraceProvider.AETHERS_EMBRACE).resolve().get()).getCharge(); }
    @Override
    public ArmoryResourceStorage getResourceStorage(ItemStack itemStack) { return ((ArmoryResourceStorage)itemStack.getCapability(AethersEmbraceProvider.AETHERS_EMBRACE).resolve().get()); }

    @Override
    public List<MutableComponent> getTooltip(DetailLevel detail, int rank, ItemStack itemStack) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank, itemStack));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.desc.aethers_embrace.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.desc.aethers_embrace.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.desc.aethers_embrace.line.3"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.desc.aethers_embrace.line.4"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.stat.aethers_embrace.line.1", getStatForRank(Stats.CHARGE, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }

    @SubscribeEvent
    public static void arrowImpactLivingEvent(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (projectile.level().isClientSide()) return;
        if (!(projectile instanceof AbstractArrow arrow)) return;
        if (!(arrow.getOwner() instanceof ServerPlayer player)) return;
        if (!(event.getRayTraceResult() instanceof EntityHitResult)) return;

        ProjectileImpactEvent.ImpactResult result = event.getImpactResult();
        if (!result.equals(ProjectileImpactEvent.ImpactResult.DEFAULT) && !result.equals(ProjectileImpactEvent.ImpactResult.STOP_AT_CURRENT)) return;

        arrow.getCapability(ArmoryArrowProvider.ARMORY_PROJECTILE).ifPresent(projectileData -> {
            UUID itemUUID = projectileData.getItemUUID();
            if (itemUUID == null) return;

            boolean doubled = arrow.isCritArrow();
            AethersEmbraceBow mainHandBow = checkIfItemValid(player.getMainHandItem(), itemUUID);
            if (mainHandBow != null) {
                addResourceAndUpdate(player.getMainHandItem(), mainHandBow, player, doubled);
            }
            else {
                AethersEmbraceBow offHandBow = checkIfItemValid(player.getOffhandItem(), itemUUID);
                if (offHandBow != null) {
                    addResourceAndUpdate(player.getOffhandItem(), offHandBow, player, doubled);
                }
            }
        });
    }

    private static AethersEmbraceBow checkIfItemValid(ItemStack itemStack, UUID uuid) {
        if (itemStack.getItem() instanceof AethersEmbraceBow bow && bow.getUUID(itemStack).equals(uuid)) {
            return bow;
        }
        return null;
    }

    private static void addResourceAndUpdate(ItemStack itemStack, AethersEmbraceBow bow, ServerPlayer player, boolean doubled) {
        int rank = bow.getRank(itemStack);
        ArmoryResource resource = bow.getResource();
        resource.addResource(itemStack, doubled ? resource.getStatForRank(Stats.CHARGE, rank) * 2.0 : resource.getStatForRank(Stats.CHARGE, rank));
    }
}
