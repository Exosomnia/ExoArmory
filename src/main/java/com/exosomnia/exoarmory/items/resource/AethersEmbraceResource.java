package com.exosomnia.exoarmory.items.resource;

import com.exosomnia.exoarmory.capabilities.aethersembrace.AethersEmbraceProvider;
import com.exosomnia.exoarmory.capabilities.resource.ArmoryResourceProvider;
import com.exosomnia.exoarmory.capabilities.resource.IArmoryResourceStorage;
import com.exosomnia.exoarmory.items.armory.bows.AethersEmbraceBow;
import com.exosomnia.exoarmory.items.armory.swords.ShadowsEdgeSword;
import com.exosomnia.exoarmory.networking.PacketHandler;
import com.exosomnia.exoarmory.networking.packets.ArmoryResourcePacket;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.exosomnia.exolib.utils.ComponentUtils.DetailLevel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class AethersEmbraceResource extends ArmoryResource {

    public enum Stats implements ResourceStat {
        CHARGE
    }

    public AethersEmbraceResource() {
        super("aethers_embrace", 0xFFFFA0, 100.0);
    }

    public void buildRanks() {
        RANK_STATS.put(Stats.CHARGE, new double[]{1.0, 1.0, 1.0, 1.0, 2.0});
    }

    @Override
    public double getResource(ItemStack itemStack) { return ((IArmoryResourceStorage)itemStack.getCapability(AethersEmbraceProvider.AETHERS_EMBRACE).resolve().get()).getCharge(); }
    @Override
    public IArmoryResourceStorage getResourceStorage(ItemStack itemStack) { return ((IArmoryResourceStorage)itemStack.getCapability(AethersEmbraceProvider.AETHERS_EMBRACE).resolve().get()); }

    @Override
    public List<MutableComponent> getTooltip(DetailLevel detail, int rank, ItemStack itemStack) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank, itemStack));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.desc.aethers_embrace.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.desc.aethers_embrace.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.stat.aethers_embrace.line.1", getStatForRank(Stats.CHARGE, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }

    @SubscribeEvent
    public static void arrowImpactLivingEvent(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer attacker) {
            ItemStack itemStack = attacker.getMainHandItem();
            if (itemStack.getItem() instanceof AethersEmbraceBow weapon) {
                int rank = weapon.getRank(itemStack);
                ArmoryResource resource = weapon.getResource();
                resource.addResource(itemStack, resource.getStatForRank(Stats.CHARGE, rank));
                PacketHandler.sendToPlayer(new ArmoryResourcePacket(weapon.getUUID(itemStack),
                        attacker.getInventory().selected, resource.getResource(itemStack)), attacker);
            }
        }
    }
}
