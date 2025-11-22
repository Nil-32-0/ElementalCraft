package metafact.elementalcraft.api.element;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.StringRepresentable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;

public enum ElementTypeTier implements StringRepresentable {
    NONE("none", 0),
    PRIMORDIAL("primordial", 1),
    SYNTHESIZED("synthesized", 2);

    private final String name;
    private final int relativeTier;

    public static final List<ElementTypeTier> ALL_VALID = ImmutableList.copyOf(Stream.of(values()).filter(tier -> tier != NONE).toList());

    ElementTypeTier(String name, int relativeTier) {
        this.name = name;
        this.relativeTier = relativeTier;
    }

    public int getRelativeTier() {
        return this.relativeTier;
    }

    /**
     * Compares the relative tiers of the provided ElementTypes
     * @param other Another ElementTypeTier to compare
     * @return 0 if tiers are equal, <0 if other is a higher tier, >0 if this is a higher tier
     */
    public int compareTiers(ElementTypeTier other) {
        return this.relativeTier - other.relativeTier;
    }

    @Nonnull
    @Override
    public String getSerializedName() {
        return this.name;
    }

    public String getTranslationKey() {
        return "element.tier.elementalcraft." + getSerializedName();
    }

    public static ElementTypeTier byName(String name) {
        for (ElementTypeTier tier : values()) {
            if (tier.name.equals(name)) {
                return tier;
            }
        }
        return NONE;
    }
}
