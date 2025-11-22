package metafact.elementalcraft.api.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import metafact.elementalcraft.api.element.storage.IElementStorage;

public record ElementGaugeTooltip(
        IElementStorage storage
) implements TooltipComponent {
}
