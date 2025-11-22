package metafact.elementalcraft.item.source.receptacle;

import net.minecraft.world.item.ItemStack;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.source.trait.SourceTraitGameTestHelper;

public class ReceptacleGameTestHelper {

    private ReceptacleGameTestHelper() {}

    public static ItemStack createSimpleReceptacle(ElementType type) {
        return ReceptacleHelper.create(type, SourceTraitGameTestHelper.getDefaultTraits());
    }
}
