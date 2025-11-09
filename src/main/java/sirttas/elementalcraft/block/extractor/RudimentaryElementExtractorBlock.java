package sirttas.elementalcraft.block.extractor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class RudimentaryElementExtractorBlock extends AbstractElementExtractorBlock {

    public static final String NAME = "rudimentary_element_extractor";

    private static final VoxelShape BASE = Block.box(6D, 0D, 6D, 10D, 4D, 10D);
    private static final VoxelShape PILLAR = Block.box(7D, 4D, 7D, 9D, 13D, 9D);
    private static final VoxelShape TOP = Block.box(6D, 13D, 6D, 10D, 16D, 10D);

    private static final VoxelShape PIPE_N = Block.box(7D, 1D, 3D, 9D, 3D, 6D);
    private static final VoxelShape PIPE_S = Block.box(7D, 1D, 10D, 9D, 3D, 13D);
    private static final VoxelShape PIPE_E = Block.box(10D, 1D, 7D, 13D, 3D, 9D);
    private static final VoxelShape PIPE_W = Block.box(3D, 1D, 7D, 6D, 3D, 9D);

    private static final VoxelShape SHAPE = Shapes.or(BASE, PILLAR, TOP, PIPE_N, PIPE_S, PIPE_E, PIPE_W);

    public RudimentaryElementExtractorBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public ExtractorBlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ExtractorBlockEntity(pos, state);
    }

    @Nonnull
    @Override
    @Deprecated
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }

}
