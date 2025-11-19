package sirttas.elementalcraft.interaction.jei.category.element;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.source.breeder.SourceBreederBlockEntity;
import sirttas.elementalcraft.block.source.trait.SourceTraits;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.category.AbstractBlockEntityRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;
import sirttas.elementalcraft.recipe.SourceBreedingRecipe;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class SourceBreedingRecipeCategory extends AbstractBlockEntityRecipeCategory<SourceBreederBlockEntity, SourceBreedingRecipe> {

	public static final String NAME = "source_breeding";

	private final Map<ResourceKey<SourceTrait>, ISourceTraitValue> artificialTraitsMap;

	public SourceBreedingRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.source_breeding", createDrawableStack(guiHelper, new ItemStack(ECBlocks.SOURCE_BREEDER.get())), guiHelper.createBlankDrawable(67, 80));
		setOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/source_breeding.png"), 0, 0, 47, 33), 10, 10);

		artificialTraitsMap = getArtificialTraitsMap();
	}

	@Nonnull
	private static Map<ResourceKey<SourceTrait>, ISourceTraitValue> getArtificialTraitsMap() {
		var artificial = ElementalCraftApi.SOURCE_TRAIT_MANAGER.get(SourceTraits.ARTIFICIAL);

		if (artificial != null) {
			var value = artificial.load(new CompoundTag());

			if (value != null) {
				return Collections.singletonMap(SourceTraits.ARTIFICIAL, value);
			}
		}
		return Collections.emptyMap();
	}

	@Nonnull
	@Override
	public RecipeType<SourceBreedingRecipe> getRecipeType() {
		return ECJEIRecipeTypes.SOURCE_BREEDING;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull SourceBreedingRecipe recipe, @Nonnull IFocusGroup focuses) {
		var sourceReceptacleA = ReceptacleHelper.create(recipe.getIngredientElementTypes().getFirst());
        var sourceReceptacleB = ReceptacleHelper.create(recipe.getIngredientElementTypes().getSecond());
		AtomicBoolean natural = new AtomicBoolean(false);
        ForgeRegistries.ITEMS.tags().getTag(ECTags.Items.NATURAL_SOURCE_SEEDS).forEach(item -> {
            if (recipe.getCatalyst().test(new ItemStack(item))) natural.set(true);
        });

		builder.addSlot(RecipeIngredientRole.INPUT, 25, 46).addIngredients(recipe.getCatalyst());

		builder.addSlot(RecipeIngredientRole.CATALYST, 4, 38).addItemStack(sourceReceptacleA);
		builder.addSlot(RecipeIngredientRole.CATALYST, 48, 38).addItemStack(sourceReceptacleB);
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 54).addIngredient(
                ECIngredientTypes.ELEMENT, new IngredientElementType(
                        recipe.getIngredientElementTypes().getFirst(), IngredientElementType.getGaugeValue(recipe.getElementAmount())
                ));
        builder.addSlot(RecipeIngredientRole.INPUT, 48, 54).addIngredient(
                ECIngredientTypes.ELEMENT, new IngredientElementType(
                        recipe.getIngredientElementTypes().getSecond(), IngredientElementType.getGaugeValue(recipe.getElementAmount())
                ));

		builder.addSlot(RecipeIngredientRole.OUTPUT, 25, 2).addItemStack(ReceptacleHelper.create(recipe.getResultElementType(), natural.get() ? Collections.emptyMap() : artificialTraitsMap));
	}
}
