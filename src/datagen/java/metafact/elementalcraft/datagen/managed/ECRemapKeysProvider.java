package metafact.elementalcraft.datagen.managed;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import metafact.dpanvil_m.api.data.remap.AbstractRemapKeysProvider;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.block.source.trait.SourceTraits;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class ECRemapKeysProvider extends AbstractRemapKeysProvider {

    public ECRemapKeysProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    @Override
    protected void collectBuilders(HolderLookup.Provider registries) {
        remap(ElementalCraftApi.RUNE_MANAGER_KEY).add(ElementalCraftApi.createRL("cognac"), ElementalCraftApi.createRL("soaryn"));
        remap(ElementalCraftApi.SOURCE_TRAIT_MANAGER_KEY).add(ElementalCraftApi.createRL("fleeting"), SourceTraits.ARTIFICIAL);
    }

    @Nonnull
    @Override
    public String getName() {
        return "ElementalCraft Remap Keys";
    }

}
