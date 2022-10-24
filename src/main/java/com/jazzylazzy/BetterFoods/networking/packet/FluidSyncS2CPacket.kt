package com.jazzylazzy.BetterFoods.networking.packet

import com.jazzylazzy.BetterFoods.blocks.entities.RiceCookerEntity
import com.jazzylazzy.BetterFoods.screen.RiceCookerScreenHandler
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf


//TODO implement Fluid Sync
/*class FluidSyncS2CPacket {

    fun receive(client: MinecraftClient, handler: ClientPlayNetworkHandler,
                buf: PacketByteBuf, responseSender: PacketSender) {

        var variant = FluidVariant.fromPacket(buf);
        var fluidLevel = buf.readLong();
        var position = buf.readBlockPos();

        val blockEntity = client.world.getBlockEntity(position);
        if(blockEntity is RiceCookerEntity) {
            blockEntity.setFluidLevel(variant, fluidLevel);

            val screenHandler = client.player!!.currentScreenHandler;
            if(screenHandler is RiceCookerScreenHandler &&
                    screenHandler.get.getPos().equals(position)) {
                blockEntity.setFluidLevel(variant, fluidLevel);
                screenHandler.setFluid(new FluidStack(variant, fluidLevel));
            }
        }

    }

}*/