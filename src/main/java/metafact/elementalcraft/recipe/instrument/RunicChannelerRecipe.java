package metafact.elementalcraft.recipe.instrument;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.name.ECNames;
import metafact.elementalcraft.api.rune.Rune;
import metafact.elementalcraft.block.instrument.runicchanneler.RunicChannelerBlockEntity;
import metafact.elementalcraft.recipe.ECRecipeSerializers;
import metafact.elementalcraft.recipe.ECRecipeTypes;
import metafact.elementalcraft.recipe.RecipeHelper;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class RunicChannelerRecipe extends AbstractInstrumentRecipe<RunicChannelerBlockEntity> {

    public static final String NAME = "runic_channeler";

    private final Ingredient ingredient;
    private final ItemStack result;
    private final int elementAmount;
    private final List<ResourceLocation> runes;

    public RunicChannelerRecipe(ResourceLocation id, ElementType type, int elementAmount, ItemStack result, Ingredient ingredient, List<ResourceLocation> runes) {
        super(id, type);
        this.elementAmount = elementAmount;
        this.ingredient = ingredient;
        this.result = result;
        this.runes = runes;
    }

    @Override
    public int getElementAmount() {
        return elementAmount;
    }

    @Override
    public boolean matches(@Nonnull RunicChannelerBlockEntity channeler, @Nonnull Level level) {
        ItemStack stack = channeler.getInventory().getItem(0);

        if (channeler.getElementType() == getElementType() && !stack.isEmpty()) {
            return ingredient.test(stack) && channeler.getRuneHandler().getRunes().equals(getRunesAsRunes());
        }
        return false;
    }

    @Nonnull
	@Override
	public ItemStack getResultItem(@Nonnull RegistryAccess registry) {
		return result;
	}

    public List<Rune> getRunesAsRunes() {
        Rune[] realRunes = new Rune[this.runes.size()];
        for (int i = 0; i < this.runes.size(); i++) {
            realRunes[i] = ElementalCraftApi.RUNE_MANAGER.get(this.runes.get(i));
        }
        return List.of(realRunes);
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public List<ResourceLocation> getRunes() {
        return this.runes;
    }

    @Nonnull
	@Override
	public RecipeType<?> getType() {
		return ECRecipeTypes.RUNIC_CHANNELER.get();
	}

    @Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializers.RUNIC_CHANNELER.get();
	}

    public static class Serializer implements RecipeSerializer<RunicChannelerRecipe> {
        @Nonnull
        @Override
        public RunicChannelerRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            ElementType type = ElementType.byName(GsonHelper.getAsString(json, ECNames.ELEMENT_TYPE));
            int elementAmount = GsonHelper.getAsInt(json, ECNames.ELEMENT_AMOUNT);
            Ingredient ingredient = RecipeHelper.deserializeIngredient(json, ECNames.INPUT);
            ItemStack result = RecipeHelper.readRecipeOutput(json, ECNames.OUTPUT);

            JsonArray runeArray = GsonHelper.getAsJsonArray(json, ECNames.RUNE_LIST);
            ResourceLocation[] tempRunes = new ResourceLocation[runeArray.size()];

            for (int i = 0; i < runeArray.size(); i++) {
                tempRunes[i] = new ResourceLocation(runeArray.get(i).getAsString());
            }

            List<ResourceLocation> runes = List.of(tempRunes);

            return new RunicChannelerRecipe(recipeId, type, elementAmount, result, ingredient, runes);
        }

        @Override
        public RunicChannelerRecipe fromNetwork(@Nonnull ResourceLocation recipeId, FriendlyByteBuf buffer) {
            ElementType type = ElementType.byName(buffer.readUtf());
            int elementAmount = buffer.readInt();
            ItemStack result = buffer.readItem();
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            
            int runeCount = buffer.readInt();
            ResourceLocation[] tempRunes = new ResourceLocation[runeCount];
            for (int i = 0; i < runeCount; i++) {
                tempRunes[i] = new ResourceLocation(buffer.readUtf());
            }
            List<ResourceLocation> runes = List.of(tempRunes);

            return new RunicChannelerRecipe(recipeId, type, elementAmount, result, ingredient, runes);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RunicChannelerRecipe recipe) {
            buffer.writeUtf(recipe.getElementType().getSerializedName());
            buffer.writeInt(recipe.getElementAmount());
            buffer.writeItem(recipe.result);
            recipe.ingredient.toNetwork(buffer);
            buffer.writeInt(recipe.runes.size());
            recipe.runes.forEach(rune -> buffer.writeUtf(rune.toString()));
        }
    }
}
