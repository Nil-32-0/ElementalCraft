package metafact.elementalcraft.recipe.instrument;

import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.element.IElementTypeProvider;
import metafact.elementalcraft.block.instrument.IInstrument;

import java.util.List;

public interface ISingleElementInstrumentRecipe<T extends IInstrument> extends IInstrumentRecipe<T>, IElementTypeProvider {

    @Override
    default List<ElementType> getValidElementTypes() {
        return List.of(getElementType());
    }
}
