package metafact.elementalcraft.container.menu.screen;

import net.minecraft.client.gui.screens.MenuScreens;
import metafact.elementalcraft.block.spelldesk.SpellDeskScreen;
import metafact.elementalcraft.container.menu.ECMenus;
import metafact.elementalcraft.item.source.analysis.SourceAnalysisGlassScreen;
import metafact.elementalcraft.item.spell.book.SpellBookScreen;

public class ECScreens {

	private ECScreens() {}
	
	public static void initScreenFactories() {
		MenuScreens.register(ECMenus.SPELL_BOOK.get(), SpellBookScreen::new);
		MenuScreens.register(ECMenus.SPELL_DESK.get(), SpellDeskScreen::new);
		MenuScreens.register(ECMenus.SOURCE_ANALYSIS_GLASS.get(), SourceAnalysisGlassScreen::new);
	}
}
