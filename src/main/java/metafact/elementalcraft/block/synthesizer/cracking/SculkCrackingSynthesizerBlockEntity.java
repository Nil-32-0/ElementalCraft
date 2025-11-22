package metafact.elementalcraft.block.synthesizer.cracking;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.block.synthesizer.SynthesizerProperties;

public class SculkCrackingSynthesizerBlockEntity extends AbstractCrackingSynthesizerBlockEntity {
    public static float multiplier;

    public SculkCrackingSynthesizerBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.SCULK_CRACKING_SYNTHESIZER, SynthesizerProperties.getFromConfig(SculkCrackingSynthesizerBlockEntity.class),
                pos, state);
        multiplier = this.synthesisMultiplier;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SculkCrackingSynthesizerBlockEntity crackingSynthesizer) {
        crackingSynthesizer.handleSynthesis();
    }

    protected int getElementAmountForBlock(BlockState state) {
        return state.getTags().map(tag -> tag.location().getPath()).filter(tag ->
                tag.startsWith("crackable/global") || tag.startsWith("crackable/sculk")
        ).map(tag -> Integer.parseInt(
                tag.substring(tag.startsWith("crackable/global") ? 17 : 16)
        )).findAny().orElse(0);
    }
}
