package sirttas.elementalcraft.block.synthesizer.cracking;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.synthesizer.AbstractSynthesizerBlockEntity;

import javax.annotation.Nonnull;

public abstract class AbstractCrackingSynthesizerBlock extends AbstractECContainerBlock {

    protected AbstractCrackingSynthesizerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Nonnull
    @Override
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @NotNull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        final AbstractCrackingSynthesizerBlockEntity synthesizer = (AbstractCrackingSynthesizerBlockEntity) level.getBlockEntity(pos);

        if (synthesizer != null && player.getItemInHand(hand).isEmpty() && player.isShiftKeyDown()) {
            if (level.isClientSide) {
                synthesizer.startShowingRange();
            }
            return InteractionResult.SUCCESS;
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        AbstractSynthesizerBlockEntity.renderElementFlow(level, pos, rand);
    }

    @Override
    public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, BlockPos pos) {
        return BlockEntityHelper.isValidContainer(state, level, pos.below());
    }
}
