package moe.kazamata_neri.botania_retro.client.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import vazkii.botania.common.block.decor.BotaniaMushroomBlock;
import vazkii.botania.common.block.decor.FloatingFlowerBlock;

import java.util.function.BiConsumer;

import static moe.kazamata_neri.botania_retro.CommonInitializer.MOD_ID;

public final class BlockRenderLayers{
    public static void init(BiConsumer<Block, RenderType> consumer)
    {
        BuiltInRegistries.BLOCK.stream().filter(b -> BuiltInRegistries.BLOCK.getKey(b).getNamespace().equals(MOD_ID))
                .forEach(b -> {
                    if (b instanceof FloatingFlowerBlock || b instanceof FlowerBlock || b instanceof TallFlowerBlock || b instanceof BotaniaMushroomBlock || b instanceof FlowerPotBlock)
                    {
                        consumer.accept(b, RenderType.cutout());
                    }
                });
    }
}