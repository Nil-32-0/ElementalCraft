package sirttas.elementalcraft.block.synthesizer.solar;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.ElementStorageHelper;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.synthesizer.AbstractContainerSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.SynthesizerProperties;
import sirttas.elementalcraft.container.SingleItemContainer;
import sirttas.elementalcraft.item.elemental.LensItem;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.Optional;

public class SolarSynthesizerBlockEntity extends AbstractContainerSynthesizerBlockEntity {

	private final SingleItemContainer inventory;

	public SolarSynthesizerBlockEntity(BlockPos pos, BlockState state) {
        this(ECBlockEntityTypes.SOLAR_SYNTHESIZER, SynthesizerProperties.getFromConfig(SolarSynthesizerBlockEntity.class), pos, state);
	}

	protected SolarSynthesizerBlockEntity(RegistryObject<? extends BlockEntityType<?>> blockEntityType, SynthesizerProperties properties, BlockPos pos, BlockState state) {
		super(blockEntityType, properties, pos, state);
		inventory = new SingleItemContainer(this::setChanged);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, SolarSynthesizerBlockEntity solarSynthesizer) {
		solarSynthesizer.handleSynthesis();
	}

    @Override
    protected int getElementAmountForStack(ItemStack stack) {
        if (isReceivingSkyLight() && stack.is(ECTags.Items.LENSES)) {
            return Math.round(synthesisMultiplier);
        }
        return 0;
    }

    @Override
    protected int synthesizeElement() {
        var amount = super.synthesizeElement();

        if (amount > 0) {
            breakLens(this.level, this.getBlockPos());
        }

        return amount;
    }

    protected boolean isReceivingSkyLight() {
        return level != null && level.dimensionType().hasSkyLight() && level.canSeeSky(this.worldPosition) && level.isDay();
    }

	protected void breakLens(Level level, BlockPos pos) {
		ItemStack stack = inventory.getItem(0);

		if (!stack.isEmpty() && stack.getDamageValue() >= stack.getMaxDamage()) {
			Vec3 position = Vec3.atCenterOf(pos).add(0, 6.5 / 16, 0);

			inventory.setItem(0, ItemStack.EMPTY);
			level.playSound(null, position.x(), position.y(), position.z(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 0.8F, 0.8F + level.random.nextFloat() * 0.4F);
			ParticleHelper.createItemBreakParticle(level, position, level.random, stack, 3);
			setChanged();
		}
	}

    public ElementType getElementType() {
        var item = getInventory().getItem(0);

        if (item.getItem() instanceof LensItem lens) {
            return lens.getElementType();
        }
        return ElementType.NONE;
    }

	@Nonnull
    @Override
	public  <U> LazyOptional<U> getElementStorage() {
		var item = getInventory().getItem(0);

		if (item.getItem() instanceof LensItem lens) {
			return LazyOptional.of(() -> lens.getStorage(item, (int) this.synthesisMultiplier)).cast();
		}
		return ElementStorageHelper.get(item).cast();
	}

	@Nonnull
    @Override
	public Container getInventory() {
		return inventory;
	}

	public Optional<ISingleElementStorage> getElementStorageNonLazy() {
		return ElementStorageHelper.get(this)
				.filter(ISingleElementStorage.class::isInstance)
				.map(ISingleElementStorage.class::cast);
	}
}
