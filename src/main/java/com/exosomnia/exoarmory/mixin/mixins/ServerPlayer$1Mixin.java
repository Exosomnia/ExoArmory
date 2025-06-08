package com.exosomnia.exoarmory.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net.minecraft.server.level.ServerPlayer$1")
public abstract class ServerPlayer$1Mixin {

//    @Redirect(method = "sendInitialData", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"))
//    private void sendSlotInitialResource(ServerGamePacketListenerImpl connection, Packet<ClientGamePacketListener> packet) {
//        connection.send(packet);
//        ClientboundContainerSetContentPacket slotPacket = ((ClientboundContainerSetContentPacket)packet);
//        for(ItemStack itemStack : slotPacket.getItems()) {
//            if (itemStack.getItem() instanceof ResourcedItem resourcedItem) {
//                PacketHandler.sendToPlayer(new ArmoryResourcePacket(resourcedItem.getUUID(itemStack), resourcedItem.getResource().getResource(itemStack)), connection.player);
//            }
//        }
//    }
}
