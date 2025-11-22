package metafact.elementalcraft.item.elemental;

import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.api.element.IElementTypeProvider;
import metafact.elementalcraft.item.ECItem;
import metafact.elementalcraft.property.ECProperties;

public class ElementalItem extends ECItem implements IElementTypeProvider {

	protected final ElementType elementType;

	public ElementalItem(ElementType elementType) {
		this(ECProperties.Items.DEFAULT_ITEM_PROPERTIES, elementType);
	}

	public ElementalItem(Properties properties, ElementType elementType) {
		super(properties);
		this.elementType = elementType;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}
}
