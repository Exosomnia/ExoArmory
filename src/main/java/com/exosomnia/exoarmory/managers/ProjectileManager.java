package com.exosomnia.exoarmory.managers;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.capabilities.armory.item.aethersembrace.AethersEmbraceProvider;
import com.exosomnia.exoarmory.capabilities.projectile.ArmoryArrowProvider;
import com.exosomnia.exoarmory.capabilities.projectile.IArmoryArrowStorage;
import com.exosomnia.exoarmory.entities.projectiles.EphemeralArrow;
import com.exosomnia.exoarmory.item.perks.ability.Abilities;
import com.exosomnia.exoarmory.item.perks.ability.AetherBarrageAbility;
import com.exosomnia.exoarmory.item.perks.ability.SpectralPierceAbility;
import com.exosomnia.exoarmory.item.armory.bows.AethersEmbraceBow;
import com.exosomnia.exoarmory.item.armory.bows.ArmoryBowItem;
import com.exosomnia.exoarmory.item.perks.resource.ArmoryResource;
import com.exosomnia.exoarmory.mixin.mixins.AbstractArrowAccessor;
import com.exosomnia.exoarmory.networking.PacketHandler;
import com.exosomnia.exoarmory.networking.packets.AethersEmbraceTargetPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class ProjectileManager {

    //Adds strength on arrow projectile data to event damage
    @SubscribeEvent
    public void projectileHit(LivingHurtEvent event) {
        Entity sourceEntity = event.getSource().getDirectEntity();
        if (sourceEntity != null && !sourceEntity.level().isClientSide && sourceEntity instanceof AbstractArrow arrow) {
            arrow.getCapability(ArmoryArrowProvider.ARMORY_PROJECTILE).ifPresent(projectileData -> {
                event.setAmount(event.getAmount() * (float)projectileData.getStrength());
                LivingEntity defender = event.getEntity();

                if (arrow instanceof EphemeralArrow) { defender.invulnerableTime = 15; }
                if (projectileData.getArrowType() == IArmoryArrowStorage.ArmoryArrowType.AETHER.getType()) {
                    UUID itemUUID = projectileData.getItemUUID();
                    if (itemUUID == null || !(arrow.getOwner() instanceof ServerPlayer attacker)) return;

                    ItemStack wielding = attacker.getMainHandItem();
                    if (wielding.getItem() instanceof AethersEmbraceBow bow && bow.getUUID(wielding).equals(itemUUID)) {

                        wielding.getCapability(AethersEmbraceProvider.AETHERS_EMBRACE).ifPresent(aetherData -> {
                            aetherData.setTarget(defender.getUUID());
                            aetherData.setExpire(attacker.level().getGameTime() + (int) Abilities.AETHER_BARRAGE.getStatForRank(AetherBarrageAbility.Stats.DURATION, projectileData.getArrowRank()));
                            PacketHandler.sendToPlayer(new AethersEmbraceTargetPacket(itemUUID, attacker.getInventory().selected,
                                    aetherData.getTarget(), aetherData.getExpire()), attacker);
                            projectileData.setArrowType(IArmoryArrowStorage.ArmoryArrowType.NORMAL.getType());
                        });
                    }
                }
            });
        }
    }

    //Adds entity's ranged strength attribute to arrow data
    @SubscribeEvent
    public void projectileJoin(EntityJoinLevelEvent event) {
        Level level = event.getLevel();
        if (!level.isClientSide
                && event.getEntity() instanceof AbstractArrow projectile
                && projectile.getOwner() instanceof LivingEntity owner) {
            AttributeInstance strength = owner.getAttribute(ExoArmory.REGISTRY.ATTRIBUTE_RANGED_STRENGTH.get());
            AttributeInstance pierce = owner.getAttribute(ExoArmory.REGISTRY.ATTRIBUTE_ARROW_PIERCE.get());
            projectile.getCapability(ArmoryArrowProvider.ARMORY_PROJECTILE).ifPresent(projectileData -> {
                if (strength != null) {
                    projectileData.setStrength(strength.getValue());
                }

                if (owner instanceof ServerPlayer player && owner.isUsingItem()) {
                    ItemStack weapon = owner.getUseItem();
                    Item item = weapon.getItem();
                    if (item instanceof ArmoryBowItem armoryBow) {
                        projectileData.setItemUUID(armoryBow.getUUID(weapon));
                        if (item instanceof AethersEmbraceBow aetherBow) {
                            int rank = aetherBow.getRank(weapon);
                            //Check for Spectral Pierce ability
                            SpectralPierceAbility ability = Abilities.SPECTRAL_PIERCE;
                            if (projectile instanceof SpectralArrow && aetherBow.getAbilities(weapon, owner).containsKey(Abilities.SPECTRAL_PIERCE)) {
                                projectile.setPierceLevel((byte)ability.getStatForRank(SpectralPierceAbility.Stats.LEVEL, rank));
                            }
                            //Check for Aether Barrage ability
                            if (ExoArmory.ABILITY_MANAGER.isPlayerActive(player)) {

                                ArmoryResource resource = aetherBow.getResource();
                                double cost = Abilities.AETHER_BARRAGE.getStatForRank(AetherBarrageAbility.Stats.COST, rank);
                                if (resource.getResource(weapon) >= cost) {
                                    resource.removeResource(weapon, cost);
                                    projectileData.setArrowType(IArmoryArrowStorage.ArmoryArrowType.AETHER.getType());
                                    projectileData.setArrowRank(aetherBow.getRank(weapon));
                                }
                            }
                        }
                    }
                    double pierceValue = pierce.getValue();
                    int guaranteedPierce = (int)(pierceValue - 1.0);
                    guaranteedPierce += (level.random.nextDouble() < pierceValue % 1.0) ? 1 : 0;
                    if (guaranteedPierce > 0) {
                        projectile.setPierceLevel((byte) guaranteedPierce);
                        player.playNotifySound(SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 2.0F);
                    }
                }
            });
        }
    }

    //Removes an arrow on impact if it's ephemeral
    @SubscribeEvent
    public void projectileImpact(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (projectile.level().isClientSide || !(projectile instanceof AbstractArrow arrow)) { return; }
        projectile.getCapability(ArmoryArrowProvider.ARMORY_PROJECTILE).ifPresent(projectileData -> {
            if (projectileData.isEphemeral()) { projectile.remove(Entity.RemovalReason.DISCARDED); }
        });

        if (arrow.getOwner() instanceof Player player && event.getRayTraceResult().getType().equals(HitResult.Type.BLOCK) &&
                arrow.getPersistentData().getBoolean("Recovery")) {
            player.getInventory().add(((AbstractArrowAccessor)arrow).invokeGetPickupItem());
            player.take(arrow, 1);
            arrow.discard();
        }
    }
}
