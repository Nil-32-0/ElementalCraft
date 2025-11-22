package metafact.elementalcraft.block.source.breeder.pedestal;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import metafact.elementalcraft.api.element.ElementType;
import metafact.elementalcraft.block.entity.renderer.IRuneRenderer;
import metafact.elementalcraft.block.source.SourceRendererHelper;

import javax.annotation.Nonnull;

public class SourceBreederPedestalRenderer implements IRuneRenderer<SourceBreederPedestalBlockEntity> {

    @Override
    public void render(@Nonnull SourceBreederPedestalBlockEntity pedestal, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        IRuneRenderer.super.render(pedestal, partialTicks, poseStack, buffer, light, overlay);

        var type = pedestal.getElementType();

        if (type == ElementType.NONE) {
            return;
        }
        poseStack.translate(0, 0.6, 0);
        SourceRendererHelper.renderSource(poseStack, buffer, partialTicks, light, overlay, type, false, 1);
    }
}
