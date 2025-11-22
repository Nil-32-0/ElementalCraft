package metafact.elementalcraft.api.source.trait.value;

import com.mojang.serialization.Codec;
import metafact.dpanvil_m.api.codec.ICodecProvider;

public record SourceTraitValueProviderType<T extends ISourceTraitValueProvider>(Codec<T> codec) implements ICodecProvider<T> {

}
