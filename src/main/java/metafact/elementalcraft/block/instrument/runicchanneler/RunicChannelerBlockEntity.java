package metafact.elementalcraft.block.instrument.runicchanneler;

import javax.annotation.Nonnull;

import metafact.elementalcraft.block.entity.ECBlockEntityTypes;
import metafact.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import metafact.elementalcraft.config.ECConfig;
import metafact.elementalcraft.container.SingleItemContainer;
import metafact.elementalcraft.recipe.ECRecipeTypes;
import metafact.elementalcraft.recipe.instrument.RunicChannelerRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;

public class RunicChannelerBlockEntity extends AbstractInstrumentBlockEntity<RunicChannelerBlockEntity, RunicChannelerRecipe> {
    private static final Config<RunicChannelerBlockEntity, RunicChannelerRecipe> CONFIG = new Config<>(
        ECBlockEntityTypes.RUNIC_CHANNELER,
        ECRecipeTypes.RUNIC_CHANNELER,
        ECConfig.SERVER.runicChannelerTransferSpeed,
        ECConfig.SERVER.runicChannelerMaxRunes,
        0,
        true,
        false
    );

    private final SingleItemContainer inventory;

    public RunicChannelerBlockEntity(BlockPos pos, BlockState state) {
        super(CONFIG, pos, state);
        this.inventory = new SingleItemContainer(this::setChanged);
    }

    @Nonnull
    @Override
    public Container getInventory() {
        return inventory;
    }

}
