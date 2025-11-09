package sirttas.elementalcraft.interaction.jei.category.element.synthesis.cracking;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.synthesizer.cracking.SculkCrackingSynthesizerBlockEntity;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;

import java.util.List;

public class SculkCrackingRecipeCategory extends AbstractCrackingRecipeCategory {

    public static String NAME = "sculk_cracking";

    private static final ItemStack SCULK_CRACKING_SYNTHESIZER = new ItemStack(ECBlocks.SCULK_CRACKING_SYNTHESIZER.get());
    private static final List<ItemStack> CONTAINERS = List.of(
            new ItemStack(ECBlocks.CONTAINER.get()),
            new ItemStack(ECBlocks.RESERVOIRS.get(ElementType.EARTH).get()));

    public SculkCrackingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, "elementalcraft.jei.sculk_cracking", SCULK_CRACKING_SYNTHESIZER, CONTAINERS);
    }

    @Override
    public @NotNull RecipeType<Block> getRecipeType() {
        return ECJEIRecipeTypes.SCULK_CRACKING;
    }

    @Override
    protected float getSynthesizerMultiplier() {
        return SculkCrackingSynthesizerBlockEntity.multiplier;
    }
}
