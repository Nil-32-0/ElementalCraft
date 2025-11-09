package sirttas.elementalcraft.block.synthesizer.cracking;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.synthesizer.SynthesizerProperties;

public class CrackingSynthesizerBlockEntity extends AbstractCrackingSynthesizerBlockEntity {

    public static float multiplier;

    public CrackingSynthesizerBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.CRACKING_SYNTHESIZER, SynthesizerProperties.getFromConfig(CrackingSynthesizerBlockEntity.class),
                pos, state);
        multiplier = this.synthesisMultiplier;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CrackingSynthesizerBlockEntity crackingSynthesizer) {
        crackingSynthesizer.handleSynthesis();
    }

    protected int getElementAmountForBlock(BlockState state) {
        return state.getTags().map(tag -> tag.location().getPath()).filter(tag ->
                tag.startsWith("crackable/global") || tag.startsWith("crackable/normal")
        ).map(tag -> Integer.parseInt(tag.substring(17))).findAny().orElse(0);
    }
}
