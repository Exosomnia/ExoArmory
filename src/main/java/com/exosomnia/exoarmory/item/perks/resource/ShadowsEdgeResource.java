package com.exosomnia.exoarmory.item.perks.resource;

import com.exosomnia.exoarmory.item.armory.swords.ShadowsEdgeSword;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.exosomnia.exolib.utils.ComponentUtils.DetailLevel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
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
        RANK_STATS.put(Stats.CHARGE, new double[]{
                1.5, 1.75, 2.0, 2.25, 2.5
        });
    }

    @Override
    public List<MutableComponent> getTooltip(DetailLevel detail, int rank, ItemStack itemStack) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank, itemStack));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.desc.shadows_edge.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.desc.shadows_edge.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.stat.shadows_edge.line.1", getStatForRank(Stats.CHARGE, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }

    @SubscribeEvent
    public static void livingAttackEvent(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof ServerPlayer attacker && source.is(DamageTypes.PLAYER_ATTACK)) {
            ItemStack itemStack = attacker.getMainHandItem();
            if (itemStack.getItem() instanceof ShadowsEdgeSword weapon && attacker.hasEffect(MobEffects.INVISIBILITY)) {
                int rank = weapon.getRank(itemStack);
                ArmoryResource resource = weapon.getResource();
                resource.addResource(itemStack, resource.getStatForRank(Stats.CHARGE, rank));
            }
        }
    }
}
