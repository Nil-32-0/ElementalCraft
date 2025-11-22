package metafact.elementalcraft.interaction.jei.category.element.synthesis;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.ECBlocks;
import metafact.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import metafact.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import metafact.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import metafact.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;

import javax.annotation.Nonnull;
import java.util.List;

public class DrainingRecipeCategory extends AbstractECRecipeCategory<IngredientElementType> {

    public static final String NAME = "draining";
    public static final ResourceLocation TEXTURE = ElementalCraftApi.createRL("textures/gui/overlay/draining.png");

    private static final ItemStack DRAINING_SYNTHESIZER = new ItemStack(ECBlocks.DRAINING_SYNTHESIZER.get());
    private static final List<ItemStack> CONTAINERS = List.of(
            new ItemStack(ECBlocks.SMALL_CONTAINER.get()),
            new ItemStack(ECBlocks.CONTAINER.get()),
            new ItemStack(ECBlocks.RESERVOIRS.get(ElementType.WATER).get()));

    private final IGuiHelper guiHelper;

    public DrainingRecipeCategory(IGuiHelper guiHelper) {
        super("elementalcraft.jei.draining", createDrawableStack(guiHelper, DRAINING_SYNTHESIZER), guiHelper.createBlankDrawable(55, 46));
        setOverlay(guiHelper.createDrawable(TEXTURE, 0, 0, 36, 9), 2, 3);
        this.guiHelper = guiHelper;
    }

    @Nonnull
    @Override
    public RecipeType<IngredientElementType> getRecipeType() {
        return ECJEIRecipeTypes.DRAINING;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull IngredientElementType recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.CATALYST, 0, 15)
                .addItemStack(DRAINING_SYNTHESIZER);
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 0, 30)
                .addItemStacks(CONTAINERS);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 39, 0)
                .addIngredient(ECIngredientTypes.ELEMENT, recipe);
    }

}