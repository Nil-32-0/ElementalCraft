package metafact.elementalcraft.block.shrine.overload;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.gametest.GameTestHolder;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.block.shrine.ShrineGameTestHelper;
import metafact.elementalcraft.container.ContainerGameTestHelper;

import static metafact.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class OverloadShrineGameTests {

    // elementalcraft:overloadshrinegametests.should_speedupfurnace
    @GameTest(batch = ShrineGameTestHelper.BATCH_NAME, timeoutTicks = 200)
    public static void should_speedupFurnace(GameTestHelper helper) {
        var furnace = ContainerGameTestHelper.getItemHandler(helper, new BlockPos(0, 2, 0));
        var shrine = ShrineGameTestHelper.getShrine(helper, new BlockPos(0, 1, 0)).getElementStorage();

        helper.startSequence()
                .thenExecute(() -> {
                    furnace.insertItem(0, new ItemStack(Items.RAW_IRON), false);
                    furnace.insertItem(1, new ItemStack(Items.COAL), false);
                })
                .thenExecuteFor(152, shrine::fill)
                .thenExecute(() -> assertThat(furnace)
                        .isEmpty(0)
                        .isEmpty(1)
                        .contains(2, Items.IRON_INGOT))
                .thenSucceed();
    }
}
