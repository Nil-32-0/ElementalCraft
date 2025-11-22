package metafact.elementalcraft.block.entity;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import metafact.elementalcraft.container.ContainerBlockEntityWrapper;
import metafact.elementalcraft.container.IContainerBlockEntity;
import metafact.elementalcraft.recipe.IContainerBlockEntityRecipe;

public interface ICraftingBlockEntity extends IContainerBlockEntity {

	boolean isRecipeAvailable();

	boolean isRunning();

	int getProgress();

	void process();

	default <C extends ICraftingBlockEntity, U extends IContainerBlockEntityRecipe<C>> U lookupRecipe(Level level, RecipeType<U> recipeType) {
		if (recipeType == null) {
			return null;
		}

		return level.getRecipeManager().getRecipeFor(recipeType, getContainerWrapper(), level).orElse(null);
	}

	@SuppressWarnings("unchecked")
	default <C extends ICraftingBlockEntity> ContainerBlockEntityWrapper<C> getContainerWrapper() {
		return ContainerBlockEntityWrapper.from((C) this);
	}
}
