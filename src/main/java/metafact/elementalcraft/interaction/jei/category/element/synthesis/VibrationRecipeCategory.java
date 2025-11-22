package metafact.elementalcraft.interaction.jei.category.element.synthesis;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import mezz.jei.api.recipe.RecipeType;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.ECBlocks;
import metafact.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import metafact.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import metafact.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import metafact.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;

import javax.annotation.Nonnull;
import java.util.List;

public class VibrationRecipeCategory extends AbstractECRecipeCategory<IngredientElementType> {

    public static final String NAME = "vibration";

    private static final ResourceLocation TEXTURE = ElementalCraftApi.createRL("textures/gui/overlay/vibration.png");

    private static final ItemStack VIBRATION_SYNTHESIZER = new ItemStack(ECBlocks.VIBRATION_SYNTHESIZER.get());
    private static final List<ItemStack> CONTAINERS = List.of(
            new ItemStack(ECBlocks.SMALL_CONTAINER.get()),
            new ItemStack(ECBlocks.CONTAINER.get()),
            new ItemStack(ECBlocks.RESERVOIRS.get(ElementType.AIR).get()));

    private final IGuiHelper guiHelper;

    public VibrationRecipeCategory(IGuiHelper guiHelper) {
        super("elementalcraft.jei.vibration", createDrawableStack(guiHelper, VIBRATION_SYNTHESIZER), guiHelper.createBlankDrawable(61, 54));
        setOverlay(guiHelper.createDrawable(TEXTURE, 0, 0, 23, 9), 21, 9);
        this.guiHelper = guiHelper;
    }

    @Nonnull
    @Override
    public RecipeType<IngredientElementType> getRecipeType() {
        return ECJEIRecipeTypes.VIBRATION;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull IngredientElementType recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.CATALYST, 6, 19)
                .addItemStack(VIBRATION_SYNTHESIZER);
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 6, 36)
                .addItemStacks(CONTAINERS);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 45, 6)
                .addIngredient(ECIngredientTypes.ELEMENT, recipe);
    }
}
