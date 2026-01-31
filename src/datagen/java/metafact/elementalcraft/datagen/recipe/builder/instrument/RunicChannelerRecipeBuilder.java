package metafact.elementalcraft.datagen.recipe.builder.instrument;

import java.util.List;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.name.ECNames;
import metafact.elementalcraft.api.rune.Rune;
import metafact.elementalcraft.datagen.recipe.builder.AbstractFinishedRecipe;
import metafact.elementalcraft.recipe.ECRecipeSerializers;
import metafact.elementalcraft.recipe.instrument.RunicChannelerRecipe;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public class RunicChannelerRecipeBuilder {
    private final Item result;
    private final ElementType elementType;
    private Ingredient ingredient;
    private int elementAmount;
    private List<ResourceLocation> runes;

    private final RecipeSerializer<?> serializer;

    public RunicChannelerRecipeBuilder(RecipeSerializer<?> serializer, ItemLike result, ElementType elementType) {
        this.serializer = serializer;
        this.elementType = elementType;
        this.elementAmount = 500;
        this.result = result.asItem();
    }

    public static RunicChannelerRecipeBuilder runicChannelerRecipe(ItemLike result, ElementType elementType) {
        return new RunicChannelerRecipeBuilder(ECRecipeSerializers.RUNIC_CHANNELER.get(), result, elementType);
    }

    public RunicChannelerRecipeBuilder withElementAmount(int elementAmount) {
        this.elementAmount = elementAmount;
        return this;
    }

    public RunicChannelerRecipeBuilder setRunes(List<ResourceLocation> runes) {
        this.runes = runes;
        return this;
    }

    public RunicChannelerRecipeBuilder setRunesKeys(List<ResourceKey<Rune>> runes) {
        List<ResourceLocation> locationList = runes.stream().map(ResourceKey::location).toList();
        this.runes = locationList;
        return this;
    }

    public RunicChannelerRecipeBuilder setIngredient(TagKey<Item> tag) {
		return this.setIngredient(Ingredient.of(tag));
	}

	public RunicChannelerRecipeBuilder setIngredient(ItemLike item) {
		return this.setIngredient(Ingredient.of(item));
	}

    public RunicChannelerRecipeBuilder setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(this.result);

        this.save(consumer, ElementalCraftApi.createRL(RunicChannelerRecipe.NAME + "/" + id.getPath()));
    }

    public void save(Consumer<FinishedRecipe> consumer, String save) {
        ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);		
        if ((new ResourceLocation(save)).equals(resourcelocation)) {
            throw new IllegalStateException("Runic Channeler Recipe " + save + " should remove its 'save' argument");
        } else {
            this.save(consumer, ElementalCraftApi.createRL(RunicChannelerRecipe.NAME + '/' + save));
        }
	}

	public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
		consumer.accept(new Result(id, this.serializer, new ItemStack(this.result), this.elementType, this.elementAmount, this.ingredient, this.runes));
	}

    public static class Result extends AbstractFinishedRecipe {
        private final Ingredient ingredient;
        private final ItemStack result;
        private final ElementType elementType;
        private final int elementAmount;
        private final List<ResourceLocation> runes;

        public Result(ResourceLocation id, RecipeSerializer<?> serializer, ItemStack result, ElementType elementType, int elementAmount, Ingredient ingredient, List<ResourceLocation> runes) {
            super(id, serializer);
            this.result = result;
            this.elementType = elementType;
            this.elementAmount = elementAmount;
            this.ingredient = ingredient;
            this.runes = runes;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty(ECNames.ELEMENT_TYPE, this.elementType.getSerializedName());
            json.addProperty(ECNames.ELEMENT_AMOUNT, this.elementAmount);
            json.add(ECNames.INPUT, this.ingredient.toJson());

            JsonObject outputJson = new JsonObject();
            outputJson.addProperty(ECNames.ITEM, ForgeRegistries.ITEMS.getKey(this.result.getItem()).toString());
            json.add(ECNames.OUTPUT, outputJson);

            JsonArray runeArray = new JsonArray();
            runes.stream().map(ResourceLocation::toString).forEach(runeArray::add);
            json.add(ECNames.RUNE_LIST, runeArray);
        }
    }
}