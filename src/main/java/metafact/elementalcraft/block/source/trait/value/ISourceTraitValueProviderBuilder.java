package metafact.elementalcraft.block.source.trait.value;

import metafact.dpanvil_m.api.predicate.block.IBlockPosPredicate;
import metafact.elementalcraft.api.source.trait.value.ISourceTraitValueProvider;

@FunctionalInterface
public interface ISourceTraitValueProviderBuilder {

    default ISourceTraitValueProviderBuilder chance(float chance) {
        return chance(chance, chance);
    }

    default ISourceTraitValueProviderBuilder chance(float chance, float chanceOnBred) {
        return () -> new ChanceSourceTraitValueProvider(this.build(), chance, chanceOnBred);
    }

    default ISourceTraitValueProviderBuilder predicate(IBlockPosPredicate predicate) {
        return () -> new PredicateSourceTraitValueProvider(this.build(), predicate);
    }

    ISourceTraitValueProvider build();

}
