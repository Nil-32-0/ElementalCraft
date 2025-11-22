package metafact.elementalcraft.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.tooltip.ElementGaugeTooltip;
import metafact.elementalcraft.block.container.AbstractElementContainerBlock;
import metafact.elementalcraft.container.menu.screen.ECScreens;
import metafact.elementalcraft.gui.tooltip.ElementGaugeClientTooltip;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECClientHandler {

	private ECClientHandler() {}
	
	@SubscribeEvent
	public static void setupClient(FMLClientSetupEvent event) {
		ECScreens.initScreenFactories();
	}

	@SubscribeEvent
	public static void registerTooltipImages(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(ElementGaugeTooltip.class, ElementGaugeClientTooltip::new);
		event.register(AbstractElementContainerBlock.Tooltip.class, AbstractElementContainerBlock.ClientTooltip::new);
	}
	
}
