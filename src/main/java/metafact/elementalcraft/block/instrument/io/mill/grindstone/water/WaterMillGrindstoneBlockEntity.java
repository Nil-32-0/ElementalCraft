package metafact.elementalcraft.block.instrument.io.mill.grindstone.water;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.block.instrument.io.mill.grindstone.AbstractMillGrindstoneBlockEntity;
import metafact.elementalcraft.config.ECConfig;
import metafact.elementalcraft.recipe.ECRecipeTypes;
import metafact.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public class WaterMillGrindstoneBlockEntity extends AbstractMillGrindstoneBlockEntity {

	private static final Config<AbstractMillGrindstoneBlockEntity, IGrindingRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.WATER_MILL_GRINDSTONE,
			ECRecipeTypes.GRINDING,
			ECConfig.SERVER.waterMillsTransferSpeed,
			ECConfig.SERVER.waterMillsMaxRunes,
			1,
			false,
            false
	);

	public WaterMillGrindstoneBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, ElementType.WATER, pos, state);
	}
}
