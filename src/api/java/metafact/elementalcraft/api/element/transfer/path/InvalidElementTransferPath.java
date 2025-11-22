package metafact.elementalcraft.api.element.transfer.path;

import metafact.elementalcraft.api.element.ElementType;

import java.util.Collections;
import java.util.List;

public class InvalidElementTransferPath implements IElementTransferPath {

    public static final InvalidElementTransferPath INSTANCE = new InvalidElementTransferPath();

    private InvalidElementTransferPath() {}

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void transfer() {
        // nothing to do
    }

    @Override
    public List<IElementTransferPathNode> getNodes() {
        return Collections.emptyList();
    }

    @Override
    public ElementType getElementType() {
        return ElementType.NONE;
    }
}
