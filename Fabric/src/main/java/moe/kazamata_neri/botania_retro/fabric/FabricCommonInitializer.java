package moe.kazamata_neri.botania_retro.fabric;

import moe.kazamata_neri.botania_retro.CommonInitializer;
import moe.kazamata_neri.botania_retro.common.block.ExtraBotaniaBlocks;
import moe.kazamata_neri.botania_retro.common.block.block_entity.ExtraBotaniaBlockEntities;
import moe.kazamata_neri.botania_retro.common.component.ExtraBotaniaDataComponents;
import moe.kazamata_neri.botania_retro.common.crafting.ExtraBotaniaRecipeTypes;
import moe.kazamata_neri.botania_retro.common.item.ExtraBotaniaItems;
import moe.kazamata_neri.botania_retro.common.item.relic.RingOfAesirItem;
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

public final class FabricCommonInitializer implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonInitializer.init();
        registryInit();

        registerCapabilities();
        registerEvents();
    }

    private void registryInit() {
        ExtraBotaniaDataComponents.registerComponents(bind(BuiltInRegistries.DATA_COMPONENT_TYPE));

        ExtraBotaniaBlocks.registerBlocks(bind(BuiltInRegistries.BLOCK));
        ExtraBotaniaBlocks.registerItemBlocks(boundForItem);
        ExtraBotaniaBlockEntities.registerTiles(bind(BuiltInRegistries.BLOCK_ENTITY_TYPE));
        ExtraBotaniaItems.registerItems(boundForItem);
        ExtraBotaniaRecipeTypes.submitRecipeSerializers(bind(BuiltInRegistries.RECIPE_SERIALIZER));

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
    }

    private void registerCapabilities() {
        BotaniaFabricCapabilities.RELIC.registerForItems((st, c) -> RingOfAesirItem.makeRelic(st), ExtraBotaniaItems.aesirRing);
    }

    private void registerEvents() {
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

}
