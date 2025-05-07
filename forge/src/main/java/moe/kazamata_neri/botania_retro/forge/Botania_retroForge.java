package moe.kazamata_neri.botania_retro.forge;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.eventbus.Subscribe;
import moe.kazamata_neri.botania_retro.Botania_retro;
import moe.kazamata_neri.botania_retro.common.item.ExtraBotaniaItems;
import moe.kazamata_neri.botania_retro.common.item.relic.RingOfAesirItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.BotaniaRegistries;
import vazkii.botania.api.item.Relic;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.forge.CapabilityUtil;
import vazkii.botania.forge.integration.curios.CurioIntegration;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static moe.kazamata_neri.botania_retro.Botania_retro.MOD_ID;
import static moe.kazamata_neri.botania_retro.common.helper.ResourceLocationHelper.prefix;
import static moe.kazamata_neri.botania_retro.common.item.ExtraBotaniaItems.AESIR_RING;
import static moe.kazamata_neri.botania_retro.common.item.ExtraBotaniaItems.aesirRing;

@Mod(MOD_ID)
public final class Botania_retroForge {
    private static final Supplier<Map<Item, Function<ItemStack, Relic>>> RELIC = Suppliers.memoize(() -> Map.of(
            ExtraBotaniaItems.aesirRing, RingOfAesirItem::makeRelic
    ));
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final RegistryObject<Item> RING_OF_AESIR = ITEMS.register(AESIR_RING, () -> aesirRing);

    public Botania_retroForge() {
        Botania_retro.init();
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modBus);
        modBus.addListener((BuildCreativeModeTabContentsEvent e) -> {
            if (e.getTabKey() == BotaniaRegistries.BOTANIA_TAB_KEY) {
                e.accept(aesirRing);
            }});
        bind(Registries.RECIPE_SERIALIZER, Botania_retro::registerRecipeSerializers);
        IEventBus eventBus = MinecraftForge.EVENT_BUS;
        eventBus.addListener((PlayerEvent.ItemCraftedEvent e) -> RingOfAesirItem.onItemCrafted(e.getEntity(), e.getCrafting()));
        eventBus.addGenericListener(ItemStack.class, this::attachItemCaps);
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
