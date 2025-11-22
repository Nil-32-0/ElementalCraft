package metafact.elementalcraft.recipe.instrument;

import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.instrument.IInstrument;
import metafact.elementalcraft.recipe.IContainerBlockEntityRecipe;

import java.util.List;

public interface IInstrumentRecipe<T extends IInstrument> extends IContainerBlockEntityRecipe<T> {

    List<ElementType> getValidElementTypes();

    int getElementAmount();

    default int getElementAmount(T input) {
        return getElementAmount();
    }
}
