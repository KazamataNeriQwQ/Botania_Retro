package moe.kazamata_neri.botania_retro.common.item;

//import moe.kazamata_neri.botania_retro.common.crafting.recipe.*;
import moe.kazamata_neri.botania_retro.common.item.relic.RingOfAesirItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static moe.kazamata_neri.botania_retro.CommonInitializer.*;
import static moe.kazamata_neri.botania_retro.common.lib.LibItemNames.*;
import static vazkii.botania.common.item.BotaniaItems.defaultBuilder;

public final class ExtraBotaniaItems {
    private static final Map<String, Item> ALL = new LinkedHashMap<>();

    public static final Item aesirRing = make(AESIR_RING, new RingOfAesirItem(defaultBuilder().stacksTo(1).fireResistant().rarity(Rarity.EPIC)));

    private static <T extends Item> T make(String name, T item) {
        var old = ALL.put(name, item);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate name: " + name);
        }
        return item;
    }

    public static void registerItems(BiConsumer<Item, ResourceLocation> r) {
        for (var e : ALL.entrySet()) {
            r.accept(e.getValue(), RL(e.getKey()));
        }
    }
}
