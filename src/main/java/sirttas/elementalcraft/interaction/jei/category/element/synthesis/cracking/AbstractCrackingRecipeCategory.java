package sirttas.elementalcraft.interaction.jei.category.element.synthesis.cracking;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractCrackingRecipeCategory extends AbstractECRecipeCategory<Block> {

    private final List<ItemStack> containers;

    private final ItemStack synthesizer;

    protected AbstractCrackingRecipeCategory(IGuiHelper guiHelper, String translationKey, ItemStack synthesizer, List<ItemStack> containers) {
        super(translationKey, createDrawableStack(guiHelper, synthesizer), guiHelper.createBlankDrawable(108, 36));
        setOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/cracking.png"), 0, 0, 73, 14), 17, 17);
        this.containers = containers;
        this.synthesizer = synthesizer;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull Block input, @Nonnull IFocusGroup focus) {
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 20)
                .addItemStack(new ItemStack(input));
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 41, 17)
                .addItemStacks(containers);
        builder.addSlot(RecipeIngredientRole.CATALYST, 41, 1)
                .addItemStack(synthesizer);

        Integer elementAmount = input.defaultBlockState().getTags().map(tag -> tag.location().getPath())
                .filter(tag -> tag.startsWith("crackable/")
                ).map(tag -> Integer.parseInt(
                        tag.substring(tag.startsWith("crackable/sculk") ? 16 : 17)
                )).findAny().orElseThrow();

        builder.addSlot(RecipeIngredientRole.OUTPUT, 70, 0)
                .addIngredient(ECIngredientTypes.ELEMENT, new IngredientElementType(
                        ElementType.EARTH,
                        IngredientElementType.getGaugeValue(Math.round(elementAmount * getSynthesizerMultiplier()))
                ));
    }

    protected abstract float getSynthesizerMultiplier();
}
