package moe.kazamata_neri.botania_retro.common.block;

import com.google.common.base.Supplier;
import moe.kazamata_neri.botania_retro.common.block.block_entity.ExtraBotaniaBlockEntities;
import moe.kazamata_neri.botania_retro.common.lib.LibBlockNames;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.common.block.flower.*;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.block.SpecialFlowerBlockItem;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static moe.kazamata_neri.botania_retro.CommonInitializer.*;
import static moe.kazamata_neri.botania_retro.common.lib.LibBlockNames.*;


public final class ExtraBotaniaBlocks {
    private static final Map<String, Block> ALL = new LinkedHashMap<>();

    private static final BlockBehaviour.Properties FLOWER_PROPS = BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY);
    public static final BlockBehaviour.Properties FLOATING_PROPS = BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).strength(0.5F).sound(SoundType.GRAVEL).lightLevel(s -> s.getValue(BotaniaStateProperties.DIMMED) ? 3 : 15);

    public static final Block dayBloom = make(LibBlockNames.SUBblock_DAYBLOOM, new SlowGeneratingFlowerWithCooldownBlock(MobEffects.DAMAGE_BOOST, 1, FLOWER_PROPS, () -> ExtraBotaniaBlockEntities.DAYBLOOM));
    public static final Block dayBloomFloating = make(floating(LibBlockNames.SUBblock_DAYBLOOM), new FloatingSlowGeneratingFlowerWithCooldownBlock(FLOATING_PROPS, () -> ExtraBotaniaBlockEntities.DAYBLOOM));
    public static final Block dayBloomPotted = make(potted(LibBlockNames.SUBblock_DAYBLOOM), flowerPot(dayBloom, 0));

    public static final Block dayBloomPrime = make(LibBlockNames.SUBblock_DAYBLOOM_PRIME, new SlowGeneratingFlowerWithCooldownBlock(MobEffects.DAMAGE_BOOST, 1, FLOWER_PROPS, () -> ExtraBotaniaBlockEntities.DAYBLOOM_PRIME));
    public static final Block dayBloomPrimeFloating = make(floating(LibBlockNames.SUBblock_DAYBLOOM_PRIME), new FloatingSlowGeneratingFlowerWithCooldownBlock(FLOATING_PROPS, () -> ExtraBotaniaBlockEntities.DAYBLOOM_PRIME));
    public static final Block dayBloomPrimePotted = make(potted(LibBlockNames.SUBblock_DAYBLOOM_PRIME), flowerPot(dayBloomPrime, 0));


    public static final Block nightShade = make(LibBlockNames.SUBblock_NIGHTSHADE, new SlowGeneratingFlowerWithCooldownBlock(MobEffects.NIGHT_VISION, 1, FLOWER_PROPS, () -> ExtraBotaniaBlockEntities.NIGHTSHADE));
    public static final Block nightShadeFloating =  make(floating(LibBlockNames.SUBblock_NIGHTSHADE), new FloatingSlowGeneratingFlowerWithCooldownBlock(FLOATING_PROPS, () -> ExtraBotaniaBlockEntities.NIGHTSHADE));
    public static final Block nightShadePotted = make(potted(LibBlockNames.SUBblock_NIGHTSHADE), flowerPot(nightShade, 0));

    public static final Block nightShadePrime = make(LibBlockNames.SUBblock_NIGHTSHADE_PRIME, new SlowGeneratingFlowerWithCooldownBlock(MobEffects.NIGHT_VISION, 1, FLOWER_PROPS, () -> ExtraBotaniaBlockEntities.NIGHTSHADE_PRIME));
    public static final Block nightShadePrimeFloating =  make(floating(LibBlockNames.SUBblock_NIGHTSHADE_PRIME), new FloatingSlowGeneratingFlowerWithCooldownBlock(FLOATING_PROPS, () -> ExtraBotaniaBlockEntities.NIGHTSHADE_PRIME));
    public static final Block nightShadePrimePotted = make(potted(LibBlockNames.SUBblock_NIGHTSHADE_PRIME), flowerPot(nightShadePrime, 0));

    private static <T extends Block> T make(String name, T block) {
        var old = ALL.put(name, block);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate name: " + name);
        }
        return block;
    }

    static FlowerPotBlock flowerPot(Block block, int lightLevel) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY);
        return new FlowerPotBlock(block, lightLevel > 0 ? properties.lightLevel(blockState -> lightLevel) : properties);
    }

    private static String floating(String orig) {
        return FLOATING_PREIFX + orig;
    }

    private static String potted(String orig) {
        return POTTED_PREFIX + orig;
    }

    public static void registerBlocks(BiConsumer<Block, ResourceLocation> r) {
        for (var e : ALL.entrySet()) {
            r.accept(e.getValue(), RL(e.getKey()));
        }
    }

    public static void registerItemBlocks(BiConsumer<Item, ResourceLocation> r) {
        Item.Properties props = BotaniaItems.defaultBuilder();

        r.accept(new SpecialFlowerBlockItem(dayBloom, props), BuiltInRegistries.BLOCK.getKey(dayBloom));
        r.accept(new SpecialFlowerBlockItem(dayBloomFloating, props), BuiltInRegistries.BLOCK.getKey(dayBloomFloating));

        r.accept(new SpecialFlowerBlockItem(dayBloomPrime, props), BuiltInRegistries.BLOCK.getKey(dayBloomPrime));
        r.accept(new SpecialFlowerBlockItem(dayBloomPrimeFloating, props), BuiltInRegistries.BLOCK.getKey(dayBloomPrimeFloating));

        r.accept(new SpecialFlowerBlockItem(nightShade, props), BuiltInRegistries.BLOCK.getKey(nightShade));
        r.accept(new SpecialFlowerBlockItem(nightShadeFloating, props), BuiltInRegistries.BLOCK.getKey(nightShadeFloating));

        r.accept(new SpecialFlowerBlockItem(nightShadePrime, props), BuiltInRegistries.BLOCK.getKey(nightShadePrime));
        r.accept(new SpecialFlowerBlockItem(nightShadePrimeFloating, props), BuiltInRegistries.BLOCK.getKey(nightShadePrimeFloating));

    }

    public static void registerFlowerPotPlants(BiConsumer<ResourceLocation, Supplier<? extends Block>> consumer) {
        registerBlocks((block, resourceLocation) -> {
            if (block instanceof FlowerPotBlock) {
                var id = BuiltInRegistries.BLOCK.getKey(block);
                consumer.accept(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath().substring(LibBlockNames.POTTED_PREFIX.length())), () -> block);
            }
        });
    }
}