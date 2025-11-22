package metafact.elementalcraft.block.container.reservoir;

import net.minecraft.world.entity.player.Player;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.element.storage.single.StaticElementStorage;

public class ReservoirElementStorage extends StaticElementStorage {

	public ReservoirElementStorage(ElementType elementType, int elementCapacity, Runnable syncCallback) {
		super(elementType, elementCapacity, syncCallback);
	}
	
	@Override
	public boolean doesRenderGauge(Player player) {
		return true;
	}
}
