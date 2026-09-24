package moe.kazamata_neri.botania_retro.fabric.client;

import moe.kazamata_neri.botania_retro.client.render.BlockRenderLayers;
import moe.kazamata_neri.botania_retro.client.render.entity.EntityRenderers;
import moe.kazamata_neri.botania_retro.common.block.block_entity.ExtraBotaniaBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import vazkii.botania.api.fabric.BotaniaFabricCapabilities;
import vazkii.botania.api.block.WandHUD;

public final class FabricClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayers.init(BlockRenderLayerMap.INSTANCE::putBlock);
        EntityRenderers.registerBlockEntityRenderers(BlockEntityRenderers::register);

        registerCapabilities();
    }

    private static void registerCapabilities() {
        ExtraBotaniaBlockEntities.registerWandHudCaps((factory, types) -> BotaniaFabricCapabilities.getBlockApiLookupById(WandHUD.BLOCK_LOOKUP).registerForBlockEntities((be, c) -> factory.apply(be), types));
    }
}
