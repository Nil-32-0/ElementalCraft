package metafact.elementalcraft.block.synthesizer.culinary;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.block.synthesizer.AbstractContainerSynthesizerBlockEntity;
import metafact.elementalcraft.block.synthesizer.SynthesizerProperties;
import metafact.elementalcraft.container.SingleStackContainer;

public class CulinarySynthesizerBlockEntity extends AbstractContainerSynthesizerBlockEntity {

    private final SingleStackContainer inventory;

    public CulinarySynthesizerBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.CULINARY_SYNTHESIZER, SynthesizerProperties.getFromConfig(CulinarySynthesizerBlockEntity.class), pos, state);
        inventory = new SingleStackContainer(this::setChanged) {
            @Override
            public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
                return super.canPlaceItem(index, stack) && stack.getFoodProperties(null) != null;
            }
        };
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CulinarySynthesizerBlockEntity culinarySynthesizer) {
        culinarySynthesizer.handleSynthesis();
    }

    @Override
    protected int getElementAmountForStack(ItemStack stack) {
        var foodProperties = stack.getFoodProperties(null);

        if (foodProperties == null) {
            return 0;
        }
        return Math.round((foodProperties.getNutrition() + foodProperties.getSaturationModifier()) * this.synthesisMultiplier);
    }

    @NotNull
    @Override
    public Container getInventory() {
        return inventory;
    }

    @Override
    protected ElementType getElementType() {
        return ElementType.WATER;
    }
}
