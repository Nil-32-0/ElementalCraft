package metafact.elementalcraft.recipe.instrument;

import net.minecraft.resources.ResourceLocation;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.instrument.IInstrument;

import javax.annotation.Nonnull;

public abstract class AbstractInstrumentRecipe<T extends IInstrument> implements ISingleElementInstrumentRecipe<T> {

	protected final ElementType elementType;
	protected final ResourceLocation id;

	protected AbstractInstrumentRecipe(ResourceLocation id, ElementType type) {
		this.elementType = type;
		this.id = id;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	@Nonnull
    @Override
	public ResourceLocation getId() {
		return id;
	}
}
