package metafact.elementalcraft.block.pureinfuser.pedestal;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.element.storage.single.StaticElementStorage;
import metafact.elementalcraft.config.ECConfig;

public class PedestalElementStorage extends StaticElementStorage {

	public PedestalElementStorage(ElementType elementType, Runnable syncCallback) {
		super(elementType, ECConfig.SERVER.pedestalCapacity.get(), syncCallback);
	}

	@Override
	public boolean canPipeExtract(ElementType elementType, Direction side) {
		return false;
	}

	@Override
	public boolean doesRenderGauge(Player player) {
		return true;
	}

}
