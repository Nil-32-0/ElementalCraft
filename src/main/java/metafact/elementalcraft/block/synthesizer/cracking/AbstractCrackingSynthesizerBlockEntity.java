package metafact.elementalcraft.block.synthesizer.cracking;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.synthesizer.AbstractSynthesizerBlockEntity;
import metafact.elementalcraft.block.synthesizer.SynthesizerProperties;
import metafact.elementalcraft.range.RangeRenderTimer;
import metafact.elementalcraft.tag.ECTags;

import java.util.Optional;

public abstract class AbstractCrackingSynthesizerBlockEntity extends AbstractSynthesizerBlockEntity {

    private final RangeRenderTimer rangeRenderTimer = new RangeRenderTimer();

    public AbstractCrackingSynthesizerBlockEntity(
            RegistryObject<? extends BlockEntityType<?>> blockEntityType,
            SynthesizerProperties properties,
            BlockPos pos,
            BlockState state
    ) {
        super(blockEntityType, properties, pos, state);
    }

    @Override
    protected int synthesizeElement() {
        return findValidBlock()
                .map(pos -> {
                    BlockState state = level.getBlockState(pos);

                    level.destroyBlock(pos, false);
                    level.updateNeighborsAt(pos, level.getBlockState(pos).getBlock());

                    return Math.round(getElementAmountForBlock(state)*synthesisMultiplier);
                }).orElse(0);
    }

    protected abstract int getElementAmountForBlock(BlockState state);

    private Optional<BlockPos> findValidBlock() {

        return getBlocksInRange()
                .<BlockPos>mapMulti((pos, downstream) -> {
                    var state = level.getBlockState(pos);

                    if (state.isAir()) return;
                    if (state.is(ECTags.Blocks.CRACKABLE)) {
                        downstream.accept(pos);
                    }
                })
                .findAny();
    }

    public boolean showsRange() {
        return rangeRenderTimer.showsRange();
    }

    public void startShowingRange() {
        rangeRenderTimer.startShowingRange();
    }

    @Override
    protected ElementType getElementType() {
        return ElementType.EARTH;
    }
}
