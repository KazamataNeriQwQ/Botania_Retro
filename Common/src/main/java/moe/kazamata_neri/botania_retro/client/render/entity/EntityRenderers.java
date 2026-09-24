package moe.kazamata_neri.botania_retro.client.render.entity;

import moe.kazamata_neri.botania_retro.common.block.ExtraBotaniaFlowerBlocks;
import vazkii.botania.client.render.block_entity.SpecialFlowerBlockEntityRenderer;
import vazkii.botania.client.render.entity.EntityRenderers.BERConsumer;

public final class EntityRenderers {
    public static void registerBlockEntityRenderers(BERConsumer consumer) {
        consumer.register(ExtraBotaniaFlowerBlocks.DAYBLOOM, SpecialFlowerBlockEntityRenderer::new);
        consumer.register(ExtraBotaniaFlowerBlocks.DAYBLOOMPRIME, SpecialFlowerBlockEntityRenderer::new);
        consumer.register(ExtraBotaniaFlowerBlocks.NIGHTSHADE, SpecialFlowerBlockEntityRenderer::new);
        consumer.register(ExtraBotaniaFlowerBlocks.NIGHTSHADEPRIME, SpecialFlowerBlockEntityRenderer::new);
    }
}
