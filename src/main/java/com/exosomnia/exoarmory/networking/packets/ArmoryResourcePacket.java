package com.exosomnia.exoarmory.networking.packets;

import com.exosomnia.exoarmory.capabilities.resource.ArmoryResourceProvider;
import com.exosomnia.exoarmory.items.armory.ArmoryItem;
import com.exosomnia.exoarmory.items.armory.ResourcedItem;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ArmoryResourcePacket {

    private UUID uuid;
    private int slot;
    private double charge;

    public ArmoryResourcePacket(UUID uuid, int slot, double charge) {
        this.uuid = uuid;
        this.slot = slot;
        this.charge = charge;
    }

    public ArmoryResourcePacket(FriendlyByteBuf buffer) {
        uuid = buffer.readUUID();
        slot = buffer.readInt();
        charge = buffer.readDouble();
    }

    public static void encode(ArmoryResourcePacket packet, FriendlyByteBuf buffer) {
        buffer.writeUUID(packet.uuid);
        buffer.writeInt(packet.slot);
        buffer.writeDouble(packet.charge);
    }

    public static void handle(ArmoryResourcePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            NetworkDirection packetDirection = context.get().getDirection();
            if (packetDirection.equals(NetworkDirection.PLAY_TO_CLIENT)) {
                ItemStack item = Minecraft.getInstance().player.getInventory().getItem(packet.slot);
                if ((item.getItem() instanceof ArmoryItem armoryItem) && (armoryItem instanceof ResourcedItem) && (armoryItem.getUUID(item).equals(packet.uuid))){
                    item.getCapability(ArmoryResourceProvider.ARMORY_RESOURCE).resolve().get().setCharge(packet.charge);
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
