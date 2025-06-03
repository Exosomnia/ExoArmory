package com.exosomnia.exoarmory.items.abilities;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exolib.utils.ComponentUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class HerosFortitudeAbility extends ArmoryAbility {

    public enum Stats implements AbilityStat {
        CHANCE,
        AMOUNT
    }

    public HerosFortitudeAbility() {
        super("heros_fortitude", 0xD19356);
    }

    //region ArmoryAbility Overrides
    public void buildRanks() {
        RANK_STATS.put(Stats.CHANCE, new double[]{0.0, 0.15, 0.5, 0.5, 0.25});
        RANK_STATS.put(Stats.AMOUNT, new double[]{0.0, 0.0, 0.0, 0.0, 0.0});
    }

    @Override
    public List<MutableComponent> getTooltip(ComponentUtils.DetailLevel detail, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.heros_fortitude.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.heros_fortitude.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.heros_fortitude.line.3"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.heros_fortitude.line.1", getStatForRank(Stats.CHANCE, rank) * 100.0),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.heros_fortitude.line.2", ((int)getStatForRank(Stats.AMOUNT, rank) + 1) * 4),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
    //endregion

    @SubscribeEvent
    public void livingDeathEvent(LivingDeathEvent event) {
        LivingEntity defender = event.getEntity();
        if (defender.getType().getCategory().equals(MobCategory.MONSTER) && event.getSource().getEntity() instanceof ServerPlayer attacker) {
            ItemStack itemStack = attacker.getMainHandItem();
            if (itemStack.getItem() instanceof AbilityItem weapon) {
                int rank = weapon.getRank(itemStack);
                HerosFortitudeAbility ability = weapon.getAbility(ExoArmory.REGISTRY.ABILITY_HEROS_FORTITUDE, itemStack, rank);
                if (ability != null && attacker.getRandom().nextDouble() < ability.getStatForRank(Stats.CHANCE, rank)) {
                    attacker.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 400, (int)ability.getStatForRank(Stats.AMOUNT, rank)));
                    attacker.playNotifySound(ExoArmory.REGISTRY.SOUND_MAGIC_CLASH.get(), SoundSource.PLAYERS, 0.333F, 0.667F);
                }
            }
        }
    }
}
