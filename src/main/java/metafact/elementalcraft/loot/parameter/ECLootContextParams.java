package metafact.elementalcraft.loot.parameter;

import net.minecraft.core.Direction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import metafact.elementalcraft.api.ElementalCraftApi;

public class ECLootContextParams {

    public static final LootContextParam<Direction> DIRECTION = create("direction");

    private ECLootContextParams() {}

    private static <T> LootContextParam<T> create(String name) {
        return new LootContextParam<>(ElementalCraftApi.createRL(name));
    }
}
