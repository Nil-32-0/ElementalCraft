package metafact.elementalcraft.interaction.jei;

import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.shrine.budding.BuddingShrineBlock;
import metafact.elementalcraft.block.shrine.spring.SpringShrineBlock;
import metafact.elementalcraft.interaction.jei.category.element.*;
import metafact.elementalcraft.interaction.jei.category.element.synthesis.*;
import metafact.elementalcraft.interaction.jei.category.element.synthesis.cracking.CrackingRecipeCategory;
import metafact.elementalcraft.interaction.jei.category.element.synthesis.cracking.SculkCrackingRecipeCategory;
import metafact.elementalcraft.interaction.jei.category.instrument.EnchantmentLiquefactionRecipeCategory;
import metafact.elementalcraft.interaction.jei.category.instrument.RunicChannelerRecipeCategory;
import metafact.elementalcraft.interaction.jei.category.instrument.io.PurificationRecipeCategory;
import metafact.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import metafact.elementalcraft.recipe.PureInfusionRecipe;
import metafact.elementalcraft.recipe.SourceBreedingRecipe;
import metafact.elementalcraft.recipe.SpellCraftRecipe;
import metafact.elementalcraft.recipe.instrument.CrystallizationRecipe;
import metafact.elementalcraft.recipe.instrument.InscriptionRecipe;
import metafact.elementalcraft.recipe.instrument.ItemDiffusionRecipe;
import metafact.elementalcraft.recipe.instrument.RunicChannelerRecipe;
import metafact.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import metafact.elementalcraft.recipe.instrument.enchantment.liquefaction.EnchantmentLiquefactionRecipe;
import metafact.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;
import metafact.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;
import metafact.elementalcraft.recipe.instrument.io.IPurifierRecipe;
import metafact.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;
import metafact.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;
import metafact.elementalcraft.recipe.melting.MeltingRecipe;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class ECJEIRecipeTypes {

    private ECJEIRecipeTypes() {}

    public static final RecipeType<ExtractionRecipeCategory.ExtractionRecipe> EXTRACTION = create(ExtractionRecipeCategory.NAME, ExtractionRecipeCategory.ExtractionRecipe.class);
    public static final RecipeType<Ingredient> EVAPORATION = create(EvaporationRecipeCategory.NAME, Ingredient.class);
    public static final RecipeType<RunicChannelerRecipe> RUNIC_CHANNELER = create(RunicChannelerRecipeCategory.NAME, RunicChannelerRecipe.class);
    public static final RecipeType<IngredientElementType> AIR_MILL_SYNTHESIS = create(AirMillSynthesisRecipeCategory.NAME, IngredientElementType.class);
    public static final RecipeType<IJeiFuelingRecipe> COMBUSTION = create(CombustionRecipeCategory.NAME, IJeiFuelingRecipe.class);
    public static final RecipeType<Block> CRACKING = create(CrackingRecipeCategory.NAME, Block.class);
    public static final RecipeType<ItemStack> CULINARY = create(CulinaryRecipeCategory.NAME, ItemStack.class);
    public static final RecipeType<IngredientElementType> DRAINING = create(DrainingRecipeCategory.NAME, IngredientElementType.class);
    public static final RecipeType<Block> SCULK_CRACKING = create(SculkCrackingRecipeCategory.NAME, Block.class);
    public static final RecipeType<IngredientElementType> VIBRATION = create(VibrationRecipeCategory.NAME, IngredientElementType.class);
    public static final RecipeType<Ingredient> SOLAR_SYNTHESIS = create(SolarSynthesisRecipeCategory.NAME, Ingredient.class);
    public static final RecipeType<IInfusionRecipe> INFUSION = create(IInfusionRecipe.NAME, IInfusionRecipe.class);
    public static final RecipeType<IInfusionRecipe> TOOL_INFUSION = create(ToolInfusionRecipe.NAME, IInfusionRecipe.class);
    public static final RecipeType<AbstractBindingRecipe> BINDING = create(AbstractBindingRecipe.NAME, AbstractBindingRecipe.class);
    public static final RecipeType<CrystallizationRecipe> CRYSTALLIZATION = create(CrystallizationRecipe.NAME, CrystallizationRecipe.class);
    public static final RecipeType<InscriptionRecipe> INSCRIPTION = create(InscriptionRecipe.NAME, InscriptionRecipe.class);
    public static final RecipeType<EnchantmentLiquefactionRecipeCategory.RecipeWrapper> ENCHANTMENT_LIQUEFACTION = create(EnchantmentLiquefactionRecipe.NAME, EnchantmentLiquefactionRecipeCategory.RecipeWrapper.class);
    public static final RecipeType<PureInfusionRecipe> PURE_INFUSION = create(PureInfusionRecipe.NAME, PureInfusionRecipe.class);
    public static final RecipeType<IPurifierRecipe> PURIFICATION = create(PurificationRecipeCategory.NAME, IPurifierRecipe.class);
    public static final RecipeType<IGrindingRecipe> GRINDING = create(IGrindingRecipe.NAME, IGrindingRecipe.class);
    public static final RecipeType<SawingRecipe> SAWING = create(SawingRecipe.NAME, SawingRecipe.class);
    public static final RecipeType<SpellCraftRecipe> SPELL_CRAFTING = create(SpellCraftRecipe.NAME, SpellCraftRecipe.class);
    public static final RecipeType<ElementType> DISPLACEMENT = create(DisplacementRecipeCategory.NAME, ElementType.class);
    public static final RecipeType<BuddingShrineBlock.CrystalType> BUDDING_SHRINE = create(BuddingShrineBlock.NAME, BuddingShrineBlock.CrystalType.class);
    public static final RecipeType<MeltingRecipe> MELTING_SHRINE = create(MeltingRecipe.NAME, MeltingRecipe.class);
    public static final RecipeType<SpringShrineBlock> SPRING_SHRINE = create(SpringShrineBlock.NAME, SpringShrineBlock.class);
    public static final RecipeType<ElementType> CRYSTAL_THROWING = create(CrystalThrowingRecipeCategory.NAME, ElementType.class);
    public static final RecipeType<SourceBreedingRecipe> SOURCE_BREEDING = create(SourceBreedingRecipeCategory.NAME, SourceBreedingRecipe.class);
    public static final RecipeType<ItemDiffusionRecipe> ITEM_DIFFUSION = create(ItemDiffusionRecipe.NAME, ItemDiffusionRecipe.class);

    private static <T> RecipeType<T> create(String path, Class<? extends T> recipeClass) {
        return RecipeType.create(ElementalCraftApi.MODID, path, recipeClass);
    }
}
