package sirttas.elementalcraft.item.elemental;

import net.minecraft.world.item.Item;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ECItems;

public class ElementalItemHelper {

    public static final String ERROR_MESSAGE = "Element Type must not be NONE";

    private ElementalItemHelper() {}

    public static Item getCrystalForType(ElementType type) {
        if (!ECItems.CRYSTALS.containsKey(type)) return ECItems.INERT_CRYSTAL.get();
        return ECItems.CRYSTALS.get(type).get();
    }

    public static Item getShardForType(ElementType type) {
        if (!ECItems.SHARDS.containsKey(type)) throw new IllegalArgumentException(ERROR_MESSAGE);
        return ECItems.SHARDS.get(type).get();
    }

    public static Item getPowerfulShardForType(ElementType type) {
        if (!ECItems.POWERFUL_SHARDS.containsKey(type)) throw new IllegalArgumentException(ERROR_MESSAGE);
        return ECItems.POWERFUL_SHARDS.get(type).get();
    }

    public static Item getDisplacementPlate(ElementType type) {
        if (!ECBlocks.SOURCE_DISPLACEMENT_PLATES.containsKey(type)) throw new IllegalArgumentException(ERROR_MESSAGE);
        return ECBlocks.SOURCE_DISPLACEMENT_PLATES.get(type).get().asItem();
    }
}
