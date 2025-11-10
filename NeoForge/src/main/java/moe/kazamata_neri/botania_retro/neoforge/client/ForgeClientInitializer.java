package moe.kazamata_neri.botania_retro.neoforge.client;

import moe.kazamata_neri.botania_retro.client.render.BlockRenderLayers;
import moe.kazamata_neri.botania_retro.client.render.entity.EntityRenderers;
import moe.kazamata_neri.botania_retro.common.block.block_entity.ExtraBotaniaBlockEntities;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import java.util.stream.Stream;

import static moe.kazamata_neri.botania_retro.CommonInitializer.MOD_ID;


@EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class ForgeClientInitializer {
    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent evt) {
        BlockRenderLayers.init(ItemBlockRenderTypes::setRenderLayer);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers evt) {
        EntityRenderers.registerBlockEntityRenderers(evt::registerBlockEntityRenderer);
    }

    @SubscribeEvent
    private static void attachClientCapabilities(RegisterCapabilitiesEvent e) {
        ExtraBotaniaBlockEntities.registerWandHudCaps((factory, types) -> Stream.of(types).forEach(blockEntityType -> e.registerBlockEntity(BotaniaForgeClientCapabilities.BLOCK_WAND_HUD, blockEntityType,
                (blockEntity, context) -> factory.apply(blockEntity))));
    }
}
