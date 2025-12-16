package metafact.elementalcraft.interaction.jei.category.instrument;

import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.ECBlocks;
import metafact.elementalcraft.block.instrument.itemdiffuser.ItemDiffuserBlockEntity;
import metafact.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import metafact.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import metafact.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import metafact.elementalcraft.recipe.instrument.ItemDiffusionRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemDiffusionRecipeCategory extends AbstractInstrumentRecipeCategory<ItemDiffuserBlockEntity, ItemDiffusionRecipe> {

    private static final ItemStack ITEM_DIFFUSER = new ItemStack(ECBlocks.ITEM_DIFFUSER.get());

    public ItemDiffusionRecipeCategory(IGuiHelper guiHelper) {
        super("elementalcraft.jei.item_diffusion", createDrawableStack(guiHelper, ITEM_DIFFUSER), guiHelper.createBlankDrawable(130, 90));
        setOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/extraction.png"), 0, 0, 25, 12), 40, 38);
    }

    @Nonnull
    @Override
    public RecipeType<ItemDiffusionRecipe> getRecipeType() {
        return ECJEIRecipeTypes.ITEM_DIFFUSION;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ItemDiffusionRecipe recipe, @Nonnull IFocusGroup focuses) {
        var ingredient = recipe.getInput();

        builder.addSlot(RecipeIngredientRole.INPUT, 15, 15)
                .addIngredients(ingredient);
        builder.addSlot(RecipeIngredientRole.CATALYST, 15, 35)
                .addItemStack(ITEM_DIFFUSER);

        builder.addSlot(RecipeIngredientRole.INPUT, 15, 55)
                .addIngredients(ECIngredientTypes.ELEMENT, getElementTypeIngredients(recipe));


        var results = recipe.getResults();
        int maxResults = 9;
        for (int i = 0; i < maxResults; i++) {
            int xOffset = i % 3;
            int yOffset = (maxResults-i-1) / 3;
            int xPos = 70 + 20*xOffset;
            int yPos = 15 + 20*yOffset;
            if (i < results.size()) {
                builder.addSlot(RecipeIngredientRole.OUTPUT, xPos, yPos)
                        .addIngredient(ECIngredientTypes.ELEMENT, new IngredientElementType(
                                results.get(i).getFirst(),
                                getGaugeValue(results.get(i).getSecond())
                        ));
            } else {
                builder.addSlot(RecipeIngredientRole.OUTPUT, xPos, yPos)
                        .addIngredient(ECIngredientTypes.ELEMENT, new IngredientElementType(
                                ElementType.NONE,
                                -1
                        ));
            }
        }
    }
}
