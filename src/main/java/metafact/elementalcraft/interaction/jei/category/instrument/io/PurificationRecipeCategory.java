package metafact.elementalcraft.interaction.jei.category.instrument.io;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import metafact.elementalcraft.block.ECBlocks;
import metafact.elementalcraft.block.instrument.io.purifier.PurifierBlockEntity;
import metafact.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import metafact.elementalcraft.recipe.instrument.io.IPurifierRecipe;

import javax.annotation.Nonnull;

public class PurificationRecipeCategory extends AbstractIOInstrumentRecipeCategory<PurifierBlockEntity, IPurifierRecipe> {

	public static final String NAME = "purification";

	public PurificationRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.purification", ECBlocks.PURIFIER.get());
	}

	@Nonnull
	@Override
	public RecipeType<IPurifierRecipe> getRecipeType() {
		return ECJEIRecipeTypes.PURIFICATION;
	}
}
