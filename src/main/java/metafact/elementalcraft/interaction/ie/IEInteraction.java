package metafact.elementalcraft.interaction.ie;

import blusunrize.immersiveengineering.api.crafting.IERecipeTypes;
import blusunrize.immersiveengineering.common.util.compat.jei.JEIRecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.IForgeRegistry;
import metafact.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import metafact.elementalcraft.block.ECBlocks;
import metafact.elementalcraft.block.instrument.io.mill.grindstone.AbstractMillGrindstoneBlockEntity;
import metafact.elementalcraft.interaction.ie.injector.ArcFurnacePureOreRecipeInjector;
import metafact.elementalcraft.interaction.ie.injector.CrusherPureOreRecipeInjector;
import metafact.elementalcraft.interaction.ie.recipe.IECrusherRecipeWrapper;
import metafact.elementalcraft.pureore.injector.PureOreRecipeInjectors;
import metafact.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public class IEInteraction {

    private IEInteraction() {}

    public static void registerPureOreRecipeInjectors(IForgeRegistry<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> registry) {
        PureOreRecipeInjectors.register(registry, new ArcFurnacePureOreRecipeInjector());
        PureOreRecipeInjectors.register(registry, new CrusherPureOreRecipeInjector());
    }

    public static void addAirMillToCrushing(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(ECBlocks.AIR_MILL_GRINDSTONE.get()), JEIRecipeTypes.CRUSHER);
    }

    public static IGrindingRecipe lookupCrusherRecipe(Level level, AbstractMillGrindstoneBlockEntity millGrindstone) {
        return level.getRecipeManager().getRecipeFor(IERecipeTypes.CRUSHER.get(), millGrindstone.getInventory(), level)
                .map(IECrusherRecipeWrapper::new)
                .filter(r -> r.matches(millGrindstone, level))
                .orElse(null);
    }

}
