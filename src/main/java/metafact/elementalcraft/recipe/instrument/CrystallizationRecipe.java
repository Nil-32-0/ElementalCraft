package metafact.elementalcraft.recipe.instrument;

import com.google.gson.JsonObject;
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
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.name.ECNames;
import metafact.elementalcraft.block.instrument.crystallizer.CrystallizerBlockEntity;
import metafact.elementalcraft.recipe.ECRecipeSerializers;
import metafact.elementalcraft.recipe.ECRecipeTypes;
import metafact.elementalcraft.recipe.RecipeHelper;

import javax.annotation.Nonnull;
import java.util.List;

public class CrystallizationRecipe extends AbstractInstrumentRecipe<CrystallizerBlockEntity> {

	public static final String NAME = "crystallization";
	
	private final NonNullList<Ingredient> ingredients;
	private final ItemStack result;
	private final int elementAmount;

	public CrystallizationRecipe(ResourceLocation id, ElementType type, int elementAmount, ItemStack result, List<Ingredient> ingredients) {
		super(id, type);
		this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients.toArray(Ingredient[]::new));
		this.result = result;
		this.elementAmount = elementAmount;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public boolean matches(@Nonnull CrystallizerBlockEntity crystallizer, @Nonnull Level level) {
		if (crystallizer.getContainerElementType() == getElementType() && crystallizer.getItemCount() >= 2) {
			for (int i = 0; i < 2; i++) {
				if (!ingredients.get(i).test(crystallizer.getInventory().getItem(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Nonnull
	@Override
	public ItemStack getResultItem(@Nonnull RegistryAccess registry) {
		return result;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Nonnull
	@Override
	public RecipeType<?> getType() {
		return ECRecipeTypes.CRYSTALLIZATION.get();
	}
	
	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializers.CRYSTALLIZATION.get();
	}
	
	public static class Serializer implements RecipeSerializer<CrystallizationRecipe> {

		@Nonnull
		@Override
		public CrystallizationRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			ElementType type = ElementType.byName(GsonHelper.getAsString(json, ECNames.ELEMENT_TYPE));
			int elementAmount = GsonHelper.getAsInt(json, ECNames.ELEMENT_AMOUNT);
			NonNullList<Ingredient> ingredients = readIngredients(GsonHelper.getAsJsonObject(json, ECNames.INGREDIENTS));
            ItemStack result = RecipeHelper.readRecipeOutput(json, ECNames.OUTPUT);
			
			return new CrystallizationRecipe(recipeId, type, elementAmount, result, ingredients);
		}

		public static NonNullList<Ingredient> readIngredients(JsonObject json) {
			NonNullList<Ingredient> list = NonNullList.create();

			list.add(RecipeHelper.deserializeIngredient(json, ECNames.GEM));
			list.add(RecipeHelper.deserializeIngredient(json, ECNames.CRYSTAL));
			return list;
		}

		@Override
		public CrystallizationRecipe fromNetwork(@Nonnull ResourceLocation recipeId, FriendlyByteBuf buffer) {
			ElementType type = ElementType.byName(buffer.readUtf());
			int elementAmount = buffer.readInt();
            ItemStack result = buffer.readItem();
			
			int i = buffer.readInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < i; ++j) {
				ingredients.set(j, Ingredient.fromNetwork(buffer));
			}

			return new CrystallizationRecipe(recipeId, type, elementAmount, result, ingredients);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, CrystallizationRecipe recipe) {
			buffer.writeUtf(recipe.getElementType().getSerializedName());
			buffer.writeInt(recipe.getElementAmount());
            buffer.writeItem(recipe.result);
			buffer.writeInt(recipe.getIngredients().size());
			recipe.getIngredients().forEach(ingredient -> ingredient.toNetwork(buffer));
		}
	}
}
