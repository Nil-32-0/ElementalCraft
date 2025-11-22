package metafact.elementalcraft.block.synthesizer.mill;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.block.synthesizer.AbstractSynthesizerBlockEntity;
import metafact.elementalcraft.block.synthesizer.SynthesizerProperties;

public class AirMillSynthesizerBlockEntity extends AbstractSynthesizerBlockEntity {

    public AirMillSynthesizerBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.AIR_MILL_SYNTHESIZER, SynthesizerProperties.getFromConfig(AirMillSynthesizerBlockEntity.class), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AirMillSynthesizerBlockEntity airMillSynthesizer) {
        airMillSynthesizer.handleSynthesis();
    }

    @Override
    protected int synthesizeElement() {
        return Math.round(this.synthesisMultiplier);
    }

    @Override
    protected ElementType getElementType() {
        return ElementType.AIR;
    }
}
