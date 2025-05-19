package moe.kazamata_neri.botania_retro.fabric;

import moe.kazamata_neri.botania_retro.CommonInitializer;
import moe.kazamata_neri.botania_retro.common.block.ExtraBotaniaFlowerBlocks;
import moe.kazamata_neri.botania_retro.common.item.ExtraBotaniaItems;
import moe.kazamata_neri.botania_retro.common.item.relic.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import vazkii.botania.api.BotaniaFabricCapabilities;
import vazkii.botania.api.BotaniaRegistries;
import vazkii.botania.common.item.CustomCreativeTabContents;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiConsumer;

import static moe.kazamata_neri.botania_retro.common.item.ExtraBotaniaItems.*;

public final class FabricCommonInitializer implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonInitializer.init();
        ExtraBotaniaItems.registerItems(boundForItem);
        ExtraBotaniaFlowerBlocks.registerBlocks(bind(BuiltInRegistries.BLOCK));
        ExtraBotaniaFlowerBlocks.registerItemBlocks(boundForItem);
        ExtraBotaniaFlowerBlocks.registerTEs(bind(BuiltInRegistries.BLOCK_ENTITY_TYPE));
        registerCapabilities();
        ItemGroupEvents.modifyEntriesEvent(BotaniaRegistries.BOTANIA_TAB_KEY)
                .register(entries -> {
                    for (Item item : this.itemsToAddToCreativeTab) {
                        if (item instanceof CustomCreativeTabContents cc) {
                            cc.addToCreativeTab(item, entries);
                        } else if (item instanceof BlockItem bi && bi.getBlock() instanceof CustomCreativeTabContents cc) {
                            cc.addToCreativeTab(item, entries);
                        } else {
                            entries.accept(item);
                        }
                    }
                });
        registerRecipeSerializers(bind(BuiltInRegistries.RECIPE_SERIALIZER));
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof ItemEntity itemEntity)
            {
                if(itemEntity.getOwner() != null)
                {
                    RingOfAesirItem.OnDropped(itemEntity);
                }
            }
        });
    }
    private final Set<Item> itemsToAddToCreativeTab = new LinkedHashSet<>();

    private final BiConsumer<Item, ResourceLocation> boundForItem =
            (t, id) -> {
                itemsToAddToCreativeTab.add(t);
                Registry.register(BuiltInRegistries.ITEM, id, t);
            };

    private static <T> BiConsumer<T, ResourceLocation> bind(Registry<? super T> registry) {
        return (t, id) -> Registry.register(registry, id, t);
    }

    private void registerCapabilities() {
        BotaniaFabricCapabilities.RELIC.registerForItems((st, c) -> RingOfAesirItem.makeRelic(st), aesirRing);
    }
}
