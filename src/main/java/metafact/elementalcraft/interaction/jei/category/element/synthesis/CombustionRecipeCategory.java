package metafact.elementalcraft.interaction.jei.category.element.synthesis;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
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

public class CombustionRecipeCategory extends AbstractECRecipeCategory<IJeiFuelingRecipe> {

    public static final String NAME = "combustion";

    private static final ResourceLocation TEXTURE = ElementalCraftApi.createRL("textures/gui/overlay/combustion.png");
    private static final ItemStack COMBUSTION_SYNTHESIZER = new ItemStack(ECBlocks.COMBUSTION_SYNTHESIZER.get());
    private static final List<ItemStack> CONTAINERS = List.of(
            new ItemStack(ECBlocks.SMALL_CONTAINER.get()),
            new ItemStack(ECBlocks.CONTAINER.get()),
            new ItemStack(ECBlocks.RESERVOIRS.get(ElementType.FIRE).get()));

    public CombustionRecipeCategory(IGuiHelper guiHelper) {
        super("elementalcraft.jei.combustion", createDrawableStack(guiHelper, COMBUSTION_SYNTHESIZER), guiHelper.createBlankDrawable(55, 63));
        setOverlay(guiHelper.createDrawable(TEXTURE, 0, 0, 38, 13), 0, 17);
        setOverlay(guiHelper.drawableBuilder(TEXTURE, 0, 13, 14, 14).buildAnimated(100, IDrawableAnimated.StartDirection.TOP, true), 0, 17);
    }

    @Nonnull
    @Override
    public RecipeType<IJeiFuelingRecipe> getRecipeType() {
        return ECJEIRecipeTypes.COMBUSTION;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull IJeiFuelingRecipe recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 0)
                .addItemStacks(recipe.getInputs());

        builder.addSlot(RecipeIngredientRole.CATALYST, 0, 31)
                .addItemStack(COMBUSTION_SYNTHESIZER);
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 0, 47)
                .addItemStacks(CONTAINERS);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 39, 16)
                .addIngredient(ECIngredientTypes.ELEMENT, new IngredientElementType(ElementType.FIRE, IngredientElementType.getGaugeValue(recipe.getBurnTime())));
    }
}