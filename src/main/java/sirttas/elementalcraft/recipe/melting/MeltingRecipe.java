package sirttas.elementalcraft.recipe.melting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import metafact.dpanvil_m.api.codec.CodecHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
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
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.melting.MeltingShrineBlockEntity;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.IECRecipe;
import sirttas.elementalcraft.tag.ECTags;

import java.util.Optional;

public record MeltingRecipe (
        HolderSet<Block> input,
        Fluid result,
        int cooldown,
        int elementAmount,
        float fillingAmount
) implements IECRecipe<Container> {

    public static final String NAME = "melting";

    public MeltingRecipe(TagKey<Block> blockTag, Fluid result, int cooldown, int elementAmount, float fillingAmount) {
        this(
                ECTags.Blocks.getTag(blockTag).stream().toList().isEmpty() ?
                        HolderSet.direct(Holder.direct(Blocks.STONE)) : ECTags.Blocks.getTag(blockTag),
                result, cooldown, elementAmount, fillingAmount
        );
        if (ECTags.Blocks.getTag(blockTag).stream().toList().isEmpty())
            ElementalCraftApi.LOGGER.warn("Recipes failed to load for melting shrine recipe {}, " +
                    "and defaulted to stone. Reload to fix this!", getId());
    }

    public boolean matches(@NotNull MeltingRecipeInput recipeInput, @NotNull Level level) {
        return recipeInput.elementAmount() >= elementAmount && input.contains(Holder.direct(recipeInput.state().getBlock()));
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
                RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf(ECNames.INPUT).forGetter(MeltingRecipe::input),
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

            JsonElement input = recipeJson.get(ECNames.INPUT);
            if (input.isJsonArray()) {
                HolderSet<Block> inputs = HolderSet.direct(input.getAsJsonArray().asList().stream()
                        .map(element -> ForgeRegistries.BLOCKS.getCodec().decode(JsonOps.INSTANCE, element))
                        .map(DataResult::result).filter(Optional::isPresent).map(Optional::get)
                        .map(pair -> Holder.direct(pair.getFirst())).toList());
                return new MeltingRecipe(inputs, result, cooldown, elementAmount, fillingAmount);
            }
            if (input.isJsonObject()) {
                TagKey<Block> inputTag = BlockTags.create(ElementalCraftApi.createRL(input.getAsJsonObject().get(ECNames.TAG).getAsString()));
                return new MeltingRecipe(inputTag, result, cooldown, elementAmount, fillingAmount);
            }
            throw new IllegalStateException("Error: Loading of recipe '"+ id +"' failed");
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
