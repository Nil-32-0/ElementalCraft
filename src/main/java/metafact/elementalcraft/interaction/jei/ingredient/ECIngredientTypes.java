package metafact.elementalcraft.interaction.jei.ingredient;

import mezz.jei.api.ingredients.IIngredientType;
import metafact.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import metafact.elementalcraft.interaction.jei.ingredient.source.IngredientSource;

public class ECIngredientTypes {
	
	public static final IIngredientType<IngredientElementType> ELEMENT = () -> IngredientElementType.class;
	public static final IIngredientType<IngredientSource> SOURCE = () -> IngredientSource.class;

	private ECIngredientTypes() {}
	
}
