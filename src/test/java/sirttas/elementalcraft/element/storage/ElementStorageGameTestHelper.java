package sirttas.elementalcraft.element.storage;

import net.minecraft.world.level.block.entity.BlockEntity;
import sirttas.elementalcraft.api.element.storage.ElementStorageHelper;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

public class ElementStorageGameTestHelper {

    private ElementStorageGameTestHelper() { }

    public static IElementStorage get(BlockEntity blockEntity) {
        assertThat(blockEntity).isNotNull();

        var storage = ElementStorageHelper.get(blockEntity).resolve().orElse(null);

        assertThat(storage).isNotNull();

        return storage;
    }
}