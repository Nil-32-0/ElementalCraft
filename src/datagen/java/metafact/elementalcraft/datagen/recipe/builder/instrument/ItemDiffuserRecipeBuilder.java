package metafact.elementalcraft.datagen.recipe.builder.instrument;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.name.ECNames;
import metafact.elementalcraft.datagen.recipe.builder.AbstractFinishedRecipe;
import metafact.elementalcraft.recipe.ECRecipeSerializers;
import metafact.elementalcraft.recipe.instrument.ItemDiffusionRecipe;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Consumer;

public class ItemDiffuserRecipeBuilder {

    private final ResourceLocation key;
    private final ElementType catalystType;
    private int catalystAmount;
    private Ingredient input;
    private final Map<ElementType, Integer> results = new HashMap<>();
    private final RecipeSerializer<?> serializer;

    public ItemDiffuserRecipeBuilder(RecipeSerializer<?> serializer, ElementType catalystType, ResourceLocation key) {
        this.serializer = serializer;
        this.catalystType = catalystType;
        this.catalystAmount = 5000;
        this.key = key;
    }

    public static ItemDiffuserRecipeBuilder itemDiffuserRecipe(ElementType catalystType, TagKey<Item> tag) {
        ItemDiffuserRecipeBuilder builder = new ItemDiffuserRecipeBuilder(
                ECRecipeSerializers.ITEM_DIFFUSION.get(),
                catalystType,
                ElementalCraftApi.createRL(ItemDiffusionRecipe.NAME + "/" +
                        catalystType.getSerializedName() + "_catalyzed_" +
                        tag.location().getPath().replace("/", "_") + "_tag"
                )
        );
        return builder.withInput(tag);
    }

    public static ItemDiffuserRecipeBuilder itemDiffuserRecipe(ElementType catalystType, ItemLike input) {
        ItemDiffuserRecipeBuilder builder = new ItemDiffuserRecipeBuilder(
                ECRecipeSerializers.ITEM_DIFFUSION.get(),
                catalystType,
                ElementalCraftApi.createRL(ItemDiffusionRecipe.NAME + "/" +
                        catalystType.getSerializedName() + "_catalyzed_" +
                        ForgeRegistries.ITEMS.getKey(input.asItem()).getPath()
                )
        );
        return builder.withInput(input);
    }

    public ItemDiffuserRecipeBuilder withElementAmount(int amount) {
        this.catalystAmount = amount;
        return this;
    }

    public ItemDiffuserRecipeBuilder withInput(TagKey<Item> input) {
        return this.withInput(Ingredient.of(input));
    }

    public ItemDiffuserRecipeBuilder withInput(ItemLike input) {
        return this.withInput(Ingredient.of(input));
    }

    public ItemDiffuserRecipeBuilder withInput(Ingredient input) {
        this.input = input;
        return this;
    }

    public ItemDiffuserRecipeBuilder withResult(ElementType type, int amount) {
        results.put(type, amount);
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        save(consumer, this.key);
    }

    public void save(Consumer<FinishedRecipe> consumer, String save) {
        ResourceLocation newKey = ElementalCraftApi.createRL(ItemDiffusionRecipe.NAME + "/" + save);

        if (newKey.equals(this.key)) {
            throw new IllegalStateException("Item Diffusion Recipe " + save + " should remove its 'save' argument");
        } else {
            this.save(consumer, newKey);
        }
    }

    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, this.serializer, this.catalystType, this.catalystAmount, this.input, this.results));
    }

    public static class Result extends AbstractFinishedRecipe {
        private final ElementType catalystType;
        private final int catalystAmount;
        private final Ingredient input;
        private final Map<ElementType, Integer> outputs;

        public Result(ResourceLocation id, RecipeSerializer<?> serializer, ElementType catalystType, int catalystAmount, Ingredient input, Map<ElementType, Integer> results) {
            super(id, serializer);
            this.catalystType = catalystType;
            this.catalystAmount = catalystAmount;
            this.input = input;
            this.outputs = results;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty(ECNames.ELEMENT_TYPE, catalystType.getSerializedName());
            json.addProperty(ECNames.ELEMENT_AMOUNT, catalystAmount);
            json.add(ECNames.INPUT, input.toJson());
            JsonArray results = new JsonArray();

            for (ElementType key : outputs.keySet()) {
                JsonObject result = new JsonObject();
                result.addProperty(ECNames.ELEMENT_TYPE, key.getSerializedName());
                result.addProperty(ECNames.ELEMENT_AMOUNT, outputs.get(key));
                results.add(result);
            }

            json.add(ECNames.OUTPUTS, results);
        }
    }
}
