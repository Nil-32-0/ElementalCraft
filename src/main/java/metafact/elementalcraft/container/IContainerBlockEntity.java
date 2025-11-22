package metafact.elementalcraft.container;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public interface IContainerBlockEntity extends Clearable {

	@Nonnull
	Container getInventory();

	@Override
	default void clearContent() {
		getInventory().clearContent();
	}

    @Nonnull
    default LazyOptional<IItemHandler> getItemHandler(@Nullable Direction direction) {
        var inv = this.getInventory();

        if (inv instanceof WorldlyContainer worldlyContainer) {
            return LazyOptional.of(() -> new SidedInvWrapper(worldlyContainer, direction));
        }
        return LazyOptional.of(() -> new InvWrapper(this.getInventory()));
    }
}
