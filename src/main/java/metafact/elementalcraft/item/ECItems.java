package metafact.elementalcraft.item;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import metafact.elementalcraft.ElementalCraft;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.element.ElementTypeTier;
import metafact.elementalcraft.block.ECBlocks;
import metafact.elementalcraft.block.container.AbstractElementContainerBlock;
import metafact.elementalcraft.block.container.ElementContainerBlockItem;
import metafact.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import metafact.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import metafact.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import metafact.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeBlockItem;
import metafact.elementalcraft.item.chisel.ChiselItem;
import metafact.elementalcraft.item.chisel.ChiselTiers;
import metafact.elementalcraft.item.elemental.CrystalItem;
import metafact.elementalcraft.item.elemental.ElementalItem;
import metafact.elementalcraft.item.elemental.FireFuelItem;
import metafact.elementalcraft.item.elemental.LensItem;
import metafact.elementalcraft.item.elemental.ShardItem;
import metafact.elementalcraft.item.holder.ElementHolderItem;
import metafact.elementalcraft.item.holder.PureElementHolderItem;
import metafact.elementalcraft.item.jewel.JewelItem;
import metafact.elementalcraft.item.jewel.JewelModel;
import metafact.elementalcraft.item.pipe.CoverFrameItem;
import metafact.elementalcraft.item.pipe.PipeUpgradeItem;
import metafact.elementalcraft.item.pureore.PureOreItem;
import metafact.elementalcraft.item.rune.RuneItem;
import metafact.elementalcraft.item.rune.RuneModel;
import metafact.elementalcraft.item.source.SourceStabilizerItem;
import metafact.elementalcraft.item.source.analysis.SourceAnalysisGlassItem;
import metafact.elementalcraft.item.source.receptacle.ReceptacleHelper;
import metafact.elementalcraft.item.source.receptacle.ReceptacleItem;
import metafact.elementalcraft.item.spell.FocusItem;
import metafact.elementalcraft.item.spell.ScrollItem;
import metafact.elementalcraft.item.spell.SpellEffectItem;
import metafact.elementalcraft.item.spell.StaffItem;
import metafact.elementalcraft.item.spell.book.SpellBookItem;
import metafact.elementalcraft.property.ECProperties;
import metafact.elementalcraft.registry.RegistryHelper;
import metafact.elementalcraft.spell.SpellHelper;

