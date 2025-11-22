package metafact.elementalcraft.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
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
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.name.ECNames;
import metafact.elementalcraft.block.source.breeder.SourceBreederBlockEntity;
import metafact.elementalcraft.item.source.receptacle.ReceptacleHelper;

import javax.annotation.Nonnull;

public class SourceBreedingRecipe implements IContainerBlockEntityRecipe<SourceBreederBlockEntity> {

    public static final String NAME = "source_breeding";

    private final Pair<ElementType, ElementType> ingredientElementTypes;
    private final Ingredient catalyst;
    private final ElementType output;
    private final int elementAmount;
    private final ResourceLocation id;

    public SourceBreedingRecipe(
            ResourceLocation id,
            int elementAmount,
            ElementType output,
            Ingredient catalyst,
            Pair<ElementType, ElementType> ingredientElementTypes
    ) {
        this.id = id;
        this.elementAmount = elementAmount;
        this.output = output;
        this.catalyst = catalyst;
        this.ingredientElementTypes = ingredientElementTypes;
    }

    @Override
    public boolean matches(@Nonnull SourceBreederBlockEntity inv, @Nonnull Level level) {
        return catalyst.test(inv.getInventory().getItem(0))
                && inv.getActiveWrappers().size() == 2
                && inv.getActiveWrappers().stream().allMatch(SourceBreederBlockEntity.PedestalWrapper::hasSource)
                && (
                    inv.getActiveWrappers().get(0).getElementType().equals(ingredientElementTypes.getFirst())
                    || inv.getActiveWrappers().get(1).getElementType().equals(ingredientElementTypes.getFirst())
                )
                && (
                    inv.getActiveWrappers().get(0).getElementType().equals(ingredientElementTypes.getSecond())
                    || inv.getActiveWrappers().get(1).getElementType().equals(ingredientElementTypes.getSecond())
                );
    }

    @Nonnull
    @Override
    public ItemStack getResultItem(@Nonnull RegistryAccess registry) {
        return ReceptacleHelper.create(output);
    }

    public ElementType getResultElementType() {
        return output;
    }

    public Ingredient getCatalyst() {
        return catalyst;
    }

    public Pair<ElementType, ElementType> getIngredientElementTypes() {
        return ingredientElementTypes;
    }

    public int getElementAmount() {
        return elementAmount;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ECRecipeSerializers.BREEDING.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ECRecipeTypes.BREEDING.get();
    }

    public static class Serializer implements RecipeSerializer<SourceBreedingRecipe> {

        @Nonnull
        @Override
        public SourceBreedingRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            int elementAmount = GsonHelper.getAsInt(json, ECNames.ELEMENT_AMOUNT);
            JsonArray ingredientElements = GsonHelper.getAsJsonArray(json, ECNames.INPUT);
            Pair<ElementType, ElementType> ingredientElementTypes = Pair.of(
                    ElementType.byName(ingredientElements.get(0).getAsString()),
                    ElementType.byName(ingredientElements.get(1).getAsString())
            );
            ElementType output = ElementType.byName(GsonHelper.getAsString(json, ECNames.OUTPUT));
            Ingredient catalyst = RecipeHelper.deserializeIngredient(json, ECNames.CATALYST);

            return new SourceBreedingRecipe(recipeId, elementAmount, output, catalyst, ingredientElementTypes);
        }

        @Override
        public SourceBreedingRecipe fromNetwork(@Nonnull ResourceLocation recipeId, FriendlyByteBuf buffer) {
            ElementType output = ElementType.byName(buffer.readUtf());
            Pair<ElementType, ElementType> ingredientElementTypes = Pair.of(
                    ElementType.byName(buffer.readUtf()),
                    ElementType.byName(buffer.readUtf())
            );
            int elementAmount = buffer.readInt();
            Ingredient catalyst = Ingredient.fromNetwork(buffer);

            return new SourceBreedingRecipe(recipeId, elementAmount, output, catalyst, ingredientElementTypes);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SourceBreedingRecipe recipe) {
            buffer.writeUtf(recipe.output.getSerializedName());
            buffer.writeUtf(recipe.ingredientElementTypes.getFirst().getSerializedName());
            buffer.writeUtf(recipe.ingredientElementTypes.getSecond().getSerializedName());
            buffer.writeInt(recipe.elementAmount);
            recipe.catalyst.toNetwork(buffer);
        }
    }
}
