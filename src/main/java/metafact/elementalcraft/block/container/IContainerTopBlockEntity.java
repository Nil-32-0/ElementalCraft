package metafact.elementalcraft.block.container;

import net.minecraft.world.level.block.entity.BlockEntity;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.element.storage.single.ISingleElementStorage;
import metafact.elementalcraft.block.entity.BlockEntityHelper;

public interface IContainerTopBlockEntity {

	private BlockEntity self() {
		return (BlockEntity) this;
	}

	default ISingleElementStorage getContainer() {
		var self = self();

		//noinspection ConstantConditions
		return self.hasLevel() ? BlockEntityHelper.getElementContainer(self.getBlockState(), self.getLevel(), self.getBlockPos().below()).orElse(null) : null;
	}

	default ElementType getContainerElementType() {
		ISingleElementStorage container = getContainer();

		return container != null ? container.getElementType() : ElementType.NONE;
	}

}
