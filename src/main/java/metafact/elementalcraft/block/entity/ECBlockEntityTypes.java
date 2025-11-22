package metafact.elementalcraft.block.entity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.block.ECBlocks;
import metafact.elementalcraft.block.container.ElementContainerBlock;
import metafact.elementalcraft.block.container.ElementContainerBlockEntity;
import metafact.elementalcraft.block.container.creative.CreativeElementContainerBlockEntity;
import metafact.elementalcraft.block.container.reservoir.ReservoirBlock;
import metafact.elementalcraft.block.container.reservoir.ReservoirBlockEntity;
import metafact.elementalcraft.block.diffuser.DiffuserBlockEntity;
import metafact.elementalcraft.block.evaporator.EvaporatorBlockEntity;
import metafact.elementalcraft.block.extractor.ExtractorBlock;
import metafact.elementalcraft.block.extractor.ExtractorBlockEntity;
import metafact.elementalcraft.block.instrument.binder.BinderBlockEntity;
import metafact.elementalcraft.block.instrument.binder.improved.ImprovedBinderBlockEntity;
import metafact.elementalcraft.block.instrument.crystallizer.CrystallizerBlockEntity;
import metafact.elementalcraft.block.instrument.enchantment.liquefier.EnchantmentLiquefierBlockEntity;
import metafact.elementalcraft.block.instrument.infuser.InfuserBlockEntity;
import metafact.elementalcraft.block.instrument.inscriber.InscriberBlockEntity;
import metafact.elementalcraft.block.instrument.io.firefurnace.FireFurnaceBlockEntity;
import metafact.elementalcraft.block.instrument.io.firefurnace.blast.FireBlastFurnaceBlockEntity;
import metafact.elementalcraft.block.instrument.io.mill.grindstone.air.AirMillGrindstoneBlockEntity;
import metafact.elementalcraft.block.instrument.io.mill.grindstone.water.WaterMillGrindstoneBlockEntity;
import metafact.elementalcraft.block.instrument.io.mill.woodsaw.air.AirMillWoodSawBlockEntity;
import metafact.elementalcraft.block.instrument.io.mill.woodsaw.water.WaterMillWoodSawBlockEntity;
import metafact.elementalcraft.block.instrument.io.purifier.PurifierBlockEntity;
import metafact.elementalcraft.block.pipe.ElementPipeBlock;
import metafact.elementalcraft.block.pipe.ElementPipeBlockEntity;
import metafact.elementalcraft.block.pureinfuser.PureInfuserBlockEntity;
import metafact.elementalcraft.block.pureinfuser.pedestal.PedestalBlock;
import metafact.elementalcraft.block.pureinfuser.pedestal.PedestalBlockEntity;
import metafact.elementalcraft.block.shrine.breeding.BreedingShrineBlockEntity;
import metafact.elementalcraft.block.shrine.budding.BuddingShrineBlockEntity;
import metafact.elementalcraft.block.shrine.enderlock.EnderLockShrineBlockEntity;
import metafact.elementalcraft.block.shrine.firepylon.FirePylonBlockEntity;
import metafact.elementalcraft.block.shrine.grove.GroveShrineBlockEntity;
import metafact.elementalcraft.block.shrine.growth.GrowthShrineBlockEntity;
import metafact.elementalcraft.block.shrine.harvest.HarvestShrineBlockEntity;
import metafact.elementalcraft.block.shrine.melting.MeltingShrineBlockEntity;
import metafact.elementalcraft.block.shrine.lumber.LumberShrineBlockEntity;
import metafact.elementalcraft.block.shrine.ore.OreShrineBlockEntity;
import metafact.elementalcraft.block.shrine.overload.OverloadShrineBlockEntity;
import metafact.elementalcraft.block.shrine.spawning.SpawningShrineBlockEntity;
import metafact.elementalcraft.block.shrine.spring.SpringShrineBlockEntity;
import metafact.elementalcraft.block.shrine.sweet.SweetShrineBlockEntity;
import metafact.elementalcraft.block.shrine.upgrade.acceleration.AccelerationShrineUpgradeBlockEntity;
import metafact.elementalcraft.block.shrine.upgrade.acceleration.overclocked.OverclockedAccelerationShrineUpgradeBlockEntity;
import metafact.elementalcraft.block.shrine.upgrade.horizontal.fortune.greater.GreaterFortuneShrineUpgradeBlockEntity;
import metafact.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeBlockEntity;
import metafact.elementalcraft.block.shrine.upgrade.vortex.VortexShrineUpgradeBlockEntity;
import metafact.elementalcraft.block.shrine.vacuum.VacuumShrineBlockEntity;
import metafact.elementalcraft.block.sorter.SorterBlockEntity;
import metafact.elementalcraft.block.source.SourceBlockEntity;
import metafact.elementalcraft.block.source.breeder.SourceBreederBlockEntity;
import metafact.elementalcraft.block.source.breeder.pedestal.SourceBreederPedestalBlockEntity;
import metafact.elementalcraft.block.source.displacement.plate.SourceDisplacementPlateBlock;
import metafact.elementalcraft.block.source.displacement.plate.SourceDisplacementPlateBlockEntity;
import metafact.elementalcraft.block.synthesizer.combustion.CombustionSynthesizerBlockEntity;
import metafact.elementalcraft.block.synthesizer.cracking.CrackingSynthesizerBlockEntity;
import metafact.elementalcraft.block.synthesizer.cracking.SculkCrackingSynthesizerBlockEntity;
import metafact.elementalcraft.block.synthesizer.culinary.CulinarySynthesizerBlockEntity;
import metafact.elementalcraft.block.synthesizer.draining.DrainingSynthesizerBlockEntity;
import metafact.elementalcraft.block.synthesizer.mana.ManaSynthesizerBlock;
import metafact.elementalcraft.block.synthesizer.mill.AirMillSynthesizerBlockEntity;
import metafact.elementalcraft.block.synthesizer.solar.SolarSynthesizerBlockEntity;
import metafact.elementalcraft.block.synthesizer.vibration.VibrationSynthesizerBlockEntity;

