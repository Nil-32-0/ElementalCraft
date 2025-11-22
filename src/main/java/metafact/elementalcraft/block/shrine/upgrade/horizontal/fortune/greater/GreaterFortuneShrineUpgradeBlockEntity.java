package metafact.elementalcraft.block.shrine.upgrade.horizontal.fortune.greater;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import metafact.elementalcraft.api.ElementalCraftCapabilities;
import metafact.elementalcraft.api.name.ECNames;
import metafact.elementalcraft.api.rune.handler.IRuneHandler;
import metafact.elementalcraft.api.rune.handler.RuneHandler;
import metafact.elementalcraft.block.entity.AbstractECBlockEntity;
import metafact.elementalcraft.block.entity.ECBlockEntityTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GreaterFortuneShrineUpgradeBlockEntity extends AbstractECBlockEntity {

    private final RuneHandler runeHandler;

    public GreaterFortuneShrineUpgradeBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.GREATER_FORTUNE_SHRINE_UPGRADE, pos, state);
        runeHandler = new RuneHandler(1, this::setChanged);
    }

    @Nonnull
    public RuneHandler getRuneHandler() {
        return runeHandler;
    }

    @Override
    public void load(@Nonnull CompoundTag compound) {
        super.load(compound);
        if (compound.contains(ECNames.RUNE_HANDLER)) {
            IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, 8));
        }
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
    }

    @Override
    @Nonnull
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
        if (!this.remove && cap == ElementalCraftCapabilities.RUNE_HANDLE) {
            return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
        }
        return super.getCapability(cap, side);
    }
}