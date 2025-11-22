package metafact.elementalcraft.block.source.breeder;

import net.minecraft.world.item.ItemStack;
import metafact.elementalcraft.container.SingleItemContainer;
import metafact.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;

public class SourceBreederItemContainer extends SingleItemContainer {

    public SourceBreederItemContainer(Runnable syncCallback) {
        super(syncCallback);
    }

    @Override
    public boolean canPlaceItem(int slot, @Nonnull ItemStack stack) {
        return slot == 0 && stack.is(ECTags.Items.SOURCE_SEEDS);
    }

}
