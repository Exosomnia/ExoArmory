package com.exosomnia.exoarmory.networking.packets;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AbilityActivePacket {

    public boolean active = false;

    public AbilityActivePacket(boolean active) {
        this.active = active;
    }

    public AbilityActivePacket(FriendlyByteBuf buffer) {
        active = buffer.readBoolean();
    }

    public static void encode(AbilityActivePacket packet, FriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.active);
    }

    public static void handle(AbilityActivePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            NetworkDirection packetDirection = context.get().getDirection();
            if (packetDirection.equals(NetworkDirection.PLAY_TO_SERVER)) {
                ServerPlayer player = context.get().getSender();
                if (player != null) { ExoArmory.ABILITY_MANAGER.setPlayerActive(player, packet.active); }
            }
        });
        context.get().setPacketHandled(true);
    }
}
