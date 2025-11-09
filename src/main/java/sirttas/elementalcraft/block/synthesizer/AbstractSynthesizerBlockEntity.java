package sirttas.elementalcraft.block.synthesizer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.range.Range;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.container.IContainerTopBlockEntity;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.container.IRuneableBlockEntity;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.range.RangeHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

public abstract class AbstractSynthesizerBlockEntity extends AbstractECBlockEntity implements IContainerTopBlockEntity, IRuneableBlockEntity {
    protected final RuneHandler runeHandler;
    protected final SingleElementStorage bufferElementStorage;
    protected final Range range;

    protected final float synthesisMultiplier;
    protected final int synthesisSpeed;

    protected boolean working;
    private ISingleElementStorage containerCache;

    protected AbstractSynthesizerBlockEntity(
            RegistryObject<? extends BlockEntityType<?>> blockEntityType,
            SynthesizerProperties properties,
            BlockPos pos,
            BlockState state
    ) {
        super(blockEntityType, pos, state);

        this.runeHandler = new RuneHandler(properties.maxRunes(), this::setChanged);
        this.synthesisMultiplier = properties.synthesisMultiplier();
        this.synthesisSpeed = properties.synthesisSpeed();
        this.working = false;
        this.bufferElementStorage = new SingleElementStorage(properties.bufferCapacity(), this::setChanged);
        this.range = properties.range();
    }

    public static void renderElementFlow(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        BlockEntityHelper.getBlockEntityAs(level, pos, AbstractSynthesizerBlockEntity.class)
                .filter(AbstractSynthesizerBlockEntity::isWorking)
                .ifPresent(synthesizer -> ParticleHelper.createElementFlowParticle(synthesizer.getElementType(), level, Vec3.atCenterOf(pos.below()), Direction.DOWN, 1, rand));
    }

    protected void handleSynthesis() {
        var container = getContainer();
        var type = getElementType();

        if (container == null || type == ElementType.NONE) {
            return;
        } else if (bufferElementStorage.getElementAmount() < synthesisSpeed) {
            bufferElementStorage.insertElement(synthesizeElement(), getElementType(), false);
            setChanged();
        }

        if (bufferElementStorage.getElementAmount() <= 0) {
            working = false;
            setChanged();
            return;
        }

        var synthesized = runeHandler.handleElementTransfer(bufferElementStorage, container, type, synthesisSpeed);
        var hasSynthesized = synthesized > 0;

        if (hasSynthesized || working) {
            working = hasSynthesized;
            setChanged();
        }
    }

    protected abstract int synthesizeElement();

    public AABB getRange() {
        return this.runeHandler.getRange(this.range).move(this.worldPosition);
    }

    public Stream<BlockPos> getBlocksInRange() {
        return RangeHelper.getBlocksInAABB(getRange());
    }

    protected abstract ElementType getElementType();

    public boolean isWorking() {
        return working;
    }

    @Override
    @Nonnull
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
        if (!this.remove) {
            if (cap == ElementalCraftCapabilities.ELEMENT_STORAGE) {
                return getElementStorage();
            } else if (cap == ElementalCraftCapabilities.RUNE_HANDLE) {
                return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Nonnull
    public  <U> LazyOptional<U> getElementStorage() {
        return LazyOptional.of(() -> bufferElementStorage).cast();
    }

    @Override
    public ISingleElementStorage getContainer() {
        if (containerCache == null) {
            containerCache = IContainerTopBlockEntity.super.getContainer();
        }
        return containerCache;
    }

    @Override
    public RuneHandler getRuneHandler() {
        return runeHandler;
    }

    @Override
    public void load(@Nonnull CompoundTag compound) {
        super.load(compound);
        if (compound.contains(ECNames.ELEMENT_STORAGE)) {
            bufferElementStorage.deserializeNBT(compound.getCompound(ECNames.ELEMENT_STORAGE));
        }
        if (compound.contains(ECNames.RUNE_HANDLER)) {
            IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, Tag.OBJECT_HEADER));
        }
        working = compound.getBoolean(ECNames.WORKING);
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put(ECNames.ELEMENT_STORAGE, bufferElementStorage.serializeNBT());
        compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
        compound.putBoolean(ECNames.WORKING, working);
    }
}
