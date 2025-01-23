package com.exosomnia.exoarmory.items.abilities;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.items.armory.swords.SolarSword;
import com.exosomnia.exoarmory.items.resource.ArmoryResource;
import com.exosomnia.exoarmory.networking.PacketHandler;
import com.exosomnia.exoarmory.networking.packets.ArmoryResourcePacket;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.exosomnia.exolib.utils.ComponentUtils.DetailLevel;
import com.exosomnia.exolib.ExoLib;
import com.exosomnia.exolib.networking.packets.ParticleShapePacket;
import com.exosomnia.exolib.particles.options.RGBSParticleOptions;
import com.exosomnia.exolib.particles.shapes.ParticleShapeDome;
import com.exosomnia.exolib.particles.shapes.ParticleShapeOptions;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SolarFlareAbility extends ArmoryAbility {

    public enum Stats implements AbilityStat {
        COST,
        RADIUS,
        DAMAGE,
        BURN_TIME
    }

    public SolarFlareAbility() {
        super("solar_flare", 0xFFBF00);
    }

    //region ArmoryAbility Overrides
    public void buildRanks() {
        RANK_STATS.put(Stats.COST, new double[]{25.0, 25.0, 25.0, 25.0, 25.0});
        RANK_STATS.put(Stats.RADIUS, new double[]{2.5, 2.5, 3.0, 3.0, 3.5});
        RANK_STATS.put(Stats.DAMAGE, new double[]{3.0, 3.0, 4.0, 4.0, 5.0});
        RANK_STATS.put(Stats.BURN_TIME, new double[]{3.0, 3.0, 4.0, 4.0, 5.0});
    }

    @Override
    public List<MutableComponent> getTooltip(DetailLevel detail, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.solar_flare.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.solar_flare.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.solar_flare.line.1", getStatForRank(Stats.COST, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.solar_flare.line.2", getStatForRank(Stats.RADIUS, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.solar_flare.line.3", getStatForRank(Stats.DAMAGE, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.solar_flare.line.4", (int)getStatForRank(Stats.BURN_TIME, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
    //endregion

    //region Ability Logic
    public void createSolarFlare(Vec3 position, ServerLevel level, int rank, @Nullable LivingEntity owner, @Nullable LivingEntity defender) {
        double radius = getStatForRank(Stats.RADIUS, rank);
        int burnTime = (int)getStatForRank(Stats.BURN_TIME, rank);
        float damage = (float)getStatForRank(Stats.RADIUS, rank);
        DamageSource source = owner == null ? level.damageSources().explosion(null, null) :
                owner.damageSources().explosion(owner, owner);

        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, new AABB(position, position).inflate(radius), livingEntity -> {
            if (!livingEntity.isAlive() || livingEntity == owner || livingEntity == defender) { return false; }
            else if ( (livingEntity instanceof TamableAnimal tamableAnimal) && (tamableAnimal.isTame()) ) { return false; }
            else return (!(livingEntity instanceof NeutralMob neutralMob)) || ((owner != null) && (neutralMob.isAngryAt(owner)));
        });

        if (defender != null) { defender.setSecondsOnFire(burnTime); }
        for (LivingEntity entity : nearbyEntities) {
            Vec3 entityPos = new Vec3(entity.position().toVector3f());
            if (entityPos.distanceTo(position) <= radius) {
                entity.setSecondsOnFire(burnTime);
                entity.hurt(source, damage);
                entityPos = entityPos.subtract(position).normalize().multiply(0.67, 0.33, 0.67);
                entity.push(entityPos.x, entityPos.y + 0.33, entityPos.z);
            }
        }
        level.playSeededSound(null, position.x, position.y, position.z, ExoArmory.REGISTRY.SOUND_FIERY_EXPLOSION.get(), SoundSource.PLAYERS,
                0.34F, 1.25F, 0);
        level.sendParticles(ParticleTypes.FLAME, position.x, position.y+.5, position.z, 25, 0, 0, 0, 0.075);

        for(ServerPlayer player : level.players()) {
            com.exosomnia.exolib.networking.PacketHandler.sendToPlayer(new ParticleShapePacket(
                    new ParticleShapeDome(new RGBSParticleOptions(ExoLib.REGISTRY.SPIRAL_PARTICLE.get(), 1.0F, 0.5F, 0.0F, 0.1f),
                            position, new ParticleShapeOptions.Dome((float)radius, 128))), player);
        }
    }

    @SubscribeEvent
    public void onEntityCrit(CriticalHitEvent event) {
        Player player = event.getEntity();
        if (!(player.level() instanceof ServerLevel level)
                || !event.isVanillaCritical()
                || !(event.getTarget() instanceof LivingEntity defender)) return;

        ServerPlayer attacker = (ServerPlayer)player;
        ItemStack attackerItem = attacker.getMainHandItem();
        if (attackerItem.getItem() instanceof SolarSword solarSword) {
            int rank = solarSword.getRank(attackerItem);
            if (attacker.hasEffect(ExoArmory.REGISTRY.EFFECT_SUNFIRE_SURGE.get())) {
                createSolarFlare(defender.position(), level, rank, attacker, defender);
                return;
            }
            ArmoryResource resource = solarSword.getResource();
            if (resource.getResource(attackerItem) >= getStatForRank(Stats.COST, rank)) {
                createSolarFlare(defender.position(), level, rank, attacker, defender);
                resource.removeResource(attackerItem, getStatForRank(Stats.COST, rank));
                PacketHandler.sendToPlayer(new ArmoryResourcePacket(solarSword.getUUID(attackerItem), attacker.getInventory().selected, resource.getResource(attackerItem)),
                        attacker);
            }
        }
    }
    //endregion
}
