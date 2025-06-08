package com.exosomnia.exoarmory.item.ability;

import com.exosomnia.exolib.utils.ComponentUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public class ColdSnapAbility extends ArmoryAbility {

    public enum Stats implements AbilityStat {
        RADIUS
    }

    public ColdSnapAbility() {
        super("cold_snap", 0x1695A6);
    }

    //region ArmoryAbility Overrides
    public void buildRanks() {
        RANK_STATS.put(Stats.RADIUS, new double[]{0.0, 0.0, 4.0, 5.0, 6.0});
    }

    @Override
    public List<MutableComponent> getTooltip(ComponentUtils.DetailLevel detail, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.cold_snap.line.1"),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.cold_snap.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.cold_snap.line.1", getStatForRank(Stats.RADIUS, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
    //endregion

//  TODO:REFACTOR TO NEW SYSTEM
//    @SubscribeEvent
//    public void livingDeathEvent(LivingDeathEvent event) {
//        LivingEntity defender = event.getEntity();
//        if (event.getSource().getEntity() instanceof ServerPlayer attacker) {
//            ItemStack itemStack = attacker.getMainHandItem();
//            if (itemStack.getItem() instanceof AbilityItem weapon) {
//                int rank = weapon.getRank(itemStack);
//                ColdSnapAbility ability = weapon.getAbility(ExoArmory.REGISTRY.ABILITY_COLD_SNAP, itemStack, rank);
//                if (defender.hasEffect(ExoArmory.REGISTRY.EFFECT_FROSTED.get()) && ability != null) {
//                    ServerLevel level = (ServerLevel)defender.level();
//                    Vec3 center = defender.position();
//                    level.playSound(null, defender.getX(), defender.getY(), defender.getZ(),
//                            ExoArmory.REGISTRY.SOUND_MAGIC_ICE_CAST.get(), SoundSource.PLAYERS, 0.34F, 1.0F);
//                    level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, center.x, center.y + 0.5, center.z, 30, 0, 0, 0, 0.125);
//                    for(ServerPlayer player : level.players()) {
//                        float[] color = ColorUtils.intToFloats(0x1695A6);
//                        com.exosomnia.exolib.networking.PacketHandler.sendToPlayer(new ParticleShapePacket(new ParticleShapeRing(new RGBSParticleOptions(ExoLib.REGISTRY.SPARKLE_PARTICLE.get(), color[0], color[1], color[2], 0.2F), defender.position().add(0, .5, 0),
//                                new ParticleShapeOptions.Ring((float)ability.getStatForRank(Stats.RADIUS, rank), 80))), player);
//                    }
//
//                    double radius = ability.getStatForRank(Stats.RADIUS, rank);
//                    AABB area = new AABB(center.x, center.y, center.z, center.x, center.y, center.z).inflate(radius);
//                    List<LivingEntity> validTargets = defender.level().getEntitiesOfClass(LivingEntity.class, area, target -> TargetUtils.validTarget(attacker, target));
//                    for (LivingEntity target : validTargets) {
//                        target.addEffect(new MobEffectInstance(ExoArmory.REGISTRY.EFFECT_FROSTED.get(), 200, 0));
//                    }
//                }
//            }
//        }
//    }
}
