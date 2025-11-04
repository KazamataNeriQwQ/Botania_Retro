package moe.kazamata_neri.botania_retro.neoforge;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import moe.kazamata_neri.botania_retro.CommonInitializer;
import moe.kazamata_neri.botania_retro.common.block.ExtraBotaniaBlocks;
import moe.kazamata_neri.botania_retro.common.block.block_entity.ExtraBotaniaBlockEntities;
import moe.kazamata_neri.botania_retro.common.component.ExtraBotaniaDataComponents;
import moe.kazamata_neri.botania_retro.common.crafting.ExtraBotaniaRecipeTypes;
import moe.kazamata_neri.botania_retro.common.item.ExtraBotaniaItems;
import moe.kazamata_neri.botania_retro.common.item.relic.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.*;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.registries.RegisterEvent;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.BotaniaRegistries;
import vazkii.botania.api.item.Relic;
import vazkii.botania.common.item.CustomCreativeTabContents;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static moe.kazamata_neri.botania_retro.CommonInitializer.MOD_ID;

@Mod(MOD_ID)
public class ForgeCommonInitializer {
    private static final Supplier<Map<Item, Function<ItemStack, Relic>>> RELIC = Suppliers.memoize(() -> Map.of(
            ExtraBotaniaItems.aesirRing, RingOfAesirItem::makeRelic
    ));

    public ForgeCommonInitializer(IEventBus modBus, ModContainer modContainer) {
        CommonInitializer.init();
        modBus.register(this);
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent evt) {
        IEventBus gameBus = NeoForge.EVENT_BUS;
        registerEvents(gameBus);
        evt.enqueueWork(() -> {
            BiConsumer<ResourceLocation, Supplier<? extends Block>> consumer = (resourceLocation, blockSupplier) -> ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(resourceLocation, blockSupplier);
            ExtraBotaniaBlocks.registerFlowerPotPlants(consumer);
        });
    }

    @SubscribeEvent
    private void registryInit(RegisterEvent event) {
        bind(event, Registries.DATA_COMPONENT_TYPE, ExtraBotaniaDataComponents::registerComponents);

        bind(event, Registries.BLOCK, ExtraBotaniaBlocks::registerBlocks);
        bindForItems(event, ExtraBotaniaBlocks::registerItemBlocks);
        bind(event, Registries.BLOCK_ENTITY_TYPE, ExtraBotaniaBlockEntities::registerTiles);
        bindForItems(event, ExtraBotaniaItems::registerItems);

        bind(event, Registries.RECIPE_SERIALIZER, ExtraBotaniaRecipeTypes::submitRecipeSerializers);
    }

    private final Set<Item> itemsToAddToCreativeTab = new LinkedHashSet<>();

    private static <T> void bind(RegisterEvent event, ResourceKey<Registry<T>> registryKey, Consumer<BiConsumer<T, ResourceLocation>> source) {
        Registry<T> registry = event.getRegistry(registryKey);
        if (registry != null) {
            source.accept((t, rl) -> Registry.register(registry, rl, t));
        }
    }

    private void bindForItems(RegisterEvent event, Consumer<BiConsumer<Item, ResourceLocation>> source) {
        Registry<Item> registry = event.getRegistry(Registries.ITEM);
        if (registry != null) {

            source.accept((t, rl) -> {
                itemsToAddToCreativeTab.add(t);
                Registry.register(registry, rl, t);
            });
        }
    }

    private void registerEvents(IEventBus bus) {
        bus.addListener((PlayerEvent.ItemCraftedEvent e) -> RingOfAesirItem.onCrafted(e.getEntity(), e.getCrafting()));
        bus.addListener((ItemTossEvent e) -> RingOfAesirItem.OnDropped(e.getEntity()));
    }

    @SubscribeEvent
    private void attachItemCaps(RegisterCapabilitiesEvent e) {
        attachMappedItemCaps(e , BotaniaForgeCapabilities.RELIC, RELIC.get());
    }

    private static <T> void attachMappedItemCaps(RegisterCapabilitiesEvent e, ItemCapability<T, Void> capability,
                                                 Map<Item, Function<ItemStack, T>> itemProviderMap) {
        itemProviderMap.forEach((item, provider) -> e.registerItem(
                capability, (stack, context) -> provider.apply(stack), item));
    }

    @SubscribeEvent
    private void addItemsToCreativeTab(BuildCreativeModeTabContentsEvent e) {
        if (e.getTabKey() == BotaniaRegistries.BOTANIA_TAB_KEY) {
            for (Item item : this.itemsToAddToCreativeTab) {
                if (item instanceof CustomCreativeTabContents cc) {
                    cc.addToCreativeTab(item, e);
                } else if (item instanceof BlockItem bi && bi.getBlock() instanceof CustomCreativeTabContents cc) {
                    cc.addToCreativeTab(item, e);
                } else {
                    e.accept(item);
                }
            }
        }
    }
}
