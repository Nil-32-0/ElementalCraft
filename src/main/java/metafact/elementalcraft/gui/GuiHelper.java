package metafact.elementalcraft.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.config.ECConfig;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("resource")
public class GuiHelper {

	private static final ResourceLocation GAUGE = ElementalCraftApi.createRL("textures/gui/element_gauge.png");

	private GuiHelper() {}

	public static void renderElementGauge(GuiGraphics guiGraphics, Font font, int x, int y, int amount, int max, ElementType type) {
		renderElementGauge(guiGraphics, font, x, y, amount, max, type, true);
	}

	public static void renderElementGauge(GuiGraphics guiGraphics, Font font, int x, int y, int amount, int max, ElementType type, boolean showDebugInfo) {
		guiGraphics.blit(ElementalCraftApi.createRL(type.getGaugeTextureLocation()), x, y, 0, 0, 16, 16);

		int progress = Math.max(0, (int) ((double) Math.min(amount, max) / (double) max * 16));

		if (progress <= 1 && amount > 0) {
			progress = 2;
		}
		guiGraphics.blit(ElementalCraftApi.createRL(type.getGaugeTextureLocation()), x, y + 16 - progress, type.getGaugeOffset() * 16, 16 - progress + (Boolean.TRUE.equals(ECConfig.CLIENT.usePaleElementGauge.get()) ? 16 : 0), 16, progress);
		if (showDebugInfo() && showDebugInfo) {
			guiGraphics.drawString(font, amount + "/" + max, x, y + 16, 16777215, true);
		}
	}

	public static void renderCheck(GuiGraphics guiGraphics, Check check, int x, int y) {
		guiGraphics.blit(GAUGE, x, y, 0, 16 + check.offset, 6, 6);
	}


	public static boolean showDebugInfo() {
		Minecraft minecraft = Minecraft.getInstance();

		return minecraft.player.isCreative() && minecraft.options.advancedItemTooltips;
	}

	public enum Check {
		VALID(0),
		PAUSED(6),
		INVALID(12);

		private final int offset;

		Check(int offset) {
			this.offset = offset;
		}

		public int getOffset() {
			return offset;
		}
	}

}
