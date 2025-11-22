package metafact.elementalcraft.recipe;

import metafact.elementalcraft.container.ContainerBlockEntityWrapper;
import metafact.elementalcraft.container.IContainerBlockEntity;

public interface IRuntimeContainerBlockEntityRecipe<T extends IContainerBlockEntity> extends IContainerBlockEntityRecipe<T>, IRuntimeRecipe<ContainerBlockEntityWrapper<T>> {

}