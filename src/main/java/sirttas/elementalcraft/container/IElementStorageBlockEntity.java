package sirttas.elementalcraft.container;

import net.minecraftforge.common.util.LazyOptional;

public interface IElementStorageBlockEntity {

    <U> LazyOptional<U> getElementStorage();

}