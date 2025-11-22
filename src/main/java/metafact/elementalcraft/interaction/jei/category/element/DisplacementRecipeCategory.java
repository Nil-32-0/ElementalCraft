package metafact.elementalcraft.interaction.jei.category.element;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.ECBlocks;
import metafact.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import metafact.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import metafact.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import metafact.elementalcraft.interaction.jei.ingredient.source.IngredientSource;
import metafact.elementalcraft.item.elemental.ElementalItemHelper;
import metafact.elementalcraft.item.source.receptacle.ReceptacleHelper;

import javax.annotation.Nonnull;

public class DisplacementRecipeCategory extends AbstractECRecipeCategory<ElementType> {

	public static final String NAME = "displacement";


	public DisplacementRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.displacement", createDrawableStack(guiHelper, new ItemStack(ECBlocks.SOURCE_DISPLACEMENT_PLATES.get(ElementType.FIRE).get())), guiHelper.createBlankDrawable(64, 32));
		setOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/extraction.png"), 0, 0, 24, 9), 21, 19);
	}

	@Nonnull
	@Override
	public RecipeType<ElementType> getRecipeType() {
		return ECJEIRecipeTypes.DISPLACEMENT;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ElementType type, @Nonnull IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 0, 0).addIngredient(ECIngredientTypes.SOURCE, new IngredientSource(type));
		builder.addSlot(RecipeIngredientRole.INPUT, 0, 16).addItemStack(new ItemStack(ElementalItemHelper.getDisplacementPlateForElement(type)));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 47, 0).addItemStack(ReceptacleHelper.create(type));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 47, 16).addItemStack(new ItemStack(ECBlocks.BROKEN_SOURCE_DISPLACEMENT_PLATE.get()));
	}
}
