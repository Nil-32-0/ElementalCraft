package metafact.elementalcraft;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import metafact.dpanvil_m.api.data.IDataManager;
import metafact.dpanvil_m.api.imc.DataManagerIMC;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.infusion.tool.ToolInfusion;
import metafact.elementalcraft.api.name.ECNames;
import metafact.elementalcraft.api.rune.Rune;
import metafact.elementalcraft.api.source.trait.SourceTrait;
import metafact.elementalcraft.block.ECBlocks;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import metafact.elementalcraft.block.shrine.properties.ShrineProperties;
import metafact.elementalcraft.block.shrine.upgrade.ShrineUpgrade;
import metafact.elementalcraft.block.source.trait.value.SourceTraitValueProviderTypes;
import metafact.elementalcraft.commands.ECFlux;
import metafact.elementalcraft.config.ECConfig;
import metafact.elementalcraft.container.menu.ECMenus;
import metafact.elementalcraft.data.predicate.block.ECBlockPosPredicateTypes;
import metafact.elementalcraft.entity.ECEntities;
import metafact.elementalcraft.event.ClientEvents;
import metafact.elementalcraft.infusion.tool.effect.ToolInfusionEffectTypes;
import metafact.elementalcraft.item.ECCreativeModeTabs;
import metafact.elementalcraft.item.ECItems;
import metafact.elementalcraft.jewel.Jewels;
import metafact.elementalcraft.loot.ECLootModifiers;
import metafact.elementalcraft.loot.entry.ECLootPoolEntries;
import metafact.elementalcraft.loot.function.ECLootFunctions;
import metafact.elementalcraft.network.message.MessageHandler;
import metafact.elementalcraft.particle.ECParticles;
import metafact.elementalcraft.pureore.PureOreManager;
import metafact.elementalcraft.pureore.injector.PureOreRecipeInjectors;
import metafact.elementalcraft.pureore.loader.IPureOreLoader;
import metafact.elementalcraft.pureore.loader.PureOreLoaderTypes;
import metafact.elementalcraft.recipe.ECRecipeSerializers;
import metafact.elementalcraft.recipe.ECRecipeTypes;
import metafact.elementalcraft.sound.ECSounds;
import metafact.elementalcraft.spell.Spells;
import metafact.elementalcraft.spell.properties.SpellProperties;
import metafact.elementalcraft.world.feature.ECFeatures;
import metafact.elementalcraft.world.feature.placement.ECPlacements;
import metafact.elementalcraft.world.feature.structure.ECStructureTypes;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.Map;

@Mod(ElementalCraftApi.MODID)
public class ElementalCraft {
	
	public static final PureOreManager PURE_ORE_MANAGER = new PureOreManager();

	public static final ResourceKey<IDataManager<ShrineUpgrade>> SHRINE_UPGRADE_MANAGER_KEY = IDataManager.createManagerKey(ElementalCraftApi.createRL(ECNames.SHRINE_UPGRADE));
	public static final IDataManager<ShrineUpgrade> SHRINE_UPGRADE_MANAGER = IDataManager.builder(ShrineUpgrade.class, SHRINE_UPGRADE_MANAGER_KEY)
			.withIdSetter(ShrineUpgrade::setId)
			.merged(ShrineUpgrade::merge)
			.build();

	public static final ResourceKey<IDataManager<SpellProperties>> SPELL_PROPERTIES_MANAGER_KEY = IDataManager.createManagerKey(ElementalCraftApi.createRL(ECNames.SPELL_PROPERTIES));
	public static final IDataManager<SpellProperties> SPELL_PROPERTIES_MANAGER = IDataManager.builder(SpellProperties.class, SPELL_PROPERTIES_MANAGER_KEY)
			.withDefault(SpellProperties.NONE)
			.build();

	public static final ResourceKey<IDataManager<ShrineProperties>> SHRINE_PROPERTIES_MANAGER_KEY = IDataManager.createManagerKey(ElementalCraftApi.createRL(ECNames.SHRINE_PROPERTIES));
	public static final IDataManager<ShrineProperties> SHRINE_PROPERTIES_MANAGER = IDataManager.builder(ShrineProperties.class, SHRINE_PROPERTIES_MANAGER_KEY)
			.withDefault(ShrineProperties.DEFAULT)
			.build();

