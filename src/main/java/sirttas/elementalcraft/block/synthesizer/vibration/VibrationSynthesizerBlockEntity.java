package sirttas.elementalcraft.block.synthesizer.vibration;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.synthesizer.AbstractSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.SynthesizerProperties;
import sirttas.elementalcraft.range.RangeRenderTimer;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VibrationSynthesizerBlockEntity extends AbstractSynthesizerBlockEntity implements GameEventListener.Holder<VibrationSystem.Listener>, VibrationSystem {
    private final RangeRenderTimer rangeRenderTimer = new RangeRenderTimer();
    private VibrationSystem.Data vibrationData;
    private final VibrationSystem.Listener vibrationListener;
    private final VibrationSystem.User vibrationUser;

    public VibrationSynthesizerBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.VIBRATION_SYNTHESIZER, SynthesizerProperties.getFromConfig(VibrationSynthesizerBlockEntity.class), pos, state);
        this.vibrationData = new VibrationSystem.Data();
        this.vibrationListener = new VibrationSystem.Listener(this);
        this.vibrationUser = new VibrationUser(pos);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, VibrationSynthesizerBlockEntity vibrationSynthesizer) {
        vibrationSynthesizer.handleSynthesis();
        VibrationSystem.Ticker.tick(level, vibrationSynthesizer.vibrationData, vibrationSynthesizer.vibrationUser);
    }

    @Override
    protected int synthesizeElement() {
        return 0;
    }

    public boolean showsRange() {
        return rangeRenderTimer.showsRange();
    }

    public void startShowingRange() {
        rangeRenderTimer.startShowingRange();
    }

    @Override
    public @NotNull Listener getListener() {
        return this.vibrationListener;
    }

    @Override
    public @NotNull Data getVibrationData() {
        return this.vibrationData;
    }

    @Override
    public @NotNull User getVibrationUser() {
        return this.vibrationUser;
    }

    @Override
    public ElementType getElementType() {
        return ElementType.AIR;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);

        VibrationSystem.Data.CODEC.encodeStart(NbtOps.INSTANCE, this.vibrationData)
                .resultOrPartial(message -> ElementalCraftApi.LOGGER.error("Failed to encode vibration listener for vibration air synthesizer: '{}'", message))
                .ifPresent(tag -> compound.put(ECNames.LISTENER, tag));
    }

    @Override
    public void load(@Nonnull CompoundTag compoundTag) {
        super.load(compoundTag);

        if (compoundTag.contains(ECNames.LISTENER, 10)) {
            VibrationSystem.Data.CODEC.parse(NbtOps.INSTANCE, compoundTag.getCompound(ECNames.LISTENER))
                    .resultOrPartial(message -> ElementalCraftApi.LOGGER.error("Failed to parse vibration listener for vibration air synthesizer: '{}'", message))
                    .ifPresent(data -> this.vibrationData = data);
        }
    }

    private class VibrationUser implements VibrationSystem.User {
        private final PositionSource positionSource;

        private VibrationUser(BlockPos pos) {
            this.positionSource = new BlockPositionSource(pos);
        }

        @Override
        public int getListenerRadius() {
            var range = getRange();

            return (int) Math.round(range.getSize() * 2); // * 2 to make sure we match everything in range
        }

        @Override
        public @NotNull PositionSource getPositionSource() {
            return positionSource;
        }

        @Override
        public boolean canReceiveVibration(@NotNull ServerLevel serverLevel, @NotNull BlockPos pos, @NotNull GameEvent gameEvent, @NotNull GameEvent.Context context) {
            return pos != getBlockPos() &&
                    getRange().contains(pos.getCenter()) &&
                    getBlockState().getValue(VibrationSynthesizerBlock.PHASE) == SculkSensorPhase.INACTIVE &&
                    gameEvent.is(ECTags.GameEvents.SYNTHESIZABLE_TO_AIR);
        }

        @Override
        public void onReceiveVibration(@NotNull ServerLevel serverLevel, @NotNull BlockPos pos, @NotNull GameEvent gameEvent, @Nullable Entity entity, @Nullable Entity owner, float range) {
            var state = getBlockState();

            ((SingleElementStorage) getElementStorage().resolve().orElseThrow())
                    .insertElement(Math.round(synthesisMultiplier), ElementType.AIR, false);
            serverLevel.setBlockAndUpdate(getBlockPos(), state.setValue(VibrationSynthesizerBlock.PHASE, SculkSensorPhase.ACTIVE));
            serverLevel.scheduleTick(getBlockPos(), state.getBlock(), 30);
            serverLevel.playSound(
                    null,
                    worldPosition.getX() + 0.5,
                    worldPosition.getY() + 0.5,
                    worldPosition.getZ() + 0.5,
                    SoundEvents.SCULK_CLICKING,
                    SoundSource.BLOCKS,
                    1.0F,
                    serverLevel.random.nextFloat() * 0.2F + 0.8F
            );
            // TODO turn off other synthesizers in range
        }
    }
}