import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECItems {
	private static final DeferredRegister<Item> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, ElementalCraftApi.MODID);

	public static final RegistryObject<FocusItem> FOCUS = register(FocusItem::new, FocusItem.NAME);
	public static final RegistryObject<StaffItem> STAFF = register(StaffItem::new, StaffItem.NAME);
	public static final RegistryObject<ScrollItem> SCROLL = register(ScrollItem::new, ScrollItem.NAME);
	public static final RegistryObject<SpellBookItem> SPELL_BOOK = register(SpellBookItem::new, SpellBookItem.NAME);
	public static final RegistryObject<ReceptacleItem> RECEPTACLE = register(ReceptacleItem::new, ReceptacleItem.NAME);
	public static final RegistryObject<SourceStabilizerItem> SOURCE_STABILIZER = register(SourceStabilizerItem::new, SourceStabilizerItem.NAME);
	public static final RegistryObject<SourceAnalysisGlassItem> SOURCE_ANALYSIS_GLASS = register(SourceAnalysisGlassItem::new, SourceAnalysisGlassItem.NAME);

    public static final Map<ElementType, RegistryObject<ElementHolderItem>> ELEMENT_HOLDERS = ElementType.ALL_VALID.stream()
            .collect(Collectors.toMap(
                    type -> type,
                    type -> register(() -> new ElementHolderItem(type), ElementHolderItem.generateName(type))
            ));

	public static final RegistryObject<ECItem> PURE_HOLDER_CORE = register(ECItem::new, PureElementHolderItem.NAME + "_core");
	public static final RegistryObject<PureElementHolderItem> PURE_HOLDER = register(PureElementHolderItem::new, PureElementHolderItem.NAME);
	public static final RegistryObject<PureOreItem> PURE_ORE = register(PureOreItem::new, PureOreItem.NAME);
	public static final RegistryObject<RuneItem> RUNE = register(RuneItem::new, RuneItem.NAME);
    public static final RegistryObject<ChiselItem> DRENCHED_IRON_CHISEL = register(() -> new ChiselItem(ChiselTiers.DRENCHED_IRON, new Item.Properties()), ChiselItem.NAME_DRENCHED_IRON);
    public static final RegistryObject<ChiselItem> SWIFT_ALLOY_CHISEL = register(() -> new ChiselItem(ChiselTiers.SWIFT_ALLOY, new Item.Properties()), ChiselItem.NAME_SWIFT_ALLOY);
    public static final RegistryObject<ChiselItem> FIREITE_CHISEL = register(() -> new ChiselItem(ChiselTiers.FIREITE, new Item.Properties()), ChiselItem.NAME_FIREITE);
	public static final RegistryObject<FireFuelItem> ELEMENTAL_FIREFUEL = register(FireFuelItem::new, FireFuelItem.NAME);
	public static final RegistryObject<CoverFrameItem> COVER_FRAME = register(CoverFrameItem::new, CoverFrameItem.NAME);
	public static final RegistryObject<PipeUpgradeItem> ELEMENT_PUMP = register(PipeUpgradeTypes.ELEMENT_PUMP);
	public static final RegistryObject<PipeUpgradeItem> PIPE_PRIORITY_RINGS = register(PipeUpgradeTypes.PIPE_PRIORITY_RINGS);
	public static final RegistryObject<PipeUpgradeItem> ELEMENT_VALVE = register(PipeUpgradeTypes.ELEMENT_VALVE);
	public static final RegistryObject<PipeUpgradeItem> ELEMENT_BEAM = register(PipeUpgradeTypes.ELEMENT_BEAM);

	public static final RegistryObject<Item> ELEMENTOPEDIA = RegistryObject.create(new ResourceLocation("patchouli", "guide_book"), ForgeRegistries.ITEMS);

	public static final RegistryObject<ECItem> INERT_CRYSTAL = register(ECItem::new, "inert_crystal");
	public static final RegistryObject<ECItem> CONTAINED_CRYSTAL = register(ECItem::new, "contained_crystal");
	public static final RegistryObject<ECItem> STRONGLY_CONTAINED_CRYSTAL = register(ECItem::new, "strongly_contained_crystal");
	public static final RegistryObject<ECItem> PURE_CRYSTAL = register(() -> new ECItem().setFoil(true), "pure_crystal");
	public static final RegistryObject<ECItem> DRENCHED_IRON_INGOT = register(ECItem::new, "drenched_iron_ingot");
	public static final RegistryObject<ECItem> DRENCHED_IRON_NUGGET = register(ECItem::new, "drenched_iron_nugget");
	public static final RegistryObject<ECItem> SWIFT_ALLOY_INGOT = register(ECItem::new, "swift_alloy_ingot");
	public static final RegistryObject<ECItem> SWIFT_ALLOY_NUGGET = register(ECItem::new, "swift_alloy_nugget");
	public static final RegistryObject<ECItem> HARDENED_HANDLE = register(ECItem::new, "hardened_handle");
	public static final RegistryObject<ECItem> DRENCHED_SAW_BLADE = register(ECItem::new, "drenched_saw_blade");
	public static final RegistryObject<ECItem> SHRINE_BASE = register(ECItem::new, "shrinebase");
	public static final RegistryObject<ECItem> FIREITE_INGOT = register(ECItem::new, "fireite_ingot");
	public static final RegistryObject<ECItem> FIREITE_NUGGET = register(ECItem::new, "fireite_nugget");
	public static final RegistryObject<ECItem> AIR_SILK = register(ECItem::new, "air_silk");
	public static final RegistryObject<ECItem> SHRINE_UPGRADE_CORE = register(ECItem::new, "shrine_upgrade_core");
    public static final RegistryObject<ECItem> ADVANCED_SHRINE_UPGRADE_CORE = register(ECItem::new, "advanced_shrine_upgrade_core");
	public static final RegistryObject<ECItem> SCROLL_PAPER = register(ECItem::new, "scroll_paper");
	public static final RegistryObject<ECItem> SPRINGALINE_SHARD = register(ECItem::new, "springaline_shard");
	public static final RegistryObject<ECItem> SOLAR_PRISM = register(ECItem::new, "solar_prism");

    public static final Map<ElementType, RegistryObject<CrystalItem>> CRYSTALS = ElementType.ALL_VALID.stream()
            .collect(Collectors.toMap(
                    type -> type,
                    type -> register(() -> new CrystalItem(type), CrystalItem.generateName(type))
            ));
    public static final Map<ElementType, RegistryObject<ShardItem>> SHARDS = ElementType.ALL_VALID.stream()
            .collect(Collectors.toMap(
                    type -> type,
                    type -> register(() -> new ShardItem(type), ShardItem.generateName(type))
            ));
    public static final Map<ElementType, RegistryObject<ShardItem>> POWERFUL_SHARDS = ElementType.ALL_VALID.stream()
            .collect(Collectors.toMap(
                    type -> type,
                    type -> register(() -> new ShardItem(type, 9), ShardItem.generateNamePowerful(type))
            ));
    public static final Map<ElementType, RegistryObject<ElementalItem>> CRUDE_GEMS = ElementType.ALL_VALID.stream()
            .collect(Collectors.toMap(
                    type -> type,
                    type -> register(() -> new ElementalItem(type), "crude_" + type.getSerializedName() + "_gem")
            ));
    public static final Map<ElementType, RegistryObject<ElementalItem>> FINE_GEMS = ElementType.ALL_VALID.stream()
            .collect(Collectors.toMap(
                    type -> type,
                    type -> register(() -> new ElementalItem(type), "fine_" + type.getSerializedName() + "_gem")
            ));
    public static final Map<ElementType, RegistryObject<ElementalItem>> PRISTINE_GEMS = ElementType.ALL_VALID.stream()
            .collect(Collectors.toMap(
                    type -> type,
                    type -> register(() -> new ElementalItem(type), "pristine_" + type.getSerializedName() + "_gem")
            ));

    public static final RegistryObject<ECItem> PRISTINE_SHARD = register(ECItem::new, "pristine_shard");
    public static final RegistryObject<ECItem> AIR_MILL = register(ECItem::new, "air_mill");

    public static final Map<ElementType, RegistryObject<LensItem>> LENSES =
        ElementType.getElementsTier(ElementTypeTier.PRIMORDIAL).stream()
            .collect(Collectors.toMap(
                    type -> type,
                    type -> register(() -> new LensItem(type), LensItem.generateName(type))
            ));

	public static final RegistryObject<ECItem> MINOR_RUNE_SLATE = register(ECItem::new, "minor_rune_slate");
	public static final RegistryObject<ECItem> RUNE_SLATE = register(ECItem::new, "rune_slate");
	public static final RegistryObject<ECItem> MAJOR_RUNE_SLATE = register(ECItem::new, "major_rune_slate");
	public static final RegistryObject<ECItem> UNSET_JEWEL = register(ECItem::new, "unset_jewel");
	public static final RegistryObject<JewelItem> JEWEL = register(JewelItem::new, JewelItem.NAME);

    public static final Map<ElementType, RegistryObject<ElementalItem>> ARTIFICIAL_SOURCE_SEEDS = ElementType.ALL_VALID.stream()
            .collect(Collectors.toMap(
                    type -> type,
                    type -> register(() -> new ElementalItem(type), "artificial_" + type.getSerializedName() + "_source_seed")
            ));
    public static final Map<ElementType, RegistryObject<ElementalItem>> NATURAL_SOURCE_SEEDS =
        ElementType.getElementsTier(ElementTypeTier.PRIMORDIAL).stream()
            .collect(Collectors.toMap(
                    type -> type,
                    type -> register(() -> new ElementalItem(type), "natural_" + type.getSerializedName() + "_source_seed")
            ));

	public static final RegistryObject<SpellEffectItem> REPAIR_HAMMER = register(SpellEffectItem::new, "repair_hammer");


	private ECItems() {}

	@SubscribeEvent
	public static void registerBlockItems(RegisterEvent event) {
		if (!event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
			return;
		}

		IForgeRegistry<Item> registry = event.getForgeRegistry();

		if (registry == null) {
			return;
		}
		RegistryHelper.register(registry, new BlockItem(ECBlocks.FIREITE_BLOCK.get(), ECProperties.Items.FIREITE), ECBlocks.FIREITE_BLOCK);
		RegistryHelper.register(registry, new TranslocationShrineUpgradeBlockItem(ECBlocks.TRANSLOCATION_SHRINE_UPGRADE.get(), ECProperties.Items.DEFAULT_ITEM_PROPERTIES), ECBlocks.TRANSLOCATION_SHRINE_UPGRADE);
		for (Block block : ForgeRegistries.BLOCKS) {
			var registryName = ForgeRegistries.BLOCKS.getKey(block);

			if (registryName != null && ElementalCraft.owns(registryName) && !registry.containsKey(registryName)) {
				BlockItem blockItem;

				if (block instanceof AbstractElementContainerBlock containerBlock) {
					blockItem = new ElementContainerBlockItem(containerBlock, ECProperties.Items.DEFAULT_ITEM_PROPERTIES);
				} else {
					blockItem = new BlockItem(block, ECProperties.Items.DEFAULT_ITEM_PROPERTIES);
				}

				RegistryHelper.register(registry, blockItem, registryName);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void replaceModels(ModelEvent.ModifyBakingResult event) {
		var modelRegistry = event.getModels();

		replaceModels(modelRegistry, RuneItem.NAME, RuneModel::new);
		replaceModels(modelRegistry, JewelItem.NAME, JewelModel::new);
	}

	@OnlyIn(Dist.CLIENT)
	private static void replaceModels(Map<ResourceLocation, BakedModel> modelRegistry, String name, UnaryOperator<BakedModel> modelFactory) {
		modelRegistry.computeIfPresent(new ModelResourceLocation(ElementalCraftApi.createRL(name), "inventory"), (k, v) -> modelFactory.apply(v));
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
		event.register((s, l) -> l == 0 ? -1 : ReceptacleHelper.getElementType(s).getColor(), RECEPTACLE.get());
		event.register((s, l) -> {
			var colors = ElementalCraft.PURE_ORE_MANAGER.getColors(s);

			return colors != null && l < colors.length ? colors[l] : -1;
		}, PURE_ORE.get());
		event.register((s, l) -> l == 0 ? -1 : SpellHelper.getSpell(s).getColor(), SCROLL.get());
        ELEMENT_HOLDERS.values().forEach(holder ->
                event.register((s, l) -> l == 0 ? -1 : ((ElementHolderItem) s.getItem()).getElementType().getColor(), holder.get())
        );
	}

	private static <T extends PipeUpgrade> RegistryObject<PipeUpgradeItem> register(RegistryObject<PipeUpgradeType<T>> pipeUpgrade) {
		return register(() -> new PipeUpgradeItem(pipeUpgrade::get, ECProperties.Items.DEFAULT_ITEM_PROPERTIES), pipeUpgrade.getId().getPath());
	}

	private static <T extends Item> RegistryObject<T> register(Supplier<T> item, String name) {
		return DEFERRED_REGISTER.register(name, item);
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}
}
