package metafact.elementalcraft.block.evaporator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.element.storage.single.ISingleElementStorage;
import metafact.elementalcraft.api.element.storage.single.SingleElementStorage;
import metafact.elementalcraft.api.rune.handler.RuneHandler;
import metafact.elementalcraft.block.container.IContainerTopBlockEntity;
import metafact.elementalcraft.block.entity.AbstractIERBlockEntity;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.config.ECConfig;
import metafact.elementalcraft.container.SingleStackContainer;
import metafact.elementalcraft.item.elemental.ShardItem;

import javax.annotation.Nonnull;

public class EvaporatorBlockEntity extends AbstractIERBlockEntity implements IContainerTopBlockEntity {

	private final SingleStackContainer inventory;
	private final SingleElementStorage elementStorage;
	private final RuneHandler runeHandler;

	private ISingleElementStorage containerCache;

	public EvaporatorBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.EVAPORATOR, pos, state);
		inventory = new SingleStackContainer(this::setChanged);
		this.elementStorage = new SingleElementStorage(ECConfig.SERVER.shardElementAmount.get() * 20, this::setChanged);
		runeHandler = new RuneHandler(ECConfig.SERVER.evaporatorMaxRunes.get(), this::setChanged);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, EvaporatorBlockEntity evaporator) {
		ItemStack stack = evaporator.inventory.getItem(0);
		Item item = stack.getItem();
		ElementType type = EvaporatorBlock.getShardElementType(stack);
		float extractionAmount = evaporator.runeHandler.getTransferSpeed(ECConfig.SERVER.evaporatorExtractionAmount.get());

		if (type != ElementType.NONE && evaporator.elementStorage.getElementAmount() <= extractionAmount) {
			evaporator.elementStorage.insertElement(evaporator.getShardElementAmount((ShardItem) item), type, false);
			stack.shrink(1);
			if (stack.isEmpty()) {
				evaporator.inventory.setItem(0, ItemStack.EMPTY);
			}
		}
		if (evaporator.canExtract()) {
			evaporator.elementStorage.transferTo(evaporator.getContainer(), extractionAmount, evaporator.runeHandler.getElementPreservation());
		}
	}

	public boolean canExtract() {
		ISingleElementStorage container = getContainer();

		return !elementStorage.isEmpty() && hasLevel() && container != null && (container.getElementAmount() < container.getElementCapacity() || container.getElementType() != elementStorage.getElementType());
	}

	private int getShardElementAmount(ShardItem item) {
		return Math.round(ECConfig.SERVER.shardElementAmount.get() * item.getElementAmount() * runeHandler.getElementPreservation());
	}

	@Nonnull
    @Override
	public Container getInventory() {
		return inventory;
	}

	@Override
	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

	@Override
	public RuneHandler getRuneHandler() {
		return runeHandler;
	}

	@Override
	public ISingleElementStorage getContainer() {
		if (containerCache == null) {
			containerCache = IContainerTopBlockEntity.super.getContainer();
		}
		return containerCache;
	}
}
