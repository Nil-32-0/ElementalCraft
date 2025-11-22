package metafact.elementalcraft.data.predicate.block.pipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import metafact.dpanvil_m.api.codec.CodecHelper;
import metafact.dpanvil_m.api.predicate.block.BlockPosPredicateType;
import metafact.dpanvil_m.api.predicate.block.IBlockPosPredicate;
import metafact.elementalcraft.api.name.ECNames;
import metafact.elementalcraft.block.pipe.ElementPipeBlockEntity;
import metafact.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import metafact.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import metafact.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import metafact.elementalcraft.data.predicate.block.ECBlockPosPredicateTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public record HasPipeUpgrade(PipeUpgradeType<?> type) implements IPipePredicate {

    public static final String NAME = "has_pipe_upgrade";
    public static final Codec<HasPipeUpgrade> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            CodecHelper.getRegistryCodec(PipeUpgradeTypes.REGISTRY).fieldOf(ECNames.PIPE_UPGRADE).forGetter(HasPipeUpgrade::type)
    ).apply(builder, HasPipeUpgrade::new));

    @Override
    public boolean test(@Nonnull ElementPipeBlockEntity pipe, @Nullable Direction direction) {
        if (direction == null) {
            return pipe.getUpgrades().values().stream().anyMatch(this::test);
        }
        return test(pipe.getUpgrade(direction));
    }

    private boolean test(PipeUpgrade upgrade) {
        return upgrade != null && upgrade.getType() == type;
    }

    @Override
    public BlockPosPredicateType<? extends IBlockPosPredicate> getType() {
        return ECBlockPosPredicateTypes.HAS_PIPE_UPGRADE.get();
    }
}
