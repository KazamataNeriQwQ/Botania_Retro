package moe.kazamata_neri.botania_retro.fabric.client;

import moe.kazamata_neri.botania_retro.client.render.BlockRenderLayers;
import moe.kazamata_neri.botania_retro.client.render.entity.EntityRenderers;
import moe.kazamata_neri.botania_retro.common.block.ExtraBotaniaFlowerBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import vazkii.botania.api.BotaniaFabricClientCapabilities;

public final class FabricClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRenderers.registerBlockEntityRenderers(BlockEntityRenderers::register);
        BlockRenderLayers.init(BlockRenderLayerMap.INSTANCE::putBlock);
        ExtraBotaniaFlowerBlocks.registerWandHudCaps((factory, types) -> BotaniaFabricClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> factory.apply(be), types));
    }
}
