package metafact.elementalcraft.interaction.jei.category.instrument.io.mill;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import metafact.elementalcraft.block.ECBlocks;
import metafact.elementalcraft.block.instrument.io.mill.grindstone.AbstractMillGrindstoneBlockEntity;
import metafact.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import metafact.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

import javax.annotation.Nonnull;

public class GrindingRecipeCategory extends AbstractMillRecipeCategory<AbstractMillGrindstoneBlockEntity, IGrindingRecipe> {

	public GrindingRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.grinding", ECBlocks.WATER_MILL_GRINDSTONE.get(), ECBlocks.AIR_MILL_GRINDSTONE.get());
	}

	@Nonnull
	@Override
	public RecipeType<IGrindingRecipe> getRecipeType() {
		return ECJEIRecipeTypes.GRINDING;
	}
}
