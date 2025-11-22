package metafact.elementalcraft.element.storage;

import net.minecraft.world.level.block.entity.BlockEntity;
import metafact.elementalcraft.api.element.storage.ElementStorageHelper;
import metafact.elementalcraft.api.element.storage.IElementStorage;

import static metafact.elementalcraft.assertion.Assertions.assertThat;

public class ElementStorageGameTestHelper {

    private ElementStorageGameTestHelper() { }

    public static IElementStorage get(BlockEntity blockEntity) {
        assertThat(blockEntity).isNotNull();

        var storage = ElementStorageHelper.get(blockEntity).resolve().orElse(null);

        assertThat(storage).isNotNull();

        return storage;
    }
}