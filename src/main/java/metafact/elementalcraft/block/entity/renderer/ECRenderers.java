package metafact.elementalcraft.block.entity.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent.RegisterGeometryLoaders;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.block.container.ContainerRenderer;
import metafact.elementalcraft.block.diffuser.DiffuserRenderer;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.block.instrument.binder.BinderRenderer;
import metafact.elementalcraft.block.instrument.crystallizer.CrystallizerRenderer;
import metafact.elementalcraft.block.instrument.enchantment.liquefier.EnchantmentLiquefierRenderer;
import metafact.elementalcraft.block.instrument.inscriber.InscriberRenderer;
import metafact.elementalcraft.block.instrument.io.firefurnace.FireFurnaceRenderer;
import metafact.elementalcraft.block.instrument.io.mill.MillRenderer;
import metafact.elementalcraft.block.instrument.io.purifier.PurifierRenderer;
import metafact.elementalcraft.block.pipe.ElementPipeRenderer;
import metafact.elementalcraft.block.pureinfuser.PureInfuserRenderer;
import metafact.elementalcraft.block.shrine.ShrineRenderer;
import metafact.elementalcraft.block.shrine.upgrade.acceleration.AccelerationShrineUpgradeRenderer;
import metafact.elementalcraft.block.shrine.upgrade.acceleration.overclocked.OverclockedAccelerationShrineUpgradeRenderer;
import metafact.elementalcraft.block.shrine.upgrade.horizontal.fortune.greater.GreaterFortuneShrineUpgradeRenderer;
import metafact.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeRenderer;
import metafact.elementalcraft.block.shrine.upgrade.vortex.VortexShrineUpgradeRenderer;
import metafact.elementalcraft.block.sorter.SorterRenderer;
import metafact.elementalcraft.block.source.SourceRenderer;
import metafact.elementalcraft.block.source.breeder.SourceBreederRenderer;
import metafact.elementalcraft.block.source.breeder.pedestal.SourceBreederPedestalRenderer;
import metafact.elementalcraft.block.source.displacement.plate.SourceDisplacementPlateRenderer;
import metafact.elementalcraft.block.synthesizer.cracking.CrackingSynthesizerRenderer;
import metafact.elementalcraft.block.synthesizer.draining.DrainingSynthesizerRenderer;
import metafact.elementalcraft.block.synthesizer.mana.ManaSynthesizerRenderer;
import metafact.elementalcraft.block.synthesizer.mill.AirMillSynthesizerRenderer;
import metafact.elementalcraft.block.synthesizer.solar.SolarSynthesizerRenderer;
import metafact.elementalcraft.block.synthesizer.vibration.VibrationSynthesizerRenderer;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ECRenderers {

	private ECRenderers() {}
	
	@SubscribeEvent
	public static void registerModels(RegisterGeometryLoaders evt) {
		register(ECBlockEntityTypes.PIPE, ElementPipeRenderer::new);
		register(ECBlockEntityTypes.INFUSER, () -> new SingleItemRenderer<>(new Vec3(0.5, 0.2, 0.5)));
		register(ECBlockEntityTypes.EXTRACTOR, IRuneRenderer::create);
		register(ECBlockEntityTypes.EVAPORATOR, () -> new SingleItemRenderer<>(new Vec3(0.5, 0.2, 0.5), 0.5F));
        register(ECBlockEntityTypes.AIR_MILL_SYNTHESIZER, AirMillSynthesizerRenderer::new);
        register(ECBlockEntityTypes.CRACKING_SYNTHESIZER, CrackingSynthesizerRenderer::new);
        register(ECBlockEntityTypes.DRAINING_SYNTHESIZER, DrainingSynthesizerRenderer::new);
        register(ECBlockEntityTypes.VIBRATION_SYNTHESIZER, VibrationSynthesizerRenderer::new);
        register(ECBlockEntityTypes.SCULK_CRACKING_SYNTHESIZER, CrackingSynthesizerRenderer::new);
        register(ECBlockEntityTypes.COMBUSTION_SYNTHESIZER, () -> new SingleItemRenderer<>(new Vec3(0.5, 0.5, 0.5), 0.7F));
        register(ECBlockEntityTypes.SOLAR_SYNTHESIZER, SolarSynthesizerRenderer::new);
		register(ECBlockEntityTypes.MANA_SYNTHESIZER, ManaSynthesizerRenderer::new);
		register(ECBlockEntityTypes.DIFFUSER, DiffuserRenderer::new);
		register(ECBlockEntityTypes.BINDER, BinderRenderer::new);
		register(ECBlockEntityTypes.BINDER_IMPROVED, BinderRenderer::new);
		register(ECBlockEntityTypes.CRYSTALLIZER, CrystallizerRenderer::new);
		register(ECBlockEntityTypes.INSCRIBER, InscriberRenderer::new);
		register(ECBlockEntityTypes.WATER_MILL_GRINDSTONE, d -> new MillRenderer<>(MillRenderer.WATER_MILL_GRINDSTONE_SHAFT_LOCATION));
		register(ECBlockEntityTypes.AIR_MILL_GRINDSTONE, d -> new MillRenderer<>(MillRenderer.AIR_MILL_GRINDSTONE_SHAFT_LOCATION));
		register(ECBlockEntityTypes.WATER_MILL_WOOD_SAW, d -> new MillRenderer<>(MillRenderer.WATER_MILL_WOOD_SAW_SHAFT_LOCATION));
		register(ECBlockEntityTypes.AIR_MILL_WOOD_SAW, d -> new MillRenderer<>(MillRenderer.AIR_MILL_WOOD_SAW_SHAFT_LOCATION));
        register(ECBlockEntityTypes.ENCHANTMENT_LIQUEFIER, EnchantmentLiquefierRenderer::new);
        register(ECBlockEntityTypes.PEDESTAL, () -> new SingleItemRenderer<>(new Vec3(0.5, 0.9, 0.5)));
		register(ECBlockEntityTypes.PURE_INFUSER, PureInfuserRenderer::new);
		register(ECBlockEntityTypes.FIRE_FURNACE, FireFurnaceRenderer::new);
		register(ECBlockEntityTypes.FIRE_BLAST_FURNACE, FireFurnaceRenderer::new);
		register(ECBlockEntityTypes.PURIFIER, PurifierRenderer::new);
		register(ECBlockEntityTypes.ACCELERATION_SHRINE_UPGRADE, AccelerationShrineUpgradeRenderer::new);
		register(ECBlockEntityTypes.VORTEX_SHRINE_UPGRADE, VortexShrineUpgradeRenderer::new);
		register(ECBlockEntityTypes.TRANSLOCATION_SHRINE_UPGRADE, TranslocationShrineUpgradeRenderer::new);
		register(ECBlockEntityTypes.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE, OverclockedAccelerationShrineUpgradeRenderer::new);
        register(ECBlockEntityTypes.GREATER_FORTUNE_SHRINE_UPGRADE, GreaterFortuneShrineUpgradeRenderer::new);
		register(ECBlockEntityTypes.SORTER, SorterRenderer::new);
		register(ECBlockEntityTypes.SOURCE, SourceRenderer::new);

		register(ECBlockEntityTypes.FIRE_PYLON, ShrineRenderer::new);
		register(ECBlockEntityTypes.GROVE_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.BUDDING_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.BREEDING_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.SPAWNING_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.MELTING_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.ORE_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.OVERLOAD_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.SWEET_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.GROWTH_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.HARVEST_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.LUMBER_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.ENDER_LOCK_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.SPRING_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.VACUUM_SHRINE, ShrineRenderer::new);

		register(ECBlockEntityTypes.CONTAINER, ContainerRenderer::new);
		register(ECBlockEntityTypes.CREATIVE_CONTAINER, ContainerRenderer::new);
		register(ECBlockEntityTypes.RESERVOIR, ContainerRenderer::new);

		register(ECBlockEntityTypes.SOURCE_DISPLACEMENT_PLATE, SourceDisplacementPlateRenderer::new);
		register(ECBlockEntityTypes.SOURCE_BREEDER, SourceBreederRenderer::new);
		register(ECBlockEntityTypes.SOURCE_BREEDER_PEDESTAL, SourceBreederPedestalRenderer::new);
	}

	public static <T extends BlockEntity> void register(RegistryObject<BlockEntityType<T>> type, Supplier<BlockEntityRenderer<? super T>> renderProvider) {
		register(type.get(), renderProvider);
	}

	public static <T extends BlockEntity> void register(RegistryObject<BlockEntityType<T>> type, BlockEntityRendererProvider<T> renderProvider) {
		BlockEntityRenderers.register(type.get(), renderProvider);
	}

	public static <T extends BlockEntity> void register(BlockEntityType<T> type, Supplier<BlockEntityRenderer<? super T>> renderProvider) {
		BlockEntityRenderers.register(type, d -> renderProvider.get());
	}
}
