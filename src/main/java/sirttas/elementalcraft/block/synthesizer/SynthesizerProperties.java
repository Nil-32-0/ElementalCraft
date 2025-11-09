package sirttas.elementalcraft.block.synthesizer;

import net.minecraftforge.common.ForgeConfigSpec;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.range.Range;
import sirttas.elementalcraft.block.synthesizer.cracking.CrackingSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.cracking.SculkCrackingSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.vibration.VibrationSynthesizerBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

public record SynthesizerProperties(
    int maxRunes,
    int synthesisSpeed,
    float synthesisMultiplier,
    int bufferCapacity,
    Range range
) {
    public static SynthesizerProperties DEFAULT = new SynthesizerProperties(0, 0, 0F, 0, Range.DEFAULT);

    public static SynthesizerProperties getFromConfig(Class<? extends AbstractSynthesizerBlockEntity> blockEntityType) {
        var values = ECConfig.SERVER.synthesizerValues.get(blockEntityType);
        int defaultSynthSpeed;
        int defaultCapacity;
        int maxRunes;
        float mult;
        try {
            defaultSynthSpeed = ((ForgeConfigSpec.IntValue) values.get("speed")).get();
            defaultCapacity = ((ForgeConfigSpec.IntValue) values.get("capacity")).get();
            maxRunes = ((ForgeConfigSpec.IntValue) values.get("runes")).get();
            mult = ((ForgeConfigSpec.DoubleValue) values.get("mult")).get().floatValue();
        } catch (Exception e) {
            ElementalCraftApi.LOGGER.error("Config not generated for {}, falling back to default values.", blockEntityType.getName());
            return SynthesizerProperties.DEFAULT;
        }

        Range range = Range.DEFAULT;
        if (blockEntityType.equals(CrackingSynthesizerBlockEntity.class)) {
            range = Range.builder().expandingDown(5, 2).move(0, -1, 0).stitch().fixedHeight().build();
        }
        if (blockEntityType.equals(SculkCrackingSynthesizerBlockEntity.class)) {
            range = Range.builder().box(8).stitch().build();
        }
        if (blockEntityType.equals(VibrationSynthesizerBlockEntity.class)) {
            range = Range.builder().box(10).build();
        }

        return new SynthesizerProperties(
                maxRunes,
                defaultSynthSpeed,
                mult,
                defaultCapacity,
                range
        );
    }
}
