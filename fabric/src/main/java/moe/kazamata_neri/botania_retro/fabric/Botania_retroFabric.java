package moe.kazamata_neri.botania_retro.fabric;

import moe.kazamata_neri.botania_retro.Botania_retro;
import moe.kazamata_neri.botania_retro.common.item.relic.RingOfAesirItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import vazkii.botania.api.BotaniaFabricCapabilities;

import java.util.function.BiConsumer;

import static moe.kazamata_neri.botania_retro.common.helper.ResourceLocationHelper.prefix;
import static moe.kazamata_neri.botania_retro.common.item.ExtraBotaniaItems.AESIR_RING;
import static moe.kazamata_neri.botania_retro.common.item.ExtraBotaniaItems.aesirRing;
import static vazkii.botania.api.BotaniaRegistries.BOTANIA_TAB_KEY;

public final class Botania_retroFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Botania_retro.init();
        Registry.register(BuiltInRegistries.ITEM, prefix(AESIR_RING), aesirRing);
        BotaniaFabricCapabilities.RELIC.registerForItems((st, c) -> RingOfAesirItem.makeRelic(st), aesirRing);
        ItemGroupEvents.modifyEntriesEvent(BOTANIA_TAB_KEY).register(entries -> entries.accept(aesirRing));
        Botania_retro.registerRecipeSerializers(bind(BuiltInRegistries.RECIPE_SERIALIZER));
    }

    private static <T> BiConsumer<T, ResourceLocation> bind(Registry<? super T> registry) {
        return (t, id) -> Registry.register(registry, id, t);
    }
}
