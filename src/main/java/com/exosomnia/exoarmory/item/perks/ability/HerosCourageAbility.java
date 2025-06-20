package com.exosomnia.exoarmory.item.perks.ability;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.utils.AbilityItemUtils;
import com.exosomnia.exolib.utils.ComponentUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class HerosCourageAbility extends ArmoryAbility {

    public enum Stats implements AbilityStat {
        THRESHOLD,
        RANGE,
        BONUS
    }

    public HerosCourageAbility() {
        super("heros_courage", 0xD43F51);
    }

    //region ArmoryAbility Overrides
    public void buildStats() {
        RANK_STATS.put(Stats.THRESHOLD, new double[]{
                5.0, 4.0, 3.0
        });
        RANK_STATS.put(Stats.RANGE, new double[]{
                6.5, 7.0, 7.5
        });
        RANK_STATS.put(Stats.BONUS, new double[]{
                0.0, 0.0, 1.0
        });
    }

    @Override
    public List<MutableComponent> getTooltip(ComponentUtils.DetailLevel detail, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.heros_courage.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle(),
                        ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.heros_courage.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle(),
                        ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.heros_courage.line.3"), ComponentUtils.Styles.DEFAULT_DESC.getStyle(),
                        ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.heros_courage.line.1", (int)getStatForRank(Stats.THRESHOLD, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.heros_courage.line.2", getStatForRank(Stats.RANGE, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.heros_courage.line.3", ((int)getStatForRank(Stats.BONUS, rank) + 1) * 2.5),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
    //endregion

//  TODO:REFACTOR TO NEW SYSTEM
    @SubscribeEvent
    public static void playerTickEvent(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();
        if (event.phase.equals(TickEvent.Phase.END) || level.isClientSide || level.getGameTime() % 10 != 0) return;

        ItemStack itemStack = player.getMainHandItem();
        if (itemStack.getItem() instanceof AbilityItem weapon) {
            int rank = AbilityItemUtils.getAbilityRank(Abilities.HEROS_COURAGE, itemStack, player);
            if (weapon.getAbilities(itemStack, player).containsKey(Abilities.HEROS_COURAGE)) {
                int count = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(Abilities.HEROS_COURAGE.getStatForRank(Stats.RANGE, rank)),
                        (entity) -> entity.getType().getCategory().equals(MobCategory.MONSTER)).size();
                if (count >= Abilities.HEROS_COURAGE.getStatForRank(Stats.THRESHOLD, rank)) {
                    player.addEffect(new MobEffectInstance(ExoArmory.REGISTRY.EFFECT_COURAGE.get(), 40, (int)Abilities.HEROS_COURAGE.getStatForRank(Stats.BONUS, rank), true, false, true));
                }
            }
        }
    }
}
