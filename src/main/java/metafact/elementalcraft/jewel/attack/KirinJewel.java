package metafact.elementalcraft.jewel.attack;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.damagesource.ECDamageTypes;

import java.util.List;

public class KirinJewel extends AbstractAttackJewel {

    public static final String NAME = "kirin";

    public KirinJewel() {
        super(ElementType.FIRE, 2000);
    }

    public static DamageSource holyFire(Entity source) {
        Holder<DamageType> damageSourceHolder = source.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ECDamageTypes.HOLY_FIRE);
        return new DamageSource(damageSourceHolder, source);
    }

    @Override
    public void onAttack(Entity attacker, LivingEntity target) {
        var isUndead = target.getMobType() == MobType.UNDEAD;

        target.hurt(holyFire(attacker), isUndead ? 10 : 5);
        target.setSecondsOnFire(isUndead ? 5 : 2);
    }

    @Override
    public void appendHoverText(List<Component> tooltip) {
        tooltip.add(Component.translatable("tooltip.elementalcraft.kirin").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(tooltip);
    }
}
