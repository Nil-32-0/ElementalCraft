package metafact.elementalcraft.jewel.effect.mole;

import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.jewel.JewelHelper;
import metafact.elementalcraft.jewel.Jewels;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class MoleJewelHandler {

    private MoleJewelHandler() {}

    @SubscribeEvent
    public static void onLeftClickBlock(@Nonnull PlayerInteractEvent.LeftClickBlock event) {
        var player = event.getEntity();
        var mole = Jewels.MOLE.get();

        if (JewelHelper.hasJewel(player, mole) && ForgeHooks.isCorrectToolForDrops(player.level().getBlockState(event.getPos()), player)) {
            mole.consume(player);
            mole.apply(player);
        }
    }
}
