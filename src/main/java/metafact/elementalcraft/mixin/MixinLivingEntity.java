package metafact.elementalcraft.mixin;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import metafact.elementalcraft.jewel.DemigodJewel;
import metafact.elementalcraft.jewel.Jewel;
import metafact.elementalcraft.jewel.JewelHelper;
import metafact.elementalcraft.jewel.StriderJewel;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

    protected MixinLivingEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "checkTotemDeathProtection(Lnet/minecraft/world/damagesource/DamageSource;)Z",
            at = @At("RETURN"),
            cancellable = true)
    private void checkTotemDeathProtection$return(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || cir.getReturnValueZ()) {
            return;
        }
        if (DemigodJewel.trigger((LivingEntity) (Object) this)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "canStandOnFluid(Lnet/minecraft/world/level/material/FluidState;)Z",
            at = @At("RETURN"),
            cancellable = true)
    public void canStandOnFluid$return(FluidState state, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            return;
        }
        for (Jewel jewel :JewelHelper.getActiveJewels(this)) {
            if (jewel instanceof StriderJewel striderJewel && state.is(striderJewel.getTag())) {
                cir.setReturnValue(true);
                return;
            }
        }
    }

}
