package metafact.elementalcraft.recipe.melting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import metafact.elementalcraft.block.ECBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.name.ECNames;
import metafact.elementalcraft.block.shrine.melting.MeltingShrineBlockEntity;
import metafact.elementalcraft.recipe.ECRecipeSerializers;
import metafact.elementalcraft.recipe.ECRecipeTypes;
import metafact.elementalcraft.recipe.IECRecipe;
import metafact.elementalcraft.tag.ECTags;

import java.util.Optional;

public record MeltingRecipe (
        TagKey<Block> input,
        Fluid result,
        int cooldown,
        int elementAmount,
        float fillingAmount
) implements IECRecipe<Container> {

    public static final String NAME = "melting";

    public boolean matches(@NotNull MeltingRecipeInput recipeInput, @NotNull Level level) {
        return recipeInput.elementAmount() >= elementAmount && recipeInput.state().is(input);
    }

    public boolean matches(MeltingShrineBlockEntity meltingShrine, @NotNull Level level) {
        return matches(new MeltingRecipeInput(
                level.getBlockState(meltingShrine.getTargetPos()),
                meltingShrine.getElementStorage().getElementAmount(),
                meltingShrine.getConsumeAmount()
        ), level);
    }

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        return false;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return ElementalCraftApi.createRL(NAME + "/"+ForgeRegistries.FLUIDS.getKey(this.result).getPath());
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ECRecipeSerializers.MELTING.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ECRecipeTypes.MELTING.get();
    }

    public static class Serializer implements RecipeSerializer<MeltingRecipe> {
        public static final Codec<MeltingRecipe> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                TagKey.codec(Registries.BLOCK).fieldOf(ECNames.INPUT).forGetter(MeltingRecipe::input),
                ForgeRegistries.FLUIDS.getCodec().optionalFieldOf(ECNames.RESULT, Fluids.EMPTY).forGetter(MeltingRecipe::result),
                Codec.INT.fieldOf("cooldown").forGetter(MeltingRecipe::cooldown),
                Codec.INT.fieldOf("element_amount").forGetter(MeltingRecipe::elementAmount),
                Codec.FLOAT.optionalFieldOf("filling_amount", 1000F).forGetter(MeltingRecipe::fillingAmount)
        ).apply(builder, MeltingRecipe::new));


        @Override
        public MeltingRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject recipeJson) {
            JsonObject output = recipeJson.get(ECNames.RESULT).getAsJsonObject();
            Fluid result = ForgeRegistries.FLUIDS.getValue(ElementalCraftApi.createRL(output.get(ECNames.FLUID).getAsString()));
            int cooldown = recipeJson.get(ECNames.COOLDOWN).getAsInt();
            float fillingAmount = recipeJson.get(ECNames.STRENGTH).getAsFloat();
            int elementAmount = recipeJson.get(ECNames.ELEMENT_AMOUNT).getAsInt();
            String tagLocation = recipeJson.get(ECNames.INPUT).getAsString().substring(1);
            TagKey<Block> input = ForgeRegistries.BLOCKS.tags().createTagKey(ElementalCraftApi.createRL(tagLocation));

            return new MeltingRecipe(input, result, cooldown, elementAmount, fillingAmount);
        }

        @Override
        public @Nullable MeltingRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
            return buf.readJsonWithCodec(CODEC);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull MeltingRecipe pRecipe) {
            buf.writeJsonWithCodec(CODEC, pRecipe);
        }
    }
}
