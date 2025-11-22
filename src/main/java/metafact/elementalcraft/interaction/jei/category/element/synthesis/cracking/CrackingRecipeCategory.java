package metafact.elementalcraft.interaction.jei.category.element.synthesis.cracking;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.ECBlocks;
import metafact.elementalcraft.block.synthesizer.cracking.CrackingSynthesizerBlockEntity;
import metafact.elementalcraft.interaction.jei.ECJEIRecipeTypes;

import java.util.List;

public class CrackingRecipeCategory extends AbstractCrackingRecipeCategory {

    public static String NAME = "cracking";

    private static final ItemStack CRACKING_SYNTHESIZER = new ItemStack(ECBlocks.CRACKING_SYNTHESIZER.get());
    private static final List<ItemStack> CONTAINERS = List.of(
            new ItemStack(ECBlocks.SMALL_CONTAINER.get()),
            new ItemStack(ECBlocks.CONTAINER.get()),
            new ItemStack(ECBlocks.RESERVOIRS.get(ElementType.EARTH).get()));

    public CrackingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, "elementalcraft.jei.cracking", CRACKING_SYNTHESIZER, CONTAINERS);
    }

    @Override
    public @NotNull RecipeType<Block> getRecipeType() {
        return ECJEIRecipeTypes.CRACKING;
    }

    @Override
    protected float getSynthesizerMultiplier() {
        return CrackingSynthesizerBlockEntity.multiplier;
    }
}