package io.github.yo56789.toywatergun.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FluidComponent(String id, int mb) {
    public static final Codec<FluidComponent> CODEC = RecordCodecBuilder.create(builder -> {
       return builder.group(
               Codec.STRING.fieldOf("identifier").forGetter(FluidComponent::id),
               Codec.INT.fieldOf("mb").forGetter(FluidComponent::mb)
       ).apply(builder, FluidComponent::new);
    });
}
