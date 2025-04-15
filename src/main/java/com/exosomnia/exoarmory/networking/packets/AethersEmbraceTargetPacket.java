package com.exosomnia.exoarmory.networking.packets;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.capabilities.aethersembrace.AethersEmbraceProvider;
import com.exosomnia.exoarmory.items.armory.bows.AethersEmbraceBow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class AethersEmbraceTargetPacket {

    public UUID itemUUID;
    public int slot;
    public UUID targetUUID;
    public long expirationTime;

    public AethersEmbraceTargetPacket(UUID itemUUID, int slot, UUID targetUUID, long expirationTime) {
        this.itemUUID = itemUUID;
        this.slot = slot;
        this.targetUUID = targetUUID;
        this.expirationTime = expirationTime;
    }

    public AethersEmbraceTargetPacket(FriendlyByteBuf buffer) {
        itemUUID = buffer.readUUID();
        slot = buffer.readInt();
        targetUUID = buffer.readUUID();
        expirationTime = buffer.readLong();
    }

    public static void encode(AethersEmbraceTargetPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUUID(packet.itemUUID);
        buffer.writeInt(packet.slot);
        buffer.writeUUID(packet.targetUUID);
        buffer.writeLong(packet.expirationTime);
    }

    public static void handle(AethersEmbraceTargetPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            NetworkDirection packetDirection = context.get().getDirection();
            if (packetDirection.equals(NetworkDirection.PLAY_TO_CLIENT)) {
                LocalPlayer player = Minecraft.getInstance().player;
                ItemStack itemStack = player.getInventory().getItem(packet.slot);
                if (itemStack.getItem() instanceof AethersEmbraceBow bow && bow.getUUID(itemStack).equals(packet.itemUUID)) {
                    itemStack.getCapability(AethersEmbraceProvider.AETHERS_EMBRACE).ifPresent(data -> {
                        data.setTarget(packet.targetUUID);
                        data.setExpire(packet.expirationTime);
                        if (packet.expirationTime > 0) player.playSound(ExoArmory.REGISTRY.SOUND_MAGIC_CLASH.get(), 0.34F, 1.75F);
                    });
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
