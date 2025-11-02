package sirttas.elementalcraft.api.source.flux;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface ISourceFlux {
    float getRatio();

    void consume();
}
