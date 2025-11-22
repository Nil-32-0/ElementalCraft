package metafact.elementalcraft.item.source.receptacle;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.name.ECNames;
import metafact.elementalcraft.api.source.trait.SourceTrait;
import metafact.elementalcraft.api.source.trait.value.ISourceTraitValue;
import metafact.elementalcraft.block.source.trait.SourceTraitHelper;
import metafact.elementalcraft.item.ECItems;
import metafact.elementalcraft.nbt.NBTHelper;

import java.util.Map;

public class ReceptacleHelper {

	private ReceptacleHelper() {}
	
	public static ElementType getElementType(ItemStack stack) {
		return stack.isEmpty() || stack.getTag() == null ? ElementType.NONE : ElementType.byName(stack.getTag().getString(ECNames.ELEMENT_TYPE));
	}

	private static ItemStack setElementType(ItemStack stack, ElementType elementType) {
		stack.getOrCreateTag().putString(ECNames.ELEMENT_TYPE, elementType.getSerializedName());
		return stack;
	}

	public static ItemStack create(ElementType elementType) {
		return setElementType(new ItemStack(ECItems.RECEPTACLE.get()), elementType);
	}

	public static ItemStack create(ElementType elementType, Map<ResourceKey<SourceTrait>, ISourceTraitValue> traits) {
		var stack = create(elementType);
		var tag = NBTHelper.getOrCreate(stack.getOrCreateTag(), ECNames.BLOCK_ENTITY_TAG);

		tag.putBoolean(ECNames.ANALYZED, true);
		tag.put(ECNames.TRAITS_HOLDER, SourceTraitHelper.saveTraits(traits));
		return stack;
	}
}
