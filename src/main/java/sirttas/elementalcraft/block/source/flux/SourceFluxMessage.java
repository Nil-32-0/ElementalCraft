package sirttas.elementalcraft.block.source.flux;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;
import sirttas.elementalcraft.network.message.MessageHelper;

import java.util.function.Supplier;

public class SourceFluxMessage {
    private final SourceFlux flux;

    public SourceFluxMessage(SourceFlux flux) {
        this.flux = flux;
    }


    public static SourceFluxMessage decode(FriendlyByteBuf buf) {
        var flux = new SourceFlux(SourceFluxModHandler.getConfig(), buf.readInt(), buf.readInt());

        flux.setFlux(buf.readFloat());

        return new SourceFluxMessage(flux);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(flux.getX());
        buf.writeInt(flux.getY());
        buf.writeFloat(flux.getCurrentFlux());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                updateFlux();
            }
            if (ctx.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
                queryFlux(ctx.get().getSender());
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private void updateFlux() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        LevelChunk chunk = level.getChunk(flux.getX(), flux.getY());

        var cap = chunk.getCapability(ElementalCraftCapabilities.SOURCE_FLUX);

        cap.ifPresent(handler -> handler.setFlux(flux.getCurrentFlux()));
    }

    private void queryFlux(ServerPlayer player) {
        ServerLevel level = player.serverLevel();

        LevelChunk chunk = level.getChunk(flux.getX(), flux.getY());

        var cap = chunk.getCapability(ElementalCraftCapabilities.SOURCE_FLUX).resolve().orElseThrow();

        MessageHelper.sendToPlayer(player, new SourceFluxMessage((SourceFlux) cap));
    }
}
