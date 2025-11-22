package metafact.elementalcraft.datagen;

import metafact.elementalcraft.datagen.tag.*;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.block.pipe.ElementPipeBlock;
import metafact.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import metafact.elementalcraft.datagen.interaction.ECSilentGearMaterialProvider;
import metafact.elementalcraft.datagen.loot.ECLootTableProvider;
import metafact.elementalcraft.datagen.managed.ECRemapKeysProvider;
import metafact.elementalcraft.datagen.managed.RunesProvider;
import metafact.elementalcraft.datagen.managed.SourceTraitsProvider;
import metafact.elementalcraft.datagen.managed.SpellPropertiesProvider;
import metafact.elementalcraft.datagen.managed.ToolInfusionProvider;
import metafact.elementalcraft.datagen.managed.pure.ore.loader.loader.PureOreLoaderProvider;
import metafact.elementalcraft.datagen.managed.shrine.ShrinePropertiesProvider;
import metafact.elementalcraft.datagen.managed.shrine.ShrineUpgradeProvider;
import metafact.elementalcraft.datagen.recipe.ECRecipeProvider;
import metafact.elementalcraft.datagen.registry.ECDamageTypeProvider;
import metafact.elementalcraft.datagen.registry.ECTrimMaterialProvider;
import metafact.elementalcraft.datagen.registry.world.ECBiomeModifierProvider;
import metafact.elementalcraft.datagen.registry.world.ECFeaturesProvider;
import metafact.elementalcraft.datagen.registry.world.ECStructureSetsProvider;
import metafact.elementalcraft.datagen.registry.world.ECStructuresProvider;
import metafact.elementalcraft.interaction.ECinteractions;

import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECDataGenerators {

	private ECDataGenerators() {}
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		PipeUpgradeTypes.setup();

		var generator = event.getGenerator();
		var output = generator.getPackOutput();
		var fileHelper = event.getExistingFileHelper();
		var includeServer = event.includeServer();
		var includeClient = event.includeClient();

		var itemModelProvider = new ECItemModelProvider(output, fileHelper);
		var datapackProvider = generator.addProvider(includeServer, new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), new RegistrySetBuilder()
				.add(Registries.PLACED_FEATURE, new ECFeaturesProvider())
				.add(Registries.STRUCTURE, new ECStructuresProvider())
				.add(Registries.STRUCTURE_SET, new ECStructureSetsProvider())
				.add(ForgeRegistries.Keys.BIOME_MODIFIERS, new ECBiomeModifierProvider())
				.add(Registries.TRIM_MATERIAL, new ECTrimMaterialProvider())
                .add(Registries.DAMAGE_TYPE, new ECDamageTypeProvider()),
				Set.of(ElementalCraftApi.MODID)));
		var registries = datapackProvider.getRegistryProvider();

		generator.addProvider(includeClient, new ECSpriteSourceProvider(output, fileHelper));
		generator.addProvider(includeServer, new ECLootTableProvider(output));
		generator.addProvider(includeClient, new ECBlockStateProvider(output, fileHelper));
		generator.addProvider(includeClient, itemModelProvider);
		var blockTagsProvider = generator.addProvider(includeServer, new ECBlockTagsProvider(output, registries, fileHelper));
		generator.addProvider(includeServer, new ECItemTagsProvider(output, registries, blockTagsProvider.contentsGetter(), fileHelper));
		generator.addProvider(includeServer, new ECBiomeTagsProvider(output, registries, fileHelper));
        generator.addProvider(includeServer, new ECDamageTypeTagsProvider(output, registries, fileHelper));
        generator.addProvider(includeServer, new ECGameEventTagsProvider(output, registries, fileHelper));
		generator.addProvider(includeServer, new ECRecipeProvider(output, event.getLookupProvider(), fileHelper));
		generator.addProvider(includeServer, new ForgeAdvancementProvider(output, registries, fileHelper, List.of(new ECAdvancementProvider())));
		generator.addProvider(includeServer, new RunesProvider(output, registries, itemModelProvider));
		generator.addProvider(includeServer, new ShrineUpgradeProvider(output, registries));
		generator.addProvider(includeServer, new SpellPropertiesProvider(output, registries));
		generator.addProvider(includeServer, new ToolInfusionProvider(output, registries));
		generator.addProvider(includeServer, new SourceTraitsProvider(output, registries));
		generator.addProvider(includeServer, new ShrinePropertiesProvider(output, registries));
		generator.addProvider(includeServer, new PureOreLoaderProvider(output, registries));
		generator.addProvider(includeServer, new ECRemapKeysProvider(output, registries));
		if (ECinteractions.isSilentGearActive()) {
			generator.addProvider(includeServer, new ECSilentGearMaterialProvider(generator));
		}
	}

	public static String getPipeTexture(ElementPipeBlock.PipeType type) {
		return switch (type) {
			case IMPAIRED -> "iron";
			case STANDARD -> "brass";
			case IMPROVED -> "pure_iron";
			case CREATIVE -> "creative_iron";
		};
	}

}
