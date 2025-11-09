package sirttas.elementalcraft.block.synthesizer.combustion;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.synthesizer.AbstractContainerSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.SynthesizerProperties;
import sirttas.elementalcraft.container.SingleStackContainer;

public class CombustionSynthesizerBlockEntity extends AbstractContainerSynthesizerBlockEntity {

    private final SingleStackContainer inventory;

    public CombustionSynthesizerBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.COMBUSTION_SYNTHESIZER, SynthesizerProperties.getFromConfig(CombustionSynthesizerBlockEntity.class), pos, state);
        inventory = new SingleStackContainer(this::setChanged) {
            @Override
            public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
                return super.canPlaceItem(index, stack) && AbstractFurnaceBlockEntity.isFuel(stack);
            }
        };
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CombustionSynthesizerBlockEntity combustionSynthesizer) {
        combustionSynthesizer.handleSynthesis();
    }

    @Override
    protected int getElementAmountForStack(ItemStack stack) {
        int burnTime = ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
        return Math.round(burnTime * this.synthesisMultiplier);
    }

    @NotNull
    @Override
    public Container getInventory() {
        return inventory;
    }

    @Override
    protected ElementType getElementType() {
        return ElementType.FIRE;
    }
}
