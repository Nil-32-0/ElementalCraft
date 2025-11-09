package sirttas.elementalcraft.block.synthesizer.draining;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.synthesizer.AbstractSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.SynthesizerProperties;

public class DrainingSynthesizerBlockEntity extends AbstractSynthesizerBlockEntity {

    public DrainingSynthesizerBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.DRAINING_SYNTHESIZER, SynthesizerProperties.getFromConfig(DrainingSynthesizerBlockEntity.class), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, DrainingSynthesizerBlockEntity drainingSynthesizer) {
        drainingSynthesizer.handleSynthesis();
    }

    @Override
    protected int synthesizeElement() {
        return 0;
    }

    public void fill() {
        insertElement(false);
    }

    public boolean needsElement() {
        return insertElement(true) <= 0;
    }

    private int insertElement(boolean simulate) {
        return ((SingleElementStorage) this.getElementStorage().resolve().orElseThrow())
                .insertElement(Math.round(this.synthesisMultiplier), ElementType.WATER, simulate);
    }

    @Override
    protected ElementType getElementType() {
        return ElementType.WATER;
    }
}
