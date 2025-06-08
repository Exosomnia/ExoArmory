package com.exosomnia.exoarmory.item.resource;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.item.armory.swords.FrostbiteSword;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.exosomnia.exolib.utils.ComponentUtils.DetailLevel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class FrostbiteResource extends ArmoryResource {

    public enum Stats implements ResourceStat {
        CHARGE
    }

    public FrostbiteResource() {
        super("frostbite", 0x719BDE, 20.0);
    }

    public void buildRanks() {
        RANK_STATS.put(Stats.CHARGE, new double[]{1.0, 1.0, 1.0, 1.0, 1.0});
    }

    @Override
    public List<MutableComponent> getTooltip(DetailLevel detail, int rank, ItemStack itemStack) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank, itemStack));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.desc.frostbite.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.desc.frostbite.line.2"),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("resource.exoarmory.stat.frostbite.line.1", getStatForRank(Stats.CHARGE, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }

    @SubscribeEvent
    public static void livingDeathEvent(LivingDeathEvent event) {
        LivingEntity defender = event.getEntity();
        if (event.getSource().getEntity() instanceof ServerPlayer attacker) {
            ItemStack itemStack = attacker.getMainHandItem();
            if (itemStack.getItem() instanceof FrostbiteSword weapon) {
                int rank = weapon.getRank(itemStack);
                ArmoryResource resource = weapon.getResource();
                if (defender.hasEffect(ExoArmory.REGISTRY.EFFECT_FROSTED.get())) {
                    attacker.getCooldowns().removeCooldown(weapon);
                    resource.addResource(itemStack, resource.getStatForRank(Stats.CHARGE, rank));
                }
            }
        }
    }
}
