package metafact.elementalcraft.block.shrine.upgrade.acceleration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import metafact.elementalcraft.block.entity.AbstractECBlockEntity;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;

public class AccelerationShrineUpgradeBlockEntity extends AbstractECBlockEntity {

	public AccelerationShrineUpgradeBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.ACCELERATION_SHRINE_UPGRADE, pos, state);
	}

}
