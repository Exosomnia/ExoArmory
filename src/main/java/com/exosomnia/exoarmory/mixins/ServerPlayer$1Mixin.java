package com.exosomnia.exoarmory.mixins;

import com.exosomnia.exoarmory.items.resource.ResourcedItem;
import com.exosomnia.exoarmory.networking.PacketHandler;
import com.exosomnia.exoarmory.networking.packets.ArmoryResourcePacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.server.level.ServerPlayer$1")
public abstract class ServerPlayer$1Mixin {

    @Redirect(method = "sendInitialData", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"))
    private void sendSlotInitialResource(ServerGamePacketListenerImpl connection, Packet<ClientGamePacketListener> packet) {
        connection.send(packet);
        ClientboundContainerSetContentPacket slotPacket = ((ClientboundContainerSetContentPacket)packet);
        for(ItemStack itemStack : slotPacket.getItems()) {
            if (itemStack.getItem() instanceof ResourcedItem resourcedItem) {
                PacketHandler.sendToPlayer(new ArmoryResourcePacket(resourcedItem.getUUID(itemStack), resourcedItem.getResource().getResource(itemStack)), connection.player);
            }
        }
    }
}
