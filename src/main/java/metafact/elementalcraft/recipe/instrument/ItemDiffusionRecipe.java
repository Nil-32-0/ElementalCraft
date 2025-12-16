package metafact.elementalcraft.recipe.instrument;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.name.ECNames;
import metafact.elementalcraft.block.instrument.itemdiffuser.ItemDiffuserBlockEntity;
import metafact.elementalcraft.recipe.ECRecipeSerializers;
import metafact.elementalcraft.recipe.ECRecipeTypes;
import metafact.elementalcraft.recipe.RecipeHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.*;

public class ItemDiffusionRecipe extends AbstractInstrumentRecipe<ItemDiffuserBlockEntity> {

    public static final String NAME = "item_diffusion";

    private final Ingredient ingredient;
    private final Map<ElementType, Integer> results;
    private final int catalystAmount;

    public ItemDiffusionRecipe(ResourceLocation id, ElementType catalystType, int catalystAmount, @NotNull List<Pair<ElementType, Integer>> results, Ingredient ingredient) {
        super(id, catalystType);
        this.catalystAmount = catalystAmount;
        this.results = new HashMap<>();
        for (Pair<ElementType, Integer> pair : results) {
            this.results.put(pair.getFirst(), pair.getSecond());
        }
        this.ingredient = ingredient;
    }

    public ItemDiffusionRecipe(ResourceLocation id, int catalystAmount, List<Pair<ElementType, Integer>> results, Ingredient ingredient) {
        this(id, ElementType.ENTROPY, catalystAmount, results, ingredient);
    }

    public Ingredient getInput() {
        return ingredient;
    }

    public List<Pair<ElementType, Integer>> getResults() {
        List<Pair<ElementType, Integer>> result = new ArrayList<>(results.size());
        for (ElementType key : results.keySet()) {
            result.add(new Pair<>(key, results.get(key)));
        }
        result.sort(Comparator.comparingInt(Pair::getSecond));
        Collections.reverse(result);
        return result;
    }

    @Override
    public int getElementAmount() {
        return catalystAmount;
    }

    @Override
    public boolean matches(@Nonnull ItemDiffuserBlockEntity diffuser, @Nonnull Level level) {
        ItemStack input = diffuser.getInventory().getItem(0);
        if (
            diffuser.getContainerElementType() == getElementType() &&
            !input.isEmpty() &&
            diffuser.numValidContainers() >= results.size()
        ) {
            return ingredient.test(input);
        }
        return false;
    }

    @Nonnull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, getInput());
    }

    @Nonnull
    @Override
    public ItemStack getResultItem(@Nonnull RegistryAccess registry) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public RecipeType<?> getType() {
        return ECRecipeTypes.ITEM_DIFFUSION.get();
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ECRecipeSerializers.ITEM_DIFFUSION.get();
    }

    public static class Serializer implements RecipeSerializer<ItemDiffusionRecipe> {

        @Nonnull
        @Override
        public ItemDiffusionRecipe fromJson(@Nonnull ResourceLocation resourceId, @Nonnull JsonObject json) {
            ElementType catalyst = ElementType.byName(GsonHelper.getAsString(json, ECNames.ELEMENT_TYPE));
            int elementAmount = GsonHelper.getAsInt(json, ECNames.ELEMENT_AMOUNT);
            Ingredient input = RecipeHelper.deserializeIngredient(json, ECNames.INPUT);
            JsonArray results = GsonHelper.getAsJsonArray(json, ECNames.OUTPUTS);
            List<Pair<ElementType, Integer>> resultTypes = new LinkedList<>();
            for (JsonElement element : results) {
                ElementType type = ElementType.byName(GsonHelper.getAsString((JsonObject) element, ECNames.ELEMENT_TYPE));
                Integer amount = GsonHelper.getAsInt((JsonObject) element, ECNames.ELEMENT_AMOUNT);
                resultTypes.add(new Pair<>(type, amount));
            }

            return new ItemDiffusionRecipe(resourceId, catalyst, elementAmount, resultTypes, input);
        }

        @Nonnull
        @Override
        public ItemDiffusionRecipe fromNetwork(@Nonnull ResourceLocation resourceId, FriendlyByteBuf buffer) {
            ElementType type = ElementType.byName(buffer.readUtf());
            int elementAmount = buffer.readInt();
            Ingredient input = Ingredient.fromNetwork(buffer);

            int i = buffer.readInt();
            NonNullList<Pair<ElementType, Integer>> results = NonNullList.withSize(i, new Pair<>(ElementType.NONE, 0));

            for (int j = 0; j < i; ++j) {
                results.set(j, new Pair<>(ElementType.byName(buffer.readUtf()), buffer.readInt()));
            }

            return new ItemDiffusionRecipe(resourceId, type, elementAmount, results, input);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ItemDiffusionRecipe recipe) {
            buffer.writeUtf(recipe.getElementType().getSerializedName());
            buffer.writeInt(recipe.getElementAmount());
            recipe.getInput().toNetwork(buffer);
            buffer.writeInt(recipe.getResults().size());
            recipe.getResults().forEach(r -> {
                buffer.writeUtf(r.getFirst().getSerializedName());
                buffer.writeInt(r.getSecond());
            });
        }
    }
}
