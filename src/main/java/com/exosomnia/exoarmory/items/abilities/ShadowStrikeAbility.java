package com.exosomnia.exoarmory.items.abilities;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.items.armory.ArmoryItem;
import com.exosomnia.exolib.utils.ComponentUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ShadowStrikeAbility extends ArmoryAbility {

    public enum Stats implements AbilityStat {
        DAMAGE,
        DURATION
    }

    public ShadowStrikeAbility() {
        super("shadow_strike", 0x686673);
    }

    //region ArmoryAbility Overrides
    public void buildRanks() {
        RANK_STATS.put(Stats.DAMAGE, new double[]{0.0, 0.0, 0.0, 1.0, 2.0});
        RANK_STATS.put(Stats.DURATION, new double[]{0.0, 0.0, 0.0, 10.0, 10.0});
    }

    @Override
    public List<MutableComponent> getTooltip(ComponentUtils.DetailLevel detail, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.shadow_strike.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.shadow_strike.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.shadow_strike.line.3"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.shadow_strike.line.1", getStatForRank(Stats.DAMAGE, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.shadow_strike.line.2", (int)getStatForRank(Stats.DURATION, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
    //endregion

    @SubscribeEvent
    public void livingAttackEvent(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ItemStack itemStack = attacker.getMainHandItem();
            if (itemStack.getItem() instanceof AbilityItem weapon) {
                int rank = weapon.getRank(itemStack);
                ShadowStrikeAbility ability = weapon.getAbility(ExoArmory.REGISTRY.ABILITY_SHADOW_STRIKE, itemStack, rank);
                if (ability != null && attacker.hasEffect(MobEffects.INVISIBILITY)) {
                    event.setAmount(event.getAmount() + (float)ability.getStatForRank(Stats.DAMAGE, rank));
                }
            }
        }
    }

    @SubscribeEvent
    public void livingEntityDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ItemStack itemStack = attacker.getMainHandItem();
            if (itemStack.getItem() instanceof AbilityItem weapon) {
                int rank = weapon.getRank(itemStack);
                ShadowStrikeAbility ability = weapon.getAbility(ExoArmory.REGISTRY.ABILITY_SHADOW_STRIKE, itemStack, rank);
                if (ability != null && attacker.hasEffect(MobEffects.INVISIBILITY)) {
                    MobEffectInstance currentEffect = attacker.getEffect(MobEffects.INVISIBILITY);
                    attacker.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, (int) (currentEffect.getDuration() + getStatForRank(Stats.DURATION, rank) * 20.0),
                            currentEffect.getAmplifier(), true, false, true));
                }
            }
        }
    }
}
