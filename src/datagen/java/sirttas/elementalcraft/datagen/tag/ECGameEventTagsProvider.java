package sirttas.elementalcraft.datagen.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.GameEventTagsProvider;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class ECGameEventTagsProvider extends GameEventTagsProvider {

    public ECGameEventTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper existingFileHelper) {
        super(output, provider, ElementalCraftApi.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider provider) {
        tag(ECTags.GameEvents.SYNTHESIZABLE_TO_AIR).add(
                GameEvent.STEP,
                GameEvent.DRINK,
                GameEvent.EAT,
                GameEvent.ELYTRA_GLIDE,
                GameEvent.TELEPORT,
                GameEvent.SWIM,
                GameEvent.HIT_GROUND,
                GameEvent.SPLASH,
                GameEvent.FLAP);
    }

}