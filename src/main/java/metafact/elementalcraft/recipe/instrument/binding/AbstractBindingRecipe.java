package metafact.elementalcraft.recipe.instrument.binding;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.instrument.binder.IBinder;
import metafact.elementalcraft.recipe.ECRecipeTypes;
import metafact.elementalcraft.recipe.instrument.AbstractInstrumentRecipe;

import javax.annotation.Nonnull;

public abstract class AbstractBindingRecipe extends AbstractInstrumentRecipe<IBinder> {

	public static final String NAME = "binding";
	
	protected AbstractBindingRecipe(ResourceLocation id, ElementType type) {
		super(id, type);
	}

	@Nonnull
	@Override
	public RecipeType<?> getType() {
		return ECRecipeTypes.BINDING.get();
	}
}
