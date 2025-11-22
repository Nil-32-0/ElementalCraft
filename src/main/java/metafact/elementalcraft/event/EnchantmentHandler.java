package metafact.elementalcraft.event;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.StringUtils;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.config.ECConfig;
import metafact.elementalcraft.item.ECItems;
import metafact.elementalcraft.jewel.JewelHelper;
import metafact.elementalcraft.spell.SpellHelper;
import metafact.elementalcraft.tag.ECTags;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class EnchantmentHandler {

	private EnchantmentHandler() {}
	
	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();

		if (left.is(ECTags.Items.SPELL_CAST_TOOLS) && right.is(ECItems.SCROLL.get()) && SpellHelper.getSpellCount(left) < ECConfig.SERVER.focusMaxSpell.get()) {
			ItemStack result = left.copy();
			int n = 4 * (SpellHelper.getSpellCount(left) + 1);

			if (StringUtils.isBlank(event.getName())) {
				if (left.hasCustomHoverName()) {
					n++;
					result.resetHoverName();
				}
			} else if (!event.getName().equals(left.getHoverName().getString())) {
				n++;
				result.setHoverName(Component.literal(event.getName()));
			}
			SpellHelper.addSpell(result, SpellHelper.getSpell(right));
			event.setCost(n);
			event.setOutput(result);
		} else if (left.is(ECTags.Items.JEWEL_SOCKETABLES) && right.is(ECItems.JEWEL.get()) && JewelHelper.getJewel(left) == null) {
			ItemStack result = left.copy();

			JewelHelper.setJewel(result, JewelHelper.getJewel(right));
			event.setCost(10);
			event.setOutput(result);
		}
	}
}
