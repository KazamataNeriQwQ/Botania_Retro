package moe.kazamata_neri.botania_retro.common.component;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;


import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

import static moe.kazamata_neri.botania_retro.CommonInitializer.RL;

public class ExtraBotaniaDataComponents {
    private static final Map<String, DataComponentType<?>> ALL = new HashMap();

    public static final DataComponentType<Integer> TICK_COUNT =  make("tick_count", (builder) -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));


    public static void registerComponents(BiConsumer<DataComponentType<?>, ResourceLocation> biConsumer) {
        for(Map.Entry<String, DataComponentType<?>> entry : ALL.entrySet()) {
            biConsumer.accept((DataComponentType)entry.getValue(), RL((String)entry.getKey()));
        }

    }

    protected static <T> DataComponentType<T> make(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        if (!name.matches("[a-z]+(?:_[a-z0-9]+)*")) {
            throw new IllegalArgumentException("Typo? Name should be in snake_case: " + name);
        } else {
            DataComponentType<T> type = ((DataComponentType.Builder)builder.apply(DataComponentType.builder())).build();
            DataComponentType<?> old = (DataComponentType)ALL.put(name, type);
            if (old != null) {
                throw new IllegalArgumentException("Typo? Duplicate name " + name);
            } else {
                return type;
            }
        }
    }

    protected static DataComponentType<Unit> makeUnit(String name) {
        return make(name, (builder) -> builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));
    }

    protected static DataComponentType<Unit> makeTransientUnit(String name) {
        return make(name, (builder) -> builder.networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));
    }
}
