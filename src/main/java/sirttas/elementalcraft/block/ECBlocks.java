package sirttas.elementalcraft.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.anchor.TranslocationAnchorBlock;
import sirttas.elementalcraft.block.container.ElementContainerBlock;
import sirttas.elementalcraft.block.container.SmallElementContainerBlock;
import sirttas.elementalcraft.block.container.creative.CreativeElementContainerBlock;
import sirttas.elementalcraft.block.container.reservoir.ReservoirBlock;
import sirttas.elementalcraft.block.diffuser.DiffuserBlock;
import sirttas.elementalcraft.block.evaporator.EvaporatorBlock;
import sirttas.elementalcraft.block.extractor.ExtractorBlock;
import sirttas.elementalcraft.block.extractor.RudimentaryElementExtractorBlock;
import sirttas.elementalcraft.block.extractor.ImprovedExtractorBlock;
import sirttas.elementalcraft.block.instrument.binder.BinderBlock;
import sirttas.elementalcraft.block.instrument.binder.improved.ImprovedBinderBlock;
import sirttas.elementalcraft.block.instrument.crystallizer.CrystallizerBlock;
import sirttas.elementalcraft.block.instrument.enchantment.liquefier.EnchantmentLiquefierBlock;
import sirttas.elementalcraft.block.instrument.infuser.InfuserBlock;
import sirttas.elementalcraft.block.instrument.inscriber.InscriberBlock;
import sirttas.elementalcraft.block.instrument.io.firefurnace.FireFurnaceBlock;
import sirttas.elementalcraft.block.instrument.io.firefurnace.blast.FireBlastFurnaceBlock;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.air.AirMillGrindstoneBlock;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.water.WaterMillGrindstoneBlock;
import sirttas.elementalcraft.block.instrument.io.mill.woodsaw.air.AirMillWoodSawBlock;
import sirttas.elementalcraft.block.instrument.io.mill.woodsaw.water.WaterMillWoodSawBlock;
import sirttas.elementalcraft.block.instrument.io.purifier.PurifierBlock;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlock;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlock;
import sirttas.elementalcraft.block.retriever.RetrieverBlock;
import sirttas.elementalcraft.block.shrine.breeding.BreedingShrineBlock;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrineBlock;
import sirttas.elementalcraft.block.shrine.enderlock.EnderLockShrineBlock;
import sirttas.elementalcraft.block.shrine.firepylon.FirePylonBlock;
import sirttas.elementalcraft.block.shrine.grove.GroveShrineBlock;
import sirttas.elementalcraft.block.shrine.growth.GrowthShrineBlock;
import sirttas.elementalcraft.block.shrine.harvest.HarvestShrineBlock;
import sirttas.elementalcraft.block.shrine.lava.LavaShrineBlock;
import sirttas.elementalcraft.block.shrine.lumber.LumberShrineBlock;
import sirttas.elementalcraft.block.shrine.ore.OreShrineBlock;
import sirttas.elementalcraft.block.shrine.overload.OverloadShrineBlock;
import sirttas.elementalcraft.block.shrine.spawning.SpawningShrineBlock;
import sirttas.elementalcraft.block.shrine.spring.SpringShrineBlock;
import sirttas.elementalcraft.block.shrine.sweet.SweetShrineBlock;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.AccelerationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.overclocked.OverclockedAccelerationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.CapacityShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.EfficiencyShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.OptimizationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.RangeShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.StrengthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.*;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.fortune.FortuneShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.fortune.greater.GreaterFortuneShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BonelessGrowthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.CrystalGrowthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.FillingShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.MysticalGroveShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.PickupShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.StemPollinationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.vertical.PlantingShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.vortex.VortexShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.vacuum.VacuumShrineBlock;
import sirttas.elementalcraft.block.sorter.SorterBlock;
import sirttas.elementalcraft.block.source.SourceBlock;
import sirttas.elementalcraft.block.source.breeder.SourceBreederBlock;
import sirttas.elementalcraft.block.source.breeder.pedestal.SourceBreederPedestalBlock;
import sirttas.elementalcraft.block.source.displacement.plate.BrokenSourceDisplacementPlateBlock;
import sirttas.elementalcraft.block.source.displacement.plate.SourceDisplacementPlateBlock;
import sirttas.elementalcraft.block.spelldesk.SpellDeskBlock;
import sirttas.elementalcraft.block.synthesizer.combustion.CombustionSynthesizerBlock;
import sirttas.elementalcraft.block.synthesizer.cracking.CrackingSynthesizerBlock;
import sirttas.elementalcraft.block.synthesizer.cracking.SculkCrackingSynthesizerBlock;
import sirttas.elementalcraft.block.synthesizer.culinary.CulinarySynthesizerBlock;
import sirttas.elementalcraft.block.synthesizer.draining.DrainingSynthesizerBlock;
import sirttas.elementalcraft.block.synthesizer.mana.ManaSynthesizerBlock;
import sirttas.elementalcraft.block.synthesizer.mill.AirMillSynthesizerBlock;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerBlock;
import sirttas.elementalcraft.block.synthesizer.vibration.VibrationSynthesizerBlock;
import sirttas.elementalcraft.item.elemental.CrystalItem;
import sirttas.elementalcraft.property.ECProperties;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ECBlocks {

    private static final DeferredRegister<Block> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, ElementalCraftApi.MODID);

	private ECBlocks() { }

	public static final RegistryObject<SmallElementContainerBlock> SMALL_CONTAINER = register(SmallElementContainerBlock.NAME, () ->
            new SmallElementContainerBlock(ECProperties.Blocks.CONTAINER));
	public static final RegistryObject<ElementContainerBlock> CONTAINER = register(ElementContainerBlock.NAME, () ->
            new ElementContainerBlock(ECProperties.Blocks.CONTAINER));

    public static final Map<ElementType, RegistryObject<ReservoirBlock>> RESERVOIRS = ElementType.ALL_VALID.stream()
            .collect(Collectors.toMap(
                    type -> type,
                    type -> register(ReservoirBlock.generateName(type), () -> new ReservoirBlock(type, ECProperties.Blocks.CONTAINER))
            ));

	public static final RegistryObject<CreativeElementContainerBlock> CREATIVE_CONTAINER = register(
            CreativeElementContainerBlock.NAME,
            () -> new CreativeElementContainerBlock(ECProperties.Blocks.CONTAINER));
	public static final RegistryObject<ExtractorBlock> EXTRACTOR = registerDefault(ExtractorBlock.NAME, ExtractorBlock::new);
	public static final RegistryObject<ImprovedExtractorBlock> IMPROVED_EXTRACTOR = registerDefault(ImprovedExtractorBlock.NAME, ImprovedExtractorBlock::new);
    public static final RegistryObject<RudimentaryElementExtractorBlock> RUDIMENTARY_EXTRACTOR = registerDefault(RudimentaryElementExtractorBlock.NAME, RudimentaryElementExtractorBlock::new);
	public static final RegistryObject<EvaporatorBlock> EVAPORATOR = registerDefault(EvaporatorBlock.NAME, EvaporatorBlock::new);
	public static final RegistryObject<AirMillSynthesizerBlock> AIR_MILL_SYNTHESIZER = registerDefault(AirMillSynthesizerBlock.NAME, AirMillSynthesizerBlock::new);
    public static final RegistryObject<CombustionSynthesizerBlock> COMBUSTION_SYNTHESIZER = registerDefault(CombustionSynthesizerBlock.NAME, CombustionSynthesizerBlock::new);
    public static final RegistryObject<CrackingSynthesizerBlock> CRACKING_SYNTHESIZER = registerDefault(CrackingSynthesizerBlock.NAME, CrackingSynthesizerBlock::new);
    public static final RegistryObject<CulinarySynthesizerBlock> CULINARY_SYNTHESIZER = registerDefault(CulinarySynthesizerBlock.NAME, CulinarySynthesizerBlock::new);
    public static final RegistryObject<DrainingSynthesizerBlock> DRAINING_SYNTHESIZER = registerDefault(DrainingSynthesizerBlock.NAME, DrainingSynthesizerBlock::new);
    public static final RegistryObject<SculkCrackingSynthesizerBlock> SCULK_CRACKING_SYNTHESIZER = registerDefault(SculkCrackingSynthesizerBlock.NAME, SculkCrackingSynthesizerBlock::new);
    public static final RegistryObject<VibrationSynthesizerBlock> VIBRATION_SYNTHESIZER = registerDefault(VibrationSynthesizerBlock.NAME, VibrationSynthesizerBlock::new);
    public static final RegistryObject<SolarSynthesizerBlock> SOLAR_SYNTHESIZER = registerDefault(SolarSynthesizerBlock.NAME, SolarSynthesizerBlock::new);
	public static final RegistryObject<ManaSynthesizerBlock> MANA_SYNTHESIZER = registerDefault(ManaSynthesizerBlock.NAME, ManaSynthesizerBlock::new);
	public static final RegistryObject<DiffuserBlock> DIFFUSER = registerDefault(DiffuserBlock.NAME, DiffuserBlock::new);
	public static final RegistryObject<InfuserBlock> INFUSER = registerDefault(InfuserBlock.NAME, InfuserBlock::new);
	public static final RegistryObject<BinderBlock> BINDER = registerDefault(BinderBlock.NAME, BinderBlock::new);
	public static final RegistryObject<ImprovedBinderBlock> BINDER_IMPROVED = registerDefault(ImprovedBinderBlock.NAME, ImprovedBinderBlock::new);
	public static final RegistryObject<CrystallizerBlock> CRYSTALLIZER = registerDefault(CrystallizerBlock.NAME, CrystallizerBlock::new);
	public static final RegistryObject<InscriberBlock> INSCRIBER = registerDefault(InscriberBlock.NAME, InscriberBlock::new);
	public static final RegistryObject<WaterMillGrindstoneBlock> WATER_MILL_GRINDSTONE = registerDefault(WaterMillGrindstoneBlock.NAME, WaterMillGrindstoneBlock::new);
	public static final RegistryObject<AirMillGrindstoneBlock> AIR_MILL_GRINDSTONE = registerDefault(AirMillGrindstoneBlock.NAME, AirMillGrindstoneBlock::new);
	public static final RegistryObject<WaterMillWoodSawBlock> WATER_MILL_WOOD_SAW = registerDefault(WaterMillWoodSawBlock.NAME, WaterMillWoodSawBlock::new);
	public static final RegistryObject<AirMillWoodSawBlock> AIR_MILL_WOOD_SAW = registerDefault(AirMillWoodSawBlock.NAME, AirMillWoodSawBlock::new);
	public static final RegistryObject<EnchantmentLiquefierBlock> ENCHANTMENT_LIQUEFIER = registerDefault(EnchantmentLiquefierBlock.NAME, EnchantmentLiquefierBlock::new);
    public static final RegistryObject<PedestalBlock> FIRE_PEDESTAL = register(PedestalBlock.NAME_FIRE, () ->
            new PedestalBlock(ElementType.FIRE, ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES));
	public static final RegistryObject<PedestalBlock> WATER_PEDESTAL = register(PedestalBlock.NAME_WATER, () ->
            new PedestalBlock(ElementType.WATER, ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES));
	public static final RegistryObject<PedestalBlock> EARTH_PEDESTAL = register(PedestalBlock.NAME_EARTH, () ->
            new PedestalBlock(ElementType.EARTH, ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES));
	public static final RegistryObject<PedestalBlock> AIR_PEDESTAL = register(PedestalBlock.NAME_AIR, () ->
            new PedestalBlock(ElementType.AIR, ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES));
	public static final RegistryObject<PureInfuserBlock> PURE_INFUSER = registerDefault(PureInfuserBlock.NAME, PureInfuserBlock::new);
	public static final RegistryObject<FireFurnaceBlock> FIRE_FURNACE = registerDefault(FireFurnaceBlock.NAME, FireFurnaceBlock::new);
	public static final RegistryObject<FireBlastFurnaceBlock> FIRE_BLAST_FURNACE = registerDefault(FireBlastFurnaceBlock.NAME, FireBlastFurnaceBlock::new);
	public static final RegistryObject<PurifierBlock> PURIFIER = registerDefault(PurifierBlock.NAME, PurifierBlock::new);
	public static final RegistryObject<ElementPipeBlock> PIPE_IMPAIRED = register(ElementPipeBlock.NAME_IMPAIRED, () ->
            new ElementPipeBlock(ElementPipeBlock.PipeType.IMPAIRED, ECProperties.Blocks.PIPE));
	public static final RegistryObject<ElementPipeBlock> PIPE = register(ElementPipeBlock.NAME, () ->
            new ElementPipeBlock(ElementPipeBlock.PipeType.STANDARD, ECProperties.Blocks.PIPE));
	public static final RegistryObject<ElementPipeBlock> PIPE_IMPROVED = register(ElementPipeBlock.NAME_IMPROVED, () ->
            new ElementPipeBlock(ElementPipeBlock.PipeType.IMPROVED, ECProperties.Blocks.PIPE));
	public static final RegistryObject<ElementPipeBlock> PIPE_CREATIVE = register(ElementPipeBlock.NAME_CREATIVE, () ->
            new ElementPipeBlock(ElementPipeBlock.PipeType.CREATIVE, ECProperties.Blocks.PIPE));
	public static final RegistryObject<RetrieverBlock> RETRIEVER = registerDefault(RetrieverBlock.NAME, RetrieverBlock::new);
	public static final RegistryObject<SorterBlock> SORTER = registerNoOcclusion(SorterBlock.NAME, SorterBlock::new);
	public static final RegistryObject<SpellDeskBlock> SPELL_DESK = registerDefault(SpellDeskBlock.NAME, SpellDeskBlock::new);
	public static final RegistryObject<FirePylonBlock> FIRE_PYLON = registerNoOcclusion(FirePylonBlock.NAME, FirePylonBlock::new);
	public static final RegistryObject<VacuumShrineBlock> VACUUM_SHRINE = registerNoOcclusion(VacuumShrineBlock.NAME, VacuumShrineBlock::new);
	public static final RegistryObject<GrowthShrineBlock> GROWTH_SHRINE = registerNoOcclusion(GrowthShrineBlock.NAME, GrowthShrineBlock::new);
	public static final RegistryObject<HarvestShrineBlock> HARVEST_SHRINE = registerNoOcclusion(HarvestShrineBlock.NAME, HarvestShrineBlock::new);
	public static final RegistryObject<LumberShrineBlock> LUMBER_SHRINE = registerNoOcclusion(LumberShrineBlock.NAME, LumberShrineBlock::new);
	public static final RegistryObject<LavaShrineBlock> LAVA_SHRINE = registerNoOcclusion(LavaShrineBlock.NAME, LavaShrineBlock::new);
	public static final RegistryObject<OreShrineBlock> ORE_SHRINE = registerNoOcclusion(OreShrineBlock.NAME, OreShrineBlock::new);
	public static final RegistryObject<OverloadShrineBlock> OVERLOAD_SHRINE = registerNoOcclusion(OverloadShrineBlock.NAME, OverloadShrineBlock::new);
	public static final RegistryObject<SweetShrineBlock> SWEET_SHRINE = registerNoOcclusion(SweetShrineBlock.NAME, SweetShrineBlock::new);
	public static final RegistryObject<EnderLockShrineBlock> ENDER_LOCK_SHRINE = registerNoOcclusion(EnderLockShrineBlock.NAME, EnderLockShrineBlock::new);
	public static final RegistryObject<BreedingShrineBlock> BREEDING_SHRINE = registerNoOcclusion(BreedingShrineBlock.NAME, BreedingShrineBlock::new);
	public static final RegistryObject<GroveShrineBlock> GROVE_SHRINE = registerNoOcclusion(GroveShrineBlock.NAME, GroveShrineBlock::new);
	public static final RegistryObject<SpringShrineBlock> SPRING_SHRINE = registerNoOcclusion(SpringShrineBlock.NAME, SpringShrineBlock::new);
	public static final RegistryObject<BuddingShrineBlock> BUDDING_SHRINE = registerNoOcclusion(BuddingShrineBlock.NAME, BuddingShrineBlock::new);
	public static final RegistryObject<SpawningShrineBlock> SPAWNING_SHRINE = registerNoOcclusion(SpawningShrineBlock.NAME, SpawningShrineBlock::new);
	public static final RegistryObject<AccelerationShrineUpgradeBlock> ACCELERATION_SHRINE_UPGRADE = registerNoOcclusion(AccelerationShrineUpgradeBlock.NAME, AccelerationShrineUpgradeBlock::new);
	public static final RegistryObject<OverclockedAccelerationShrineUpgradeBlock> OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE = registerNoOcclusion(OverclockedAccelerationShrineUpgradeBlock.NAME, OverclockedAccelerationShrineUpgradeBlock::new);
	public static final RegistryObject<RangeShrineUpgradeBlock> RANGE_SHRINE_UPGRADE = registerNoOcclusion(RangeShrineUpgradeBlock.NAME, RangeShrineUpgradeBlock::new);
	public static final RegistryObject<CapacityShrineUpgradeBlock> CAPACITY_SHRINE_UPGRADE = registerNoOcclusion(CapacityShrineUpgradeBlock.NAME, CapacityShrineUpgradeBlock::new);
	public static final RegistryObject<EfficiencyShrineUpgradeBlock> EFFICIENCY_SHRINE_UPGRADE = registerNoOcclusion(EfficiencyShrineUpgradeBlock.NAME, EfficiencyShrineUpgradeBlock::new);
	public static final RegistryObject<StrengthShrineUpgradeBlock> STRENGTH_SHRINE_UPGRADE = registerNoOcclusion(StrengthShrineUpgradeBlock.NAME, StrengthShrineUpgradeBlock::new);
    public static final RegistryObject<OverwhelmingStrengthShrineUpgradeBlock> OVERWHELMING_STRENGTH_SHRINE_UPGRADE = registerNoOcclusion(OverwhelmingStrengthShrineUpgradeBlock.NAME, OverwhelmingStrengthShrineUpgradeBlock::new);
	public static final RegistryObject<OptimizationShrineUpgradeBlock> OPTIMIZATION_SHRINE_UPGRADE = registerNoOcclusion(OptimizationShrineUpgradeBlock.NAME, OptimizationShrineUpgradeBlock::new);
	public static final RegistryObject<FortuneShrineUpgradeBlock> FORTUNE_SHRINE_UPGRADE = registerNoOcclusion(FortuneShrineUpgradeBlock.NAME, FortuneShrineUpgradeBlock::new);
	public static final RegistryObject<GreaterFortuneShrineUpgradeBlock> GREATER_FORTUNE_SHRINE_UPGRADE = registerNoOcclusion(GreaterFortuneShrineUpgradeBlock.NAME, GreaterFortuneShrineUpgradeBlock::new);
    public static final RegistryObject<SilkTouchShrineUpgradeBlock> SILK_TOUCH_SHRINE_UPGRADE = registerNoOcclusion(SilkTouchShrineUpgradeBlock.NAME, SilkTouchShrineUpgradeBlock::new);
	public static final RegistryObject<PlantingShrineUpgradeBlock> PLANTING_SHRINE_UPGRADE = registerNoOcclusion(PlantingShrineUpgradeBlock.NAME, PlantingShrineUpgradeBlock::new);
	public static final RegistryObject<BonelessGrowthShrineUpgradeBlock> BONELESS_GROWTH_SHRINE_UPGRADE = registerNoOcclusion(BonelessGrowthShrineUpgradeBlock.NAME, BonelessGrowthShrineUpgradeBlock::new);
	public static final RegistryObject<PickupShrineUpgradeBlock> PICKUP_SHRINE_UPGRADE = registerNoOcclusion(PickupShrineUpgradeBlock.NAME, PickupShrineUpgradeBlock::new);
	public static final RegistryObject<VortexShrineUpgradeBlock> VORTEX_SHRINE_UPGRADE = registerNoOcclusion(VortexShrineUpgradeBlock.NAME, VortexShrineUpgradeBlock::new);
	public static final RegistryObject<NectarShrineUpgradeBlock> NECTAR_SHRINE_UPGRADE = registerNoOcclusion(NectarShrineUpgradeBlock.NAME, NectarShrineUpgradeBlock::new);
	public static final RegistryObject<MysticalGroveShrineUpgradeBlock> MYSTICAL_GROVE_SHRINE_UPGRADE = registerNoOcclusion(MysticalGroveShrineUpgradeBlock.NAME, MysticalGroveShrineUpgradeBlock::new);
	public static final RegistryObject<StemPollinationShrineUpgradeBlock> STEM_POLLINATION_SHRINE_UPGRADE = registerNoOcclusion(StemPollinationShrineUpgradeBlock.NAME, StemPollinationShrineUpgradeBlock::new);
	public static final RegistryObject<ProtectionShrineUpgradeBlock> PROTECTION_SHRINE_UPGRADE = registerNoOcclusion(ProtectionShrineUpgradeBlock.NAME, ProtectionShrineUpgradeBlock::new);
	public static final RegistryObject<FillingShrineUpgradeBlock> FILLING_SHRINE_UPGRADE = registerNoOcclusion(FillingShrineUpgradeBlock.NAME, FillingShrineUpgradeBlock::new);
	public static final RegistryObject<SpringalineShrineUpgradeBlock> SPRINGALINE_SHRINE_UPGRADE = registerNoOcclusion(SpringalineShrineUpgradeBlock.NAME, SpringalineShrineUpgradeBlock::new);
	public static final RegistryObject<CrystalHarvestShrineUpgradeBlock> CRYSTAL_HARVEST_SHRINE_UPGRADE = registerNoOcclusion(CrystalHarvestShrineUpgradeBlock.NAME, CrystalHarvestShrineUpgradeBlock::new);
	public static final RegistryObject<CrystalGrowthShrineUpgradeBlock> CRYSTAL_GROWTH_SHRINE_UPGRADE = registerNoOcclusion(CrystalGrowthShrineUpgradeBlock.NAME, CrystalGrowthShrineUpgradeBlock::new);
	public static final RegistryObject<TranslocationShrineUpgradeBlock> TRANSLOCATION_SHRINE_UPGRADE = registerNoOcclusion(TranslocationShrineUpgradeBlock.NAME, TranslocationShrineUpgradeBlock::new);
	public static final RegistryObject<SourceBlock> SOURCE = register(SourceBlock.NAME, () -> new SourceBlock(ECProperties.Blocks.SOURCE));

    public static final Map<ElementType, RegistryObject<SourceDisplacementPlateBlock>> SOURCE_DISPLACEMENT_PLATES = ElementType.ALL_VALID.stream()
            .collect(Collectors.toMap(
                    type -> type,
                    type -> register(SourceDisplacementPlateBlock.generateName(type), () ->
                            new SourceDisplacementPlateBlock(type, ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES)
                    )
            ));

	public static final RegistryObject<BrokenSourceDisplacementPlateBlock> BROKEN_SOURCE_DISPLACEMENT_PLATE = registerDefault(BrokenSourceDisplacementPlateBlock.NAME, BrokenSourceDisplacementPlateBlock::new);
	public static final RegistryObject<SourceBreederBlock> SOURCE_BREEDER = registerDefault(SourceBreederBlock.NAME, SourceBreederBlock::new);
	public static final RegistryObject<SourceBreederPedestalBlock> SOURCE_BREEDER_PEDESTAL = registerNoOcclusion(SourceBreederPedestalBlock.NAME, SourceBreederPedestalBlock::new);
	public static final RegistryObject<TranslocationAnchorBlock> TRANSLOCATION_ANCHOR = registerDefault(TranslocationAnchorBlock.NAME, TranslocationAnchorBlock::new);


	public static final RegistryObject<DropExperienceBlock> CRYSTAL_ORE = register("inert_crystal_ore", () ->
            new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE), UniformInt.of(0, 3)));
	public static final RegistryObject<DropExperienceBlock> DEEPSLATE_CRYSTAL_ORE = register("deepslate_inert_crystal_ore", () ->
            new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE), UniformInt.of(0, 3)));
	public static final RegistryObject<Block> WHITE_ROCK = registerSimple("whiterock", ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<SlabBlock> WHITE_ROCK_SLAB = registerSlab(WHITE_ROCK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<StairBlock> WHITE_ROCK_STAIRS = registerStairs(WHITE_ROCK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<WallBlock> WHITE_ROCK_WALL = registerWall(WHITE_ROCK, ECProperties.Blocks.WHITEROCK);

	public static final RegistryObject<FenceBlock> WHITE_ROCK_FENCE = registerFence(WHITE_ROCK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<Block> WHITE_ROCK_BRICK = registerSimple("whiterock_brick", ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<SlabBlock> WHITE_ROCK_BRICK_SLAB = registerSlab(WHITE_ROCK_BRICK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<StairBlock> WHITE_ROCK_BRICK_STAIRS = registerStairs(WHITE_ROCK_BRICK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<WallBlock> WHITE_ROCK_BRICK_WALL = registerWall(WHITE_ROCK_BRICK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<Block> MOSSY_WHITE_ROCK = registerSimple("whiterock_mossy", ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<SlabBlock> MOSSY_WHITE_ROCK_SLAB = registerSlab(MOSSY_WHITE_ROCK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<StairBlock> MOSSY_WHITE_ROCK_STAIRS = registerStairs(MOSSY_WHITE_ROCK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<WallBlock> MOSSY_WHITE_ROCK_WALL = registerWall(MOSSY_WHITE_ROCK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<Block> BURNT_WHITE_ROCK = registerSimple("whiterock_burnt", ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<SlabBlock> BURNT_WHITE_ROCK_SLAB = registerSlab(BURNT_WHITE_ROCK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<StairBlock> BURNT_WHITE_ROCK_STAIRS = registerStairs(BURNT_WHITE_ROCK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<WallBlock> BURNT_WHITE_ROCK_WALL = registerWall(BURNT_WHITE_ROCK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<Block> PURE_ROCK = registerSimple("purerock", ECProperties.Blocks.PUREROCK);
	public static final RegistryObject<SlabBlock> PURE_ROCK_SLAB = registerSlab(PURE_ROCK, ECProperties.Blocks.PUREROCK);
	public static final RegistryObject<StairBlock> PURE_ROCK_STAIRS = registerStairs(PURE_ROCK, ECProperties.Blocks.PUREROCK);
	public static final RegistryObject<WallBlock> PURE_ROCK_WALL = registerWall(PURE_ROCK, ECProperties.Blocks.PUREROCK);
	public static final RegistryObject<GlassBlock> BURNT_GLASS = registerGlass("burnt_glass");
	public static final RegistryObject<IronBarsBlock> BURNT_GLASS_PANE = registerGlassPane(BURNT_GLASS);
	public static final RegistryObject<Block> DRENCHED_IRON_BLOCK = registerSimple("drenched_iron_block", BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK));
	public static final RegistryObject<Block> SWIFT_ALLOY_BLOCK = registerSimple("swift_alloy_block", BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK));
	public static final RegistryObject<Block> FIREITE_BLOCK = registerSimple("fireite_block", BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK));
	public static final RegistryObject<Block> INERT_CRYSTAL_BLOCK = registerSimple("inert_crystal_block");

    public static final Map<ElementType, RegistryObject<Block>> CRYSTAL_BLOCKS = ElementType.ALL_VALID.stream()
            .collect(Collectors.toMap(
                    type -> type,
                    type -> registerSimple(CrystalItem.generateNameBlock(type))
            ));

	public static final RegistryObject<AmethystBlock> SPRINGALINE_BLOCK = register("springaline_block", () -> new AmethystBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)));
	public static final RegistryObject<AmethystClusterBlock> SPRINGALINE_CLUSTER = register("springaline_cluster", () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER)));
	public static final RegistryObject<AmethystClusterBlock> LARGE_SPRINGALINE_BUD = register("large_springaline_bud", () -> new AmethystClusterBlock(5, 3, BlockBehaviour.Properties.copy(Blocks.LARGE_AMETHYST_BUD)));
	public static final RegistryObject<AmethystClusterBlock> MEDIUM_SPRINGALINE_BUD = register("medium_springaline_bud", () -> new AmethystClusterBlock(4, 3, BlockBehaviour.Properties.copy(Blocks.MEDIUM_AMETHYST_BUD)));
	public static final RegistryObject<AmethystClusterBlock> SMALL_SPRINGALINE_BUD = register("small_springaline_bud", () -> new AmethystClusterBlock(3, 4, BlockBehaviour.Properties.copy(Blocks.SMALL_AMETHYST_BUD)));
	public static final RegistryObject<GlassBlock> SPRINGALINE_GLASS = registerGlass("springaline_glass");
	public static final RegistryObject<IronBarsBlock> SPRINGALINE_GLASS_PANE = registerGlassPane(SPRINGALINE_GLASS);
	public static final RegistryObject<Block> SPRINGALINE_LANTERN = register("springaline_lantern", () -> new Block(BlockBehaviour.Properties.of()
			.mapColor(MapColor.QUARTZ)
			.strength(0.3F)
			.sound(SoundType.GLASS)
			.lightLevel(s -> 15)));

	private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
		return DEFERRED_REGISTER.register(name, block);
	}

    private static <T extends Block> RegistryObject<T> registerDefault(String name, Function<BlockBehaviour.Properties, T> block) {
        return register(name, () -> block.apply(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES));
    }

    private static <T extends Block> RegistryObject<T> registerNoOcclusion(String name, Function<BlockBehaviour.Properties, T> block) {
        return register(name, () -> block.apply(ECProperties.Blocks.DEFAULT_NO_OCCLUSION));
    }

	private static RegistryObject<Block> registerSimple(String name, BlockBehaviour.Properties properties) {
        return register(name, () -> new Block(properties));
	}

	private static RegistryObject<Block> registerSimple(String name) {
        return registerSimple(name, ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES);
	}

	private static <T extends Block> RegistryObject<SlabBlock> registerSlab(RegistryObject<T> block, BlockBehaviour.Properties properties) {
		return register(block.getId().getPath() + "_slab", () -> new SlabBlock(properties));
	}

	private static <T extends Block> RegistryObject<StairBlock> registerStairs(RegistryObject<T> block, BlockBehaviour.Properties properties) {
		return register(block.getId().getPath() + "_stairs", () -> new StairBlock(() -> block.get().defaultBlockState(), properties));
	}

	private static <T extends Block> RegistryObject<WallBlock> registerWall(RegistryObject<T> block, BlockBehaviour.Properties properties) {
		return register(block.getId().getPath() + "_wall", () -> new WallBlock(properties));
	}

	private static <T extends Block> RegistryObject<FenceBlock> registerFence(RegistryObject<T> block, BlockBehaviour.Properties properties) {
		return register(block.getId().getPath() + "_fence", () -> new FenceBlock(properties));
	}

	private static RegistryObject<GlassBlock> registerGlass(String name) {
        return register(name, () -> new GlassBlock(ECProperties.Blocks.GLASS));
	}
	private static RegistryObject<IronBarsBlock> registerGlassPane(RegistryObject<? extends GlassBlock> block) {
		return register(block.getId().getPath() + "_pane", () -> new IronBarsBlock(ECProperties.Blocks.GLASS_PANE));
    }

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}
}
