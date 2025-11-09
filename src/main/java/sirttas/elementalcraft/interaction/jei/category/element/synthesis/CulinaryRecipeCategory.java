package sirttas.elementalcraft.interaction.jei.category.element.synthesis;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;

import javax.annotation.Nonnull;
import java.util.List;

public class CulinaryRecipeCategory extends AbstractECRecipeCategory<ItemStack> {
    public static final String NAME = "culinary";

    private static final ItemStack CULINARY_SYNTHESIZER = new ItemStack(ECBlocks.CULINARY_SYNTHESIZER.get());
    private static final List<ItemStack> CONTAINERS = List.of(
            new ItemStack(ECBlocks.CONTAINER.get()),
            new ItemStack(ECBlocks.RESERVOIRS.get(ElementType.WATER).get()));

    public CulinaryRecipeCategory(IGuiHelper guiHelper) {
        super("elementalcraft.jei.culinary", createDrawableStack(guiHelper, CULINARY_SYNTHESIZER), guiHelper.createBlankDrawable(55, 63));
        setOverlay(guiHelper.createDrawable(DrainingRecipeCategory.TEXTURE, 0, 0, 36, 9), 2, 20);
    }

    @Nonnull
    @Override
    public RecipeType<ItemStack> getRecipeType() {
        return ECJEIRecipeTypes.CULINARY;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ItemStack recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 0)
                .addItemStack(recipe);
        builder.addSlot(RecipeIngredientRole.CATALYST, 0, 32)
                .addItemStack(CULINARY_SYNTHESIZER);
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 0, 47)
                .addItemStacks(CONTAINERS);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 39, 17)
                .addIngredient(ECIngredientTypes.ELEMENT, new IngredientElementType(ElementType.WATER, 2));
    }}
