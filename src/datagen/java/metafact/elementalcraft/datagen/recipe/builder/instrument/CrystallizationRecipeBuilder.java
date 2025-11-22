package metafact.elementalcraft.datagen.recipe.builder.instrument;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.name.ECNames;
import metafact.elementalcraft.datagen.recipe.builder.AbstractFinishedRecipe;
import metafact.elementalcraft.recipe.ECRecipeSerializers;
import metafact.elementalcraft.recipe.instrument.CrystallizationRecipe;

import java.util.List;
import java.util.function.Consumer;

public class CrystallizationRecipeBuilder {

	private final Item result;
	private final List<Ingredient> ingredients = Lists.newArrayList(Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY);
	private final ElementType elementType;
	private int elementAmount;
	private final RecipeSerializer<?> serializer;

	public CrystallizationRecipeBuilder(RecipeSerializer<?> serializer, ItemLike result, ElementType elementType) {
		this.serializer = serializer;
		this.elementType = elementType;
		elementAmount = 5000;
		this.result = result.asItem();
	}

	public static CrystallizationRecipeBuilder crystallizationRecipe(ItemLike result, ElementType elementType) {
		return new CrystallizationRecipeBuilder(ECRecipeSerializers.CRYSTALLIZATION.get(), result, elementType);
	}

	public CrystallizationRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public CrystallizationRecipeBuilder setGem(ItemLike item) {
		return this.setIngredient(0, item);
	}

	public CrystallizationRecipeBuilder setCrystal(ItemLike item) {
		return this.setIngredient(1, item);
	}

	private CrystallizationRecipeBuilder setIngredient(int index, TagKey<Item> tag) {
		return this.setIngredient(index, Ingredient.of(tag));
	}

	private CrystallizationRecipeBuilder setIngredient(int index, ItemLike item) {
		return this.setIngredient(index, Ingredient.of(item));
	}

	private CrystallizationRecipeBuilder setIngredient(int index, Ingredient ingredient) {
		this.ingredients.set(index, ingredient);
		return this;
	}

	public void save(Consumer<FinishedRecipe> consumer, String save) {
        ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
        if ((new ResourceLocation(save)).equals(resourcelocation)) {
            throw new IllegalStateException("Crystallization Recipe " + save + " should remove its 'save' argument");
        } else {
            this.save(consumer, ElementalCraftApi.createRL(CrystallizationRecipe.NAME + '/' + save));
        }
	}

	public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
		consumer.accept(new Result(id, this.serializer, this.ingredients, new ItemStack(this.result), elementType, elementAmount));
	}

	public static class Result extends AbstractFinishedRecipe {
		private final List<Ingredient> ingredients;
        private final ItemStack result;
		private final ElementType elementType;
		private final int elementAmount;

		public Result(ResourceLocation id, RecipeSerializer<?> serializer, List<Ingredient> ingredients, ItemStack result, ElementType elementType, int elementAmount) {
			super(id, serializer);
			this.ingredients = ingredients;
			this.result = result;
			this.elementType = elementType;
			this.elementAmount = elementAmount;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty(ECNames.ELEMENT_TYPE, this.elementType.getSerializedName());
			json.addProperty(ECNames.ELEMENT_AMOUNT, elementAmount);
			JsonObject ingredientsJson = new JsonObject();
            JsonObject outputJson = new JsonObject();

			ingredientsJson.add(ECNames.GEM, this.ingredients.get(0).toJson());
			ingredientsJson.add(ECNames.CRYSTAL, this.ingredients.get(1).toJson());
            outputJson.addProperty(ECNames.ITEM, ForgeRegistries.ITEMS.getKey(this.result.getItem()).toString());

			json.add(ECNames.INGREDIENTS, ingredientsJson);
            json.add(ECNames.OUTPUT, outputJson);
		}
	}
}
