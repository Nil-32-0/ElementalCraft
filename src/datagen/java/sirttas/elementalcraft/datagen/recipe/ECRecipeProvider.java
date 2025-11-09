package sirttas.elementalcraft.datagen.recipe;

import com.google.gson.JsonObject;
import mekanism.api.MekanismAPI;
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.StringUtils;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.ElementTypeTier;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.synthesizer.mana.ManaSynthesizerBlock;
import sirttas.elementalcraft.datagen.recipe.builder.PureInfusionRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.SpellCraftRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.BindingRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.CrystallizationRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.GrindingRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.InscriptionRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.SawingRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion.InfusionRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion.ToolInfusionRecipeBuilder;
import sirttas.elementalcraft.infusion.tool.effect.AutoSmeltToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.DodgeToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.FastDrawToolInfusionEffect;
import sirttas.elementalcraft.item.ECCreativeModeTabs;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.source.receptacle.NaturalSourceIngredient;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;
import sirttas.elementalcraft.rune.Runes;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.Spells;
import sirttas.elementalcraft.tag.ECTags;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.patchouli.api.PatchouliAPI;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class ECRecipeProvider extends RecipeProvider {

	private static final String HAS_INERT_CRYSTAL = "has_inert_crystal";
	private static final String HAS_CONTAINED_CRYSTAL = "has_contained_crystal";
	private static final String HAS_PURECRYSTAL = "has_purecrystal";
	private static final String HAS_WHITEROCK = "has_whiterock";
	private static final String HAS_SHRINE_UPGRADE_CORE = "has_shrine_upgrade_core";
    private static final String HAS_ADVANCED_SHRINE_UPGRADE_CORE = "has_advanced_shrine_upgrade_core";
	private static final String HAS_SPRINGALINE_SHARD = "has_springaline_shard";

	private static final String HAS_DRENCHED_IRON_NUGGET = "has_drenched_iron_nugget";
	private static final String HAS_DRENCHED_IRON_INGOT = "has_drenched_iron_ingot";
	private static final String HAS_SWIFT_ALLOY_NUGGET = "has_swift_alloy_nugget";
	private static final String HAS_SWIFT_ALLOY_INGOT = "has_swift_alloy_ingot";
	private static final String HAS_FIREITE_INGOT = "has_fireite_ingot";
	public static final String FROM = "_from_";

	private final ExistingFileHelper existingFileHelper;

	public ECRecipeProvider(PackOutput pOutput, ExistingFileHelper exFileHelper) {
		super(pOutput);
		existingFileHelper = exFileHelper;
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		registerSlabsStairsWalls(consumer);
		registerInertCrystal(consumer);
		registerNuggetIngotBlocks(consumer);
		registerMaterials(consumer);
		registerPipes(consumer);
		registerContainers(consumer);
		registerInstruments(consumer);
		registerPureInfuser(consumer);
		registerInfusions(consumer);
		registerSpringaline(consumer);
		registerHolders(consumer);
		registerTools(consumer);
		registerShards(consumer);
		registerLenses(consumer);
		registerShrines(consumer);
		registerShrineUpgrades(consumer);
		registerSourceDisplacementPlates(consumer);
		registerJewels(consumer);
		registerSpells(consumer);
		registerToolInfusions(consumer);
		registerGrinding(consumer);
		registerSawing(consumer);
		registerRunes(consumer);
		registerEmptying(consumer);
		registerCrystallizations(consumer);
		registerDecorations(consumer);
		registerSourceBreeding(consumer);
	}

	private static void registerMaterials(@Nonnull Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.CONTAINED_CRYSTAL.get())
				.define('g', Tags.Items.NUGGETS_GOLD)
				.define('c', ECItems.INERT_CRYSTAL.get())
				.pattern(" g ")
				.pattern("gcg")
				.pattern(" g ")
				.unlockedBy(HAS_INERT_CRYSTAL, has(ECItems.INERT_CRYSTAL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.STRONGLY_CONTAINED_CRYSTAL.get())
				.define('g', ECTags.Items.NUGGETS_SWIFT_ALLOY)
				.define('c', ECItems.CONTAINED_CRYSTAL.get())
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.pattern("sgs")
				.pattern("gcg")
				.pattern("sgs")
				.unlockedBy(HAS_CONTAINED_CRYSTAL, has(ECItems.CONTAINED_CRYSTAL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.SHRINE_BASE.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.INERT_CRYSTAL.get())
				.define('p', ECBlocks.PIPE.get())
				.pattern(" p ")
				.pattern("pcp")
				.pattern("www")
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.SHRINE_UPGRADE_CORE.get())
				.define('c', ECItems.CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('r', Tags.Items.DUSTS_REDSTONE)
				.pattern("rir")
				.pattern("ici")
				.pattern("rir")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.ADVANCED_SHRINE_UPGRADE_CORE.get())
                .define('C', ECItems.SHRINE_UPGRADE_CORE.get())
                .define('c', ECItems.PURE_CRYSTAL.get())
                .define('i', Tags.Items.INGOTS_GOLD)
                .define('s', ECItems.SOLAR_PRISM.get())
                .define('n', ECTags.Items.NUGGETS_FIREITE)
                .pattern("nsn")
                .pattern("iCi")
                .pattern("ncn")
                .unlockedBy(HAS_PURECRYSTAL, has(ECItems.PURE_CRYSTAL))
                .save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.SOLAR_PRISM.get())
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('c', Tags.Items.INGOTS_COPPER)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.pattern(" s ")
                .pattern("cdc")
				.pattern(" s ")
				.unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.DRENCHED_SAW_BLADE.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('n', ECTags.Items.NUGGETS_DRENCHED_IRON)
				.define('r', Tags.Items.INGOTS_IRON)
				.pattern("nin")
				.pattern("iri")
				.pattern("nin")
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_DRENCHED_IRON))
				.save(consumer);

		BindingRecipeBuilder.bindingRecipe(ECItems.SWIFT_ALLOY_INGOT.get(), ElementType.AIR)
				.addIngredient(Tags.Items.INGOTS_GOLD)
				.addIngredient(ECTags.Items.INGOTS_DRENCHED_IRON)
				.addIngredient(Tags.Items.INGOTS_COPPER)
				.addIngredient(Tags.Items.DUSTS_REDSTONE)
				.addIngredient(ECItems.CRYSTALS.get(ElementType.AIR).get())
				.withElementAmount(1250)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.FIREITE_INGOT.get(), ElementType.FIRE)
				.addIngredient(Tags.Items.INGOTS_NETHERITE)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.withElementAmount(30000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.HARDENED_HANDLE.get(), ElementType.EARTH)
				.addIngredient(Tags.Items.RODS_WOODEN)
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECItems.AIR_SILK.get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.EARTH).get())
				.withElementAmount(1250)
				.save(consumer);

		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.PURE_CRYSTAL.get())
				.setIngredient(Tags.Items.GEMS_DIAMOND)
				.setIngredient(ElementType.WATER, ECItems.CRYSTALS.get(ElementType.WATER).get())
				.setIngredient(ElementType.FIRE, ECItems.CRYSTALS.get(ElementType.FIRE).get())
				.setIngredient(ElementType.EARTH, ECItems.CRYSTALS.get(ElementType.EARTH).get())
				.setIngredient(ElementType.AIR, ECItems.CRYSTALS.get(ElementType.AIR).get())
				.save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ECItems.PRISTINE_SHARD.get(), 8)
                .requires(ECItems.PURE_CRYSTAL.get())
                .requires(ECTags.Items.CHISELS)
                .unlockedBy(HAS_PURECRYSTAL, has(ECItems.PURE_CRYSTAL))
                .save(consumer);
	}

	private static void registerLenses(@Nonnull Consumer<FinishedRecipe> consumer) {
        ElementType.getElementsTier(ElementTypeTier.PRIMORDIAL).forEach(type -> BindingRecipeBuilder.bindingRecipe(
                ECItems.LENSES.get(type).get(), type)
                .addIngredient(ECItems.SPRINGALINE_SHARD.get())
                .addIngredient(ECBlocks.SPRINGALINE_GLASS_PANE.get())
                .addIngredient(Tags.Items.INGOTS_COPPER)
                .addIngredient(ECItems.CRYSTALS.get(type).get())
                .save(consumer)
        );
	}

	private static void registerSpringaline(@Nonnull Consumer<FinishedRecipe> consumer) {
		BindingRecipeBuilder.bindingRecipe(ECItems.SPRINGALINE_SHARD.get(), ElementType.WATER)
				.addIngredient(Items.AMETHYST_SHARD)
				.addIngredient(Tags.Items.GEMS_QUARTZ)
				.addIngredient(ECItems.CRYSTALS.get(ElementType.WATER).get())
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SPRINGALINE_CLUSTER.get(), ElementType.WATER)
				.addIngredient(Items.AMETHYST_BLOCK)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_QUARTZ /* FIXME use all quartz blocks */)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.WATER).get())
				.save(consumer);
	}

	private static void registerTools(@Nonnull Consumer<FinishedRecipe> consumer) {
        ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition(PatchouliAPI.MOD_ID))
                .addRecipe( ShapelessRecipeBuilder
                        .shapeless(RecipeCategory.TOOLS, ECCreativeModeTabs.createElementopedia().getItem())
                        .requires(ECItems.INERT_CRYSTAL.get())
                        .requires(Items.BOOK)
                        .unlockedBy(HAS_INERT_CRYSTAL, has(ECItems.INERT_CRYSTAL))::save
                )
                .build(consumer, ElementalCraftApi.createRL("element_book"));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECItems.SOURCE_STABILIZER.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('s', ECBlocks.SPRINGALINE_GLASS.get())
				.pattern("sis")
				.pattern("i i")
				.pattern("sis")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECItems.SOURCE_ANALYSIS_GLASS.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('g', ECBlocks.SPRINGALINE_GLASS.get())
				.define('s', ECItems.AIR_SILK.get())
				.define('h', ECTags.Items.HARDENED_RODS)
				.pattern(" sg")
				.pattern(" is")
				.pattern("h  ")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECItems.DRENCHED_IRON_CHISEL.get())
                .define('h', Tags.Items.RODS_WOODEN)
                .define('s', ECItems.AIR_SILK.get())
                .define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
                .pattern(" i ")
                .pattern(" si")
                .pattern("h  ")
                .unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_DRENCHED_IRON))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECItems.SWIFT_ALLOY_CHISEL.get())
				.define('h', ECTags.Items.HARDENED_RODS)
				.define('s', ECItems.AIR_SILK.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.pattern(" i ")
				.pattern(" si")
				.pattern("h  ")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECItems.FIREITE_CHISEL.get())
                .define('h', ECTags.Items.HARDENED_RODS)
                .define('s', ECItems.AIR_SILK.get())
                .define('i', ECTags.Items.INGOTS_FIREITE)
                .pattern(" i ")
                .pattern(" si")
                .pattern("h  ")
                .unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
                .save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECBlocks.TRANSLOCATION_ANCHOR.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('g', ECItems.PRISTINE_GEMS.get(ElementType.AIR).get())
				.define('f', ECTags.Items.NUGGETS_FIREITE)
				.define('e', Items.ENDER_EYE)
				.pattern(" e ")
				.pattern("fgf")
				.pattern("www")
				.unlockedBy("has_fireite_nugget", has(ECTags.Items.NUGGETS_FIREITE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECBlocks.RETRIEVER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('h', Blocks.HOPPER)
				.define('d', Blocks.DISPENSER)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.pattern("iw ")
				.pattern("hdi")
				.pattern("iw "
				).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECBlocks.SORTER.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('h', Blocks.HOPPER)
				.define('d', Blocks.DISPENSER)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.pattern("ii ")
				.pattern("hdi")
				.pattern("ii ")
				.save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECItems.AIR_MILL.get())
                .define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
                .define('h', ECTags.Items.HARDENED_RODS)
                .define('c', ItemTags.WOOL_CARPETS)
                .pattern("cic")
                .pattern("cic")
                .pattern(" h ")
                .unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
                .save(consumer);

		PureInfusionRecipeBuilder.pureInfusionRecipe(ECBlocks.PURE_ROCK.get())
				.setIngredient(Items.OBSIDIAN)
				.setIngredient(ElementType.WATER, Items.PRISMARINE)
				.setIngredient(ElementType.FIRE, ECTags.Items.INGOTS_FIREITE)
				.setIngredient(ElementType.EARTH, ECBlocks.WHITE_ROCK.get())
				.setIngredient(ElementType.AIR, Items.PURPUR_BLOCK)
				.save(consumer);

		BindingRecipeBuilder.bindingRecipe(ECItems.ELEMENTAL_FIREFUEL.get(), ElementType.FIRE)
				.addIngredient(ECItems.CRYSTALS.get(ElementType.FIRE).get())
				.addIngredient(ItemTags.COALS)
				.addIngredient(Tags.Items.RODS_BLAZE)
				.addIngredient(Items.LAVA_BUCKET)
				.withElementAmount(20000)
				.save(consumer);
	}

	private void registerInstruments(@Nonnull Consumer<FinishedRecipe> consumer) {
        prepareInstrumentRecipe(ECBlocks.RUDIMENTARY_EXTRACTOR)
                .define('i', Tags.Items.INGOTS_IRON)
                .pattern(" c ")
                .pattern(" i ")
                .pattern("ici")
                .save(consumer);
        prepareWhiterockInstrumentRecipe(ECBlocks.EXTRACTOR.get())
                .define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
                .define('d', ECTags.Items.INGOTS_DRENCHED_IRON)
                .define('e', ECBlocks.RUDIMENTARY_EXTRACTOR.get())
                .pattern("e e")
                .pattern("idi")
                .pattern("wcw")
                .save(consumer);
        prepareWhiterockInstrumentRecipe(ECBlocks.IMPROVED_EXTRACTOR.get(), ECItems.PURE_CRYSTAL.get())
                .define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
                .define('e', ECBlocks.EXTRACTOR.get())
                .define('r', ECBlocks.RUDIMENTARY_EXTRACTOR.get())
                .pattern(" r ")
                .pattern("eie")
                .pattern("wcw")
                .save(consumer);
		prepareInstrumentRecipe(ECBlocks.EVAPORATOR)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('g', Tags.Items.GLASS)
				.pattern("igi")
				.pattern("igi")
				.pattern("ici")
				.save(consumer);
        prepareWhiterockInstrumentRecipe(ECBlocks.AIR_MILL_SYNTHESIZER.get(), ECItems.CRYSTALS.get(ElementType.AIR).get())
                .define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
                .define('a', ECItems.AIR_MILL.get())
                .pattern("iai")
                .pattern("wcw")
                .save(consumer);
        prepareInstrumentRecipe(ECBlocks.CRACKING_SYNTHESIZER)
                .define('i', Tags.Items.INGOTS_IRON)
                .define('p', Items.STONE_PICKAXE)
                .pattern("ipi")
                .pattern(" c ")
                .save(consumer);
        prepareWhiterockInstrumentRecipe(ECBlocks.CULINARY_SYNTHESIZER.get(), ECItems.CRYSTALS.get(ElementType.WATER).get())
                .define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
                .define('l', Items.CAKE)
                .define('b', Items.BUCKET)
                .pattern(" l ")
                .pattern("ibi")
                .pattern("wcw")
                .save(consumer);
        prepareInstrumentRecipe(ECBlocks.DRAINING_SYNTHESIZER.get())
                .define('i', Tags.Items.INGOTS_IRON)
                .define('g', ECBlocks.BURNT_GLASS.get())
                .pattern("igi")
                .pattern(" c ")
                .save(consumer);
        prepareWhiterockInstrumentRecipe(ECBlocks.SCULK_CRACKING_SYNTHESIZER.get())
                .define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
                .define('h', Items.DIAMOND_HOE)
                .pattern("ihi")
                .pattern("wcw")
                .save(consumer);
        prepareWhiterockInstrumentRecipe(ECBlocks.VIBRATION_SYNTHESIZER.get())
                .define('i', ECTags.Items.NUGGETS_DRENCHED_IRON)
                .define('p', ItemTags.WOOL_CARPETS)
                .pattern("ipi")
                .pattern("wcw")
                .save(consumer);
        prepareWhiterockInstrumentRecipe(ECBlocks.COMBUSTION_SYNTHESIZER.get())
                .define('i', Tags.Items.INGOTS_IRON)
                .pattern("i i")
                .pattern("wcw")
                .save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.SOLAR_SYNTHESIZER.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('h', ECTags.Items.HARDENED_RODS)
				.define('p', ECItems.SOLAR_PRISM.get())
				.pattern("dhd")
				.pattern("ipi")
				.pattern("wcw")
				.save(consumer);
		ConditionalRecipe.builder()
				.addCondition(new ModLoadedCondition(BotaniaAPI.MODID))
				.addRecipe(prepareInstrumentRecipe(ECBlocks.MANA_SYNTHESIZER)
						.define('s', ECBlocks.SOLAR_SYNTHESIZER.get())
						.define('p', BotaniaBlocks.manaPool)
						.define('a', ECTags.Items.INGOTS_SWIFT_ALLOY)
						.define('l', BotaniaBlocks.livingrock)
						.define('m', BotaniaItems.manaDiamond)
						.pattern("msm")
						.pattern("apa")
						.pattern("lcl")::save)
				.build(consumer, ElementalCraftApi.createRL(ManaSynthesizerBlock.NAME));
		prepareWhiterockInstrumentRecipe(ECBlocks.DIFFUSER.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern(" c ")
				.pattern("did")
				.pattern("wcw")
				.save(consumer);
		prepareInstrumentRecipe(ECBlocks.INFUSER)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('n', Tags.Items.NUGGETS_IRON)
				.pattern("n n")
				.pattern("ici")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.BINDER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern("i i")
				.pattern("wcw")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.BINDER_IMPROVED.get(), ECItems.PURE_CRYSTAL.get())
				.define('s', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('b', ECBlocks.BINDER.get())
				.define('i', ECBlocks.INFUSER.get())
				.pattern("did")
				.pattern("sbs")
				.pattern("wcw")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.CRYSTALLIZER.get(), ECItems.STRONGLY_CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.pattern("iwi")
				.pattern("i i")
				.pattern("wcw")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.WATER_MILL_GRINDSTONE.get(), ECItems.CRYSTALS.get(ElementType.WATER).get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('g', Items.GRINDSTONE)
				.pattern("www")
				.pattern("igi")
				.pattern("wcw")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.AIR_MILL_GRINDSTONE.get(), ECItems.CRYSTALS.get(ElementType.AIR).get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('a', ECItems.AIR_MILL.get())
				.define('g', Items.GRINDSTONE)
				.pattern(" a ")
				.pattern("igi")
				.pattern("wcw")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.WATER_MILL_WOOD_SAW.get(), ECItems.CRYSTALS.get(ElementType.WATER).get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('s', ECItems.DRENCHED_SAW_BLADE.get())
				.pattern("www")
				.pattern("isi")
				.pattern("wcw")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.AIR_MILL_WOOD_SAW.get(), ECItems.CRYSTALS.get(ElementType.AIR).get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('a', ECItems.AIR_MILL.get())
				.define('s', ECItems.DRENCHED_SAW_BLADE.get())
				.pattern(" a ")
				.pattern("isi")
				.pattern("wcw")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.INSCRIBER.get(), ECItems.CONTAINED_CRYSTAL.get())
                .define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.pattern(" wi")
                .pattern("wdi")
				.pattern("wcw")
				.save(consumer);
        prepareWhiterockInstrumentRecipe(ECBlocks.ENCHANTMENT_LIQUEFIER.get(), ECItems.PURE_CRYSTAL.get())
                .define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
                .define('t', Items.ENCHANTING_TABLE)
                .define('e', Tags.Items.GEMS_EMERALD)
                .define('g', ECItems.PRISTINE_GEMS.get(ElementType.WATER).get())
                .pattern("ege")
                .pattern("iti")
                .pattern("wcw")
                .save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.FIRE_FURNACE.get(), ECItems.CRYSTALS.get(ElementType.FIRE).get())
				.define('f', Blocks.FURNACE)
				.pattern("www")
				.pattern("wfw")
				.pattern("wcw")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.FIRE_BLAST_FURNACE.get(), ECItems.CRYSTALS.get(ElementType.FIRE).get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('F', Blocks.BLAST_FURNACE)
				.define('g', ECBlocks.BURNT_GLASS.get())
				.pattern("www")
				.pattern("gFg")
				.pattern("ici")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.PURIFIER.get(), ECItems.PURE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('e', ECItems.FINE_GEMS.get(ElementType.EARTH).get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.pattern("gig")
				.pattern("wew")
				.pattern("ici")
				.save(consumer);
	}

	private void registerPureInfuser(@Nonnull Consumer<FinishedRecipe> consumer) {
		prepareWhiterockInstrumentRecipe(ECBlocks.PURE_INFUSER.get(), ECItems.STRONGLY_CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('n', ECBlocks.INFUSER.get())
				.pattern("wnw")
				.pattern("ici")
				.pattern("www")
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.FIRE_PEDESTAL.get(), ElementType.FIRE)
				.addIngredient(ECBlocks.INFUSER.get())
				.addIngredient(ECItems.FINE_GEMS.get(ElementType.FIRE).get())
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.withElementAmount(30000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.WATER_PEDESTAL.get(), ElementType.WATER)
				.addIngredient(ECBlocks.INFUSER.get())
				.addIngredient(ECItems.FINE_GEMS.get(ElementType.WATER).get())
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.withElementAmount(30000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.EARTH_PEDESTAL.get(), ElementType.EARTH)
				.addIngredient(ECBlocks.INFUSER.get())
				.addIngredient(ECItems.FINE_GEMS.get(ElementType.EARTH).get())
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.withElementAmount(30000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.AIR_PEDESTAL.get(), ElementType.AIR)
				.addIngredient(ECBlocks.INFUSER.get())
				.addIngredient(ECItems.FINE_GEMS.get(ElementType.AIR).get())
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.withElementAmount(30000)
				.save(consumer);
	}

	private void registerContainers(@Nonnull Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.SMALL_CONTAINER.get())
				.define('g', Tags.Items.GLASS)
				.define('p', ECBlocks.PIPE_IMPAIRED.get())
				.pattern(" p ")
				.pattern("pgp")
				.pattern(" p ")
				.unlockedBy(HAS_CONTAINED_CRYSTAL, has(ECItems.CONTAINED_CRYSTAL))
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('g', ECBlocks.BURNT_GLASS.get())
				.define('p', ECBlocks.PIPE.get())
				.pattern("ici")
				.pattern("pgp")
				.pattern("www")
				.save(consumer);

        ElementType.ALL_VALID.forEach(type -> BindingRecipeBuilder
                .bindingRecipe(ECBlocks.RESERVOIRS.get(type).get(), type)
                .addIngredient(ECBlocks.CONTAINER.get())
                .addIngredient(ECBlocks.SPRINGALINE_GLASS.get())
                .addIngredient(ECItems.PURE_CRYSTAL.get())
                .addIngredient(ECItems.PRISTINE_GEMS.get(type).get())
                .withElementAmount(10000)
                .save(consumer)
        );
	}

	private static void registerShards(@Nonnull Consumer<FinishedRecipe> consumer) {
        ElementType.ALL_VALID.forEach(type -> {
            String typeName = type.getSerializedName();

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.POWERFUL_SHARDS.get(type).get())
                    .define('#', ECItems.SHARDS.get(type).get())
                    .pattern("###")
                    .pattern("###")
                    .pattern("###")
                    .unlockedBy("has_"+typeName+"_shard", has(ECItems.SHARDS.get(type)))
                    .save(consumer, ElementalCraftApi.createRL(
                            "powerful_"+typeName+"_shard_from_"+typeName+"_shards"
                    ));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ECItems.SHARDS.get(type).get(), 9)
                    .requires(ECItems.POWERFUL_SHARDS.get(type).get())
                    .unlockedBy("has_powerful_"+typeName+"_shard", has(ECItems.POWERFUL_SHARDS.get(type)))
                    .save(consumer);
        });
	}

	private static void registerHolders(@Nonnull Consumer<FinishedRecipe> consumer) {
        ElementType.ALL_VALID.forEach(type -> ShapedRecipeBuilder
                .shaped(RecipeCategory.TOOLS, ECItems.ELEMENT_HOLDERS.get(type).get())
                .define('g', Tags.Items.INGOTS_GOLD)
                .define('e', ECBlocks.RUDIMENTARY_EXTRACTOR.get())
                .define('t', ECBlocks.SMALL_CONTAINER.get())
                .define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
                .define('c', ECItems.CRYSTALS.get(type).get())
                .pattern("geg")
                .pattern("iti")
                .pattern("gcg")
                .unlockedBy("has_"+type.getSerializedName()+"crystal", has(ECItems.CRYSTALS.get(type)))
                .save(consumer)
        );


		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.PURE_HOLDER_CORE.get())
				.define('i', ECTags.Items.INGOTS_FIREITE)
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern(" i ")
				.pattern("ici")
				.pattern(" i ")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
				.save(consumer);
		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.PURE_HOLDER.get())
				.setIngredient(ECItems.PURE_HOLDER_CORE.get()
				).setIngredient(ElementType.WATER, ECItems.ELEMENT_HOLDERS.get(ElementType.WATER).get())
				.setIngredient(ElementType.FIRE, ECItems.ELEMENT_HOLDERS.get(ElementType.FIRE).get())
				.setIngredient(ElementType.EARTH, ECItems.ELEMENT_HOLDERS.get(ElementType.EARTH).get())
				.setIngredient(ElementType.AIR, ECItems.ELEMENT_HOLDERS.get(ElementType.AIR).get())
				.withElementAmount(100000)
				.save(consumer);
	}

	private void registerSlabsStairsWalls(Consumer<FinishedRecipe> consumer) {
		ForgeRegistries.BLOCKS.getEntries().forEach(e -> {
			var block = e.getValue();
			var key = e.getKey().location();

			if (ElementalCraft.owns(key) && !exists(block) && (block instanceof SlabBlock || block instanceof StairBlock || block instanceof WallBlock)) {
				String name = key.getPath();
				String sourceName = name.substring(0, name.length() - (block instanceof StairBlock ? 7 : 5));
				ItemLike source = ForgeRegistries.ITEMS.getValue(ElementalCraftApi.createRL(sourceName));
				ShapedRecipeBuilder shaped = ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, block, block instanceof StairBlock ? 4 : 6).define('#', source);

				if (block instanceof SlabBlock) {
					shaped.pattern("###");
				} else if (block instanceof StairBlock) {
					shaped.pattern("#  ").pattern("## ").pattern("###");
				} else if (block instanceof WallBlock) {
					shaped.pattern("###").pattern("###");
				}
				shaped.unlockedBy("has_" + sourceName, has(source)).save(consumer);
				SingleItemRecipeBuilder.stonecutting(Ingredient.of(source), RecipeCategory.DECORATIONS, block, block instanceof SlabBlock ? 2 : 1)
						.unlockedBy("has_" + sourceName, has(source))
						.save(consumer, ElementalCraftApi.createRL(name + FROM + sourceName + "_stonecutting"));
			}
		});
	}

	private void registerInertCrystal(Consumer<FinishedRecipe> consumer) {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ECTags.Items.ORES_INERT_CRYSTAL), RecipeCategory.MISC, ECItems.INERT_CRYSTAL.get(), 0.5F, 200)
				.unlockedBy("has_crystal_ore", has(ECTags.Items.ORES_INERT_CRYSTAL))
				.save(consumer);
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(ECTags.Items.ORES_INERT_CRYSTAL), RecipeCategory.MISC, ECItems.INERT_CRYSTAL.get(), 0.5F, 100)
				.unlockedBy("has_crystal_ore", has(ECTags.Items.ORES_INERT_CRYSTAL))
				.save(consumer, ElementalCraftApi.createRL("inertcrystal_from_blasting"));

		ConditionalRecipe.builder()
				.addCondition(new ModLoadedCondition(MekanismAPI.MEKANISM_MODID))
				.addRecipe(ItemStackToItemStackRecipeBuilder.enriching(IngredientCreatorAccess.item().from(ECTags.Items.ORES_INERT_CRYSTAL), new ItemStack(ECItems.INERT_CRYSTAL.get(), 2))::build)
				.build(consumer, ElementalCraftApi.createRL("inert_crystal_from_mekanism_enriching"));
	}

	private void registerNuggetIngotBlocks(@Nonnull Consumer<FinishedRecipe> consumer) {
		createNuggetIngotBlock(ECItems.DRENCHED_IRON_NUGGET.get(), ECTags.Items.NUGGETS_DRENCHED_IRON, ECItems.DRENCHED_IRON_INGOT.get(), ECTags.Items.INGOTS_DRENCHED_IRON, ECBlocks.DRENCHED_IRON_BLOCK.get(), ECTags.Items.STORAGE_BLOCKS_DRENCHED_IRON, consumer);
		createNuggetIngotBlock(ECItems.SWIFT_ALLOY_NUGGET.get(), ECTags.Items.NUGGETS_SWIFT_ALLOY, ECItems.SWIFT_ALLOY_INGOT.get(), ECTags.Items.INGOTS_SWIFT_ALLOY, ECBlocks.SWIFT_ALLOY_BLOCK.get(), ECTags.Items.STORAGE_BLOCKS_SWIFT_ALLOY, consumer);
		createNuggetIngotBlock(ECItems.FIREITE_NUGGET.get(), ECTags.Items.NUGGETS_FIREITE, ECItems.FIREITE_INGOT.get(), ECTags.Items.INGOTS_FIREITE, ECBlocks.FIREITE_BLOCK.get(), ECTags.Items.STORAGE_BLOCKS_FIREITE, consumer);

		createStorageBlock(ECItems.INERT_CRYSTAL.get(), ECBlocks.INERT_CRYSTAL_BLOCK.get(), consumer);
        ElementType.ALL_VALID.forEach(type ->
                createStorageBlock(ECItems.CRYSTALS.get(type).get(), ECBlocks.CRYSTAL_BLOCKS.get(type).get(), consumer)
        );
	}

	private void registerPipes(Consumer<FinishedRecipe> consumer) {
		prepareInstrumentRecipe(ECBlocks.PIPE_IMPAIRED.get(), ECItems.CONTAINED_CRYSTAL.get(), 4)
				.define('i', Tags.Items.INGOTS_IRON)
				.pattern("ici")
				.save(consumer);
		prepareInstrumentRecipe(ECBlocks.PIPE.get(), ECItems.CONTAINED_CRYSTAL.get(), 4)
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern("ici")
				.save(consumer);
		prepareInstrumentRecipe(ECBlocks.PIPE_IMPROVED.get(), ECItems.CONTAINED_CRYSTAL.get(), 4)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.pattern("ici")
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ECBlocks.PIPE.get())
				.requires(ECBlocks.PIPE_IMPAIRED.get())
				.requires(Ingredient.of(ECTags.Items.NUGGETS_DRENCHED_IRON), 5)
				.unlockedBy(HAS_DRENCHED_IRON_NUGGET, has(ECTags.Items.NUGGETS_DRENCHED_IRON))
				.save(consumer, ElementalCraftApi.createRL("elementpipe_from_impaired_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ECBlocks.PIPE.get(), 4)
				.requires(ECBlocks.PIPE_IMPAIRED.get(), 4)
				.requires(Ingredient.of(ECTags.Items.INGOTS_DRENCHED_IRON), 2)
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_DRENCHED_IRON))
				.save(consumer, ElementalCraftApi.createRL("elementpipe_from_impaired_elementpipe_and_ingot"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ECBlocks.PIPE_IMPROVED.get())
				.requires(ECBlocks.PIPE.get())
				.requires(Ingredient.of(ECTags.Items.NUGGETS_SWIFT_ALLOY), 5)
				.unlockedBy(HAS_SWIFT_ALLOY_NUGGET, has(ECTags.Items.NUGGETS_SWIFT_ALLOY))
				.save(consumer, ElementalCraftApi.createRL("improved_elementpipe_from_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ECBlocks.PIPE_IMPROVED.get(), 4)
				.requires(ECBlocks.PIPE.get(), 4)
				.requires(Ingredient.of(ECTags.Items.INGOTS_SWIFT_ALLOY), 2)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(consumer, ElementalCraftApi.createRL("improved_elementpipe_from_elementpipe_and_ingot"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ECBlocks.PIPE_IMPROVED.get())
				.requires(ECBlocks.PIPE_IMPAIRED.get())
				.requires(Ingredient.of(ECTags.Items.NUGGETS_SWIFT_ALLOY), 5)
				.unlockedBy(HAS_SWIFT_ALLOY_NUGGET, has(ECTags.Items.NUGGETS_SWIFT_ALLOY))
				.save(consumer, ElementalCraftApi.createRL("improved_elementpipe_from_impaired_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ECBlocks.PIPE_IMPROVED.get(), 4)
				.requires(ECBlocks.PIPE_IMPAIRED.get(), 4)
				.requires(Ingredient.of(ECTags.Items.INGOTS_SWIFT_ALLOY), 2)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(consumer, ElementalCraftApi.createRL("improved_elementpipe_from_impaired_elementpipe_and_ingot"));

		shaped(ECItems.COVER_FRAME, 8)
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern("iii")
				.pattern("i i")
				.pattern("iii")
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_DRENCHED_IRON))
				.save(consumer);

		shaped(ECItems.PIPE_PRIORITY_RINGS, 4)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('f', ECItems.COVER_FRAME.get())
				.pattern(" i ")
				.pattern("ifi")
				.pattern(" i ")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(consumer);
		shaped(ECItems.ELEMENT_PUMP)
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('n', ECTags.Items.NUGGETS_FIREITE)
				.define('f', ECItems.COVER_FRAME.get())
				.pattern("in ")
				.pattern("cfi")
				.pattern("in ")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.NUGGETS_FIREITE))
				.save(consumer);
		shaped(ECItems.ELEMENT_VALVE)
				.define('r', Tags.Items.DUSTS_REDSTONE)
				.define('f', ECItems.COVER_FRAME.get())
				.pattern(" r ")
				.pattern("rfr")
				.pattern(" r ")
				.unlockedBy("has_cover_frame", has(ECItems.COVER_FRAME.get()))
				.save(consumer);
		shaped(ECItems.ELEMENT_BEAM, 2)
				.define('c', ECItems.CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('n', ECTags.Items.NUGGETS_SWIFT_ALLOY)
				.define('f', ECItems.COVER_FRAME.get())
				.pattern("in ")
				.pattern("fic")
				.pattern("in ")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(consumer);
	}

	private void registerInfusions(@Nonnull Consumer<FinishedRecipe> consumer) {
        ElementType.ALL_VALID.forEach(type -> {
            InfusionRecipeBuilder.infusionRecipe(
                    Ingredient.of(ECItems.INERT_CRYSTAL.get()), ECItems.CRYSTALS.get(type).get(), type
            ).save(consumer);
            InfusionRecipeBuilder.infusionRecipe(
                    Ingredient.of(Tags.Items.GEMS_DIAMOND), ECItems.CRUDE_GEMS.get(type).get(), type
            ).save(consumer);
        });

		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(Items.STONE), ECBlocks.WHITE_ROCK.get(), ElementType.EARTH)
				.withElementAmount(500)
				.save(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(Tags.Items.INGOTS_IRON), ECItems.DRENCHED_IRON_INGOT.get(), ElementType.WATER)
				.withElementAmount(500)
				.save(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(Tags.Items.GLASS), ECBlocks.BURNT_GLASS.get(), ElementType.FIRE)
				.withElementAmount(500)
				.save(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(Tags.Items.STRING), ECItems.AIR_SILK.get(), ElementType.AIR)
				.withElementAmount(500)
				.save(consumer);
	}

	private void registerShrines(Consumer<FinishedRecipe> consumer) {
		BindingRecipeBuilder.bindingRecipe(ECBlocks.FIRE_PYLON.get(), ElementType.FIRE)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.FIRE).get())
				.addIngredient(Items.LAVA_BUCKET)
				.addIngredient(Tags.Items.INGOTS_GOLD)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.VACUUM_SHRINE.get(), ElementType.AIR)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.AIR).get())
				.addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.HOPPER)
				.addIngredient(Tags.Items.GEMS_DIAMOND)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.GROWTH_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.WATER).get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.EARTH).get())
				.addIngredient(Items.WHEAT_SEEDS)
				.addIngredient(Items.BONE_MEAL)
				.addIngredient(Tags.Items.GEMS_DIAMOND)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.LAVA_SHRINE.get(), ElementType.FIRE)
				.addIngredient(ECBlocks.FIRE_PYLON.get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.FIRE).get())
				.addIngredient(ECItems.PRISTINE_GEMS.get(ElementType.FIRE).get())
				.addIngredient(Blocks.OBSIDIAN)
				.addIngredient(Items.LAVA_BUCKET)
				.addIngredient(Items.BLAZE_ROD)
				.withElementAmount(20000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.HARVEST_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.EARTH).get())
				.addIngredient(ECItems.CRUDE_GEMS.get(ElementType.EARTH).get())
				.addIngredient(Items.DIAMOND_HOE)
				.addIngredient(Items.SHEARS)
				.withElementAmount(5000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.LUMBER_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.EARTH).get())
				.addIngredient(ECItems.CRUDE_GEMS.get(ElementType.EARTH).get())
				.addIngredient(ECItems.DRENCHED_SAW_BLADE.get())
				.addIngredient(Items.DIAMOND_AXE)
				.addIngredient(Items.SHEARS)
				.withElementAmount(5000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.ORE_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.EARTH).get())
				.addIngredient(ECItems.PRISTINE_GEMS.get(ElementType.EARTH).get())
				.addIngredient(Items.DIAMOND_PICKAXE)
				.withElementAmount(20000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.OVERLOAD_SHRINE.get(), ElementType.AIR)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.AIR).get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.addIngredient(Items.CLOCK)
				.addIngredient(Items.ENDER_EYE)
				.withElementAmount(20000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SWEET_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.WATER).get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.EARTH).get())
				.addIngredient(Items.SUGAR)
				.addIngredient(Items.HONEY_BOTTLE)
				.addIngredient(Items.MILK_BUCKET)
				.withElementAmount(5000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.ENDER_LOCK_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.WATER).get())
				.addIngredient(ECItems.FINE_GEMS.get(ElementType.AIR).get())
				.addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.DRAGON_BREATH)
				.addIngredient(Items.OBSIDIAN)
				.withElementAmount(5000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.BREEDING_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.EARTH).get())
				.addIngredient(ECItems.CRUDE_GEMS.get(ElementType.WATER).get())
				.addIngredient(Tags.Items.CROPS)
				.addIngredient(Tags.Items.LEATHER)
				.addIngredient(Items.MILK_BUCKET)
				.addIngredient(Tags.Items.GEMS_DIAMOND)
				.withElementAmount(5000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.GROVE_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.WATER).get())
				.addIngredient(ECItems.CRUDE_GEMS.get(ElementType.EARTH).get())
				.addIngredient(ItemTags.FLOWERS)
				.addIngredient(Tags.Items.SEEDS)
				.addIngredient(Tags.Items.CROPS)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SPRING_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.WATER).get())
				.addIngredient(Items.BUCKET)
				.addIngredient(ItemTags.FISHES)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.BUDDING_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.EARTH).get())
				.addIngredient(ECItems.CRUDE_GEMS.get(ElementType.WATER).get())
				.addIngredient(Items.AMETHYST_BLOCK)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(Tags.Items.GEMS_DIAMOND)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SPAWNING_SHRINE.get(), ElementType.FIRE)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.CRYSTALS.get(ElementType.FIRE).get())
				.addIngredient(ECItems.FINE_GEMS.get(ElementType.EARTH).get())
				.addIngredient(Items.ROTTEN_FLESH)
				.addIngredient(Items.SPIDER_EYE)
				.addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.DIAMOND)
				.save(consumer);
	}

	private void registerShrineUpgrades(@Nonnull Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.ACCELERATION_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('i', Items.CLOCK)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('r', Tags.Items.DUSTS_REDSTONE)
				.pattern("rir")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.RANGE_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('g', Tags.Items.DUSTS_GLOWSTONE)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.CRYSTALS.get(ElementType.EARTH).get())
				.pattern("ggg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.CAPACITY_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('g', ECBlocks.SPRINGALINE_GLASS.get())
				.define('b', Items.BUCKET)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.CRYSTALS.get(ElementType.WATER).get())
				.pattern("gbg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.EFFICIENCY_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.CRYSTALS.get(ElementType.FIRE).get())
				.pattern("gdg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.STRENGTH_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('g', Tags.Items.DUSTS_GLOWSTONE)
				.define('r', Tags.Items.RODS_BLAZE)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.CRYSTALS.get(ElementType.FIRE).get())
				.pattern("grg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.OPTIMIZATION_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('f', ECTags.Items.INGOTS_FIREITE)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("dfd")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.FORTUNE_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('l', Tags.Items.GEMS_LAPIS)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.CRYSTALS.get(ElementType.WATER).get())
				.pattern("lll")
				.pattern("wCw").
				pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.SILK_TOUCH_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('s', ECItems.AIR_SILK.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("sss")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.PLANTING_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('s', Tags.Items.SEEDS)
				.define('h', Items.DIAMOND_HOE)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.CRYSTALS.get(ElementType.EARTH).get())
				.pattern("shs")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.BONELESS_GROWTH_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('b', Items.BONE_BLOCK)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("bdb")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.PICKUP_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('e', Items.ENDER_EYE)
				.define('h', Items.HOPPER)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("ehe")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.VORTEX_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('e', Items.ENDER_PEARL)
				.define('h', Items.HOPPER)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.CRYSTALS.get(ElementType.AIR).get())
				.pattern("ehe")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.NECTAR_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('h', Items.HONEY_BLOCK)
				.define('s', Items.SUGAR)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.CRYSTALS.get(ElementType.WATER).get())
				.pattern("shs")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ConditionalRecipe.builder()
				.addCondition(new ModLoadedCondition(BotaniaAPI.MODID))
				.addRecipe(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.MYSTICAL_GROVE_SHRINE_UPGRADE.get())
						.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
						.define('p', BotaniaTags.Items.PETALS)
						.define('m', BotaniaTags.Items.INGOTS_MANASTEEL)
						.define('w', BotaniaBlocks.livingrock)
						.define('c', ECItems.CRYSTALS.get(ElementType.WATER).get())
						.pattern("pmp").pattern("wCw")
						.pattern(" c ")
						.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))::save)
				.build(consumer, ForgeRegistries.BLOCKS.getKey(ECBlocks.MYSTICAL_GROVE_SHRINE_UPGRADE.get()));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.STEM_POLLINATION_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('p', Items.PUMPKIN)
				.define('b', Items.BONE_MEAL)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.CRYSTALS.get(ElementType.EARTH).get())
				.pattern("bpb")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.PROTECTION_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('s', Items.SHIELD)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.CRYSTALS.get(ElementType.EARTH).get())
				.pattern("isi")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.FILLING_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('b', Items.BUCKET)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.CRYSTALS.get(ElementType.WATER).get())
				.pattern("ibi")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.SPRINGALINE_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('p', Items.PRISMARINE_CRYSTALS)
				.define('s', ECBlocks.SPRINGALINE_BLOCK.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("sps")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.CRYSTAL_HARVEST_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('p', Items.DIAMOND_PICKAXE)
				.define('g', ECBlocks.SPRINGALINE_GLASS.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.CRYSTALS.get(ElementType.EARTH).get())
				.pattern("gpg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.CRYSTAL_GROWTH_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('s', ECBlocks.SPRINGALINE_BLOCK.get())
				.define('e', ECItems.PRISTINE_GEMS.get(ElementType.EARTH).get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.CRYSTALS.get(ElementType.WATER).get())
				.pattern("ses")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.TRANSLOCATION_SHRINE_UPGRADE.get())
                .define('C', ECItems.ADVANCED_SHRINE_UPGRADE_CORE.get())
				.define('f', ECTags.Items.NUGGETS_FIREITE)
				.define('t', createScrollIngredient(Spells.TRANSLOCATION))
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('u', ECBlocks.RANGE_SHRINE_UPGRADE.get())
				.pattern("ftf")
				.pattern("wCw")
				.pattern(" u ")
                .unlockedBy(HAS_ADVANCED_SHRINE_UPGRADE_CORE, has(ECItems.ADVANCED_SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE.get())
				.define('C', ECItems.ADVANCED_SHRINE_UPGRADE_CORE.get())
				.define('i', Items.CLOCK)
				.define('p', ECBlocks.PIPE_IMPROVED.get())
				.define('u', ECBlocks.ACCELERATION_SHRINE_UPGRADE.get())
				.define('z', createRuneIngredient(Runes.ZOD))
				.pattern("ziz")
				.pattern("pCp")
				.pattern(" u ")
                .unlockedBy(HAS_ADVANCED_SHRINE_UPGRADE_CORE, has(ECItems.ADVANCED_SHRINE_UPGRADE_CORE))
				.save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.GREATER_FORTUNE_SHRINE_UPGRADE.get())
                .define('C', ECItems.ADVANCED_SHRINE_UPGRADE_CORE.get())
                .define('l', Tags.Items.GEMS_LAPIS)
                .define('w', ECBlocks.WHITE_ROCK.get())
                .define('u', ECBlocks.FORTUNE_SHRINE_UPGRADE.get())
                .define('t', createRuneIngredient(Runes.TZEENTCH))
                .pattern("tlt")
                .pattern("wCw")
                .pattern(" u ")
                .unlockedBy(HAS_ADVANCED_SHRINE_UPGRADE_CORE, has(ECItems.ADVANCED_SHRINE_UPGRADE_CORE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.OVERWHELMING_STRENGTH_SHRINE_UPGRADE.get())
                .define('C', ECItems.ADVANCED_SHRINE_UPGRADE_CORE.get())
                .define('f', ECItems.PRISTINE_GEMS.get(ElementType.FIRE).get())
                .define('r', Tags.Items.RODS_BLAZE)
                .define('w', ECBlocks.WHITE_ROCK.get())
                .define('u', ECBlocks.STRENGTH_SHRINE_UPGRADE.get())
                .pattern("frf")
                .pattern("wCw")
                .pattern(" u ")
                .unlockedBy(HAS_ADVANCED_SHRINE_UPGRADE_CORE, has(ECItems.ADVANCED_SHRINE_UPGRADE_CORE))
                .save(consumer);
	}

	private void registerSourceDisplacementPlates(Consumer<FinishedRecipe> consumer) {
        ElementType.ALL_VALID.forEach(type -> createSourceDisplacementPlate(
                ECBlocks.SOURCE_DISPLACEMENT_PLATES.get(type),
                ECItems.PRISTINE_GEMS.get(type).get(),
                consumer
        ));
	}

	private void registerJewels(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.UNSET_JEWEL.get())
				.pattern("sis")
				.pattern("idi")
				.pattern("sis")
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(consumer);

		createJewelRecipe(Jewels.SALMON, b -> b
				.pattern(" w ")
				.pattern("cUc")
				.pattern(" a ")
				.define('w', ECItems.FINE_GEMS.get(ElementType.WATER).get())
				.define('a', ECItems.CRUDE_GEMS.get(ElementType.AIR).get())
				.define('c', ECItems.CRYSTALS.get(ElementType.WATER).get()), consumer);
		createJewelRecipe(Jewels.DOLPHIN, b -> b
				.pattern(" w ")
				.pattern("cUc")
				.pattern(" w ")
				.define('w', ECItems.CRUDE_GEMS.get(ElementType.WATER).get())
				.define('c', ECItems.CRYSTALS.get(ElementType.WATER).get()), consumer);
		createJewelRecipe(Jewels.LEOPARD, b -> b
				.pattern(" a ")
				.pattern("cUc")
				.pattern(" e ")
				.define('a', ECItems.FINE_GEMS.get(ElementType.AIR).get())
				.define('e', ECItems.FINE_GEMS.get(ElementType.EARTH).get())
				.define('c', ECItems.CRYSTALS.get(ElementType.AIR).get()), consumer);
		createJewelRecipe(Jewels.PHOENIX, b -> b
				.pattern("fgf")
				.pattern("bUb")
				.pattern("fpf")
				.define('g', ECItems.PRISTINE_GEMS.get(ElementType.FIRE).get())
				.define('b', Tags.Items.RODS_BLAZE)
				.define('f', Tags.Items.FEATHERS)
				.define('p', ECItems.PURE_CRYSTAL.get()), consumer);
		createJewelRecipe(Jewels.TORTOISE, b -> b
				.pattern("ses")
				.pattern("gUg")
				.pattern("ses")
				.define('g', Tags.Items.GRAVEL)
				.define('e', ECItems.CRUDE_GEMS.get(ElementType.EARTH).get())
				.define('s', Items.SCUTE), consumer);
		createJewelRecipe(Jewels.DEMIGOD, b -> b
				.pattern("tat")
				.pattern("cUc")
				.pattern("tet")
				.define('e', ECItems.PRISTINE_GEMS.get(ElementType.EARTH).get())
				.define('a', ECItems.PRISTINE_GEMS.get(ElementType.AIR).get())
				.define('t', Items.TOTEM_OF_UNDYING)
				.define('c', ECItems.PURE_CRYSTAL.get()), consumer);
		createJewelRecipe(Jewels.MOLE, b -> b
				.pattern(" e ")
				.pattern("sUa")
				.pattern(" p ")
				.define('e', ECItems.PRISTINE_GEMS.get(ElementType.EARTH).get())
				.define('a', Items.DIAMOND_PICKAXE)
				.define('s', Items.DIAMOND_SHOVEL)
				.define('p', ECItems.PURE_CRYSTAL.get()), consumer);
		createJewelRecipe(Jewels.TIGER, b -> b
				.pattern("sas")
				.pattern("cUc")
				.pattern("sfs")
				.define('a', ECItems.PRISTINE_GEMS.get(ElementType.AIR).get())
				.define('c', ECItems.CRYSTALS.get(ElementType.AIR).get())
				.define('f', ECItems.FINE_GEMS.get(ElementType.AIR).get())
				.define('s', Items.SUGAR), consumer);
		createJewelRecipe(Jewels.BEAR, b -> b
				.pattern("heh")
				.pattern("cUc")
				.pattern("hfh")
				.define('e', ECItems.PRISTINE_GEMS.get(ElementType.EARTH).get())
				.define('c', ECItems.CRYSTALS.get(ElementType.EARTH).get())
				.define('f', ECItems.FINE_GEMS.get(ElementType.EARTH).get())
				.define('h', Items.HONEY_BOTTLE), consumer);
		createJewelRecipe(Jewels.VIPER, b -> b
				.pattern(" w ")
				.pattern("sUs")
				.pattern(" e ")
				.define('e', ECItems.FINE_GEMS.get(ElementType.EARTH).get())
				.define('w', ECItems.FINE_GEMS.get(ElementType.WATER).get())
				.define('s', Items.SPIDER_EYE), consumer);
		createJewelRecipe(Jewels.HAWK, b -> b
				.pattern("gag")
				.pattern("cUc")
				.pattern("gag")
				.define('a', ECItems.FINE_GEMS.get(ElementType.AIR).get())
				.define('g', Tags.Items.DUSTS_GLOWSTONE)
				.define('c', ECItems.CRYSTALS.get(ElementType.AIR).get()), consumer);
		createJewelRecipe(Jewels.KIRIN, b -> b
				.pattern("sfs")
				.pattern("cUc")
				.pattern("sas")
				.define('f', ECItems.PRISTINE_GEMS.get(ElementType.FIRE).get())
				.define('a', ECItems.PRISTINE_GEMS.get(ElementType.AIR).get())
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('c', ECItems.PURE_CRYSTAL.get()), consumer);
		createJewelRecipe(Jewels.ARCTIC_HARES, b -> b
				.pattern(" w ")
				.pattern("fUf")
				.pattern(" a ")
				.define('w', ECItems.FINE_GEMS.get(ElementType.WATER).get())
				.define('a', ECItems.CRUDE_GEMS.get(ElementType.AIR).get())
				.define('f', Items.RABBIT_FOOT), consumer);
		createJewelRecipe(Jewels.STRIDER, b -> b
				.pattern(" f ")
				.pattern("bUb")
				.pattern(" f ")
				.define('f', ECItems.FINE_GEMS.get(ElementType.FIRE).get())
				.define('b', Items.LAVA_BUCKET), consumer);
		createJewelRecipe(Jewels.WATER_STRIDER, b -> b
				.pattern(" w ")
				.pattern("bUb")
				.pattern(" w ")
				.define('w', ECItems.FINE_GEMS.get(ElementType.WATER).get())
				.define('b', Items.WATER_BUCKET), consumer);
		createJewelRecipe(Jewels.BASILISK, b -> b
				.pattern("fwf")
				.pattern("cUc")
				.pattern("sws")
				.define('w', ECItems.PRISTINE_GEMS.get(ElementType.WATER).get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('f', Items.FERMENTED_SPIDER_EYE)
				.define('s', Items.SCUTE), consumer);
		createJewelRecipe(Jewels.PIGLIN, b -> b
				.pattern("gfg")
				.pattern("pUp")
				.pattern("gcg")
				.define('c', ECItems.CRYSTALS.get(ElementType.FIRE).get())
				.define('f', ECItems.CRUDE_GEMS.get(ElementType.FIRE).get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('p', Items.PORKCHOP), consumer);
	}

	private void registerSpells(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ECItems.SCROLL_PAPER.get())
				.requires(ECItems.AIR_SILK.get())
				.requires(Items.PAPER)
				.requires(Items.INK_SAC)
				.unlockedBy("has_air_silk", has(ECItems.AIR_SILK))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.SPELL_DESK.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('l', Blocks.LECTERN)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.pattern("wlw")
				.pattern(" i ")
				.pattern(" w ")
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECItems.FOCUS.get())
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('c', ECItems.CONTAINED_CRYSTAL.get())
				.define('s', ECTags.Items.HARDENED_RODS)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.pattern(" ic").pattern(" si")
				.pattern("d  ")
				.unlockedBy(HAS_CONTAINED_CRYSTAL, has(ECItems.CONTAINED_CRYSTAL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ECItems.STAFF.get())
				.define('s', ECTags.Items.STAFF_CRAFT_SWORD)
				.define('f', ECItems.FOCUS.get())
				.define('h', ECTags.Items.HARDENED_RODS)
				.define('i', ECTags.Items.INGOTS_FIREITE)
				.pattern(" if")
				.pattern("ihi")
				.pattern("si ")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
				.save(mapToStaff(consumer));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECItems.SPELL_BOOK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('s', ECItems.AIR_SILK.get())
				.define('l', Tags.Items.LEATHER)
				.define('p', ECItems.SCROLL_PAPER.get())
				.pattern("slp")
				.pattern("clp").pattern("slp")
				.unlockedBy(HAS_PURECRYSTAL, has(ECItems.PURE_CRYSTAL))
				.save(consumer);

		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.GRAVEL_FALL)
				.setGem(Items.DIAMOND)
				.setCrystal(ECItems.CRYSTALS.get(ElementType.EARTH).get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.STONE_WALL)
				.setGem(Items.DIAMOND)
				.setCrystal(ECItems.CRYSTALS.get(ElementType.EARTH).get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.FIRE_BALL)
				.setGem(Items.DIAMOND)
				.setCrystal(ECItems.CRYSTALS.get(ElementType.FIRE).get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.ITEM_PULL)
				.setGem(ECItems.FINE_GEMS.get(ElementType.AIR).get())
				.setCrystal(ECItems.CRYSTALS.get(ElementType.AIR).get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.ENDER_STRIKE)
				.setGem(ECItems.CRUDE_GEMS.get(ElementType.AIR).get())
				.setCrystal(ECItems.CRYSTALS.get(ElementType.AIR).get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.ANIMAL_GROWTH)
				.setGem(ECItems.CRUDE_GEMS.get(ElementType.WATER).get())
				.setCrystal(ECItems.CRYSTALS.get(ElementType.WATER).get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.TREE_FALL)
				.setGem(ECItems.FINE_GEMS.get(ElementType.EARTH).get())
				.setCrystal(ECItems.CRYSTALS.get(ElementType.WATER).get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.PURIFICATION)
				.setGem(ECItems.CRUDE_GEMS.get(ElementType.WATER).get())
				.setCrystal(ECItems.CRYSTALS.get(ElementType.WATER).get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.RIPENING)
				.setGem(ECItems.CRUDE_GEMS.get(ElementType.EARTH).get())
				.setCrystal(ECItems.CRYSTALS.get(ElementType.WATER).get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.FLAME_CLEAVE)
				.setGem(ECItems.CRUDE_GEMS.get(ElementType.FIRE).get())
				.setCrystal(ECItems.CRYSTALS.get(ElementType.FIRE).get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.INFERNO)
				.setGem(Items.DIAMOND)
				.setCrystal(ECItems.CRYSTALS.get(ElementType.FIRE).get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.DASH)
				.setGem(ECItems.FINE_GEMS.get(ElementType.AIR).get())
				.setCrystal(ECItems.CRYSTALS.get(ElementType.AIR).get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.SILK_VEIN)
				.setGem(ECItems.PRISTINE_GEMS.get(ElementType.EARTH).get())
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.TRANSLOCATION)
				.setGem(ECItems.PRISTINE_GEMS.get(ElementType.AIR).get())
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.FEATHER_SPIKES)
				.setGem(ECItems.FINE_GEMS.get(ElementType.AIR).get())
				.setCrystal(ECItems.CRYSTALS.get(ElementType.EARTH).get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.HEAL)
				.setGem(ECItems.PRISTINE_GEMS.get(ElementType.WATER).get())
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.SPEED)
				.setGem(ECItems.PRISTINE_GEMS.get(ElementType.AIR).get())
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.SHOCKWAVE)
				.setGem(ECItems.CRUDE_GEMS.get(ElementType.AIR).get())
				.setCrystal(ECItems.CRYSTALS.get(ElementType.AIR).get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.AIR_SHIELD)
				.setGem(ECItems.FINE_GEMS.get(ElementType.AIR).get())
				.setCrystal(ECItems.CRYSTALS.get(ElementType.AIR).get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.REPAIR)
				.setGem(ECItems.PRISTINE_GEMS.get(ElementType.FIRE).get())
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(consumer);
	}

	private void registerToolInfusions(Consumer<FinishedRecipe> consumer) {
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, Enchantments.MOB_LOOTING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, Enchantments.FIRE_ASPECT).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, Enchantments.SHARPNESS).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, ElementalCraftApi.createRL("attack_speed")).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, Enchantments.BLOCK_FORTUNE).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, ElementalCraftApi.createRL(AutoSmeltToolInfusionEffect.NAME)).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, Enchantments.UNBREAKING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, Enchantments.BLOCK_EFFICIENCY).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, Enchantments.BLOCK_FORTUNE).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, ElementalCraftApi.createRL(AutoSmeltToolInfusionEffect.NAME)).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, Enchantments.UNBREAKING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, Enchantments.BLOCK_EFFICIENCY).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, Enchantments.BLOCK_FORTUNE).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, ElementalCraftApi.createRL(AutoSmeltToolInfusionEffect.NAME)).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, Enchantments.UNBREAKING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, Enchantments.BLOCK_EFFICIENCY).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.MOB_LOOTING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.FIRE_ASPECT).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.SHARPNESS /* TODO cleaving ? */).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.BLOCK_EFFICIENCY).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PAXELS, Enchantments.BLOCK_FORTUNE).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PAXELS, ElementalCraftApi.createRL(AutoSmeltToolInfusionEffect.NAME)).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PAXELS, Enchantments.UNBREAKING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PAXELS, Enchantments.BLOCK_EFFICIENCY).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, Enchantments.PUNCH_ARROWS).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, Enchantments.FLAMING_ARROWS).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, Enchantments.UNBREAKING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, ElementalCraftApi.createRL(FastDrawToolInfusionEffect.NAME)).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.MULTISHOT).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.PIERCING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.UNBREAKING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.QUICK_CHARGE).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, Enchantments.FISHING_LUCK).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, ElementalCraftApi.createRL(AutoSmeltToolInfusionEffect.NAME)).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, Enchantments.UNBREAKING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, Enchantments.FISHING_SPEED).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.LOYALTY).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.IMPALING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.UNBREAKING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.RIPTIDE).save(consumer);

		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.RESPIRATION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.FIRE_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.ALL_DAMAGE_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.PROJECTILE_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, Enchantments.BLAST_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, Enchantments.FIRE_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, Enchantments.ALL_DAMAGE_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, ElementalCraftApi.createRL(DodgeToolInfusionEffect.NAME)).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, Enchantments.BLAST_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, Enchantments.FIRE_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, Enchantments.ALL_DAMAGE_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, ElementalCraftApi.createRL("movement_speed")).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.DEPTH_STRIDER).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.FIRE_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.ALL_DAMAGE_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.FALL_PROTECTION).save(consumer);

		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraftApi.createRL("fire_reduction")).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraftApi.createRL("water_reduction")).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraftApi.createRL("earth_reduction")).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraftApi.createRL("air_reduction")).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraftApi.createRL("fire_staff")).withElementAmount(5000).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraftApi.createRL("water_staff")).withElementAmount(5000).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraftApi.createRL("earth_staff")).withElementAmount(5000).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraftApi.createRL("air_staff")).withElementAmount(5000).save(consumer);

		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECItems.LENSES.get(ElementType.FIRE).get(), ElementalCraftApi.createRL("fire_unbreaking")).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECItems.LENSES.get(ElementType.WATER).get(), ElementalCraftApi.createRL("water_unbreaking")).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECItems.LENSES.get(ElementType.EARTH).get(), Enchantments.UNBREAKING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECItems.LENSES.get(ElementType.AIR).get(), ElementalCraftApi.createRL("air_unbreaking")).save(consumer);
	}

	private void registerGrinding(Consumer<FinishedRecipe> consumer) {
		GrindingRecipeBuilder.grindingRecipe(Items.COBBLESTONE)
				.withIngredient(Tags.Items.STONE)
				.withLuckRatio(1)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(Items.GRAVEL)
				.withIngredient(Tags.Items.COBBLESTONE)
				.withLuckRatio(2)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(Items.SAND)
				.withIngredient(Tags.Items.GRAVEL)
				.withLuckRatio(5)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(Items.BLAZE_POWDER)
				.withCount(3)
				.withIngredient(Tags.Items.RODS_BLAZE)
				.withLuckRatio(3)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(Items.NETHERITE_SCRAP)
				.withCount(2)
				.withIngredient(Tags.Items.ORES_NETHERITE_SCRAP)
				.withElementAmount(5000)
				.withLuckRatio(1)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(ECItems.INERT_CRYSTAL.get())
				.withCount(2)
				.withIngredient(ECTags.Items.ORES_INERT_CRYSTAL)
				.withLuckRatio(5)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(Items.POINTED_DRIPSTONE)
				.withCount(3)
				.withIngredient(Items.DRIPSTONE_BLOCK)
				.withLuckRatio(1)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(Items.AMETHYST_SHARD)
				.withCount(6)
				.withIngredient(Items.AMETHYST_CLUSTER)
				.withLuckRatio(5)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(ECItems.SPRINGALINE_SHARD.get())
				.withCount(6)
				.withIngredient(ECBlocks.SPRINGALINE_CLUSTER.get())
				.withLuckRatio(5)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(Items.BONE_MEAL)
				.withCount(4)
				.withIngredient(Tags.Items.BONES)
				.withLuckRatio(3)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(Items.STRING)
				.withCount(4)
				.withIngredient(ItemTags.WOOL)
				.save(consumer);

		grindToDye(Items.GREEN_DYE, Items.CACTUS, consumer);
		grindToDye(Items.WHITE_DYE, Items.BONE_MEAL, consumer);
		grindToDye(Items.BLACK_DYE, Items.INK_SAC, consumer);
		grindToDye(Items.BLUE_DYE, Tags.Items.GEMS_LAPIS, consumer);
		grindToDye(Items.RED_DYE, Tags.Items.CROPS_BEETROOT, consumer);
		grindToDye(Items.LIME_DYE, Items.SEA_PICKLE, consumer);
		grindToDye(Items.BROWN_DYE, Items.COCOA_BEANS, consumer);

		grindToDye(Items.WHITE_DYE, ECTags.Items.WHITE_FLOWERS, consumer);
		grindToDye(Items.ORANGE_DYE, ECTags.Items.ORANGE_FLOWERS, consumer);
		grindToDye(Items.MAGENTA_DYE, ECTags.Items.MAGENTA_FLOWERS, consumer);
		grindToDye(Items.LIGHT_BLUE_DYE, ECTags.Items.LIGHT_BLUE_FLOWERS, consumer);
		grindToDye(Items.YELLOW_DYE, ECTags.Items.YELLOW_FLOWERS, consumer);
		grindToDye(Items.LIME_DYE, ECTags.Items.LIME_FLOWERS, consumer);
		grindToDye(Items.PINK_DYE, ECTags.Items.PINK_FLOWERS, consumer);
		grindToDye(Items.GRAY_DYE, ECTags.Items.GRAY_FLOWERS, consumer);
		grindToDye(Items.LIGHT_GRAY_DYE, ECTags.Items.LIGHT_GRAY_FLOWERS, consumer);
		grindToDye(Items.CYAN_DYE, ECTags.Items.CYAN_FLOWERS, consumer);
		grindToDye(Items.PURPLE_DYE, ECTags.Items.PURPLE_FLOWERS, consumer);
		grindToDye(Items.BLUE_DYE, ECTags.Items.BLUE_FLOWERS, consumer);
		grindToDye(Items.BROWN_DYE, ECTags.Items.BROWN_FLOWERS, consumer);
		grindToDye(Items.GREEN_DYE, ECTags.Items.GREEN_FLOWERS, consumer);
		grindToDye(Items.BLACK_DYE, ECTags.Items.BLACK_FLOWERS, consumer);
		grindToDye(Items.RED_DYE, ECTags.Items.RED_FLOWERS, consumer);
	}

	private void grindToDye(ItemLike dye, ItemLike from, Consumer<FinishedRecipe> consumer) {
		GrindingRecipeBuilder.grindingRecipe(dye)
				.withCount(2)
				.withIngredient(from)
				.withLuckRatio(2)
				.save(consumer, ForgeRegistries.ITEMS.getKey(dye.asItem()).getPath() + FROM + ForgeRegistries.ITEMS.getKey(from.asItem()).getPath());
	}

	private void grindToDye(ItemLike dye, TagKey<Item> from, Consumer<FinishedRecipe> consumer) {
		var tagName = from.location();

		ConditionalRecipe.builder()
				.addCondition(new NotCondition(new TagEmptyCondition(tagName)))
				.addRecipe(GrindingRecipeBuilder.grindingRecipe(dye)
						.withCount(2)
						.withIngredient(from)
						.withLuckRatio(2)::save)
				.build(consumer, ElementalCraftApi.createRL(IGrindingRecipe.NAME + '/' + ForgeRegistries.ITEMS.getKey(dye.asItem()).getPath() + FROM + tagName.getNamespace() + '_' + StringUtils.replaceChars(tagName.getPath(), '/', '_')));
	}

	private void registerSawing(Consumer<FinishedRecipe> consumer) {
		sawingRecipe(Items.STRIPPED_OAK_LOG, Items.STRIPPED_OAK_WOOD, Items.OAK_PLANKS, Items.OAK_LOG, Items.OAK_WOOD, ECTags.Items.STRIPPED_OAK, consumer);
		sawingRecipe(Items.STRIPPED_DARK_OAK_LOG, Items.STRIPPED_DARK_OAK_WOOD, Items.DARK_OAK_PLANKS, Items.DARK_OAK_LOG, Items.DARK_OAK_WOOD, ECTags.Items.STRIPPED_DARK_OAK, consumer);
		sawingRecipe(Items.STRIPPED_BIRCH_LOG, Items.STRIPPED_BIRCH_WOOD, Items.BIRCH_PLANKS, Items.BIRCH_LOG, Items.BIRCH_WOOD, ECTags.Items.STRIPPED_BIRCH, consumer);
		sawingRecipe(Items.STRIPPED_ACACIA_LOG, Items.STRIPPED_ACACIA_WOOD, Items.ACACIA_PLANKS, Items.ACACIA_LOG, Items.ACACIA_WOOD, ECTags.Items.STRIPPED_ACACIA, consumer);
		sawingRecipe(Items.STRIPPED_JUNGLE_LOG, Items.STRIPPED_JUNGLE_WOOD, Items.JUNGLE_PLANKS, Items.JUNGLE_LOG, Items.JUNGLE_WOOD, ECTags.Items.STRIPPED_JUNGLE, consumer);
		sawingRecipe(Items.STRIPPED_SPRUCE_LOG, Items.STRIPPED_SPRUCE_WOOD, Items.SPRUCE_PLANKS, Items.SPRUCE_LOG, Items.SPRUCE_WOOD, ECTags.Items.STRIPPED_SPRUCE, consumer);
		sawingRecipe(Items.STRIPPED_MANGROVE_LOG, Items.STRIPPED_MANGROVE_WOOD, Items.MANGROVE_PLANKS, Items.MANGROVE_LOG, Items.MANGROVE_WOOD, ECTags.Items.STRIPPED_MANGROVE, consumer);
		sawingRecipe(Items.STRIPPED_CRIMSON_STEM, Items.STRIPPED_CRIMSON_HYPHAE, Items.CRIMSON_PLANKS, Items.CRIMSON_STEM, Items.CRIMSON_HYPHAE, ECTags.Items.STRIPPED_CRIMSON, consumer);
		sawingRecipe(Items.STRIPPED_WARPED_STEM, Items.STRIPPED_WARPED_HYPHAE, Items.WARPED_PLANKS, Items.WARPED_STEM, Items.WARPED_HYPHAE, ECTags.Items.STRIPPED_WARPED, consumer);
		sawingRecipe(Items.STRIPPED_CHERRY_LOG, Items.STRIPPED_CHERRY_WOOD, Items.CHERRY_PLANKS, Items.CHERRY_LOG, Items.CHERRY_WOOD, ECTags.Items.STRIPPED_CHERRY, consumer);

        SawingRecipeBuilder.sawingRecipe(Items.STRIPPED_BAMBOO_BLOCK)
                .withIngredient(Items.BAMBOO_BLOCK)
                .withElementAmount(250)
                .save(consumer);
        SawingRecipeBuilder.sawingRecipe(Items.BAMBOO_PLANKS)
                .withCount(6)
                .withIngredient(Items.STRIPPED_BAMBOO_BLOCK)
                .withLuckRatio(3)
                .save(consumer);
    }

	private void sawingRecipe(ItemLike stripedLog, ItemLike stripedWood, ItemLike planks, ItemLike log, ItemLike wood, TagKey<Item> stripped, Consumer<FinishedRecipe> consumer) {
		SawingRecipeBuilder.sawingRecipe(stripedLog)
				.withIngredient(log)
				.withElementAmount(250)
				.save(consumer);
		SawingRecipeBuilder.sawingRecipe(stripedWood)
				.withIngredient(wood)
				.withElementAmount(250)
				.save(consumer);
		SawingRecipeBuilder.sawingRecipe(planks)
				.withCount(6)
				.withIngredient(stripped)
				.withLuckRatio(3)
				.save(consumer);
	}

	private void registerRunes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.MINOR_RUNE_SLATE.get(), 4)
				.pattern("www")
				.pattern("wiw")
				.pattern("www")
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.RUNE_SLATE.get(), 4)
				.pattern("www")
				.pattern("wiw")
				.pattern("www")
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.MAJOR_RUNE_SLATE.get(), 4)
				.pattern("www")
				.pattern("wiw")
				.pattern("www")
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('i', ECTags.Items.INGOTS_FIREITE)
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK))
				.save(consumer);

		InscriptionRecipeBuilder.inscriptionRecipe(Runes.WII, ElementType.AIR)
				.withElementAmount(2000)
				.setSlate(ECItems.MINOR_RUNE_SLATE.get())
				.addIngredient(ECItems.CRUDE_GEMS.get(ElementType.AIR).get())
				.addIngredient(Items.SUGAR)
				.addIngredient(Items.SUGAR)
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.FUS, ElementType.AIR)
				.setSlate(ECItems.RUNE_SLATE.get())
				.addIngredient(createRuneIngredient(Runes.WII))
				.addIngredient(Tags.Items.STRING)
				.addIngredient(Tags.Items.STRING)
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.ZOD, ElementType.AIR)
				.withElementAmount(10000)
				.setSlate(ECItems.MAJOR_RUNE_SLATE.get())
				.addIngredient(createRuneIngredient(Runes.FUS))
				.addIngredient(ECItems.AIR_SILK.get())
				.addIngredient(ECItems.AIR_SILK.get())
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.MANX, ElementType.FIRE)
				.withElementAmount(2000)
				.setSlate(ECItems.MINOR_RUNE_SLATE.get())
				.addIngredient(ECItems.CRUDE_GEMS.get(ElementType.FIRE).get())
				.addIngredient(ItemTags.COALS)
				.addIngredient(ItemTags.COALS)
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.JITA, ElementType.FIRE)
				.setSlate(ECItems.RUNE_SLATE.get())
				.addIngredient(createRuneIngredient(Runes.MANX))
				.addIngredient(Items.BLAZE_ROD)
				.addIngredient(Items.BLAZE_ROD)
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.TANO, ElementType.FIRE)
				.withElementAmount(10000)
				.setSlate(ECItems.MAJOR_RUNE_SLATE.get())
				.addIngredient(createRuneIngredient(Runes.JITA))
				.addIngredient(Tags.Items.STORAGE_BLOCKS_COAL)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_COAL)
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.SOARYN, ElementType.EARTH)
				.setSlate(ECItems.MINOR_RUNE_SLATE.get())
				.addIngredient(ECItems.CRUDE_GEMS.get(ElementType.EARTH).get())
				.addIngredient(createRuneIngredient(Runes.WII))
				.addIngredient(createRuneIngredient(Runes.MANX))
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.KAWORU, ElementType.EARTH)
				.withElementAmount(10000)
				.setSlate(ECItems.RUNE_SLATE.get())
				.addIngredient(createRuneIngredient(Runes.SOARYN))
				.addIngredient(createRuneIngredient(Runes.FUS))
				.addIngredient(createRuneIngredient(Runes.JITA))
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.MEWTWO, ElementType.EARTH)
				.withElementAmount(20000)
				.setSlate(ECItems.MAJOR_RUNE_SLATE.get())
				.addIngredient(createRuneIngredient(Runes.KAWORU))
				.addIngredient(createRuneIngredient(Runes.ZOD))
				.addIngredient(createRuneIngredient(Runes.TANO))
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.CLAPTRAP, ElementType.WATER)
				.withElementAmount(2000)
				.setSlate(ECItems.MINOR_RUNE_SLATE.get())
				.addIngredient(ECItems.CRUDE_GEMS.get(ElementType.WATER).get())
				.addIngredient(Tags.Items.GEMS_LAPIS)
				.addIngredient(Tags.Items.GEMS_LAPIS)
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.BOMBADIL, ElementType.WATER)
				.setSlate(ECItems.RUNE_SLATE.get())
				.addIngredient(createRuneIngredient(Runes.CLAPTRAP))
				.addIngredient(Tags.Items.STORAGE_BLOCKS_LAPIS)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_LAPIS)
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.TZEENTCH, ElementType.WATER)
				.withElementAmount(10000)
				.setSlate(ECItems.MAJOR_RUNE_SLATE.get())
				.addIngredient(createRuneIngredient(Runes.BOMBADIL))
				.addIngredient(Tags.Items.GEMS_EMERALD)
				.addIngredient(Tags.Items.GEMS_EMERALD)
				.save(consumer);
	}

	private void registerCrystallizations(@Nonnull Consumer<FinishedRecipe> consumer) {
        ElementType.ALL_VALID.forEach(type -> {
            CrystallizationRecipeBuilder.crystallizationRecipe(ECItems.FINE_GEMS.get(type).get(), type)
                    .setGem(ECItems.CRUDE_GEMS.get(type).get())
                    .setCrystal(ECItems.CRYSTALS.get(type).get())
                    .save(consumer, type.getSerializedName()+"_gem");
            CrystallizationRecipeBuilder.crystallizationRecipe(ECItems.PRISTINE_GEMS.get(type).get(), type)
                    .withElementAmount(10000)
                    .setGem(ECItems.FINE_GEMS.get(type).get())
                    .setCrystal(ECItems.PRISTINE_SHARD.get())
                    .save(consumer, "pristine_"+type.getSerializedName()+"_gem");
        });
	}

	private Ingredient createScrollIngredient(RegistryObject<? extends Spell> spell) {
		var tag = new CompoundTag();
		var ecTag = new CompoundTag();

		tag.put(ECNames.EC_NBT, ecTag);
		ecTag.putString(ECNames.SPELL, spell.getKey().location().toString());
		return PartialNBTIngredient.of(ECItems.SCROLL.get(), tag);
	}

    private Ingredient createRuneIngredient(ResourceKey<Rune> rune) {
        return createRuneIngredient(rune.location());
    }

    private Ingredient createRuneIngredient(ResourceLocation rune) {
		var tag = new CompoundTag();
		var ecTag = new CompoundTag();

		tag.put(ECNames.EC_NBT, ecTag);
		ecTag.putString(ECNames.RUNE, rune.toString());
		return PartialNBTIngredient.of(ECItems.RUNE.get(), tag);
	}

	private void registerEmptying(@Nonnull Consumer<FinishedRecipe> consumer) {
		registerEmptying(ECBlocks.SMALL_CONTAINER.get(), consumer);
		registerEmptying(ECBlocks.CONTAINER.get(), consumer);
		registerEmptying(ECBlocks.CREATIVE_CONTAINER.get(), consumer);
        ECBlocks.RESERVOIRS.values().forEach(reservoir -> registerEmptying(reservoir.get(), consumer));
        ECItems.ELEMENT_HOLDERS.values().forEach(holder -> registerEmptying(holder.get(), consumer));
		registerEmptying(ECItems.PURE_HOLDER.get(), consumer);
	}


	private void registerEmptying(ItemLike item, Consumer<FinishedRecipe> consumer) {
		var name = ForgeRegistries.ITEMS.getKey(item.asItem()).getPath();

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, item)
				.requires(item)
				.unlockedBy("has_" + name, has(item))
				.save(consumer, ElementalCraftApi.createRL(name + "_emptying"));
	}

	private void registerDecorations(@Nonnull Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.BURNT_GLASS_PANE.get(), 16)
				.define('#', ECBlocks.BURNT_GLASS.get())
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_burnt_glass", has(ECBlocks.BURNT_GLASS))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.WHITE_ROCK_FENCE.get(), 16)
				.define('#', ECBlocks.WHITE_ROCK.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern("#i#")
				.pattern("#i#")
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.WHITE_ROCK_BRICK.get(), 4)
				.define('#', ECBlocks.WHITE_ROCK.get())
				.pattern("##")
				.pattern("##")
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(consumer);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ECBlocks.WHITE_ROCK.get()), RecipeCategory.DECORATIONS, ECBlocks.WHITE_ROCK_BRICK.get())
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(consumer, ElementalCraftApi.createRL("whiterock_brick_from_whiterock_stonecutting"));
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.MOSSY_WHITE_ROCK.get(), 8)
				.define('#', ECBlocks.WHITE_ROCK.get())
				.define('$', Blocks.MOSS_BLOCK)
				.pattern("###").pattern("#$#").pattern("###")
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.BURNT_WHITE_ROCK.get(), 8)
				.define('#', ECBlocks.WHITE_ROCK.get())
				.define('$', Blocks.MAGMA_BLOCK)
				.pattern("###").pattern("#$#")
				.pattern("###").unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.SPRINGALINE_BLOCK.get())
				.define('#', ECItems.SPRINGALINE_SHARD.get())
				.pattern("##")
				.pattern("##")
				.unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.SPRINGALINE_GLASS.get(), 2)
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('g', ECBlocks.BURNT_GLASS.get())
				.pattern(" s ")
				.pattern("sgs")
				.pattern(" s ")
				.unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.SPRINGALINE_GLASS_PANE.get(), 16)
				.define('#', ECBlocks.SPRINGALINE_GLASS.get())
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_springaline_glass", has(ECBlocks.SPRINGALINE_GLASS))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.SPRINGALINE_LANTERN.get())
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('g', Items.GLOWSTONE)
				.define('p', Tags.Items.GEMS_PRISMARINE)
				.pattern("psp")
				.pattern("sgs")
				.pattern("psp")
				.unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD))
				.save(consumer);
	}

	private void registerSourceBreeding(@Nonnull Consumer<FinishedRecipe> consumer) {
		prepareWhiterockInstrumentRecipe(ECBlocks.SOURCE_BREEDER_PEDESTAL.get(), ECItems.PURE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('s', ECItems.STRONGLY_CONTAINED_CRYSTAL.get())
				.pattern("wsw")
				.pattern("ici")
				.pattern("www")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.SOURCE_BREEDER.get(), ECItems.PURE_CRYSTAL.get())
				.define('f', ECTags.Items.INGOTS_FIREITE)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('p', ECBlocks.SOURCE_BREEDER_PEDESTAL.get())
				.pattern("iwi")
				.pattern("fcf")
				.pattern("wpw")
				.save(consumer);


        ElementType.ALL_VALID.forEach(type -> ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, ECItems.ARTIFICIAL_SOURCE_SEEDS.get(type).get(), 8)
                .define('f', ECTags.Items.NUGGETS_FIREITE)
                .define('s', ECItems.SPRINGALINE_SHARD.get())
                .define('g', ECItems.PRISTINE_GEMS.get(type).get())
                .pattern("fsf")
                .pattern("sgs")
                .pattern("fsf")
                .unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
                .save(consumer)
        );

        ElementType.getElementsTier(ElementTypeTier.PRIMORDIAL).forEach(type -> ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, ECItems.NATURAL_SOURCE_SEEDS.get(type).get())
                .requires(ECItems.ARTIFICIAL_SOURCE_SEEDS.get(type).get())
                .requires(new NaturalSourceIngredient(type))
                .unlockedBy("has_artificial_"+type.getSerializedName()+"_source_seed", has(ECItems.ARTIFICIAL_SOURCE_SEEDS.get(type).get()))
                .save(consumer)
        );
	}

	private boolean exists(Block block) {
		return existingFileHelper.exists(ForgeRegistries.BLOCKS.getKey(block), PackType.SERVER_DATA, ".json", "recipes");
	}

	private void createSourceDisplacementPlate(RegistryObject<? extends ItemLike> plate, ItemLike gem, Consumer<FinishedRecipe> consumer) {
		var plateItem = plate.get().asItem();

		prepareWhiterockInstrumentRecipe(plateItem, ECItems.PURE_CRYSTAL.get())
				.define('s', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('g', gem).pattern(" g ")
				.pattern("scs").pattern("www")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, plateItem)
				.requires(ECBlocks.BROKEN_SOURCE_DISPLACEMENT_PLATE.get())
				.requires(gem)
				.unlockedBy("has_broken_source_displacement_plate", has(ECBlocks.BROKEN_SOURCE_DISPLACEMENT_PLATE))
				.save(consumer, ElementalCraftApi.createRL(ForgeRegistries.ITEMS.getKey(plateItem).getPath() + "_repair"));

	}

	private void createNuggetIngotBlock(ItemLike nugget, TagKey<Item> nuggetTag, ItemLike ingot, TagKey<Item> ingotTag, ItemLike block, TagKey<Item> blockTag, Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ingot).define('#', nuggetTag)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy(buildHas(nugget), has(nuggetTag))
				.save(consumer, from(nugget, ingot));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, nugget, 9)
				.requires(ingotTag)
				.unlockedBy(buildHas(ingot), has(ingotTag))
				.save(consumer, from(ingot, nugget));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, block).define('#', ingotTag)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy(buildHas(ingot), has(ingotTag)
				).save(consumer, from(ingot, block));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ingot, 9)
				.requires(blockTag)
				.unlockedBy(buildHas(block), has(blockTag))
				.save(consumer, from(block, ingot));
	}

	private void createStorageBlock(ItemLike item, ItemLike block, Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, block)
				.define('#', item)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy(buildHas(item), has(item))
				.save(consumer, from(item, block));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, item, 9)
				.requires(block)
				.unlockedBy(buildHas(block), has(block))
				.save(consumer, from(block, item));
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(ItemLike result) {
		return prepareInstrumentRecipe(result, ECItems.CONTAINED_CRYSTAL.get(), 1);
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(RegistryObject<? extends ItemLike> result) {
		return prepareInstrumentRecipe(result.get());
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(ItemLike result, ItemLike crystal, int count) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result, count)
				.define('c', crystal)
				.unlockedBy(buildHas(crystal), has(crystal));
	}

	private ShapedRecipeBuilder prepareWhiterockInstrumentRecipe(ItemLike result) {
		return prepareInstrumentRecipe(result, ECItems.CONTAINED_CRYSTAL.get(), 1)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()));
	}

	private ShapedRecipeBuilder prepareWhiterockInstrumentRecipe(ItemLike result, ItemLike crystal) {
		return prepareInstrumentRecipe(result, crystal, 1)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()));
	}

	public static ShapedRecipeBuilder shaped(RegistryObject<? extends ItemLike> item) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item.get(), 1);
	}

	public static ShapedRecipeBuilder shaped(RegistryObject<? extends ItemLike> item, int count) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item.get(), count);
	}


	private Consumer<FinishedRecipe> mapToStaff(Consumer<FinishedRecipe> consumer) {
		return recipe -> consumer.accept(new FinishedRecipe() {
			@Override
			public void serializeRecipeData(@Nonnull JsonObject json) {
				recipe.serializeRecipeData(json);
			}

			@Nonnull
			@Override
			public ResourceLocation getId() {
				return recipe.getId();
			}

			@Nonnull
			@Override
			public RecipeSerializer<?> getType() {
				return ECRecipeSerializers.STAFF.get();
			}

			@Override
			public JsonObject serializeAdvancement() {
				return recipe.serializeAdvancement();
			}

			@Override
			public ResourceLocation getAdvancementId() {
				return recipe.getAdvancementId();
			}
		});
	}

	private void createJewelRecipe(RegistryObject<? extends Jewel> jewel, UnaryOperator<ShapedRecipeBuilder> patternBuilder, Consumer<FinishedRecipe> consumer) {
		createJewelRecipe(jewel.get(), patternBuilder, consumer);
	}

	private void createJewelRecipe(Jewel jewel, UnaryOperator<ShapedRecipeBuilder> patternBuilder, Consumer<FinishedRecipe> consumer) {
		var builder = shaped(ECItems.JEWEL);
		var nameSuffix = "/" + jewel.getKey().getPath();

		patternBuilder.apply(builder)
				.define('U', ECItems.UNSET_JEWEL.get())
				.unlockedBy("has_unset_jewel", has(ECItems.UNSET_JEWEL))
				.save(recipe -> consumer.accept(new FinishedRecipe() {
			@Override
			public void serializeRecipeData(@Nonnull JsonObject json) {
				recipe.serializeRecipeData(json);

				var resultJson = json.getAsJsonObject("result");
				var nbt = new JsonObject();
				var ecNbt = new JsonObject();

				ecNbt.addProperty(ECNames.JEWEL, jewel.getKey().toString());
				nbt.add(ECNames.EC_NBT, ecNbt);
				resultJson.add("nbt", nbt);
			}

			@Nonnull
			@Override
			public ResourceLocation getId() {
				var oldId = recipe.getId();

				return new ResourceLocation(oldId.getNamespace(), oldId.getPath() + nameSuffix);
			}

			@Nonnull
			@Override
			public RecipeSerializer<?> getType() {
				return recipe.getType();
			}

			@Override
			public JsonObject serializeAdvancement() {
				return recipe.serializeAdvancement();
			}

			@Override
			public ResourceLocation getAdvancementId() {
				var oldId = recipe.getAdvancementId();

				return new ResourceLocation(oldId.getNamespace(), oldId.getPath()+ nameSuffix);
			}
		}));
	}

	private ResourceLocation from(ItemLike from, ItemLike to) {
		return  ElementalCraftApi.createRL(ForgeRegistries.ITEMS.getKey(to.asItem()).getPath() + FROM + ForgeRegistries.ITEMS.getKey(from.asItem()).getPath());
	}

	private String buildHas(ItemLike item) {
		return "has_" + ForgeRegistries.ITEMS.getKey(item.asItem()).getPath();
	}

	protected static InventoryChangeTrigger.TriggerInstance has(RegistryObject<? extends ItemLike> itemLike) {
		return has(itemLike.get().asItem());
	}
}
