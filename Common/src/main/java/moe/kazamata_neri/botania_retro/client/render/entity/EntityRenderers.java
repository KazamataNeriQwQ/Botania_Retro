package moe.kazamata_neri.botania_retro.client.render.entity;

import moe.kazamata_neri.botania_retro.common.block.block_entity.ExtraBotaniaBlockEntities;
import vazkii.botania.client.render.block_entity.SpecialFlowerBlockEntityRenderer;
import vazkii.botania.client.render.entity.EntityRenderers.BERConsumer;

public final class EntityRenderers {
    public static void registerBlockEntityRenderers(BERConsumer consumer) {
        consumer.register(ExtraBotaniaBlockEntities.DAYBLOOM, SpecialFlowerBlockEntityRenderer::new);
        consumer.register(ExtraBotaniaBlockEntities.DAYBLOOM_PRIME, SpecialFlowerBlockEntityRenderer::new);
        consumer.register(ExtraBotaniaBlockEntities.NIGHTSHADE, SpecialFlowerBlockEntityRenderer::new);
        consumer.register(ExtraBotaniaBlockEntities.NIGHTSHADE_PRIME, SpecialFlowerBlockEntityRenderer::new);
    }
}
