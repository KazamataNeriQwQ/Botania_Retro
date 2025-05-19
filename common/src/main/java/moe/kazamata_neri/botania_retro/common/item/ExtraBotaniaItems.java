package moe.kazamata_neri.botania_retro.common.item;

import moe.kazamata_neri.botania_retro.common.crafting.recipe.*;
import moe.kazamata_neri.botania_retro.common.item.relic.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static moe.kazamata_neri.botania_retro.common.helper.ResourceLocationHelper.*;
import static vazkii.botania.common.item.BotaniaItems.defaultBuilder;

public final class ExtraBotaniaItems {
    private static final Map<ResourceLocation, Item> ALL = new LinkedHashMap<>();

    public static final Item aesirRing = make(prefix(AESIR_RING), new RingOfAesirItem(defaultBuilder().stacksTo(1).fireResistant().rarity(Rarity.EPIC)));

    private static <T extends Item> T make(ResourceLocation id, T item) {
        var old = ALL.put(id, item);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + id);
        }
        return item;
    }

    public static void registerItems(BiConsumer<Item, ResourceLocation> r) {
        for (var e : ALL.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    public static void registerRecipeSerializers(BiConsumer<RecipeSerializer<?>, ResourceLocation> r)
    {
        r.accept(ShapelessRelicBindRecipe.SERIALIZER, prefix("relic_bind_shapeless"));
    }
}
