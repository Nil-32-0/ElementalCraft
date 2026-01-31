package metafact.elementalcraft.interaction.jei.category.instrument;

import java.util.List;

import javax.annotation.Nonnull;

import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.rune.Rune;
import metafact.elementalcraft.block.ECBlocks;
import metafact.elementalcraft.block.instrument.runicchanneler.RunicChannelerBlockEntity;
import metafact.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import metafact.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import metafact.elementalcraft.item.ECItems;
import metafact.elementalcraft.recipe.instrument.RunicChannelerRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.world.item.ItemStack;

public class RunicChannelerRecipeCategory extends AbstractInstrumentRecipeCategory<RunicChannelerBlockEntity, RunicChannelerRecipe> {
    public static final String NAME = "runic_channeler";
    private static final ItemStack RUNIC_CHANNELER = new ItemStack(ECBlocks.RUNIC_CHANNELER.get());
	private static final int RADIUS = 42;

    public RunicChannelerRecipeCategory(IGuiHelper guiHelper) {
        super("elementalcraft.jei.runic_channeler", createDrawableStack(guiHelper, RUNIC_CHANNELER), guiHelper.createBlankDrawable(RADIUS * 2 + 48, RADIUS * 2 + 16));
        setOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/binding.png"), 0, 0, 124, 83), 10, 10);
    }
    
    @Nonnull
    @Override
    public RecipeType<RunicChannelerRecipe> getRecipeType() {
        return ECJEIRecipeTypes.RUNIC_CHANNELER;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull RunicChannelerRecipe recipe, @Nonnull IFocusGroup focuses) {
        List<Rune> runes = recipe.getRunesAsRunes();
        int i = 0;

        for (Rune rune : runes) {
            ItemStack runeItem = ECItems.RUNE.get().getRuneStack(rune);
			double a = Math.toRadians(i / (double) runes.size() * 360D + 180);

            builder.addSlot(RecipeIngredientRole.INPUT, RADIUS + (int) (-RADIUS * Math.sin(a)), RADIUS + (int) (RADIUS * Math.cos(a)))
					.addItemStack(runeItem);
			i++;
        }
        
        builder.addSlot(RecipeIngredientRole.INPUT, RADIUS, RADIUS - 24)
                .addIngredients(recipe.getIngredient());

        builder.addSlot(RecipeIngredientRole.CATALYST, RADIUS, RADIUS - 8)
				.addItemStack(RUNIC_CHANNELER);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, RADIUS, RADIUS+8)
				.addItemStack(container);

        builder.addSlot(RecipeIngredientRole.INPUT, RADIUS, RADIUS + 26)
				.addIngredients(ECIngredientTypes.ELEMENT, getElementTypeIngredients(recipe));

        builder.addSlot(RecipeIngredientRole.OUTPUT, RADIUS * 2 + 32, RADIUS)
				.addItemStack(RecipeUtil.getResultItem(recipe));
    }
}
