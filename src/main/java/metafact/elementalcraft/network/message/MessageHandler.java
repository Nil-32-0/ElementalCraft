package metafact.elementalcraft.network.message;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.block.anchor.TranslocationAnchorListMessage;
import metafact.elementalcraft.block.shrine.upgrade.vortex.VortexPullPlayerMessage;
import metafact.elementalcraft.block.source.flux.SourceFluxMessage;
import metafact.elementalcraft.item.source.analysis.SourceAnalysisGlassMessage;
import metafact.elementalcraft.item.spell.book.SpellBookMessage;
import metafact.elementalcraft.jewel.handler.ActiveJewelsMessage;
import metafact.elementalcraft.spell.ChangeSpellMessage;
import metafact.elementalcraft.spell.tick.SpellTickCooldownMessage;

public class MessageHandler {

	private static final String PROTOCOL_VERSION = "13";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(ElementalCraftApi.createRL("main"), () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);

	private MessageHandler() {}
	
	public static void setup() {
		int id = 0;

		CHANNEL.registerMessage(id++, ChangeSpellMessage.class, ChangeSpellMessage::encode, ChangeSpellMessage::decode, ChangeSpellMessage::handle);
		CHANNEL.registerMessage(id++, SpellBookMessage.class, SpellBookMessage::encode, SpellBookMessage::decode, SpellBookMessage::handle);
		CHANNEL.registerMessage(id++, SpellTickCooldownMessage.class, SpellTickCooldownMessage::encode, SpellTickCooldownMessage::decode, SpellTickCooldownMessage::handle);
		CHANNEL.registerMessage(id++, SourceAnalysisGlassMessage.class, SourceAnalysisGlassMessage::encode, SourceAnalysisGlassMessage::decode, SourceAnalysisGlassMessage::handle);
		CHANNEL.registerMessage(id++, ActiveJewelsMessage.class, ActiveJewelsMessage::encode, ActiveJewelsMessage::decode, ActiveJewelsMessage::handle);
		CHANNEL.registerMessage(id++, VortexPullPlayerMessage.class, VortexPullPlayerMessage::encode, VortexPullPlayerMessage::decode, VortexPullPlayerMessage::handle);
		CHANNEL.registerMessage(id++, TranslocationAnchorListMessage.class, TranslocationAnchorListMessage::encode, TranslocationAnchorListMessage::decode, TranslocationAnchorListMessage::handle);
        CHANNEL.registerMessage(id++, SourceFluxMessage.class, SourceFluxMessage::encode, SourceFluxMessage::decode, SourceFluxMessage::handle);
	}
}
