package com.exosomnia.exoarmory.items.abilities;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.capabilities.resource.ArmoryResourceProvider;
import com.exosomnia.exoarmory.items.armory.swords.SolarSword;
import com.exosomnia.exoarmory.networking.PacketHandler;
import com.exosomnia.exoarmory.networking.packets.ArmoryResourcePacket;
import com.exosomnia.exoarmory.utils.TooltipUtils;
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
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ExoArmory.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SolarFlareAbility extends ArmoryAbility {

    public SolarFlareAbility() {
        super("solar_flare", 0xFFBF00);
    }

    @Override
    public List<MutableComponent> getTooltip(boolean detailed, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detailed, rank));

        if (detailed) {
            description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.desc.solar_flare.line.1"), TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
            description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.desc.solar_flare.line.2"), TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
        }

        return description;
    }

    //region Ability Logic
    public static void createSolarFlare(Vec3 position, ServerLevel level, @Nullable LivingEntity owner, @Nullable LivingEntity defender) {
        double size = 3.0;
        int burnTime = 5;
        float damage = 6.0F;
        DamageSource source = owner == null ? level.damageSources().explosion(null, null) :
                owner.damageSources().explosion(owner, owner);

        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, new AABB(position, position).inflate(size), livingEntity -> {
            if (!livingEntity.isAlive() || livingEntity == owner || livingEntity.equals(defender)) { return false; }
            else if ( (livingEntity instanceof TamableAnimal tamableAnimal) && (tamableAnimal.isTame()) ) { return false; }
            else return (!(livingEntity instanceof NeutralMob neutralMob)) || ((owner != null) && (neutralMob.isAngryAt(owner)));
        });

        //if (defender != null) { defender.setSecondsOnFire(burnTime); }
        for (LivingEntity entity : nearbyEntities) {
            Vec3 entityPos = new Vec3(entity.position().toVector3f());
            if (entityPos.distanceTo(position) <= size) {
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
                            position, new ParticleShapeOptions.Dome(3.0F, 128))), player);
        }
    }

    @SubscribeEvent
    public static void onEntityCrit(CriticalHitEvent event) {
        Player player = event.getEntity();
        if (!(player.level() instanceof ServerLevel level)
                || !event.isVanillaCritical()
                || !(event.getTarget() instanceof LivingEntity defender)) return;

        ServerPlayer attacker = (ServerPlayer)player;
        ItemStack attackerItem = attacker.getMainHandItem();
        if (attackerItem.getItem() instanceof SolarSword solarSword) {
            if (attacker.hasEffect(ExoArmory.REGISTRY.EFFECT_STELLAR_INFUSION.get())) {
                createSolarFlare(defender.position(), level, attacker, defender);
            }
            else if (solarSword.getResource(attackerItem) >= 25.0) {
                createSolarFlare(defender.position(), level, attacker, defender);
                attackerItem.getCapability(ArmoryResourceProvider.ARMORY_RESOURCE).ifPresent(iArmoryResourceStorage -> {
                    iArmoryResourceStorage.removeCharge(25.0);
                    PacketHandler.sendToPlayer(new ArmoryResourcePacket(solarSword.getUUID(attackerItem), attacker.getInventory().selected, iArmoryResourceStorage.getCharge()),
                            attacker);
                });
            }
        }
    }
    //endregion
}
