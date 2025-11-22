package metafact.elementalcraft.block.instrument.crystallizer;

import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.gametest.GameTestHolder;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.instrument.InstrumentGameTestHelper;
import metafact.elementalcraft.item.ECItems;
import metafact.elementalcraft.item.elemental.ElementalItemHelper;

import java.util.Collection;
import java.util.List;

import static metafact.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class CrystallizerGameTests {

    private static final String TEMPLATE = "elementalcraft:crystallizergametests.crystallizer";
    @GameTestGenerator
    public static Collection<TestFunction> should_craftFineGem() {
        return List.of(
                InstrumentGameTestHelper.createTestFunction("should_craftFineGem", TEMPLATE, h -> should_craftFineGem(h, ElementType.FIRE)),
                InstrumentGameTestHelper.createTestFunction("should_craftFineGem", TEMPLATE, h -> should_craftFineGem(h, ElementType.WATER)),
                InstrumentGameTestHelper.createTestFunction("should_craftFineGem", TEMPLATE, h -> should_craftFineGem(h, ElementType.EARTH)),
                InstrumentGameTestHelper.createTestFunction("should_craftFineGem", TEMPLATE, h -> should_craftFineGem(h, ElementType.AIR))
        );
    }

    public static void should_craftFineGem(GameTestHelper helper, ElementType elementType) {
        InstrumentGameTestHelper.<CrystallizerBlockEntity>runInstrument(helper, List.of(
                new ItemStack(ElementalItemHelper.getCrudeGemForElement(elementType)),
                new ItemStack(ElementalItemHelper.getCrystalForElement(elementType))
        ), elementType, binder -> {
            var inv = binder.getInventory();

            assertThat(inv.getItem(0))
                    .is(ElementalItemHelper.getFineGemForElement(elementType))
                    .hasCount(1);
            assertThat(inv.getItem(1))
                    .isEmpty();
        });
    }

    @GameTestGenerator
    public static Collection<TestFunction> should_craftPristineGem() {
        return List.of(
                InstrumentGameTestHelper.createTestFunction("should_craftPristineGem", TEMPLATE, h -> should_craftPristineGem(h, ElementType.FIRE)),
                InstrumentGameTestHelper.createTestFunction("should_craftPristineGem", TEMPLATE, h -> should_craftPristineGem(h, ElementType.WATER)),
                InstrumentGameTestHelper.createTestFunction("should_craftPristineGem", TEMPLATE, h -> should_craftPristineGem(h, ElementType.EARTH)),
                InstrumentGameTestHelper.createTestFunction("should_craftPristineGem", TEMPLATE, h -> should_craftPristineGem(h, ElementType.AIR))
        );
    }

    public static void should_craftPristineGem(GameTestHelper helper, ElementType elementType) {
        InstrumentGameTestHelper.<CrystallizerBlockEntity>runInstrument(helper, List.of(
                new ItemStack(ElementalItemHelper.getFineGemForElement(elementType)),
                new ItemStack(ECItems.PRISTINE_SHARD.get())
        ), elementType, binder -> {
            var inv = binder.getInventory();

            assertThat(inv.getItem(0))
                    .is(ElementalItemHelper.getPristineGemForElement(elementType))
                    .hasCount(1);
            assertThat(inv.getItem(1))
                    .isEmpty();
        });
    }
}