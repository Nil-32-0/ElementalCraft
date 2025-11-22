package metafact.elementalcraft.block.instrument.infuser;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import metafact.elementalcraft.config.ECConfig;
import metafact.elementalcraft.container.SingleItemContainer;
import metafact.elementalcraft.recipe.ECRecipeTypes;
import metafact.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

import javax.annotation.Nonnull;

public class InfuserBlockEntity extends AbstractInstrumentBlockEntity<IInfuser, IInfusionRecipe> implements IInfuser {

	private static final Config<IInfuser, IInfusionRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.INFUSER,
			ECRecipeTypes.INFUSION,
			ECConfig.SERVER.infuserTransferSpeed,
			ECConfig.SERVER.infuserMaxRunes,
			0,
			true,
            true
	);

	private final SingleItemContainer inventory;

	public InfuserBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, pos, state);
		inventory = new SingleItemContainer(this::setChanged);
	}

	@Override
	protected IInfusionRecipe lookupRecipe() {
		return this.lookupInfusionRecipe(level);
	}

	@Override
	protected boolean shouldRetrieverExtractOutput() {
		return this.recipe == null;
	}

	@Nonnull
	@Override
	public Container getInventory() {
		return inventory;
	}
}
