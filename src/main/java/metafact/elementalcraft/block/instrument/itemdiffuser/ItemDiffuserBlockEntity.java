package metafact.elementalcraft.block.instrument.itemdiffuser;

import com.mojang.datafixers.util.Pair;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.element.IElementTypeProvider;
import metafact.elementalcraft.block.container.ElementContainerBlockEntity;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import metafact.elementalcraft.block.instrument.IInstrument;
import metafact.elementalcraft.config.ECConfig;
import metafact.elementalcraft.container.SingleItemContainer;
import metafact.elementalcraft.particle.ParticleHelper;
import metafact.elementalcraft.recipe.ECRecipeTypes;
import metafact.elementalcraft.recipe.instrument.IInstrumentRecipe;
import metafact.elementalcraft.recipe.instrument.ItemDiffusionRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ItemDiffuserBlockEntity extends AbstractInstrumentBlockEntity<ItemDiffuserBlockEntity, ItemDiffusionRecipe> {

    private static final Config<ItemDiffuserBlockEntity, ItemDiffusionRecipe> CONFIG = new Config<>(
            ECBlockEntityTypes.ITEM_DIFFUSER,
            ECRecipeTypes.ITEM_DIFFUSION,
            ECConfig.SERVER.itemDiffuserTransferSpeed,
            ECConfig.SERVER.itemDiffuserMaxRunes,
            0,
            true,
            false
    );

    private final SingleItemContainer inventory;
    private final Map<Integer, ContainerWrapper> containers;

    public ItemDiffuserBlockEntity(BlockPos pos, BlockState state) {
        super(CONFIG, pos, state);
        this.inventory = new SingleItemContainer(this::setChanged);
        this.containers = new TreeMap<>();
        for (int i = 0; i < ECConfig.SERVER.itemDiffuserMaxOutputs.get(); i++) {
            containers.put(i, new ContainerWrapper());
        }
    }

    public static <T extends IInstrument, R extends IInstrumentRecipe<T>> void tick(Level level, BlockPos pos, BlockState state, ItemDiffuserBlockEntity instrument) {
        instrument.refreshContainers();
        AbstractInstrumentBlockEntity.tick(level, pos, state, instrument);
    }

    private void refreshContainers() {
        containers.forEach((d, c) -> {
            if (c.isRemoved()) {
                if (recipe == null || recipe.getResults().size() > d) {
                    setProgress(0);
                }
                c.lookupContainer(d+1);
            }
        });
    }

    public int numValidContainers() {
        int num = 0;
        for (int i : containers.keySet()) {
            ContainerWrapper c = containers.get(i);
            if (c.isRemoved()) {
                return num;
            }
            num++;
        }
        return num;
    }

    @Override
    protected void assemble() {
        super.assemble();

        List<Pair<ElementType, Integer>> results = recipe.getResults();
        int numContainers = numValidContainers();
        int numIncreasedOutputs = Math.min(numContainers - results.size(), results.size());

        for (int i = 0; i < results.size(); i++) {
            Pair<ElementType, Integer> resultType = results.get(i);
            double multiplier = i < numIncreasedOutputs ? ECConfig.SERVER.itemDiffuserOutputBonus.get() : 1;
            if (numContainers == ECConfig.SERVER.itemDiffuserMaxOutputs.get()) multiplier += ECConfig.SERVER.itemDiffuserMaxBonus.get();
            containers.get(i).container.getElementStorage()
                    .insertElement((int) (resultType.getSecond() * multiplier), resultType.getFirst(), false);

            var pos = getBlockPos().above(i);
            ParticleHelper.createElementFlowParticle(results.get(i).getFirst(), level, Vec3.atCenterOf(pos), Direction.UP, 1, level.getRandom());
        }
    }

    @Override
    protected ItemDiffusionRecipe lookupRecipe() {
        if (getContainerElementType() == ElementType.NONE) {
            return null;
        }
        return lookupRecipe(level, ECRecipeTypes.ITEM_DIFFUSION.get());
    }

    @Nonnull
    @Override
    public Container getInventory() {
        return inventory;
    }

    public class ContainerWrapper implements IElementTypeProvider {

        private ElementContainerBlockEntity container;

        public ContainerWrapper() {
            this.container = null;
        }

        public boolean isRemoved() {
            return container == null || container.isRemoved();
        }

        public void lookupContainer(int distance) {
            var te = level != null ? level.getBlockEntity(worldPosition.above(distance)) : null;

            container = te instanceof ElementContainerBlockEntity c ? c : null;
        }

        @Override
        public ElementType getElementType() {
            return isRemoved() ? ElementType.NONE : container.getElementStorage().getElementType();
        }
    }
}