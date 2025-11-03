package sirttas.elementalcraft.api.source.flux;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface ISourceFlux {
    float getRatio();

    float getMax();

    float getCurrentFlux();

    void setFlux(float amount);

    void consume();

    void consume(float amount);

    void replenish(float amount);
}
