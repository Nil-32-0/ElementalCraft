package metafact.elementalcraft.block.source.flux;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.core.Direction;
import net.minecraft.nbt.FloatTag;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.TickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.ElementalCraftCapabilities;
import metafact.elementalcraft.api.name.ECNames;

import javax.annotation.Nonnull;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class SourceFluxHandler {

    private SourceFluxHandler() {}

    @SubscribeEvent
    public static void registerCaps(AttachCapabilitiesEvent<LevelChunk> event) {
        LevelChunk chunk = event.getObject();

        event.addCapability(ElementalCraftApi.createRL(ECNames.FLUX), createProvider(chunk));
    }

    private static ICapabilitySerializable<FloatTag> createProvider(LevelChunk chunk) {
        if (ElementalCraftCapabilities.SOURCE_FLUX != null) {
            var handler = new SourceFlux(SourceFluxModHandler.getConfig(), chunk.getPos().x, chunk.getPos().z);

            return new ICapabilitySerializable<>() {
                @Override
                public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                    return ElementalCraftCapabilities.SOURCE_FLUX.orEmpty(cap, LazyOptional.of(() -> handler));
                }

                @Override
                public @Nonnull FloatTag serializeNBT() {
                    return handler.serializeNBT();
                }

                @Override
                public void deserializeNBT(FloatTag nbt) {
                    handler.deserializeNBT(nbt);
                }
            };
        }
        return null;
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        var level = event.level;

        if (level.isClientSide || event.phase != TickEvent.Phase.END || !(level instanceof ServerLevel serverLevel)) {
            return;
        }

        handleSourceFlux(serverLevel);
    }

    private static void handleSourceFlux(ServerLevel serverLevel) {
        var profiler = serverLevel.getProfiler();

        profiler.push("elementalcraft:source_flux_transfer");

        var chunkSource = serverLevel.getChunkSource();
        var map = getSourceFlux(chunkSource);

        handleSourceFluxMap(map);
        profiler.pop();
    }

    @VisibleForTesting
    public static void handleSourceFluxMap(Map<Long, SourceFlux> map) {
        var suppliers = SourceFlux.NeighborSupplier.of(map);

        for (var sourceFlux : map.values()) {
            sourceFlux.tick(suppliers);
        }
    }

    private static Map<Long, SourceFlux> getSourceFlux(ServerChunkCache chunkSource) {
        Map<Long, SourceFlux> map = new Long2ObjectLinkedOpenHashMap<>();

        for (var chunkHolder : chunkSource.chunkMap.getChunks()) {
            var levelChunk = chunkHolder.getTickingChunk();

            if (levelChunk != null) {
                map.put(
                        levelChunk.getPos().toLong(),
                        (SourceFlux) levelChunk.getCapability(ElementalCraftCapabilities.SOURCE_FLUX).resolve().orElseThrow()
                );
            }
        }
        return map;
    }
}