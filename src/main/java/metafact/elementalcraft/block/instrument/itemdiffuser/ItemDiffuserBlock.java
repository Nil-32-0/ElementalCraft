package metafact.elementalcraft.block.instrument.itemdiffuser;

import metafact.elementalcraft.block.AbstractECContainerBlock;
import metafact.elementalcraft.block.AbstractECEntityBlock;
import metafact.elementalcraft.block.entity.BlockEntityHelper;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.block.instrument.IInstrumentBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemDiffuserBlock extends AbstractECContainerBlock implements IInstrumentBlock {

    public static final String NAME = "item_diffuser";

    private static final VoxelShape BASE_1 = Block.box(0, 0, 0, 16, 2, 0);
    private static final VoxelShape BASE_2 = Block.box(0, 14, 0, 16, 16, 16);

    private static final VoxelShape PILLAR_1 = Block.box(1, 2, 1, 3, 14, 3);
    private static final VoxelShape PILLAR_2 = Block.box(13, 2, 1, 15, 14, 3);
    private static final VoxelShape PILLAR_3 = Block.box(1, 2, 13, 3, 14, 15);
    private static final VoxelShape PILLAR_4 = Block.box(13, 2, 13, 15, 14, 15);

    private static final VoxelShape CONNECTOR_1 = Block.box(4, 2, 4, 12, 4, 12);
    private static final VoxelShape CONNECTOR_2 = Block.box(6, 4, 6, 10, 5, 10);
    private static final VoxelShape CONNECTOR_3 = Block.box(6, 11, 6, 10, 13, 10);
    private static final VoxelShape CONNECTOR_4 = Block.box(7, 13, 7, 9, 14, 9);

    private static final VoxelShape BOX = Block.box(5, 5, 5, 11, 11, 11);

    private static final VoxelShape SHAPE = Shapes.or(BASE_1, BASE_2, PILLAR_1, PILLAR_2, PILLAR_3, PILLAR_4,
            CONNECTOR_1, CONNECTOR_2, CONNECTOR_3, CONNECTOR_4, BOX);

    public ItemDiffuserBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new ItemDiffuserBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
        return AbstractECEntityBlock.createECTicker(level, type, ECBlockEntityTypes.ITEM_DIFFUSER, ItemDiffuserBlockEntity::tick);
    }

    @Nonnull
    @Override
    @Deprecated
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        return onSingleSlotActivated(world, pos, player, hand);
    }

    @Nonnull
    @Override
    @Deprecated
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }

    @Override
    @Deprecated
    public boolean canSurvive(BlockState state, @Nonnull LevelReader world, BlockPos pos) {
        return BlockEntityHelper.isValidContainer(state, world, pos.below());
    }

    @Nonnull
    @Override
    @Deprecated
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockPos facingPos) {
        return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, pos, facingPos);
    }
}
