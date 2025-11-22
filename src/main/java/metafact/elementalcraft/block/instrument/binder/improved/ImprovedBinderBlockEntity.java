package metafact.elementalcraft.block.instrument.binder.improved;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.block.instrument.binder.BinderBlockEntity;
import metafact.elementalcraft.block.instrument.binder.IBinder;
import metafact.elementalcraft.block.instrument.infuser.IInfuser;
import metafact.elementalcraft.config.ECConfig;
import metafact.elementalcraft.recipe.ECRecipeTypes;
import metafact.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import metafact.elementalcraft.recipe.instrument.binding.BinderInfusionRecipeWrapper;
import metafact.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

public class ImprovedBinderBlockEntity extends BinderBlockEntity implements IInfuser {

	private static final Config<IBinder, AbstractBindingRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.BINDER_IMPROVED,
			ECRecipeTypes.BINDING,
			ECConfig.SERVER.improvedBinderTransferSpeed,
			ECConfig.SERVER.improvedBinderMaxRunes,
			0,
			true,
            false
	);

	public ImprovedBinderBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, pos, state);
	}

	@Override
	protected AbstractBindingRecipe lookupRecipe() {
		if (getContainerElementType() == ElementType.NONE) {
			return null;
		}

		var bindingRecipe = super.lookupRecipe();

		if (bindingRecipe == null) {
			IInfusionRecipe infusionRecipe = this.lookupInfusionRecipe(level);

			return infusionRecipe != null ? new BinderInfusionRecipeWrapper(infusionRecipe) : null;
		}
		return bindingRecipe;
	}
}
