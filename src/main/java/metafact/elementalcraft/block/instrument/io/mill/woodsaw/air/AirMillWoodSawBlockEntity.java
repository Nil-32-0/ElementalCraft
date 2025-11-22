package metafact.elementalcraft.block.instrument.io.mill.woodsaw.air;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.block.instrument.io.mill.woodsaw.AbstractMillWoodSawBlockEntity;
import metafact.elementalcraft.config.ECConfig;
import metafact.elementalcraft.recipe.ECRecipeTypes;
import metafact.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;

public class AirMillWoodSawBlockEntity extends AbstractMillWoodSawBlockEntity {

	private static final Config<AbstractMillWoodSawBlockEntity, SawingRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.AIR_MILL_WOOD_SAW,
			ECRecipeTypes.SAWING,
			ECConfig.SERVER.airMillsTransferSpeed,
			ECConfig.SERVER.airMillsMaxRunes,
			1,
			false,
            false
	);

	public AirMillWoodSawBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, ElementType.AIR, pos, state);
	}
}
