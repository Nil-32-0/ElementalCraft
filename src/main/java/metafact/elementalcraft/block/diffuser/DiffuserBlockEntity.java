package metafact.elementalcraft.block.diffuser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import metafact.elementalcraft.api.ElementalCraftCapabilities;
import metafact.elementalcraft.api.element.storage.ElementStorageHelper;
import metafact.elementalcraft.api.element.storage.single.ISingleElementStorage;
import metafact.elementalcraft.api.name.ECNames;
import metafact.elementalcraft.api.range.Range;
import metafact.elementalcraft.api.rune.handler.IRuneHandler;
import metafact.elementalcraft.api.rune.handler.RuneHandler;
import metafact.elementalcraft.block.container.IContainerTopBlockEntity;
import metafact.elementalcraft.block.entity.AbstractECBlockEntity;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.config.ECConfig;
import metafact.elementalcraft.range.RangeRenderTimer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class DiffuserBlockEntity extends AbstractECBlockEntity implements IContainerTopBlockEntity {

    private static Range RANGE = Range.builder().box(10).build();

	private boolean hasDiffused;
	private final RuneHandler runeHandler;

	private ISingleElementStorage containerCache;
    private final RangeRenderTimer rangeRenderTimer;

	public DiffuserBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.DIFFUSER, pos, state);
		runeHandler = new RuneHandler(ECConfig.SERVER.diffuserMaxRunes.get(), this::setChanged);
        rangeRenderTimer = new RangeRenderTimer();
	}


	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		hasDiffused = compound.getBoolean(ECNames.HAS_DIFFUSED);
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, 8));
		}
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putBoolean(ECNames.HAS_DIFFUSED, hasDiffused);
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
	}

	@SuppressWarnings("unused")
	public static void serverTick(Level level, BlockPos pos, BlockState state, DiffuserBlockEntity diffuser) {
		ISingleElementStorage container = diffuser.getContainer();
		AtomicInteger amount = new AtomicInteger(ECConfig.SERVER.diffuserDiffusionAmount.get());
		
		diffuser.hasDiffused = false;
		if (container != null && !container.isEmpty()) {
			diffuser.getLevel().getEntities(null, new AABB(diffuser.getBlockPos()).inflate(ECConfig.SERVER.diffuserRange.get())).stream()
					.map(ElementStorageHelper::get)
					.map(LazyOptional::resolve)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.forEach(storage -> {
						if (!container.isEmpty() && amount.get() > 0 && container.transferTo(storage, container.getElementType(), diffuser.runeHandler.getTransferSpeed(amount.get()), Math.min(1, diffuser.runeHandler.getElementPreservation())) > 0) {
							diffuser.hasDiffused = true;
						}
					});
		}
	}

    public static void clientTick(Level level, BlockPos pos, BlockState state, DiffuserBlockEntity diffuser) {
        diffuser.rangeRenderTimer.tick();
    }

    public boolean showsRange() {
        return rangeRenderTimer.showsRange();
    }

    public void startShowingRange() {
        rangeRenderTimer.startShowingRange();
    }

    public AABB getRange() {
        return this.runeHandler.getRange(RANGE).move(this.worldPosition);
    }

	public boolean hasDiffused() {
		return hasDiffused;
	}
	
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

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
		if (!this.remove && cap == ElementalCraftCapabilities.RUNE_HANDLE) {
			return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
		}
		return super.getCapability(cap, side);
	}
}
