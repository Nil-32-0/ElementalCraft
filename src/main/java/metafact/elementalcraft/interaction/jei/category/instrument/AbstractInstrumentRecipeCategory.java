package metafact.elementalcraft.interaction.jei.category.instrument;

import mezz.jei.api.gui.drawable.IDrawable;
import metafact.elementalcraft.block.instrument.IInstrument;
import metafact.elementalcraft.interaction.jei.category.AbstractBlockEntityRecipeCategory;
import metafact.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import metafact.elementalcraft.recipe.instrument.IInstrumentRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractInstrumentRecipeCategory<K extends IInstrument, T extends IInstrumentRecipe<K>> extends AbstractBlockEntityRecipeCategory<K, T> {

	protected AbstractInstrumentRecipeCategory(String translationKey, IDrawable icon, IDrawable background) {
		super(translationKey, icon, background);
	}

	@Nonnull
	protected List<IngredientElementType> getElementTypeIngredients(@Nonnull T recipe) {
		return recipe.getValidElementTypes().stream()
				.map(t -> new IngredientElementType(t, getGaugeValue(recipe.getElementAmount())))
				.toList();
	}

}
