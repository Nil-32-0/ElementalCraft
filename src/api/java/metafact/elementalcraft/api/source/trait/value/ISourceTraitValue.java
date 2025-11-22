package metafact.elementalcraft.api.source.trait.value;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import metafact.elementalcraft.api.source.trait.SourceTrait;

public interface ISourceTraitValue {

	float getValue(SourceTrait.Type type);
	Component getDescription();

	default boolean isActive(Level level, BlockPos pos) {
		return true;
	}
}
