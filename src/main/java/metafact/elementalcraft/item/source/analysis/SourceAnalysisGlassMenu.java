package metafact.elementalcraft.item.source.analysis;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import metafact.elementalcraft.api.source.trait.SourceTrait;
import metafact.elementalcraft.api.source.trait.value.ISourceTraitValue;
import metafact.elementalcraft.block.source.trait.SourceTraits;
import metafact.elementalcraft.container.menu.AbstractECMenu;
import metafact.elementalcraft.container.menu.ECMenus;
import metafact.elementalcraft.network.message.MessageHelper;

import javax.annotation.Nonnull;
import java.util.Map;

public class SourceAnalysisGlassMenu extends AbstractECMenu {
	private final Player player;
	private Map<ResourceKey<SourceTrait>, ISourceTraitValue> traits;
	
	public SourceAnalysisGlassMenu(int id, Inventory inventory) {
		this(id, inventory, SourceTraits.createTraitMap());
	}
	
	public SourceAnalysisGlassMenu(int id, Inventory inventory, Map<ResourceKey<SourceTrait>, ISourceTraitValue> traits) {
		super(ECMenus.SOURCE_ANALYSIS_GLASS, id);
		this.player = inventory.player;
		this.traits = traits;
		addSlots(inventory);
	}
	
	private void addSlots(Inventory inventory) {
		addPlayerSlots(inventory, 98);
	}

	public Map<ResourceKey<SourceTrait>, ISourceTraitValue> getTraits() {
		return traits;
	}

	public void setTraits(Map<ResourceKey<SourceTrait>, ISourceTraitValue> traits) {
		this.traits = traits;
	}
	
	@Override
	public void onOpen(Player player) {
		sendTraits();
	}

	private void sendTraits() {
		if (player instanceof ServerPlayer serverPlayer) {
			MessageHelper.sendToPlayer(serverPlayer, new SourceAnalysisGlassMessage(traits));
		}
	}

	@Nonnull
	@Override
	public ItemStack quickMoveStack(@Nonnull Player player, int index) {
		return ItemStack.EMPTY;
	}
}
