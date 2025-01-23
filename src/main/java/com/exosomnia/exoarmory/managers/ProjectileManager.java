package com.exosomnia.exoarmory.managers;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.capabilities.aethersembrace.AethersEmbraceProvider;
import com.exosomnia.exoarmory.capabilities.projectile.ArmoryArrowProvider;
import com.exosomnia.exoarmory.capabilities.projectile.IArmoryArrowStorage;
import com.exosomnia.exoarmory.entities.projectiles.EphemeralArrow;
import com.exosomnia.exoarmory.items.armory.bows.AethersEmbraceBow;
import com.exosomnia.exoarmory.networking.PacketHandler;
import com.exosomnia.exoarmory.networking.packets.AethersEmbraceTargetPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
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
                            aetherData.setExpire(attacker.level().getGameTime() + 100 * ((long)projectileData.getArrowRank() + 1));
                            PacketHandler.sendToPlayer(new AethersEmbraceTargetPacket(itemUUID, attacker.getInventory().selected,
                                    aetherData.getTarget(), aetherData.getExpire()), attacker);
                        });
                    }
                }
            });
        }
    }

    //Adds entity's ranged strength attribute to arrow data
    @SubscribeEvent
    public void projectileJoin(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide
                && event.getEntity() instanceof AbstractArrow projectile
                && projectile.getOwner() instanceof LivingEntity owner) {
            AttributeInstance strength = owner.getAttribute(ExoArmory.REGISTRY.ATTRIBUTE_RANGED_STRENGTH.get());
            if (strength != null) {
                projectile.getCapability(ArmoryArrowProvider.ARMORY_PROJECTILE).ifPresent(projectileData ->
                        projectileData.setStrength(strength.getValue()));
            }

            ItemStack weapon = owner.getMainHandItem();
            if (owner instanceof ServerPlayer player && weapon.getItem() instanceof AethersEmbraceBow bow && ExoArmory.ABILITY_MANAGER.isPlayerActive(player)) {
                projectile.getCapability(ArmoryArrowProvider.ARMORY_PROJECTILE).ifPresent(projectileData -> {
                    projectileData.setItemUUID(bow.getUUID(weapon));
                    projectileData.setArrowType(IArmoryArrowStorage.ArmoryArrowType.AETHER.getType());
                    projectileData.setArrowRank(bow.getRank(weapon));
                });
            }
        }
    }

    //Removes an arrow on impact if it's ephemeral
    @SubscribeEvent
    public void projectileImpact(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (projectile.level().isClientSide && projectile instanceof AbstractArrow) { return; }
        projectile.getCapability(ArmoryArrowProvider.ARMORY_PROJECTILE).ifPresent(projectileData -> {
            if (projectileData.isEphemeral()) { projectile.remove(Entity.RemovalReason.DISCARDED); }
        });
    }
}
