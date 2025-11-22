package metafact.elementalcraft.datagen.recipe.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import metafact.elementalcraft.tag.ECTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.name.ECNames;
import metafact.elementalcraft.recipe.ECRecipeSerializers;
import metafact.elementalcraft.recipe.melting.MeltingRecipe;

import java.lang.reflect.Constructor;
import java.util.function.Consumer;

public class MeltingRecipeBuilder {
    private final TagKey<Block> input;
    private final Fluid result;
    private int cooldown;
    private int elementAmount;
    private float fillingAmount;

    private MeltingRecipeBuilder(TagKey<Block> input, Fluid result) {
        this.input = input;
        this.result = result;
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
        recipeConsumer.accept(new Result(id, ECRecipeSerializers.MELTING.get(), this.input, this.result, this.elementAmount, this.cooldown, this.fillingAmount));
    }

    public static class Result extends AbstractFinishedRecipe {
        private final TagKey<Block> input;
        private final Fluid result;
        private final int elementAmount;
        private final int cooldown;
        private final float fillingAmount;

        public Result(ResourceLocation id, RecipeSerializer<?> serializer, TagKey<Block> input, Fluid result, int elementAmount, int cooldown, float fillingAmount) {
            super(id, serializer);
            this.input = input;
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
            json.addProperty(ECNames.INPUT, '#'+input.location().getPath());

            JsonObject output = new JsonObject();
            output.addProperty(ECNames.FLUID, ForgeRegistries.FLUIDS.getKey(result).toString());

            json.add(ECNames.RESULT, output);
        }
    }
}
