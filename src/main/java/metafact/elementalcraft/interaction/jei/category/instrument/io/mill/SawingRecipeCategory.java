package metafact.elementalcraft.interaction.jei.category.instrument.io.mill;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import metafact.elementalcraft.block.ECBlocks;
import metafact.elementalcraft.block.instrument.io.mill.woodsaw.AbstractMillWoodSawBlockEntity;
import metafact.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import metafact.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;

import javax.annotation.Nonnull;

public class SawingRecipeCategory extends AbstractMillRecipeCategory<AbstractMillWoodSawBlockEntity, SawingRecipe> {

	public SawingRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.sawing", ECBlocks.WATER_MILL_WOOD_SAW.get(), ECBlocks.AIR_MILL_WOOD_SAW.get());
	}

	@Nonnull
	@Override
	public RecipeType<SawingRecipe> getRecipeType() {
		return ECJEIRecipeTypes.SAWING;
	}
}
