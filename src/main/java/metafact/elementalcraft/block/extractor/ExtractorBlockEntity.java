package metafact.elementalcraft.block.extractor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import metafact.elementalcraft.api.ElementalCraftCapabilities;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.element.storage.single.ISingleElementStorage;
import metafact.elementalcraft.api.name.ECNames;
import metafact.elementalcraft.api.rune.handler.IRuneHandler;
import metafact.elementalcraft.api.rune.handler.RuneHandler;
import metafact.elementalcraft.block.ECBlocks;
import metafact.elementalcraft.block.container.IContainerTopBlockEntity;
import metafact.elementalcraft.block.entity.AbstractECBlockEntity;
import metafact.elementalcraft.block.entity.BlockEntityHelper;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.block.source.SourceBlockEntity;
import metafact.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class ExtractorBlockEntity extends AbstractECBlockEntity implements IContainerTopBlockEntity {
	private int extractionAmount;
	private final RuneHandler runeHandler;

	private ISingleElementStorage containerCache;

	public ExtractorBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.EXTRACTOR, pos, state);
		if (state.is(ECBlocks.IMPROVED_EXTRACTOR.get())) {
            this.extractionAmount = ECConfig.SERVER.improvedExtractorExtractionAmount.get();
            runeHandler = new RuneHandler(ECConfig.SERVER.improvedExtractorMaxRunes.get(), this::setChanged);
        } else if (state.is(ECBlocks.RUDIMENTARY_EXTRACTOR.get())) {
            this.extractionAmount = ECConfig.SERVER.rudimentaryExtractorExtractionAmount.get();
            runeHandler = new RuneHandler(0, this::setChanged);
		} else {
			this.extractionAmount = ECConfig.SERVER.extractorExtractionAmount.get();
			runeHandler = new RuneHandler(ECConfig.SERVER.extractorMaxRunes.get(), this::setChanged);
		}
	}


	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		this.extractionAmount = compound.getInt(ECNames.EXTRACTION_AMOUNT);
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, 8));
		}
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putInt(ECNames.EXTRACTION_AMOUNT, this.extractionAmount);
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
	}

	protected Optional<BlockState> getSourceState() {
		return this.level != null ? Optional.of(this.level.getBlockState(worldPosition.above())) : Optional.empty();
	}

	public ElementType getSourceElementType() {
		return getSourceState().filter(s -> s.getBlock() == ECBlocks.SOURCE.get()).map(ElementType::getElementType).orElse(ElementType.NONE);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, ExtractorBlockEntity extractor) {
		if (extractor.canExtract()) {
			BlockEntityHelper.getBlockEntityAs(level, pos.above(), SourceBlockEntity.class)
                    .map(SourceBlockEntity::getElementStorage)
					.ifPresent(sourceStorage ->  extractor.runeHandler.handleElementTransfer(sourceStorage, extractor.getContainer(), extractor.extractionAmount));
		}
	}

	public boolean canExtract() {
		if (this.level == null) {
			return false;
		}

		return BlockEntityHelper.getBlockEntityAs(this.level, this.worldPosition.above(), SourceBlockEntity.class).map(source -> {
			if (source.isExhausted()) {
				return false;
			}

			ElementType sourceElementType = source.getElementType();
			ISingleElementStorage container = getContainer();

			return hasLevel() && sourceElementType != ElementType.NONE && container != null && (container.getElementAmount() < container.getElementCapacity() || container.getElementType() != sourceElementType);
		}).orElse(false);
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
