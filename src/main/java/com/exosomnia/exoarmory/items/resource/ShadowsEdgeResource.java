package com.exosomnia.exoarmory.items.resource;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.items.abilities.AbilityItem;
import com.exosomnia.exoarmory.items.abilities.ShadowStrikeAbility;
import com.exosomnia.exoarmory.items.armory.ArmoryItem;
import com.exosomnia.exoarmory.items.armory.swords.ShadowsEdgeSword;
import com.exosomnia.exoarmory.networking.PacketHandler;
import com.exosomnia.exoarmory.networking.packets.ArmoryResourcePacket;
import com.exosomnia.exoarmory.utils.TooltipUtils;
import com.exosomnia.exoarmory.utils.TooltipUtils.DetailLevel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ShadowsEdgeResource extends ArmoryResource {

    public enum Stats implements ResourceStat {
        CHARGE
    }

    public ShadowsEdgeResource() {
        super("shadows_edge", 0x705685, 50.0);
    }

    public void buildRanks() {
        RANK_STATS.put(Stats.CHARGE, new double[]{1.0, 1.25, 1.5, 1.75, 2.0});
    }

    @Override
    public List<MutableComponent> getTooltip(DetailLevel detail, int rank, ItemStack itemStack) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank, itemStack));

        switch (detail) {
            case DESCRIPTION:
                description.add(TooltipUtils.formatLine(I18n.get("resource.exoarmory.desc.shadows_edge.line.1"), TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(TooltipUtils.formatLine(I18n.get("resource.exoarmory.desc.shadows_edge.line.2"), TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(TooltipUtils.formatLine(I18n.get("resource.exoarmory.stat.shadows_edge.line.1", getStatForRank(Stats.CHARGE, rank)),
                        TooltipUtils.Styles.DEFAULT_DESC.getStyle(), TooltipUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }

    @SubscribeEvent
    public static void livingAttackEvent(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer attacker) {
            ItemStack itemStack = attacker.getMainHandItem();
            if (itemStack.getItem() instanceof ShadowsEdgeSword weapon && attacker.hasEffect(MobEffects.INVISIBILITY)) {
                int rank = ArmoryItem.getRank(itemStack);
                ArmoryResource resource = weapon.getResource();
                resource.addResource(itemStack, resource.getStatForRank(Stats.CHARGE, rank));
                PacketHandler.sendToPlayer(new ArmoryResourcePacket(weapon.getUUID(itemStack),
                        attacker.getInventory().selected, resource.getResource(itemStack)), attacker);
            }
        }
    }
}
