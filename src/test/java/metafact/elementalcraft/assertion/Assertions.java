package metafact.elementalcraft.assertion;

import net.minecraft.gametest.framework.GlobalTestReporter;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import metafact.elementalcraft.container.ItemHandlerAssert;
import metafact.elementalcraft.item.ItemStackAssert;

public class Assertions extends org.assertj.core.api.Assertions {

    static {
        GlobalTestReporter.replaceWith(new StackTraceLogTestReporter());
    }

    private Assertions() {}

    public static ItemStackAssert assertThat(ItemStack itemStack) {
        return ItemStackAssert.assertThat(itemStack);
    }

    public static ItemHandlerAssert assertThat(IItemHandler handler) {
        return ItemHandlerAssert.assertThat(handler);
    }
}
