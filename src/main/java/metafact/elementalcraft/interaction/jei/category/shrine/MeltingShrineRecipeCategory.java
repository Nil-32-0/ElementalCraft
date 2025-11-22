package metafact.elementalcraft.interaction.jei.category.shrine;

import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import metafact.elementalcraft.api.ElementalCraftApi;
import metafact.elementalcraft.block.ECBlocks;
import metafact.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import metafact.elementalcraft.recipe.melting.MeltingRecipe;
import metafact.elementalcraft.renderer.ECRendererHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class MeltingShrineRecipeCategory extends AbstractShrineRecipeCategory<MeltingRecipe> {

    private final BlockState meltingShrine;
    private final ITickTimer timer;

    public MeltingShrineRecipeCategory(IGuiHelper guiHelper) {
        super("elementalcraft.jei.melting_shrine", createDrawableStack(guiHelper, new ItemStack(ECBlocks.MELTING_SHRINE.get())), guiHelper.createBlankDrawable(121, 80));
        meltingShrine = ECBlocks.MELTING_SHRINE.get().defaultBlockState();
        timer = guiHelper.createTickTimer(100, 4, false);
        setOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/extraction.png"), 0, 0, 24, 9), 72, 64);
    }

    @Nonnull
    @Override
    public RecipeType<MeltingRecipe> getRecipeType() {
        return ECJEIRecipeTypes.MELTING_SHRINE;
    }

    @Override
    public void draw(@Nonnull MeltingRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        render3D(guiGraphics, (p, b) -> {
            setupPose(p);
            ECRendererHelper.renderBlock(meltingShrine, p, b);
            p.translate(0, 1, 0);

            var t = timer.getValue();

            var blockList = ForgeRegistries.BLOCKS.tags().getTag(recipe.input()).stream().toList();

            if (t >= blockList.size()) {
                ECRendererHelper.renderFluid(recipe.result().defaultFluidState().createLegacyBlock(), p, b);
            } else {
                ECRendererHelper.renderBlock(blockList.get(t).defaultBlockState(), p, b);
            }
        });
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull MeltingRecipe recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 51, 60).addIngredients(
                Ingredient.of(ForgeRegistries.BLOCKS.tags().getTag(recipe.input()).stream()
                .map(Block::asItem)
                .map(ItemStack::new)));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 101, 60).addFluidStack(recipe.result(), 1000);
    }
}
