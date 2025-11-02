package sirttas.elementalcraft.api.infusion.tool.effect;

import com.mojang.serialization.Codec;
import metafact.dpanvil_m.api.codec.ICodecProvider;

public record ToolInfusionEffectType<T extends IToolInfusionEffect>(Codec<T> codec) implements ICodecProvider<T> {


}
