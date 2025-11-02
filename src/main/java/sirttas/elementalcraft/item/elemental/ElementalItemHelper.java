package sirttas.elementalcraft.item.elemental;

import net.minecraft.world.item.Item;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ECItems;

public class ElementalItemHelper {

    public static final String ERROR_MESSAGE = "Element Type must not be NONE";

    private ElementalItemHelper() {}

    public static Item getCrystalForElement(ElementType type) {
        if (!ECItems.CRYSTALS.containsKey(type)) return ECItems.INERT_CRYSTAL.get();
        return ECItems.CRYSTALS.get(type).get();
    }

    public static Item getShardForElement(ElementType type) {
        if (!ECItems.SHARDS.containsKey(type)) throw new IllegalArgumentException(ERROR_MESSAGE);
        return ECItems.SHARDS.get(type).get();
    }

    public static Item getPowerfulShardForElement(ElementType type) {
        if (!ECItems.POWERFUL_SHARDS.containsKey(type)) throw new IllegalArgumentException(ERROR_MESSAGE);
        return ECItems.POWERFUL_SHARDS.get(type).get();
    }

    public static Item getCrudeGemForElement(ElementType type) {
        if (!ECItems.CRUDE_GEMS.containsKey(type)) return ECItems.INERT_CRYSTAL.get();
        return ECItems.CRUDE_GEMS.get(type).get();
    }

    public static Item getFineGemForElement(ElementType type) {
        if (!ECItems.FINE_GEMS.containsKey(type)) throw new IllegalArgumentException(ERROR_MESSAGE);
        return ECItems.FINE_GEMS.get(type).get();
    }

    public static Item getPristineGemForElement(ElementType type) {
        if (!ECItems.PRISTINE_GEMS.containsKey(type)) throw new IllegalArgumentException(ERROR_MESSAGE);
        return ECItems.PRISTINE_GEMS.get(type).get();
    }

    public static Item getDisplacementPlateForElement(ElementType type) {
        if (!ECBlocks.SOURCE_DISPLACEMENT_PLATES.containsKey(type)) throw new IllegalArgumentException(ERROR_MESSAGE);
        return ECBlocks.SOURCE_DISPLACEMENT_PLATES.get(type).get().asItem();
    }
}
