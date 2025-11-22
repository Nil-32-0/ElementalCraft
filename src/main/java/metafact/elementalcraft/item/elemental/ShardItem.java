package metafact.elementalcraft.item.elemental;

import metafact.elementalcraft.api.element.ElementType;

public class ShardItem extends ElementalItem {

	private static final String NAME = "shard";
	private static final String POWERFUL = "powerful_";

    public static String generateName(ElementType type) {
        return type.getSerializedName() + "_" + NAME;
    }

    public static String generateNamePowerful(ElementType type) {
        return POWERFUL + generateName(type);
    }

	private final int elementAmount;

	public ShardItem(ElementType elementType) {
		this(elementType, 1);
	}

	public ShardItem(ElementType elementType, int elementAmount) {
		super(elementType);
		this.elementAmount = elementAmount;
	}

	public int getElementAmount() {
		return elementAmount;
	}
}
