package metafact.elementalcraft.block.extractor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import metafact.elementalcraft.block.AbstractECEntityBlock;
import metafact.elementalcraft.block.WaterLoggingHelper;
import metafact.elementalcraft.block.entity.BlockEntityHelper;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.particle.ParticleHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractElementExtractorBlock extends AbstractECEntityBlock {

    protected AbstractElementExtractorBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
        return createECServerTicker(level, type, ECBlockEntityTypes.EXTRACTOR, ExtractorBlockEntity::serverTick);
    }

    @Override
    @Deprecated
    public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader world, BlockPos pos) {
        return BlockEntityHelper.isValidContainer(state, world, pos.below());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        BlockEntityHelper.getBlockEntityAs(world, pos, ExtractorBlockEntity.class)
                .filter(ExtractorBlockEntity::canExtract)
                .ifPresent(e -> ParticleHelper.createElementFlowParticle(e.getSourceElementType(), world, Vec3.atCenterOf(pos), Direction.DOWN, 1, rand));
    }

    @Nonnull
    @Override
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockPos facingPos) {
        WaterLoggingHelper.scheduleWaterTick(state, level, pos);
        return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, pos, facingPos);
    }

    @Nonnull
    @Override
    public FluidState getFluidState(@Nonnull BlockState state) {
        return WaterLoggingHelper.isWaterlogged(state) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED);
    }
}
