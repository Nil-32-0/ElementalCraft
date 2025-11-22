package metafact.elementalcraft.block.shrine.melting;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import metafact.elementalcraft.block.entity.BlockEntityHelper;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import metafact.elementalcraft.block.shrine.properties.ShrineProperties;
import metafact.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import metafact.elementalcraft.recipe.ECRecipeTypes;
import metafact.elementalcraft.recipe.melting.MeltingRecipe;
import metafact.elementalcraft.recipe.melting.MeltingRecipeInput;

import java.util.List;
import java.util.Optional;

public class MeltingShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(MeltingShrineBlock.NAME);

	protected static final List<Direction> UPGRADE_DIRECTIONS = List.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

    private int cooldown;

	public MeltingShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.MELTING_SHRINE, pos, state, PROPERTIES_KEY);
        cooldown = 0;
	}

    private Optional<MeltingRecipe> findRecipe() {
        if (level == null || level.getBlockState(getTargetPos()).isAir()) return Optional.empty();

        var input = new MeltingRecipeInput(level.getBlockState(getTargetPos()), this.getElementStorage().getElementAmount(), this.getConsumeAmount());

        return level.getRecipeManager().getAllRecipesFor(ECRecipeTypes.MELTING.get()).stream().filter(
                recipe -> recipe.matches(input, level)).findAny();
    }

    @Override
    public BlockPos getTargetPos() {
        return worldPosition.above();
    }

    public static boolean fill(AbstractShrineBlockEntity shrine, Direction fillingDirection, Fluid fluid, float fluidMultiplier) {
        if (shrine.getLevel() == null) return false;
        var fluidHandler = BlockEntityHelper.getBlockEntity(shrine.getLevel(), shrine.getBlockPos().relative(fillingDirection, 2))
                .flatMap(entity -> entity.getCapability(ForgeCapabilities.FLUID_HANDLER, fillingDirection.getOpposite()).resolve());


        return fluidHandler.isPresent() && fluidHandler.get().fill(
                new FluidStack(fluid, (int) Math.round(shrine.getStrength() * fluidMultiplier)), IFluidHandler.FluidAction.EXECUTE
        ) > 0;
	}

	@Override
	protected boolean doPeriod() {
        if (cooldown > 0) {
            cooldown--;
        } else {
            findRecipe().ifPresent(this::melt);
        }
        return false;
    }

    private void melt(MeltingRecipe recipe) {
        var fillingDirection = getUpgradeDirection(ShrineUpgrades.FILLING);

        if (fillingDirection != null && fill(this, fillingDirection, recipe.result(), recipe.fillingAmount())) {
            level.destroyBlock(getTargetPos(), false);
        } else {
            level.setBlock(getTargetPos(), recipe.result().defaultFluidState().createLegacyBlock(), 11);
            level.levelEvent(LevelEvent.LAVA_FIZZ, getTargetPos(), 0);
        }
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		return UPGRADE_DIRECTIONS;
	}
}
