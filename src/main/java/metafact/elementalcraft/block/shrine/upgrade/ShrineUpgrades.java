package metafact.elementalcraft.block.shrine.upgrade;

import metafact.elementalcraft.block.shrine.upgrade.horizontal.*;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.fml.common.Mod;
import metafact.dpanvil_m.api.data.IDataManager;
import metafact.elementalcraft.ElementalCraft;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.block.shrine.upgrade.acceleration.AccelerationShrineUpgradeBlock;
import metafact.elementalcraft.block.shrine.upgrade.acceleration.overclocked.OverclockedAccelerationShrineUpgradeBlock;
import metafact.elementalcraft.block.shrine.upgrade.directional.CapacityShrineUpgradeBlock;
import metafact.elementalcraft.block.shrine.upgrade.directional.EfficiencyShrineUpgradeBlock;
import metafact.elementalcraft.block.shrine.upgrade.directional.OptimizationShrineUpgradeBlock;
import metafact.elementalcraft.block.shrine.upgrade.directional.RangeShrineUpgradeBlock;
import metafact.elementalcraft.block.shrine.upgrade.directional.StrengthShrineUpgradeBlock;
import metafact.elementalcraft.block.shrine.upgrade.horizontal.fortune.FortuneShrineUpgradeBlock;
import metafact.elementalcraft.block.shrine.upgrade.horizontal.fortune.greater.GreaterFortuneShrineUpgradeBlock;
import metafact.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeBlock;
import metafact.elementalcraft.block.shrine.upgrade.unidirectional.BonelessGrowthShrineUpgradeBlock;
import metafact.elementalcraft.block.shrine.upgrade.unidirectional.CrystalGrowthShrineUpgradeBlock;
import metafact.elementalcraft.block.shrine.upgrade.directional.FillingShrineUpgradeBlock;
import metafact.elementalcraft.block.shrine.upgrade.unidirectional.MysticalGroveShrineUpgradeBlock;
import metafact.elementalcraft.block.shrine.upgrade.unidirectional.PickupShrineUpgradeBlock;
import metafact.elementalcraft.block.shrine.upgrade.unidirectional.StemPollinationShrineUpgradeBlock;
import metafact.elementalcraft.block.shrine.upgrade.vertical.PlantingShrineUpgradeBlock;
import metafact.elementalcraft.block.shrine.upgrade.vortex.VortexShrineUpgradeBlock;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class ShrineUpgrades {

	public static final String NAME = "shrine_upgrades";
	public static final String FOLDER = ElementalCraftApi.MODID + '/' + NAME;

	public static final ResourceKey<ShrineUpgrade> FORTUNE = createKey(FortuneShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> SILK_TOUCH = createKey(SilkTouchShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> PLANTING = createKey(PlantingShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> BONELESS_GROWTH = createKey(BonelessGrowthShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> PICKUP = createKey(PickupShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> VORTEX = createKey(VortexShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> NECTAR = createKey(NectarShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> MYSTICAL_GROVE = createKey(MysticalGroveShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> STEM_POLLINATION = createKey(StemPollinationShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> PROTECTION = createKey(ProtectionShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> FILLING = createKey(FillingShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> CRYSTAL_HARVEST = createKey(CrystalHarvestShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> CRYSTAL_GROWTH = createKey(CrystalGrowthShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> STRENGTH = createKey(StrengthShrineUpgradeBlock.NAME);
    public static final ResourceKey<ShrineUpgrade> SPRINGALINE = createKey(SpringalineShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> CAPACITY = createKey(CapacityShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> RANGE = createKey(RangeShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> EFFICIENCY = createKey(EfficiencyShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> OPTIMIZATION = createKey(OptimizationShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> ACCELERATION = createKey(AccelerationShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> OVERCLOCKED_ACCELERATION = createKey(OverclockedAccelerationShrineUpgradeBlock.NAME);
	public static final ResourceKey<ShrineUpgrade> TRANSLOCATION = createKey(TranslocationShrineUpgradeBlock.NAME);
    public static final ResourceKey<ShrineUpgrade> GREATER_FORTUNE = createKey(GreaterFortuneShrineUpgradeBlock.NAME);
    public static final ResourceKey<ShrineUpgrade> OVERWHELMING_STRENGTH = createKey(OverwhelmingStrengthShrineUpgradeBlock.NAME);

	private ShrineUpgrades() {}

	private static ResourceKey<ShrineUpgrade> createKey(String name) {
		return IDataManager.createKey(ElementalCraft.SHRINE_UPGRADE_MANAGER_KEY, ElementalCraftApi.createRL(name));
	}

}