import java.util.Arrays;
import java.util.function.Supplier;

public class ECBlockEntityTypes {

    private static final DeferredRegister<BlockEntityType<?>> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ElementalCraftApi.MODID);

    public static final RegistryObject<BlockEntityType<SourceBlockEntity>> SOURCE = register(SourceBlockEntity::new, ECBlocks.SOURCE);
    public static final RegistryObject<BlockEntityType<ElementContainerBlockEntity>> CONTAINER = register(() -> builder(ElementContainerBlockEntity::new, ECBlocks.CONTAINER, ECBlocks.SMALL_CONTAINER), ElementContainerBlock.NAME);

    public static final RegistryObject<BlockEntityType<ReservoirBlockEntity>> RESERVOIR = register(() ->
            BlockEntityType.Builder.of(
                ReservoirBlockEntity::new,
                ECBlocks.RESERVOIRS.values().stream().map(RegistryObject::get).toArray(Block[]::new)
            ),
            ReservoirBlock.NAME
    );

    public static final RegistryObject<BlockEntityType<CreativeElementContainerBlockEntity>> CREATIVE_CONTAINER = register(CreativeElementContainerBlockEntity::new, ECBlocks.CREATIVE_CONTAINER);
    public static final RegistryObject<BlockEntityType<ExtractorBlockEntity>> EXTRACTOR = register(() -> builder(ExtractorBlockEntity::new, ECBlocks.RUDIMENTARY_EXTRACTOR, ECBlocks.EXTRACTOR, ECBlocks.IMPROVED_EXTRACTOR), ExtractorBlock.NAME);
    public static final RegistryObject<BlockEntityType<EvaporatorBlockEntity>> EVAPORATOR = register(EvaporatorBlockEntity::new, ECBlocks.EVAPORATOR);
    public static final RegistryObject<BlockEntityType<AirMillSynthesizerBlockEntity>> AIR_MILL_SYNTHESIZER = register(AirMillSynthesizerBlockEntity::new, ECBlocks.AIR_MILL_SYNTHESIZER);
    public static final RegistryObject<BlockEntityType<CombustionSynthesizerBlockEntity>> COMBUSTION_SYNTHESIZER = register(CombustionSynthesizerBlockEntity::new, ECBlocks.COMBUSTION_SYNTHESIZER);
    public static final RegistryObject<BlockEntityType<CrackingSynthesizerBlockEntity>> CRACKING_SYNTHESIZER = register(CrackingSynthesizerBlockEntity::new, ECBlocks.CRACKING_SYNTHESIZER);
    public static final RegistryObject<BlockEntityType<CulinarySynthesizerBlockEntity>> CULINARY_SYNTHESIZER = register(CulinarySynthesizerBlockEntity::new, ECBlocks.CULINARY_SYNTHESIZER);
    public static final RegistryObject<BlockEntityType<DrainingSynthesizerBlockEntity>> DRAINING_SYNTHESIZER = register(DrainingSynthesizerBlockEntity::new, ECBlocks.DRAINING_SYNTHESIZER);
    public static final RegistryObject<BlockEntityType<SculkCrackingSynthesizerBlockEntity>> SCULK_CRACKING_SYNTHESIZER = register(SculkCrackingSynthesizerBlockEntity::new, ECBlocks.SCULK_CRACKING_SYNTHESIZER);
    public static final RegistryObject<BlockEntityType<SolarSynthesizerBlockEntity>> SOLAR_SYNTHESIZER = register(SolarSynthesizerBlockEntity::new, ECBlocks.SOLAR_SYNTHESIZER);
    public static final RegistryObject<BlockEntityType<SolarSynthesizerBlockEntity>> MANA_SYNTHESIZER = register(ManaSynthesizerBlock::createBlockEntity, ECBlocks.MANA_SYNTHESIZER);
    public static final RegistryObject<BlockEntityType<VibrationSynthesizerBlockEntity>> VIBRATION_SYNTHESIZER = register(VibrationSynthesizerBlockEntity::new, ECBlocks.VIBRATION_SYNTHESIZER);
    public static final RegistryObject<BlockEntityType<DiffuserBlockEntity>> DIFFUSER = register(DiffuserBlockEntity::new, ECBlocks.DIFFUSER);
    public static final RegistryObject<BlockEntityType<InfuserBlockEntity>> INFUSER = register(InfuserBlockEntity::new, ECBlocks.INFUSER);
    public static final RegistryObject<BlockEntityType<BinderBlockEntity>> BINDER = register(BinderBlockEntity::new, ECBlocks.BINDER);
    public static final RegistryObject<BlockEntityType<ImprovedBinderBlockEntity>> BINDER_IMPROVED = register(ImprovedBinderBlockEntity::new, ECBlocks.BINDER_IMPROVED);
    public static final RegistryObject<BlockEntityType<CrystallizerBlockEntity>> CRYSTALLIZER = register(CrystallizerBlockEntity::new, ECBlocks.CRYSTALLIZER);
    public static final RegistryObject<BlockEntityType<InscriberBlockEntity>> INSCRIBER = register(InscriberBlockEntity::new, ECBlocks.INSCRIBER);
    public static final RegistryObject<BlockEntityType<WaterMillGrindstoneBlockEntity>> WATER_MILL_GRINDSTONE = register(WaterMillGrindstoneBlockEntity::new, ECBlocks.WATER_MILL_GRINDSTONE);
    public static final RegistryObject<BlockEntityType<AirMillGrindstoneBlockEntity>> AIR_MILL_GRINDSTONE = register(AirMillGrindstoneBlockEntity::new, ECBlocks.AIR_MILL_GRINDSTONE);
    public static final RegistryObject<BlockEntityType<WaterMillWoodSawBlockEntity>> WATER_MILL_WOOD_SAW = register(WaterMillWoodSawBlockEntity::new, ECBlocks.WATER_MILL_WOOD_SAW);
    public static final RegistryObject<BlockEntityType<AirMillWoodSawBlockEntity>> AIR_MILL_WOOD_SAW = register(AirMillWoodSawBlockEntity::new, ECBlocks.AIR_MILL_WOOD_SAW);
    public static final RegistryObject<BlockEntityType<EnchantmentLiquefierBlockEntity>> ENCHANTMENT_LIQUEFIER = register(EnchantmentLiquefierBlockEntity::new, ECBlocks.ENCHANTMENT_LIQUEFIER);
    public static final RegistryObject<BlockEntityType<PedestalBlockEntity>> PEDESTAL = register(() -> builder(PedestalBlockEntity::new, ECBlocks.FIRE_PEDESTAL, ECBlocks.WATER_PEDESTAL, ECBlocks.EARTH_PEDESTAL, ECBlocks.AIR_PEDESTAL), PedestalBlock.NAME);
    public static final RegistryObject<BlockEntityType<PureInfuserBlockEntity>> PURE_INFUSER = register(PureInfuserBlockEntity::new, ECBlocks.PURE_INFUSER);
    public static final RegistryObject<BlockEntityType<FireFurnaceBlockEntity>> FIRE_FURNACE = register(FireFurnaceBlockEntity::new, ECBlocks.FIRE_FURNACE);
    public static final RegistryObject<BlockEntityType<FireBlastFurnaceBlockEntity>> FIRE_BLAST_FURNACE = register(FireBlastFurnaceBlockEntity::new, ECBlocks.FIRE_BLAST_FURNACE);
    public static final RegistryObject<BlockEntityType<PurifierBlockEntity>> PURIFIER = register(PurifierBlockEntity::new, ECBlocks.PURIFIER);
    public static final RegistryObject<BlockEntityType<ElementPipeBlockEntity>> PIPE = register(() -> builder(ElementPipeBlockEntity::new, ECBlocks.PIPE_IMPAIRED, ECBlocks.PIPE, ECBlocks.PIPE_IMPROVED, ECBlocks.PIPE_CREATIVE), ElementPipeBlock.NAME);
    public static final RegistryObject<BlockEntityType<FirePylonBlockEntity>> FIRE_PYLON = register(FirePylonBlockEntity::new, ECBlocks.FIRE_PYLON);
    public static final RegistryObject<BlockEntityType<VacuumShrineBlockEntity>> VACUUM_SHRINE = register(VacuumShrineBlockEntity::new, ECBlocks.VACUUM_SHRINE);
    public static final RegistryObject<BlockEntityType<GrowthShrineBlockEntity>> GROWTH_SHRINE = register(GrowthShrineBlockEntity::new, ECBlocks.GROWTH_SHRINE);
    public static final RegistryObject<BlockEntityType<HarvestShrineBlockEntity>> HARVEST_SHRINE = register(HarvestShrineBlockEntity::new, ECBlocks.HARVEST_SHRINE);
    public static final RegistryObject<BlockEntityType<LumberShrineBlockEntity>> LUMBER_SHRINE = register(LumberShrineBlockEntity::new, ECBlocks.LUMBER_SHRINE);
    public static final RegistryObject<BlockEntityType<MeltingShrineBlockEntity>> MELTING_SHRINE = register(MeltingShrineBlockEntity::new, ECBlocks.MELTING_SHRINE);
    public static final RegistryObject<BlockEntityType<OreShrineBlockEntity>> ORE_SHRINE = register(OreShrineBlockEntity::new, ECBlocks.ORE_SHRINE);
    public static final RegistryObject<BlockEntityType<OverloadShrineBlockEntity>> OVERLOAD_SHRINE = register(OverloadShrineBlockEntity::new, ECBlocks.OVERLOAD_SHRINE);
    public static final RegistryObject<BlockEntityType<SweetShrineBlockEntity>> SWEET_SHRINE = register(SweetShrineBlockEntity::new, ECBlocks.SWEET_SHRINE);
    public static final RegistryObject<BlockEntityType<EnderLockShrineBlockEntity>> ENDER_LOCK_SHRINE = register(EnderLockShrineBlockEntity::new, ECBlocks.ENDER_LOCK_SHRINE);
    public static final RegistryObject<BlockEntityType<BreedingShrineBlockEntity>> BREEDING_SHRINE = register(BreedingShrineBlockEntity::new, ECBlocks.BREEDING_SHRINE);
    public static final RegistryObject<BlockEntityType<GroveShrineBlockEntity>> GROVE_SHRINE = register(GroveShrineBlockEntity::new, ECBlocks.GROVE_SHRINE);
    public static final RegistryObject<BlockEntityType<SpringShrineBlockEntity>> SPRING_SHRINE = register(SpringShrineBlockEntity::new, ECBlocks.SPRING_SHRINE);
    public static final RegistryObject<BlockEntityType<BuddingShrineBlockEntity>> BUDDING_SHRINE = register(BuddingShrineBlockEntity::new, ECBlocks.BUDDING_SHRINE);
    public static final RegistryObject<BlockEntityType<SpawningShrineBlockEntity>> SPAWNING_SHRINE = register(SpawningShrineBlockEntity::new, ECBlocks.SPAWNING_SHRINE);
    public static final RegistryObject<BlockEntityType<AccelerationShrineUpgradeBlockEntity>> ACCELERATION_SHRINE_UPGRADE = register(AccelerationShrineUpgradeBlockEntity::new, ECBlocks.ACCELERATION_SHRINE_UPGRADE);
    public static final RegistryObject<BlockEntityType<OverclockedAccelerationShrineUpgradeBlockEntity>> OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE = register(OverclockedAccelerationShrineUpgradeBlockEntity::new, ECBlocks.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE);
    public static final RegistryObject<BlockEntityType<GreaterFortuneShrineUpgradeBlockEntity>> GREATER_FORTUNE_SHRINE_UPGRADE = register(GreaterFortuneShrineUpgradeBlockEntity::new, ECBlocks.GREATER_FORTUNE_SHRINE_UPGRADE);
    public static final RegistryObject<BlockEntityType<TranslocationShrineUpgradeBlockEntity>> TRANSLOCATION_SHRINE_UPGRADE = register(TranslocationShrineUpgradeBlockEntity::new, ECBlocks.TRANSLOCATION_SHRINE_UPGRADE);
    public static final RegistryObject<BlockEntityType<VortexShrineUpgradeBlockEntity>> VORTEX_SHRINE_UPGRADE = register(VortexShrineUpgradeBlockEntity::new, ECBlocks.VORTEX_SHRINE_UPGRADE);
    public static final RegistryObject<BlockEntityType<SorterBlockEntity>> SORTER = register(SorterBlockEntity::new, ECBlocks.SORTER);

    public static final RegistryObject<BlockEntityType<SourceDisplacementPlateBlockEntity>> SOURCE_DISPLACEMENT_PLATE = register(() ->
            BlockEntityType.Builder.of(
                SourceDisplacementPlateBlockEntity::new,
                ECBlocks.SOURCE_DISPLACEMENT_PLATES.values().stream().map(RegistryObject::get).toArray(Block[]::new)
            ),
            SourceDisplacementPlateBlock.NAME
    );

    public static final RegistryObject<BlockEntityType<SourceBreederBlockEntity>> SOURCE_BREEDER = register(SourceBreederBlockEntity::new, ECBlocks.SOURCE_BREEDER);
    public static final RegistryObject<BlockEntityType<SourceBreederPedestalBlockEntity>> SOURCE_BREEDER_PEDESTAL = register(SourceBreederPedestalBlockEntity::new, ECBlocks.SOURCE_BREEDER_PEDESTAL);


    private ECBlockEntityTypes() {}

    @SafeVarargs
    private static <T extends BlockEntity> BlockEntityType.Builder<T> builder(BlockEntityType.BlockEntitySupplier<T> factory, RegistryObject<? extends Block>... validBlocks) {
        return BlockEntityType.Builder.of(factory, Arrays.stream(validBlocks)
                .map(RegistryObject::get)
                .toArray(Block[]::new));
    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(BlockEntityType.BlockEntitySupplier<T> factory, RegistryObject<? extends Block> block) {
        return register(() -> builder(factory, block), block.getId().getPath());
    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(Supplier<BlockEntityType.Builder<T>> builder, String name) {
        return DEFERRED_REGISTER.register(name, () -> builder.get().build(null));
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }
}
