package com.exosomnia.exoarmory.dist;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class ClientDistHelper extends DistHelper {

    @Override
    public Level getDefaultLevel() {
        return Minecraft.getInstance().level;
    }

    @Override
    public Entity getEntity(UUID target) {
        for (Entity entity : ((ClientLevel)getDefaultLevel()).entitiesForRendering()) {
            if (entity.getUUID().equals(target)) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public Player getDefaultPlayer() {
        return Minecraft.getInstance().player;
    }
}
