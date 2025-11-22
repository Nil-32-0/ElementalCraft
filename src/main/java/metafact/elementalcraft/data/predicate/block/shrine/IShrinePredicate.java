package metafact.elementalcraft.data.predicate.block.shrine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import metafact.dpanvil_m.api.predicate.block.IBlockPosPredicate;
import metafact.elementalcraft.block.entity.BlockEntityHelper;
import metafact.elementalcraft.block.shrine.AbstractShrineBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;


public interface IShrinePredicate extends IBlockPosPredicate {

	boolean test(AbstractShrineBlockEntity shrine);

	default Predicate<AbstractShrineBlockEntity> asShrinePredicate() {
		return this::test;
	}

	@Override
	default boolean test(@Nonnull LevelReader level, @Nonnull BlockPos pos, @Nullable Direction direction) {
		return BlockEntityHelper.getBlockEntityAs(level, pos, AbstractShrineBlockEntity.class)
                .map(this::test)
                .orElse(false);
	}

}
