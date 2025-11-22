package metafact.elementalcraft.block.source.trait.holder;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.util.INBTSerializable;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.source.trait.SourceTrait;
import metafact.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import metafact.elementalcraft.api.source.trait.value.ISourceTraitValue;
import metafact.elementalcraft.block.source.trait.SourceTraitHelper;
import metafact.elementalcraft.block.source.trait.SourceTraits;

import javax.annotation.Nonnull;
import java.util.Map;

public class SourceTraitHolder implements ISourceTraitHolder, INBTSerializable<CompoundTag> {

    private final Map<ResourceKey<SourceTrait>, ISourceTraitValue> traits;

    public SourceTraitHolder() {
        traits = SourceTraits.createTraitMap();
    }

    @Override
    public Map<ResourceKey<SourceTrait>, ISourceTraitValue> getTraits() {
        return traits;
    }

    @Override
    public void setTraits(Map<ResourceKey<SourceTrait>, ISourceTraitValue> traits) {
        this.traits.clear();
        this.traits.putAll(traits);
    }

    @Override
    public boolean isArtificial() {
        return traits.containsKey(SourceTraits.ARTIFICIAL);
    }

    public void initTraits(ServerLevelAccessor level, BlockPos pos, int luck) {
        if (traits.isEmpty()) {
            for (var entry : ElementalCraftApi.SOURCE_TRAIT_MANAGER.getData().entrySet()) {
                var key = SourceTraits.key(entry.getKey());

                if (key.equals(SourceTraits.ARTIFICIAL)) {
                    continue;
                }
                var value = entry.getValue().roll(level, pos, luck);

                if (value != null) {
                    traits.put(key, value);
                }
            }
        }
    }

    public void clear() {
        traits.clear();
    }

    @Override
    public void deserializeNBT(@Nonnull CompoundTag compound) {
         SourceTraitHelper.loadTraits(compound, traits);
    }

    @Override
    public CompoundTag serializeNBT() {
        return SourceTraitHelper.saveTraits(traits);
    }
}
