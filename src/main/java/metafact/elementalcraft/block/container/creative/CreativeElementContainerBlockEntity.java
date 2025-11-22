package metafact.elementalcraft.block.container.creative;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import metafact.elementalcraft.block.container.AbstractElementContainerBlockEntity;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;

public class CreativeElementContainerBlockEntity extends AbstractElementContainerBlockEntity {

	public CreativeElementContainerBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.CREATIVE_CONTAINER, pos, state, self -> new CreativeElementStorage(self::setChanged));
	}

	@Override
	public boolean isSmall() {
		return false;
	}
}
