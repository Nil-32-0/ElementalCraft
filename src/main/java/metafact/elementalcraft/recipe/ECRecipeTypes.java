package metafact.elementalcraft.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.recipe.instrument.CrystallizationRecipe;
import metafact.elementalcraft.recipe.instrument.InscriptionRecipe;
import metafact.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import metafact.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;
import metafact.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;
import metafact.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;
import metafact.elementalcraft.recipe.melting.MeltingRecipe;

public class ECRecipeTypes {
	private static final DeferredRegister<RecipeType<?>> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ElementalCraftApi.MODID);

	public static final RegistryObject<RecipeType<IInfusionRecipe>> INFUSION = register(IInfusionRecipe.NAME);
	public static final RegistryObject<RecipeType<AbstractBindingRecipe>> BINDING = register(AbstractBindingRecipe.NAME);
	public static final RegistryObject<RecipeType<CrystallizationRecipe>> CRYSTALLIZATION = register(CrystallizationRecipe.NAME);
	public static final RegistryObject<RecipeType<InscriptionRecipe>> INSCRIPTION = register(InscriptionRecipe.NAME);
	public static final RegistryObject<RecipeType<IGrindingRecipe>> GRINDING = register(IGrindingRecipe.NAME);
	public static final RegistryObject<RecipeType<SawingRecipe>> SAWING = register(SawingRecipe.NAME);
	public static final RegistryObject<RecipeType<PureInfusionRecipe>> PURE_INFUSION = register(PureInfusionRecipe.NAME);
	public static final RegistryObject<RecipeType<SpellCraftRecipe>> SPELL_CRAFT = register(SpellCraftRecipe.NAME);
    public static final RegistryObject<RecipeType<MeltingRecipe>> MELTING = register(MeltingRecipe.NAME);
    public static final RegistryObject<RecipeType<SourceBreedingRecipe>> BREEDING = register(SourceBreedingRecipe.NAME);


	private ECRecipeTypes() {}

	private static <T extends Recipe<?>> RegistryObject<RecipeType<T>> register(String name) {
		return DEFERRED_REGISTER.register(name, () -> new RecipeType<>() {
			@Override
			public String toString() {
				return name;
			}
		});
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}
}
