package metafact.elementalcraft.block.source.breeder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import metafact.elementalcraft.api.ElementalCraftCapabilities;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.element.IElementTypeProvider;
import metafact.elementalcraft.api.name.ECNames;
import metafact.elementalcraft.api.rune.Rune;
import metafact.elementalcraft.api.rune.handler.IRuneHandler;
import metafact.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import metafact.elementalcraft.block.entity.AbstractECCraftingBlockEntity;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.block.retriever.RetrieverBlock;
import metafact.elementalcraft.block.source.breeder.pedestal.SourceBreederPedestalBlockEntity;
import metafact.elementalcraft.block.source.trait.SourceTraitHelper;
import metafact.elementalcraft.config.ECConfig;
import metafact.elementalcraft.item.source.receptacle.ReceptacleHelper;
import metafact.elementalcraft.particle.ParticleHelper;
import metafact.elementalcraft.recipe.ECRecipeTypes;
import metafact.elementalcraft.recipe.SourceBreedingRecipe;
import metafact.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SourceBreederBlockEntity extends AbstractECCraftingBlockEntity<SourceBreederBlockEntity, SourceBreedingRecipe> implements IElementTypeProvider {

    private static final Config<SourceBreederBlockEntity, SourceBreedingRecipe> CONFIG = new Config<>(
            ECBlockEntityTypes.SOURCE_BREEDER,
            ECRecipeTypes.BREEDING,
            ECConfig.SERVER.sourceBreederTransferSpeed,
            ECConfig.SERVER.sourceBreederMaxRunes,
            0,
            false,
            false
    );

    private final SourceBreederItemContainer container;

    private final Map<Direction, PedestalWrapper> pedestalWrappers;

    public SourceBreederBlockEntity(BlockPos pos, BlockState state) {
        super(CONFIG, pos, state);
        container = new SourceBreederItemContainer(this::setChanged);
        pedestalWrappers = new EnumMap<>(Direction.class);
        pedestalWrappers.put(Direction.NORTH, new PedestalWrapper());
        pedestalWrappers.put(Direction.SOUTH, new PedestalWrapper());
        pedestalWrappers.put(Direction.WEST, new PedestalWrapper());
        pedestalWrappers.put(Direction.EAST, new PedestalWrapper());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SourceBreederBlockEntity breeder) {
        breeder.refreshPedestals();
        breeder.makeProgress();
    }

    private void refreshPedestals() {
        pedestalWrappers.forEach((d, w) -> {
            if (w.isRemoved()) {
                w.progress = 0;
                w.lookupPedestal(d);
            }
        });
    }

    @Override
    public void assemble() {
       container.setItem(0, breed(
               getElementType(),
               getActiveWrappers().get(0).getTraitHolder(),
               getActiveWrappers().get(1).getTraitHolder()
       ));
       RetrieverBlock.sendOutputToRetriever(level, worldPosition, getInventory(), 0);
       RetrieverBlock.sendOutputToRetriever(level, worldPosition.above(), getInventory(), 0);
    }

    @Override
    public boolean isRunning() {
        return getActiveWrappers().stream().anyMatch(w -> !w.isRemoved() && w.progress > 0);
    }

    @Override
    public int getProgress() {
        return 0;
    }

    @Nonnull
    @Override
    public Container getInventory() {
        return container;
    }

    @Override
    public ElementType getElementType() {
        if (recipe != null) {
            return recipe.getResultElementType();
        }
        return ElementType.NONE;
    }

    public List<PedestalWrapper> getActiveWrappers() {
        return pedestalWrappers.values().stream().filter(w -> !w.isRemoved()).toList();
    }

    private void makeProgress() {
        if (recipe != null && getActiveWrappers().stream().allMatch(w -> w.progress >= recipe.getElementAmount())) {
            process();
            resetProgress();
        } else if (this.isRecipeAvailable()) {
            getActiveWrappers().forEach(this::transfer);
        } else if (recipe == null) {
            resetProgress();
        }
    }

    private void resetProgress() {
        pedestalWrappers.values().forEach(w -> w.progress = 0);
    }

    private void transfer(PedestalWrapper wrapper) {
        if (wrapper.isRemoved()) {
            return;
        }

        var oldProgress = wrapper.progress;

        float transferAmount = Math.min(getTransferSpeed(wrapper.pedestal), recipe.getElementAmount() - oldProgress);

        if (transferAmount <= 0) {
            return;
        }

        float preservation = runeHandler.getBonus(Rune.BonusType.ELEMENT_PRESERVATION) + wrapper.pedestal.getRuneHandler().getBonus(Rune.BonusType.ELEMENT_PRESERVATION) + 1;
        var newProgress = oldProgress + wrapper.pedestal.getElementStorage().extractElement(Math.max(1, Math.round(transferAmount / preservation)), false) * preservation;

        wrapper.progress = Math.round(newProgress);
        if (level != null && level.isClientSide && wrapper.progress > oldProgress && level.random.nextDouble() < 0.2) {
            ParticleHelper.createElementFlowParticle(wrapper.getElementType(), level, Vec3.atCenterOf(wrapper.pedestal.getBlockPos()).relative(Direction.UP, 0.4), Vec3.atCenterOf(worldPosition).relative(Direction.UP, 1.7), level.random);
        } else if (level != null && !level.isClientSide) {
            this.setChanged();
        }
    }

    private float getTransferSpeed(SourceBreederPedestalBlockEntity pedestal) {
        return this.transferSpeed * (runeHandler.getBonus(Rune.BonusType.SPEED) + pedestal.getRuneHandler().getBonus(Rune.BonusType.SPEED) + 1);
    }

    private ItemStack breed(ElementType elementType, ISourceTraitHolder source1, ISourceTraitHolder source2) {
        return ReceptacleHelper.create(elementType, SourceTraitHelper.breed(level.random, runeHandler.getBonus(Rune.BonusType.LUCK), container.getItem(0).is(ECTags.Items.NATURAL_SOURCE_SEEDS), source1.getTraits(), source2.getTraits()));
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putIntArray(ECNames.PROGRESS, pedestalWrappers.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getKey().get2DDataValue()))
                .mapToInt(e -> e.getValue().progress)
                .toArray());
        compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
    }

    @Override
    public void load(@Nonnull CompoundTag compound) {
        super.load(compound);
        if (compound.contains(ECNames.RUNE_HANDLER)) {
            IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, 8));
        }
    }

    @Override
    @Nonnull
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
        if (!this.remove && cap == ElementalCraftCapabilities.RUNE_HANDLE) {
            return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
        }
        return super.getCapability(cap, side);
    }

    public List<Direction> getPedestalsDirections() {
        return pedestalWrappers.entrySet().stream()
                .filter(e -> !e.getValue().isRemoved())
                .map(Map.Entry::getKey)
                .toList();
    }

    public class PedestalWrapper implements IElementTypeProvider {

        private SourceBreederPedestalBlockEntity pedestal;
        private int progress;

        public PedestalWrapper() {
            this.pedestal = null;
            this.progress = 0;
        }

        public boolean isRemoved() {
            return pedestal == null || pedestal.isRemoved();
        }

        public boolean hasSource() {
            return pedestal.hasSource();
        }

        @Override
        public ElementType getElementType() {
            return isRemoved() ? ElementType.NONE : pedestal.getElementType();
        }

        public void lookupPedestal(Direction direction) {
            var te = level != null ? level.getBlockEntity(worldPosition.relative(direction, 2)) : null;

            pedestal = te instanceof SourceBreederPedestalBlockEntity p ? p : null;
        }

        public ISourceTraitHolder getTraitHolder() {
            return pedestal.getTraitHolder();
        }
    }
}
