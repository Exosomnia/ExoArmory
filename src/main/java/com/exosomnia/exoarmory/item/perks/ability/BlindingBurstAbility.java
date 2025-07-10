package com.exosomnia.exoarmory.item.perks.ability;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.utils.TargetUtils;
import com.exosomnia.exolib.ExoLib;
import com.exosomnia.exolib.networking.PacketHandler;
import com.exosomnia.exolib.networking.packets.ParticleShapePacket;
import com.exosomnia.exolib.particles.options.RGBSParticleOptions;
import com.exosomnia.exolib.particles.shapes.ParticleShapeDome;
import com.exosomnia.exolib.particles.shapes.ParticleShapeOptions;
import com.exosomnia.exolib.utils.ColorUtils;
import com.exosomnia.exolib.utils.ComponentUtils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlindingBurstAbility extends ArmoryAbility {

    public enum Stats implements AbilityStat {
        COOLDOWN,
        DURATION,
        RADIUS
    }

    public BlindingBurstAbility() {
        super("blinding_burst", 0xFFD6AB);
    }

    //region ArmoryAbility Overrides
    public void buildStats() {
        RANK_STATS.put(Stats.COOLDOWN, new double[] {
                20.0, 19.0, 18.0, 17.0, 16.0, 15.0
        });
        RANK_STATS.put(Stats.DURATION, new double[] {
                4.0, 4.5, 5.0, 5.33, 5.66, 6.0
        });
        RANK_STATS.put(Stats.RADIUS, new double[] {
                4.0, 4.0, 5.0, 5.0, 5.0, 5.5
        });
    }

    @Override
    public List<MutableComponent> getTooltip(ComponentUtils.DetailLevel detail, ItemStack itemStack, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, itemStack, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.blinding_burst.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.blinding_burst.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.blinding_burst.line.3"), ComponentUtils.Styles.DEFAULT_DESC.getStyle(),
                        ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.blinding_burst.line.4"), ComponentUtils.Styles.DEFAULT_DESC.getStyle(),
                        ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.blinding_burst.line.1", getStatForRank(Stats.COOLDOWN, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.blinding_burst.line.2", getStatForRank(Stats.DURATION, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.blinding_burst.line.3", getStatForRank(Stats.RADIUS, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.blinding_burst.line.4", 5),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
    //endregion

    public int createBurst(ServerLevel level, Vec3 position, @Nullable LivingEntity owner, int rank) {
        double radius = getStatForRank(Stats.RADIUS, rank);
        int duration = (int)(getStatForRank(Stats.DURATION, rank) * 20.0);

        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, new AABB(position, position).inflate(radius), livingEntity ->
                TargetUtils.validTarget(owner, livingEntity) && livingEntity.position().distanceToSqr(position) <= radius * radius);
        for (LivingEntity blindedEntity : nearbyEntities) {
            blindedEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, duration, 0));
            blindedEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration, 0));
            blindedEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, 0));

            Vec3 launchVec = blindedEntity.position().subtract(position).normalize().multiply(0.333, 0.333, 0.333);
            blindedEntity.push(launchVec.x, launchVec.y + 0.333, launchVec.z);
        }

        //Visual and audio effects
        float[] colors = ColorUtils.intToFloats(0xFFD6AB);
        ParticleShapePacket packet = new ParticleShapePacket(new ParticleShapeDome(new RGBSParticleOptions(ExoLib.REGISTRY.TWINKLE_PARTICLE.get(),
                colors[0], colors[1], colors[2], 0.15F), position, new ParticleShapeOptions.Dome((float)radius, 128)));
        for(ServerPlayer player : level.players()) {
            PacketHandler.sendToPlayer(packet, player);
        }
        level.playSound(null, position.x, position.y, position.z, ExoArmory.REGISTRY.SOUND_FIERY_EFFECT.get(), SoundSource.PLAYERS, 0.5F, 2.0F);

        return nearbyEntities.size();
    }
}
