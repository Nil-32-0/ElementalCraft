package metafact.elementalcraft.block.instrument.io.mill.grindstone;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.instrument.io.mill.AbstractMillBlockEntity;
import metafact.elementalcraft.interaction.ECinteractions;
import metafact.elementalcraft.interaction.ie.IEInteraction;
import metafact.elementalcraft.interaction.mekanism.MekanismInteraction;
import metafact.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public abstract class AbstractMillGrindstoneBlockEntity extends AbstractMillBlockEntity<AbstractMillGrindstoneBlockEntity, IGrindingRecipe> {


    protected AbstractMillGrindstoneBlockEntity(Config<AbstractMillGrindstoneBlockEntity, IGrindingRecipe> config, ElementType elementType, BlockPos pos, BlockState state) {
        super(config, elementType, pos, state);
    }

    @Override
    protected IGrindingRecipe lookupRecipe() {
        if (getContainerElementType() == ElementType.NONE) {
            return null;
        }

        var recipe = super.lookupRecipe();

        if (recipe == null && ECinteractions.isMekanismActive()) {
            recipe = MekanismInteraction.lookupCrusherRecipe(level, this);
        }
        if (recipe == null && ECinteractions.isImmersiveEngineeringActive()) {
            recipe = IEInteraction.lookupCrusherRecipe(level, this);
        }
        return recipe;
    }
}
