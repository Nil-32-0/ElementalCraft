package metafact.elementalcraft.block.instrument.io.purifier;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import metafact.elementalcraft.ElementalCraft;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.block.instrument.io.AbstractIOInstrumentBlockEntity;
import metafact.elementalcraft.config.ECConfig;
import metafact.elementalcraft.recipe.instrument.io.IPurifierRecipe;

import javax.annotation.Nonnull;

public class PurifierBlockEntity extends AbstractIOInstrumentBlockEntity<PurifierBlockEntity, IPurifierRecipe> {

	private static final Config<PurifierBlockEntity, IPurifierRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.PURIFIER,
			null,
			ECConfig.SERVER.purifierTransferSpeed,
			ECConfig.SERVER.purifierMaxRunes,
			1,
			false,
            false
	);


	private final PurifierContainer inventory;

	public PurifierBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, pos, state);
		inventory = new PurifierContainer(this::setChanged);
	}

	@Nonnull
    @Override
	protected @NotNull IItemHandler createHandler() {
		return new SidedInvWrapper(inventory, null);
	}

	@Override
	protected IPurifierRecipe lookupRecipe() {
		ItemStack input = inventory.getItem(0);

		if (!input.isEmpty()) {
			IPurifierRecipe recipe = ElementalCraft.PURE_ORE_MANAGER.getRecipes(input, level);

			if (recipe != null && recipe.matches(this, level)) {
				return recipe;
			}
		}
		return null;
	}

	@Nonnull
    @Override
	public @NotNull Container getInventory() {
		return inventory;
	}
}
