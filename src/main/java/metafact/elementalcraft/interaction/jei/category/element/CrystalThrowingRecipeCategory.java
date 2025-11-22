package metafact.elementalcraft.interaction.jei.category.element;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import metafact.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import metafact.elementalcraft.item.ECItems;
import metafact.elementalcraft.item.elemental.ElementalItemHelper;

import javax.annotation.Nonnull;

public class CrystalThrowingRecipeCategory extends AbstractECRecipeCategory<ElementType> {

    public static final String NAME = "crystal_throwing";

    public CrystalThrowingRecipeCategory(IGuiHelper guiHelper) {
        super("elementalcraft.jei.crystal_throwing", createDrawableStack(guiHelper, new ItemStack(ECItems.INERT_CRYSTAL.get())), guiHelper.createBlankDrawable(100, 45));
        setOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/crystal_throwing.png"), 0, 0, 73, 24), 10, 2);
    }

    @Nonnull
    @Override
    public RecipeType<ElementType> getRecipeType() {
        return ECJEIRecipeTypes.CRYSTAL_THROWING;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ElementType type, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 2, 27).addItemStack(new ItemStack(ElementalItemHelper.getCrystalForElement(type)));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 67, 27).addItemStack(new ItemStack(ElementalItemHelper.getPowerfulShardForElement(type)));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 83, 27).addItemStack(new ItemStack(ElementalItemHelper.getShardForElement(type)));
    }
}
