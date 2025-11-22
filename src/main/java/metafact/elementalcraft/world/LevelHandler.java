package metafact.elementalcraft.world;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.name.ECNames;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class LevelHandler {

	private LevelHandler() {}
	
	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<Level> event) {
		event.addCapability(ElementalCraftApi.createRL(ECNames.ELEMENT_STORAGE), LevelElementStorage.createProvider());
	}
}
