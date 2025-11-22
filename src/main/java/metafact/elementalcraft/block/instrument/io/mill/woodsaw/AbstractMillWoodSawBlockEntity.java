package metafact.elementalcraft.block.instrument.io.mill.woodsaw;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.instrument.io.mill.AbstractMillBlockEntity;
import metafact.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;

public class AbstractMillWoodSawBlockEntity extends AbstractMillBlockEntity<AbstractMillWoodSawBlockEntity, SawingRecipe> {

	public AbstractMillWoodSawBlockEntity(Config<AbstractMillWoodSawBlockEntity, SawingRecipe> config, ElementType elementType, BlockPos pos, BlockState state) {
		super(config, elementType, pos, state);
	}
}
