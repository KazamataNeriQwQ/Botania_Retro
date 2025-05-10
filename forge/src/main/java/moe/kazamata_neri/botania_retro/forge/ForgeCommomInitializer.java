package moe.kazamata_neri.botania_retro.forge;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import moe.kazamata_neri.botania_retro.CommonInitializer;
import moe.kazamata_neri.botania_retro.common.block.ExtraBotaniaFlowerBlocks;
import moe.kazamata_neri.botania_retro.common.item.ExtraBotaniaItems;
import moe.kazamata_neri.botania_retro.common.item.relic.RingOfAesirItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.BotaniaRegistries;
import vazkii.botania.api.item.Relic;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.CustomCreativeTabContents;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.forge.CapabilityUtil;
import vazkii.botania.forge.integration.curios.CurioIntegration;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static moe.kazamata_neri.botania_retro.CommonInitializer.MOD_ID;
import static moe.kazamata_neri.botania_retro.common.helper.ResourceLocationHelper.prefix;

@Mod(MOD_ID)
public final class ForgeCommomInitializer {
    private static final Supplier<Map<Item, Function<ItemStack, Relic>>> RELIC = Suppliers.memoize(() -> Map.of(
            ExtraBotaniaItems.aesirRing, RingOfAesirItem::makeRelic
    ));

    public ForgeCommomInitializer() {
        CommonInitializer.init();

        bindForItems(ExtraBotaniaItems::registerItems);
        bindForItems(ExtraBotaniaFlowerBlocks::registerItemBlocks);
        bind(Registries.BLOCK, ExtraBotaniaFlowerBlocks::registerBlocks);
        bind(Registries.BLOCK_ENTITY_TYPE, ExtraBotaniaFlowerBlocks::registerTEs);

        bind(Registries.RECIPE_SERIALIZER, ExtraBotaniaItems::registerRecipeSerializers);
        FMLJavaModLoadingContext.get().getModEventBus().addListener((BuildCreativeModeTabContentsEvent e) -> {
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
        });

        IEventBus eventBus = MinecraftForge.EVENT_BUS;
        eventBus.addGenericListener(ItemStack.class, this::attachItemCaps);
        eventBus.addListener((PlayerEvent.ItemCraftedEvent e) -> RingOfAesirItem.onCrafted(e.getEntity(), e.getCrafting()));
        eventBus.addListener((ItemTossEvent e) -> RingOfAesirItem.OnDropped(e.getEntity()));
    }

    private final Set<Item> itemsToAddToCreativeTab = new LinkedHashSet<>();

    private void bindForItems(Consumer<BiConsumer<Item, ResourceLocation>> source) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((RegisterEvent event) -> {
            if (event.getRegistryKey().equals(Registries.ITEM)) {
                source.accept((t, rl) -> {
                    itemsToAddToCreativeTab.add(t);
                    event.register(Registries.ITEM, rl, () -> t);
                });
            }
        });
    }

    private static <T> void bind(ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((RegisterEvent event) -> {
            if (registry.equals(event.getRegistryKey())) {
                source.accept((t, rl) -> event.register(registry, rl, () -> t));
            }
        });
    }

    private void attachItemCaps(AttachCapabilitiesEvent<ItemStack> e){
        var stack = e.getObject();
        if (stack.getItem() instanceof BaubleItem
                && EquipmentHandler.instance instanceof CurioIntegration ci) {
            e.addCapability(prefix("curio"), ci.initCapability(stack));
        }

        var makeRelic = RELIC.get().get(stack.getItem());
        if (makeRelic != null) {
            e.addCapability(prefix("relic"),
                    CapabilityUtil.makeProvider(BotaniaForgeCapabilities.RELIC, makeRelic.apply(stack)));
        }
    }
}
