package metafact.elementalcraft.block.instrument.io.firefurnace;

import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraftforge.gametest.GameTestHolder;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.instrument.InstrumentGameTestHelper;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static metafact.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class FireFurnaceGameTests {

    @GameTestGenerator
    public static Collection<TestFunction> should_smelt() {
        var index = new AtomicInteger(0);

        return FireFurnaceTestHolder.HOLDERS.stream()
                .map(t -> t.createTestFunction("should_smelt#" + index.getAndIncrement(), FireFurnaceGameTests::should_smelt))
                .toList();
    }

    public static void should_smelt(GameTestHelper helper, FireFurnaceTestHolder holder) {
        InstrumentGameTestHelper.<AbstractFireFurnaceBlockEntity<?>>runInstrument(helper, holder.input().get(), ElementType.FIRE, mill -> {
            assertThat(mill.getInventory().getItem(0)).isEmpty();
            assertThat(mill.getInventory().getItem(1))
                    .is(holder.output())
                    .hasCount(1);
        });
    }
}