	public static final ResourceKey<IDataManager<IPureOreLoader>> PURE_ORE_LOADERS_MANAGER_KEY = IDataManager.createManagerKey(ElementalCraftApi.createRL(ECNames.PURE_ORE_LOADER));
	public static final IDataManager<IPureOreLoader> PURE_ORE_LOADERS_MANAGER = IDataManager.builder(IPureOreLoader.class, PURE_ORE_LOADERS_MANAGER_KEY)
			.build();

	public ElementalCraft() {
		var modBus = FMLJavaModLoadingContext.get().getModEventBus();

		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ECConfig.SERVER_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ECConfig.CLIENT_SPEC);

        if (FMLEnvironment.dist.equals(Dist.CLIENT)) {
            MinecraftForge.EVENT_BUS.register(new ClientEvents());
        }

		ECBlocks.register(modBus);
		ECBlockEntityTypes.register(modBus);
		ECItems.register(modBus);
		ECEntities.register(modBus);
		Spells.register(modBus);
		Jewels.register(modBus);
		ECMenus.register(modBus);
		ECParticles.register(modBus);
		ECRecipeTypes.register(modBus);
		ECRecipeSerializers.register(modBus);
		ECFeatures.register(modBus);
		ECStructureTypes.register(modBus);
		ECPlacements.register(modBus);
		ECLootPoolEntries.register(modBus);
		ECLootFunctions.register(modBus);
		ECLootModifiers.register(modBus);
		ECBlockPosPredicateTypes.register(modBus);
		ToolInfusionEffectTypes.register(modBus);
		SourceTraitValueProviderTypes.register(modBus);
		PureOreRecipeInjectors.register(modBus);
		PipeUpgradeTypes.register(modBus);
		ECSounds.register(modBus);
		ECCreativeModeTabs.register(modBus);
		PureOreLoaderTypes.register(modBus);

		modBus.addListener(this::setup);
		modBus.addListener(this::enqueueIMC);
		MinecraftForge.EVENT_BUS.addListener(PURE_ORE_MANAGER::reload);
	}

    public static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
		return ResourceKey.createRegistryKey(ElementalCraftApi.createRL(name));
	}

	public static <T> boolean owns(Map.Entry<ResourceKey<T>, T> entry) {
		return owns(entry.getKey());
	}

	public static boolean owns(ResourceKey<?> key) {
		return owns(key.location());
	}

	public static boolean owns(ResourceLocation location) {
		return ElementalCraftApi.MODID.equals(location.getNamespace());
	}

    private void setup(FMLCommonSetupEvent event) {
		MessageHandler.setup();
		PipeUpgradeTypes.setup();
	}
	
	private void enqueueIMC(InterModEnqueueEvent event) {
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(SHRINE_UPGRADE_MANAGER_KEY, SHRINE_UPGRADE_MANAGER).withCodec(ShrineUpgrade.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(SHRINE_PROPERTIES_MANAGER_KEY, SHRINE_PROPERTIES_MANAGER).withCodec(ShrineProperties.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(SPELL_PROPERTIES_MANAGER_KEY, SPELL_PROPERTIES_MANAGER).withCodec(SpellProperties.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(PURE_ORE_LOADERS_MANAGER_KEY, PURE_ORE_LOADERS_MANAGER).withCodec(IPureOreLoader.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(ElementalCraftApi.RUNE_MANAGER_KEY, ElementalCraftApi.RUNE_MANAGER).withCodec(Rune.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(ElementalCraftApi.TOOL_INFUSION_MANAGER_KEY, ElementalCraftApi.TOOL_INFUSION_MANAGER).withCodec(ToolInfusion.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(ElementalCraftApi.SOURCE_TRAIT_MANAGER_KEY, ElementalCraftApi.SOURCE_TRAIT_MANAGER).withCodec(SourceTrait.CODEC));
	}

    @Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void RegisterServerCommandsEvent(RegisterCommandsEvent event) {
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

            ECFlux.register(dispatcher);
        }
    }
}
