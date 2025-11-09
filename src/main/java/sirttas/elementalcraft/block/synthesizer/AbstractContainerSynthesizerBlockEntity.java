package sirttas.elementalcraft.block.synthesizer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.container.IContainerBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractContainerSynthesizerBlockEntity extends AbstractSynthesizerBlockEntity implements IContainerBlockEntity {

    protected AbstractContainerSynthesizerBlockEntity(
            RegistryObject<? extends BlockEntityType<?>> blockEntityType,
            SynthesizerProperties properties,
            BlockPos pos,
            BlockState state
    ) {
        super(blockEntityType, properties, pos, state);
    }

    @Override
    protected int synthesizeElement() {
        var inventory = getInventory();
        var stack = inventory.getItem(0);

        if (stack.isEmpty()) {
            return 0;
        }

        var amount = getElementAmountForStack(stack);

        if (amount <= 0) {
            return 0;
        } else if (stack.isDamageableItem()) {

        } else if (stack.hasCraftingRemainingItem()) {
            inventory.setItem(0, stack.getCraftingRemainingItem());
        } else if (!stack.isEmpty()) {
            stack.shrink(1);
            if (stack.isEmpty()) {
                inventory.setItem(0, stack.getCraftingRemainingItem());
            }
        }
        return amount;
    }

    protected abstract int getElementAmountForStack(ItemStack stack);

    @Override
    public void load(@Nonnull CompoundTag compound) {
        super.load(compound);
        Container inv = getInventory();

        if (inv instanceof INBTSerializable nbtInv && compound.contains(ECNames.INVENTORY)) {
            nbtInv.deserializeNBT(compound.get(ECNames.INVENTORY));
        }
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        Container inv = getInventory();

        if (inv instanceof INBTSerializable<?> nbtInv) {
            compound.put(ECNames.INVENTORY, nbtInv.serializeNBT());
        }
    }

    @Override
    @Nonnull
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return getItemHandler(side).cast();
        }
        return super.getCapability(cap, side);
    }

}
