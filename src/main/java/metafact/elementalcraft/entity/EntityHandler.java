package metafact.elementalcraft.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.element.storage.ElementStorageHelper;
import metafact.elementalcraft.api.name.ECNames;
import metafact.elementalcraft.config.ECConfig;
import metafact.elementalcraft.container.menu.IMenuOpenListener;
import metafact.elementalcraft.entity.player.PlayerElementStorage;
import metafact.elementalcraft.entity.player.PlayerSpellTickManager;
import metafact.elementalcraft.infusion.tool.ToolInfusionHelper;
import metafact.elementalcraft.item.ECItems;
import metafact.elementalcraft.jewel.handler.ClientJewelHandler;
import metafact.elementalcraft.jewel.handler.JewelHandler;
import metafact.elementalcraft.spell.tick.ISpellTickManager;
import metafact.elementalcraft.spell.tick.SpellTickHelper;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class EntityHandler {

	private EntityHandler() {}
	
	@SubscribeEvent
	public static void onEntityUseItemTick(LivingEntityUseItemEvent.Tick event) {
		int fastDraw = ToolInfusionHelper.getFasterDraw(event.getItem());
		
		if (fastDraw >= 0 && event.getDuration() % fastDraw == 0) {
			event.setDuration(event.getDuration() - 1);
		}
	}
	
	@SubscribeEvent
	public static void onEntityLivingAttack(LivingAttackEvent event) {
		var entity = event.getEntity();
		var world = entity.level();

		if (!world.isClientSide && world.getRandom().nextDouble() >= ToolInfusionHelper.getDodge(entity)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		var player = event.getEntity();
		
		if (!player.level().isClientSide && Boolean.TRUE.equals(ECConfig.SERVER.playersSpawnWithBook.get())) {
			CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);

			if (!tag.getBoolean(ECNames.HAS_BOOK)) {
				ItemStack book = new ItemStack(ECItems.ELEMENTOPEDIA.get());

				book.getOrCreateTag().putString("patchouli:book", "elementalcraft:element_book");
				ItemHandlerHelper.giveItemToPlayer(player, book);
				tag.putBoolean(ECNames.HAS_BOOK, true);
				player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
			}
		}
	}
	
	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();

		if (entity instanceof Player player && !(entity instanceof FakePlayer)) {
			var provider = PlayerElementStorage.createProvider(player);

			event.addCapability(ElementalCraftApi.createRL(ECNames.ELEMENT_STORAGE), provider);
			event.addCapability(ElementalCraftApi.createRL(ECNames.SPELL_TICK_MANAGER), PlayerSpellTickManager.createProvider(player));
			if (entity.level().isClientSide) {
				event.addCapability(ElementalCraftApi.createRL(ECNames.JEWEL), ClientJewelHandler.createProvider());
			} else {
				event.addCapability(ElementalCraftApi.createRL(ECNames.JEWEL), JewelHandler.createProvider(entity, ElementStorageHelper.get(provider).orElse(null)));
			}
		}
	}

	@SubscribeEvent
	public static void onContainerOpen(PlayerContainerEvent.Open event) {
		if (event.getContainer() instanceof IMenuOpenListener listener) {
			listener.onOpen(event.getEntity());
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			SpellTickHelper.get(event.player).ifPresent(ISpellTickManager::tick);
		}
	}
}
