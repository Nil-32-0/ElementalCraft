package metafact.elementalcraft.jewel.defence;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.jewel.Jewel;

public class DefenceJewel extends Jewel {

    protected DefenceJewel(ElementType elementType, int consumption) {
        super(elementType, consumption);
        this.ticking = false;
    }

    public float onHurt(Entity entity, DamageSource source, float amount) {
        return amount;
    }
}
