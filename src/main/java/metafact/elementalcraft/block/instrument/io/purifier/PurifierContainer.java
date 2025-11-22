package metafact.elementalcraft.block.instrument.io.purifier;

import net.minecraft.world.item.ItemStack;
import metafact.elementalcraft.ElementalCraft;
import metafact.elementalcraft.container.IOContainer;

import javax.annotation.Nonnull;

public class PurifierContainer extends IOContainer {

	public PurifierContainer(Runnable syncCallback) {
		super(syncCallback);
	}

	@Override
	public boolean canPlaceItem(int index, @Nonnull ItemStack stack) {
		return super.canPlaceItem(index, stack) && ElementalCraft.PURE_ORE_MANAGER.isValidOre(stack);
	}

}
