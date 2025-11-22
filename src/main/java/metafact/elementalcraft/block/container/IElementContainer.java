package metafact.elementalcraft.block.container;

import metafact.elementalcraft.api.element.storage.single.ISingleElementStorage;

public interface IElementContainer {

	boolean isSmall();

	ISingleElementStorage getElementStorage();
}