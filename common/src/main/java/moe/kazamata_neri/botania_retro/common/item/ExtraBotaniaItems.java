package moe.kazamata_neri.botania_retro.common.item;

import moe.kazamata_neri.botania_retro.common.item.relic.RingOfAesirItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public final class ExtraBotaniaItems {
    public static final String AESIR_RING = "aesir_ring";
    public static Item aesirRing = new RingOfAesirItem(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
}
