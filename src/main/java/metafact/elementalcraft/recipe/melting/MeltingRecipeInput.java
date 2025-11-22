package metafact.elementalcraft.recipe.melting;

import net.minecraft.world.level.block.state.BlockState;

public record MeltingRecipeInput (
        BlockState state,
        int elementAmount,
        int elementConsumption
) {
}
