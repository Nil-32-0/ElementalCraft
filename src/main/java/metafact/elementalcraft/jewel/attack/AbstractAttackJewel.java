package metafact.elementalcraft.jewel.attack;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.jewel.Jewel;

public abstract class AbstractAttackJewel extends Jewel {

    protected AbstractAttackJewel(ElementType elementType, int consumption) {
        super(elementType, consumption);
        this.ticking = false;
    }

    public abstract void onAttack(Entity attacker, LivingEntity target);
}
