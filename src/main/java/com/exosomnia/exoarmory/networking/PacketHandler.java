package com.exosomnia.exoarmory.networking;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.networking.packets.AbilityActivePacket;
import com.exosomnia.exoarmory.networking.packets.AethersEmbraceTargetPacket;
import com.exosomnia.exoarmory.networking.packets.ArmoryResourcePacket;
import com.exosomnia.exoarmory.networking.packets.ArmoryResourcePacketOld;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(ExoArmory.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;

        INSTANCE.registerMessage(id++, ArmoryResourcePacket.class, ArmoryResourcePacket::encode, ArmoryResourcePacket::new, ArmoryResourcePacket::handle);
        //TODO: DEBUGGING
        INSTANCE.registerMessage(id++, ArmoryResourcePacketOld.class, ArmoryResourcePacketOld::encode, ArmoryResourcePacketOld::new, ArmoryResourcePacketOld::handle);
        INSTANCE.registerMessage(id++, AbilityActivePacket.class, AbilityActivePacket::encode, AbilityActivePacket::new, AbilityActivePacket::handle);
        INSTANCE.registerMessage(id++, AethersEmbraceTargetPacket.class, AethersEmbraceTargetPacket::encode, AethersEmbraceTargetPacket::new, AethersEmbraceTargetPacket::handle);
    }

    public static void sendToPlayer(Object packet, ServerPlayer player) {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }
}
