package moe.kazamata_neri.botania_retro.common.block.block_entity;

import moe.kazamata_neri.botania_retro.common.block.block_entity.flower.generating.DayBloomBlockEntity;
import moe.kazamata_neri.botania_retro.common.block.block_entity.flower.generating.DayBloomBlockPrimeEntity;
import moe.kazamata_neri.botania_retro.common.block.block_entity.flower.generating.NightShadeBlockEntity;
import moe.kazamata_neri.botania_retro.common.block.block_entity.flower.generating.NightShadePrimeBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static moe.kazamata_neri.botania_retro.CommonInitializer.*;
import static moe.kazamata_neri.botania_retro.common.block.ExtraBotaniaBlocks.*;

public class ExtraBotaniaBlockEntities {
    private static final Map<ResourceLocation, BlockEntityType<?>> ALL = new HashMap<>();

    public static final BlockEntityType<DayBloomBlockEntity> DAYBLOOM = type(getId(dayBloom), DayBloomBlockEntity::new, dayBloom, dayBloomFloating);
    public static final BlockEntityType<DayBloomBlockPrimeEntity> DAYBLOOM_PRIME = type(getId(dayBloomPrime), DayBloomBlockPrimeEntity::new, dayBloomPrime, dayBloomPrimeFloating);
    public static final BlockEntityType<NightShadeBlockEntity> NIGHTSHADE = type(getId(nightShade), NightShadeBlockEntity::new, nightShade, nightShadeFloating);
    public static final BlockEntityType<NightShadePrimeBlockEntity> NIGHTSHADE_PRIME = type(getId(nightShadePrime), NightShadePrimeBlockEntity::new, nightShadePrime, nightShadePrimeFloating);

    private static <T extends BlockEntity> BlockEntityType<T> type(String id, BlockEntityType.BlockEntitySupplier<T> factory, Block... blocks) {
        return type(RL(id), factory, blocks);
    }

    private static <T extends BlockEntity> BlockEntityType<T> type(String id, BlockEntityType.BlockEntitySupplier<T> factory, Predicate<Block> blockPredicate) {
        return type(id, factory, BuiltInRegistries.BLOCK.stream()
                .filter(blockPredicate)
                .filter(block -> BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(MOD_ID))
                .toArray(Block[]::new));
    }

    private static <T extends BlockEntity> BlockEntityType<T> type(ResourceLocation id, BlockEntityType.BlockEntitySupplier<T> factory, Block... blocks) {
        // TODO: should probably set up that datafixer type instead of passing null to build()
        var ret = BlockEntityType.Builder.of(factory, blocks).build(null);
        var old = ALL.put(id, ret);
        if (old != null) {
            throw new IllegalArgumentException("Duplicate id " + id);
        }
        return ret;
    }

    private static ResourceLocation getId(Block b) {
        return BuiltInRegistries.BLOCK.getKey(b);
    }

    public static void registerTiles(BiConsumer<BlockEntityType<?>, ResourceLocation> r) {
        for (var e : ALL.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    public static void registerWandHudCaps(BotaniaBlockEntities.BECapConsumer<WandHUD> consumer) {
        consumer.accept(be -> new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>((GeneratingFlowerBlockEntity) be),
                DAYBLOOM, DAYBLOOM_PRIME, NIGHTSHADE, NIGHTSHADE_PRIME);
    }
}