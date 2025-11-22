package metafact.elementalcraft.event;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.mutable.MutableFloat;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.ElementalCraftCapabilities;
import metafact.elementalcraft.block.source.flux.SourceFlux;
import metafact.elementalcraft.block.source.flux.SourceFluxMessage;
import metafact.elementalcraft.network.message.MessageHandler;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClientEvents {
    private int lastUpdate;

    @SubscribeEvent
    public void onDebugRender(CustomizeGuiOverlayEvent.DebugText event) {
        var mc = Minecraft.getInstance();
        if (mc.options.renderDebug && (mc.player.isCreative() || mc.player.isSpectator())) {
            var prefix = ChatFormatting.GREEN + "[" + ElementalCraftApi.MODID + "]" + ChatFormatting.RESET + " ";
            List<String> left = event.getLeft();
            left.add("");
            var chunk = mc.level.getChunkAt(mc.player.blockPosition());
            var handler = chunk.getCapability(ElementalCraftCapabilities.SOURCE_FLUX).resolve().orElseThrow();
            var ratio = new MutableFloat(handler.getRatio());

            left.add(prefix + "Source Flux: " + ratio.getValue());

            lastUpdate++;

            if (lastUpdate > 20) {
                lastUpdate = 0;
                MessageHandler.CHANNEL.sendToServer(new SourceFluxMessage((SourceFlux) handler));
            }
        }


    }
}