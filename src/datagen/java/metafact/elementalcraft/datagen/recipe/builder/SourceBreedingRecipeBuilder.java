package metafact.elementalcraft.datagen.recipe.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.name.ECNames;
import metafact.elementalcraft.recipe.ECRecipeSerializers;
import metafact.elementalcraft.recipe.SourceBreedingRecipe;

import java.util.function.Consumer;

public class SourceBreedingRecipeBuilder {
    private final ElementType result;
    private Pair<ElementType, ElementType> ingredientElementTypes;
    private int elementAmount;
    private Ingredient catalyst;
    private final RecipeSerializer<?> serializer;

    public SourceBreedingRecipeBuilder(RecipeSerializer<?> serializer, ElementType result) {
        this.serializer = serializer;
        this.result = result;
    }

    public static SourceBreedingRecipeBuilder sourceBreedingRecipe(ElementType result) {
        return new SourceBreedingRecipeBuilder(ECRecipeSerializers.BREEDING.get(), result);
    }

    public SourceBreedingRecipeBuilder withElementAmount(int elementAmount) {
        this.elementAmount = elementAmount;
        return this;
    }

    public SourceBreedingRecipeBuilder setCatalyst(TagKey<Item> tag) {
        return setCatalyst(Ingredient.of(tag));
    }

    public SourceBreedingRecipeBuilder setCatalyst(ItemLike item) {
        return setCatalyst(Ingredient.of(item));
    }

    public SourceBreedingRecipeBuilder setCatalyst(Ingredient ingredient) {
        this.catalyst = ingredient;
        return this;
    }

    public SourceBreedingRecipeBuilder setIngredientElements(ElementType firstType, ElementType secondType) {
        this.ingredientElementTypes = Pair.of(firstType, secondType);
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        String id_addition = this.result.getSerializedName() + "_from_" +
                this.ingredientElementTypes.getFirst().getSerializedName() + "_and_" +
                this.ingredientElementTypes.getSecond().getSerializedName();

        this.save(consumer, ElementalCraftApi.createRL(SourceBreedingRecipe.NAME + '/' + id_addition));
    }

    public void save(Consumer<FinishedRecipe> consumer, String save) {
        this.save(consumer, ElementalCraftApi.createRL(SourceBreedingRecipe.NAME + '/' + save));
    }

    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, this.serializer, this.result, this.ingredientElementTypes, this.catalyst, this.elementAmount));
    }

    public static class Result extends AbstractFinishedRecipe {
        private final ElementType result;
        private final Pair<ElementType, ElementType> ingredientElementTypes;
        private final Ingredient catalyst;
        private final int elementAmount;

        public Result(ResourceLocation id, RecipeSerializer<?> serializer, ElementType result, Pair<ElementType, ElementType> ingredientElementTypes, Ingredient catalyst, int elementAmount) {
            super(id, serializer);
            this.result = result;
            this.ingredientElementTypes = ingredientElementTypes;
            this.catalyst = catalyst;
            this.elementAmount = elementAmount;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty(ECNames.ELEMENT_AMOUNT, this.elementAmount);
            JsonArray ingredientElements = new JsonArray(2);

            ingredientElements.add(ingredientElementTypes.getFirst().getSerializedName());
            ingredientElements.add(ingredientElementTypes.getSecond().getSerializedName());
            json.add(ECNames.INPUT, ingredientElements);

            json.addProperty(ECNames.OUTPUT, this.result.getSerializedName());
            json.add(ECNames.CATALYST, this.catalyst.toJson());
        }
    }
}
