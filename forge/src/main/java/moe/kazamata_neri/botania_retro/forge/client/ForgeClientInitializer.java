package moe.kazamata_neri.botania_retro.forge.client;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import moe.kazamata_neri.botania_retro.client.render.BlockRenderLayers;
import moe.kazamata_neri.botania_retro.client.render.entity.EntityRenderers;
import moe.kazamata_neri.botania_retro.common.block.ExtraBotaniaFlowerBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.forge.CapabilityUtil;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

import static moe.kazamata_neri.botania_retro.CommonInitializer.MOD_ID;
import static moe.kazamata_neri.botania_retro.common.helper.ResourceLocationHelper.prefix;


@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeClientInitializer {
    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent evt) {
        BlockRenderLayers.init(ItemBlockRenderTypes::setRenderLayer);
        var bus = MinecraftForge.EVENT_BUS;
        bus.addGenericListener(BlockEntity.class, ForgeClientInitializer::attachBeCapabilities);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers evt) {
        EntityRenderers.registerBlockEntityRenderers(evt::registerBlockEntityRenderer);
    }

    private static final Supplier<Map<BlockEntityType<?>, Function<BlockEntity, WandHUD>>> WAND_HUD = Suppliers.memoize(() -> {
        var ret = new IdentityHashMap<BlockEntityType<?>, Function<BlockEntity, WandHUD>>();
        ExtraBotaniaFlowerBlocks.registerWandHudCaps((factory, types) -> {
            for (var type : types) {
                ret.put(type, factory);
            }
        });
        return Collections.unmodifiableMap(ret);
    });

    private static void attachBeCapabilities(AttachCapabilitiesEvent<BlockEntity> e) {
        var be = e.getObject();

        var makeWandHud = WAND_HUD.get().get(be.getType());
        if (makeWandHud != null) {
            e.addCapability(prefix("wand_hud"),
                    CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, makeWandHud.apply(be)));
        }
    }
}
