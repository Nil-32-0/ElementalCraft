package sirttas.elementalcraft.datagen.recipe.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.melting.MeltingRecipe;

import java.util.function.Consumer;

public class MeltingRecipeBuilder {
    private final HolderSet<Block> input;
    private final TagKey<Block> inputTag;
    private final Fluid result;
    private int cooldown;
    private int elementAmount;
    private float fillingAmount;
    private final RecipeSerializer<?> serializer;

    private MeltingRecipeBuilder(TagKey<Block> input, RecipeSerializer<?> serializer, Fluid result) {
        this.input = null;
        this.inputTag = input;
        this.result = result;
        this.serializer = serializer;
    }

    private MeltingRecipeBuilder(HolderSet<Block> input, RecipeSerializer<?> serializer, Fluid result) {
        this.input = input;
        this.inputTag = null;
        this.result = result;
        this.serializer = serializer;
    }

    public MeltingRecipeBuilder(TagKey<Block> input, Fluid result) {
        this(input, ECRecipeSerializers.MELTING.get(), result);
    }

    public static MeltingRecipeBuilder melting(TagKey<Block> input, Fluid result) {
        return new MeltingRecipeBuilder(input, result);
    }

    public MeltingRecipeBuilder cooldown(int cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public MeltingRecipeBuilder elementAmount(int elementAmount) {
        this.elementAmount = elementAmount;
        return this;
    }

    public MeltingRecipeBuilder fillingAmount(float fillingAmount) {
        this.fillingAmount = fillingAmount;
        return this;
    }

    public void save(Consumer<FinishedRecipe> recipeConsumer) {
        var id = ForgeRegistries.FLUIDS.getKey(this.result);

        this.save(recipeConsumer, ElementalCraftApi.createRL(MeltingRecipe.NAME + "/" + id.getPath()));
    }

    public void save(Consumer<FinishedRecipe> recipeConsumer, String save) {
        var resourcelocation = ForgeRegistries.FLUIDS.getKey(this.result);

        if (ResourceLocation.tryParse(save).equals(resourcelocation)) {
            throw new IllegalStateException("Melting Recipe " + save + " should remove its 'save' argument");
        } else {
            this.save(recipeConsumer, ElementalCraftApi.createRL(MeltingRecipe.NAME + '/' + save));
        }
    }

    public void save(Consumer<FinishedRecipe> recipeConsumer, ResourceLocation id) {
        recipeConsumer.accept(new Result(id, this.serializer, this.input, this.inputTag, this.result, this.elementAmount, this.cooldown, this.fillingAmount));
    }

    public static class Result extends AbstractFinishedRecipe {
        private final HolderSet<Block> input;
        private final TagKey<Block> inputTag;
        private final Fluid result;
        private final int elementAmount;
        private final int cooldown;
        private final float fillingAmount;

        public Result(ResourceLocation id, RecipeSerializer<?> serializer, HolderSet<Block> input, TagKey<Block> inputTag, Fluid result, int elementAmount, int cooldown, float fillingAmount) {
            super(id, serializer);
            this.input = input;
            this.inputTag = inputTag;
            this.result = result;
            this.elementAmount = elementAmount;
            this.cooldown = cooldown;
            this.fillingAmount = fillingAmount;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty(ECNames.ELEMENT_AMOUNT, elementAmount);
            json.addProperty(ECNames.COOLDOWN, cooldown);
            json.addProperty(ECNames.STRENGTH, fillingAmount);

            if (input != null) {
                JsonArray jsonArray = new JsonArray();

                input.stream().map(Holder::get).forEach(block -> {
                    var blockResult = ForgeRegistries.BLOCKS.getCodec().encodeStart(JsonOps.INSTANCE, block).result();
                    blockResult.ifPresent(jsonArray::add);
                });

                json.add(ECNames.INPUT, jsonArray);
            }
            if (inputTag != null) {
                JsonObject jsonObject = new JsonObject();

                jsonObject.addProperty(ECNames.TAG, inputTag.location().getPath());

                json.add(ECNames.INPUT, jsonObject);
            }

            JsonObject output = new JsonObject();

            output.addProperty(ECNames.FLUID, ForgeRegistries.FLUIDS.getKey(result).toString());
            json.add(ECNames.RESULT, output);
        }
    }
}
