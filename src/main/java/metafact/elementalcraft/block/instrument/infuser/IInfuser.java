package metafact.elementalcraft.block.instrument.infuser;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.instrument.IInstrument;
import metafact.elementalcraft.recipe.ECRecipeTypes;
import metafact.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

public interface IInfuser extends IInstrument {

	default IInfusionRecipe lookupInfusionRecipe(Level level) {
		if (getContainerElementType() == ElementType.NONE) {
			return null;
		}
		return lookupRecipe(level, ECRecipeTypes.INFUSION.get());
	}

	default ItemStack getItem() {
		return this.getInventory().getItem(0);
	}
}
