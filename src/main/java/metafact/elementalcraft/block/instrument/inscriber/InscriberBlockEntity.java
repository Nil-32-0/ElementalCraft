package metafact.elementalcraft.block.instrument.inscriber;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import metafact.elementalcraft.block.instrument.InstrumentContainer;
import metafact.elementalcraft.config.ECConfig;
import metafact.elementalcraft.recipe.ECRecipeTypes;
import metafact.elementalcraft.recipe.instrument.InscriptionRecipe;

import javax.annotation.Nonnull;

public class InscriberBlockEntity extends AbstractInstrumentBlockEntity<InscriberBlockEntity, InscriptionRecipe> {

	private static final Config<InscriberBlockEntity, InscriptionRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.INSCRIBER,
			ECRecipeTypes.INSCRIPTION,
			ECConfig.SERVER.inscriberTransferSpeed,
			ECConfig.SERVER.inscriberMaxRunes,
			0,
			true,
            true
	);

	private final InstrumentContainer inventory;

	public InscriberBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, pos, state);
		inventory = new InscriberContainer(this::setChanged);
		particleOffset = new Vec3(0, 0.2, 0);
	}

	public int getItemCount() {
		return inventory.getItemCount();
	}
	
	@Nonnull
    @Override
	public Container getInventory() {
		return inventory;
	}

	@Override
	protected boolean progressOnTick() {
		return false;
	}

	public boolean useChisel() {
		return makeProgress();
	}
}
