package sirttas.elementalcraft.api.element;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import sirttas.elementalcraft.api.name.ECNames;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public enum ElementType implements StringRepresentable, IElementTypeProvider {

	NONE(0, 0, 0, "none", 0, 1),
	WATER(43, 173, 255, "water", 1, 1),
	FIRE(247, 107, 27, "fire", 2, 1),
	EARTH(13, 128, 37, "earth", 3, 1),
	AIR(238, 255, 219, "air", 4, 1),
    ENTROPY(31, 31, 31, "entropy", 6, 1),
    PURITY(255, 255, 255, "purity", 7, 1);

	public static final List<ElementType> ALL_VALID = ImmutableList.copyOf(Stream.of(values()).filter(type -> type != NONE).toList());
	public static final Codec<ElementType> CODEC = StringRepresentable.fromEnum(ElementType::values);
	public static final EnumProperty<ElementType> STATE_PROPERTY = EnumProperty.create(ECNames.ELEMENT_TYPE, ElementType.class);
	
	private final float r;
	private final float g;
	private final float b;
	private final int color;
	private final String name;
    private final int gaugeOffset;
    private final String gaugeTextureLocation;
    private final int elementTier;

    ElementType(int r, int g, int b, String name, int gaugeOffset, int elementTier) {
        this(r, g, b, name, gaugeOffset, "textures/gui/element_gauge.png", elementTier);
    }

	ElementType(int r, int g, int b, String name, int gaugeOffset, String gaugeTextureLocation, int elementTier) {
		this.r = r / 255F;
		this.g = g / 255F;
		this.b = b / 255F;
		this.name = name;
		this.color = Mth.color(this.r, this.g, this.b);
        this.gaugeOffset = gaugeOffset;
        this.gaugeTextureLocation = gaugeTextureLocation;
        this.elementTier = elementTier;
	}

	public float getRed() {
		return r;
	}

	public float getGreen() {
		return g;
	}

	public float getBlue() {
		return b;
	}

	public int getColor() {
		return this == NONE ? -1 : color;
	}

    public int getGaugeOffset() {
        return gaugeOffset;
    }

    public String getGaugeTextureLocation() {
        return gaugeTextureLocation;
    }

    public int getElementTier() {
        return elementTier;
    }

    public static List<ElementType> getElementsTier(int tier) {
        return ALL_VALID.stream().filter(type -> type.getElementTier() == tier).toList();
    }

    public static List<ElementType> getElementsTierAbove(int tier) {
        return ALL_VALID.stream().filter(type -> type.getElementTier() >= tier).toList();
    }

    public static List<ElementType> getElementsTierBelow(int tier) {
        return ALL_VALID.stream().filter(type -> type.getElementTier() <= tier).toList();
    }

	public static ElementType random() {
		return random(RandomSource.create());
	}

	public static ElementType random(RandomSource rand) {
		int random = rand.nextInt(ALL_VALID.size());
        return ALL_VALID.get(random);
	}

    public static ElementType randomOfTier(RandomSource rand, int tier) {
        int random = rand.nextInt(getElementsTier(tier).size());
        return getElementsTier(tier).get(random);
    }

	@Nonnull
	@Override
	public String getSerializedName() {
		return this.name;
	}

	@Override
	public ElementType getElementType() {
		return this;
	}

	public String getTranslationKey() {
		return "element.elementalcraft." + getSerializedName();
	}

	public Component getDisplayName() {
		return Component.translatable(getTranslationKey());
	}

	public static ElementType byName(String name) {
		for (ElementType elementType : values()) {
			if (elementType.name.equals(name)) {
				return elementType;
			}
		}
		return NONE;
	}

	public static ElementType getElementType(BlockState state) {
		if (state.hasProperty(STATE_PROPERTY)) {
			return state.getValue(STATE_PROPERTY);
		} else if (state.getBlock() instanceof IElementTypeProvider provider) {
			return provider.getElementType();
		}
		return ElementType.NONE;
	}

	public static <T> RecordCodecBuilder<T, ElementType> forGetter(final Function<T, ElementType> getter) {
		return CODEC.fieldOf(ECNames.ELEMENT_TYPE).forGetter(getter);
	}
}
