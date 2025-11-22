package metafact.elementalcraft.pureore.injector;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import metafact.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import metafact.elementalcraft.block.instrument.io.mill.grindstone.AbstractMillGrindstoneBlockEntity;
import metafact.elementalcraft.container.ContainerBlockEntityWrapper;
import metafact.elementalcraft.recipe.ECRecipeTypes;
import metafact.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;
import metafact.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

import javax.annotation.Nonnull;

public class PureOreGrindingRecipeInjector extends AbstractPureOreRecipeInjector<ContainerBlockEntityWrapper<AbstractMillGrindstoneBlockEntity>, IGrindingRecipe> {

    protected PureOreGrindingRecipeInjector() {
        super(ECRecipeTypes.GRINDING.get(), true);
    }

    @Override
    public IGrindingRecipe build(@Nonnull RegistryAccess registry, @NotNull IGrindingRecipe recipe, @NotNull Ingredient ingredient) {
        return new GrindingRecipe(buildRecipeId(recipe.getId()), ingredient, getRecipeOutput(registry, recipe), recipe.getElementAmount(), recipe instanceof GrindingRecipe airMillGrindingRecipe ? airMillGrindingRecipe.luckRation() : 0);
    }
}
