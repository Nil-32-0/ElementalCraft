package metafact.elementalcraft.block.instrument.io.mill.woodsaw.water;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.block.instrument.io.mill.woodsaw.AbstractMillWoodSawBlockEntity;
import metafact.elementalcraft.config.ECConfig;
import metafact.elementalcraft.recipe.ECRecipeTypes;
import metafact.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;

public class WaterMillWoodSawBlockEntity extends AbstractMillWoodSawBlockEntity {

	private static final Config<AbstractMillWoodSawBlockEntity, SawingRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.WATER_MILL_WOOD_SAW,
			ECRecipeTypes.SAWING,
			ECConfig.SERVER.waterMillsTransferSpeed,
			ECConfig.SERVER.waterMillsMaxRunes,
			1,
			false,
            false
	);

	public WaterMillWoodSawBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, ElementType.WATER, pos, state);
	}
}
