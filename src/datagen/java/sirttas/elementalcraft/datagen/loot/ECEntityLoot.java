package sirttas.elementalcraft.datagen.loot;

import net.minecraft.Util;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.entity.ECEntities;
import sirttas.elementalcraft.item.elemental.ElementalItemHelper;
import sirttas.elementalcraft.loot.LootHandler;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ECEntityLoot extends EntityLootSubProvider {

    private static final Map<ElementType, LootPool.Builder> ELEMENT_POOLS = ElementType.getElementsTier(1).stream().collect(Collectors.toMap(
            type -> type,
            ECEntityLoot::createShardPool
    ));

	private static final List<EntityType<?>> ENTITIES = Util.make(() -> {
		var list = new ArrayList<EntityType<?>>(LootHandler.INJECT_LIST.size() + 1);

		list.add(ECEntities.THROWN_ELEMENT_CRYSTAL.get());
		list.addAll(LootHandler.INJECT_LIST);
		return List.copyOf(list);
	});

	protected ECEntityLoot() {
		super(FeatureFlags.REGISTRY.allFlags(), FeatureFlags.REGISTRY.subset());
	}

	@Override
	public void generate() {
        ElementType.ALL_VALID.forEach(this::addThrownElementCrystal);

		addInject(ELEMENT_POOLS.get(ElementType.EARTH), EntityType.ZOMBIE);
		addInject(ELEMENT_POOLS.get(ElementType.EARTH), EntityType.ZOMBIE_VILLAGER);
		addInject(ELEMENT_POOLS.get(ElementType.EARTH), EntityType.SKELETON);
		addInject(ELEMENT_POOLS.get(ElementType.EARTH), EntityType.WITHER_SKELETON);
		addInject(ELEMENT_POOLS.get(ElementType.EARTH), EntityType.SILVERFISH);
		addInject(ELEMENT_POOLS.get(ElementType.EARTH), EntityType.IRON_GOLEM);
		addInject(ELEMENT_POOLS.get(ElementType.EARTH), EntityType.SKELETON_HORSE);
		addInject(ELEMENT_POOLS.get(ElementType.EARTH), EntityType.GOAT);
		addInject(ELEMENT_POOLS.get(ElementType.FIRE), EntityType.CREEPER);
		addInject(ELEMENT_POOLS.get(ElementType.FIRE), EntityType.GHAST);
		addInject(ELEMENT_POOLS.get(ElementType.FIRE), EntityType.BLAZE);
		addInject(ELEMENT_POOLS.get(ElementType.FIRE), EntityType.HUSK);
		addInject(ELEMENT_POOLS.get(ElementType.FIRE), EntityType.MAGMA_CUBE);
		addInject(ELEMENT_POOLS.get(ElementType.FIRE), EntityType.ZOMBIFIED_PIGLIN);
		addInject(ELEMENT_POOLS.get(ElementType.FIRE), EntityType.ZOGLIN);
		addInject(ELEMENT_POOLS.get(ElementType.WATER), EntityType.DROWNED);
		addInject(ELEMENT_POOLS.get(ElementType.WATER), EntityType.GUARDIAN);
		addInject(ELEMENT_POOLS.get(ElementType.WATER), EntityType.ELDER_GUARDIAN);
		addInject(ELEMENT_POOLS.get(ElementType.WATER), EntityType.SLIME);
		addInject(ELEMENT_POOLS.get(ElementType.WATER), EntityType.STRAY);
		addInject(ELEMENT_POOLS.get(ElementType.WATER), EntityType.SQUID);
		addInject(ELEMENT_POOLS.get(ElementType.WATER), EntityType.GLOW_SQUID);
		addInject(ELEMENT_POOLS.get(ElementType.WATER), EntityType.AXOLOTL);
		addInject(ELEMENT_POOLS.get(ElementType.WATER), EntityType.POLAR_BEAR);
		addInject(ELEMENT_POOLS.get(ElementType.WATER), EntityType.DOLPHIN);
		addInject(ELEMENT_POOLS.get(ElementType.WATER), EntityType.COD);
		addInject(ELEMENT_POOLS.get(ElementType.WATER), EntityType.SALMON);
		addInject(ELEMENT_POOLS.get(ElementType.WATER), EntityType.TROPICAL_FISH);
		addInject(ELEMENT_POOLS.get(ElementType.WATER), EntityType.PUFFERFISH);
		addInject(ELEMENT_POOLS.get(ElementType.AIR), EntityType.ENDERMAN);
		addInject(ELEMENT_POOLS.get(ElementType.AIR), EntityType.SPIDER);
		addInject(ELEMENT_POOLS.get(ElementType.AIR), EntityType.CAVE_SPIDER);
		addInject(ELEMENT_POOLS.get(ElementType.AIR), EntityType.PHANTOM);
		addInject(ELEMENT_POOLS.get(ElementType.AIR), EntityType.SHULKER);
	}

	private void addThrownElementCrystal(ElementType type) {
		var crystalLocation = ForgeRegistries.ITEMS.getKey(ElementalItemHelper.getCrystalForElement(type));

		add(ECEntities.THROWN_ELEMENT_CRYSTAL.get(), new ResourceLocation(crystalLocation.getNamespace(), "entities/thrown_element_crystal/" + crystalLocation.getPath()), LootTable.lootTable().withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(ElementalItemHelper.getShardForElement(type)).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 7))).setWeight(10))
						.add(LootItem.lootTableItem(ElementalItemHelper.getPowerfulShardForElement(type))))
				.setParamSet(LootContextParamSets.SELECTOR));
	}

	private static LootPool.Builder createShardPool(ElementType type) {
		return LootPool.lootPool().setRolls(ConstantValue.exactly(1))
				.add(LootItem.lootTableItem(ElementalItemHelper.getShardForElement(type)).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(10))
				.add(LootItem.lootTableItem(ElementalItemHelper.getPowerfulShardForElement(type)).when(LootItemKilledByPlayerCondition.killedByPlayer()))
				.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.25F, 0.03F));
	}

	private void addInject(LootPool.Builder pool, EntityType<?> entityType) {
		addInject(entityType, LootTable.lootTable().withPool(pool).setParamSet(LootContextParamSets.ENTITY), ElementalCraftApi.createRL(entityType.getDefaultLootTable().getPath()));
	}

	private void addInject(EntityType<?> entityType, LootTable.Builder builder, ResourceLocation location) {
		if (!LootHandler.INJECT_LIST.contains(entityType)) {
			throw new IllegalStateException(MessageFormat.format("{} is not present in LootHandler.INJECT_LIST and will not be injected at runtime!", location));
		}
		add(entityType, new ResourceLocation(location.getNamespace(), "inject/" + location.getPath()), builder);
	}

	@Override
	protected boolean canHaveLootTable(@NotNull EntityType<?> entityType) {
		return ENTITIES.contains(entityType);
	}
}
