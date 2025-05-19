package moe.kazamata_neri.botania_retro.common.block;

import com.google.common.base.Supplier;
import moe.kazamata_neri.botania_retro.common.block.flower.generating.DayBloomBlockEntity;
import moe.kazamata_neri.botania_retro.common.block.flower.generating.DayBloomBlockPrimeEntity;
import moe.kazamata_neri.botania_retro.common.block.flower.generating.NightShadeBlockEntity;
import moe.kazamata_neri.botania_retro.common.block.flower.generating.NightShadePrimeBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.FloatingSpecialFlowerBlock;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.block.SpecialFlowerBlockItem;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.function.BiConsumer;

import static moe.kazamata_neri.botania_retro.common.helper.ResourceLocationHelper.*;


public class ExtraBotaniaFlowerBlocks {
    private static final BlockBehaviour.Properties FLOWER_PROPS = BlockBehaviour.Properties.copy(Blocks.POPPY);
    private static final BlockBehaviour.Properties FLOATING_PROPS = BotaniaBlocks.FLOATING_PROPS;

    public static final Block dayBloom = createSpecialFlowerBlock(MobEffects.DAMAGE_BOOST ,1, FLOWER_PROPS, () -> ExtraBotaniaFlowerBlocks.DAYBLOOM);
    public static final Block dayBloomFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> ExtraBotaniaFlowerBlocks.DAYBLOOM);
    public static final Block dayBloomPotted = ExtraBotaniaFlowerBlocks.flowerPot(dayBloom, 0);
    public static final Block dayBloomPrime = createSpecialFlowerBlock(MobEffects.DAMAGE_BOOST, 1, FLOWER_PROPS, () -> ExtraBotaniaFlowerBlocks.DAYBLOOMPRIME);
    public static final Block dayBloomPrimeFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> ExtraBotaniaFlowerBlocks.DAYBLOOMPRIME);
    public static final Block dayBloomPrimePotted = ExtraBotaniaFlowerBlocks.flowerPot(dayBloomPrime, 0);


    public static final Block nightShade = createSpecialFlowerBlock(MobEffects.NIGHT_VISION ,1, FLOWER_PROPS, () -> ExtraBotaniaFlowerBlocks.NIGHTSHADE);
    public static final Block nightShadeFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> ExtraBotaniaFlowerBlocks.NIGHTSHADE);
    public static final Block nightShadePotted = ExtraBotaniaFlowerBlocks.flowerPot(nightShade, 0);
    public static final Block nightShadePrime = createSpecialFlowerBlock(MobEffects.NIGHT_VISION, 1, FLOWER_PROPS, () -> ExtraBotaniaFlowerBlocks.NIGHTSHADEPRIME);
    public static final Block nightShadePrimeFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> ExtraBotaniaFlowerBlocks.NIGHTSHADEPRIME);
    public static final Block nightShadePrimePotted = ExtraBotaniaFlowerBlocks.flowerPot(nightShadePrime, 0);

    public static final BlockEntityType<DayBloomBlockEntity> DAYBLOOM = XplatAbstractions.INSTANCE.createBlockEntityType(DayBloomBlockEntity::new, dayBloom, dayBloomFloating);
    public static final BlockEntityType<DayBloomBlockPrimeEntity> DAYBLOOMPRIME = XplatAbstractions.INSTANCE.createBlockEntityType(DayBloomBlockPrimeEntity::new,dayBloomPrime, dayBloomPrimeFloating);
    public static final BlockEntityType<NightShadeBlockEntity> NIGHTSHADE = XplatAbstractions.INSTANCE.createBlockEntityType(NightShadeBlockEntity::new, nightShade, nightShadeFloating);
    public static final BlockEntityType<NightShadePrimeBlockEntity> NIGHTSHADEPRIME = XplatAbstractions.INSTANCE.createBlockEntityType(NightShadePrimeBlockEntity::new , nightShadePrime, nightShadePrimeFloating);

    private static FlowerBlock createSpecialFlowerBlock(
            MobEffect effect, int effectDuration,
            BlockBehaviour.Properties props,
            Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> beType) {
        return XplatAbstractions.INSTANCE.createSpecialFlowerBlock(
                effect, effectDuration, props, beType);
    }

    private static FlowerBlock createSpecialFlowerBlock(
            MobEffect effect, int effectDuration,
            BlockBehaviour.Properties props,
            Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> beType,
            boolean hasComparatorOutput) {
        return XplatAbstractions.INSTANCE.createSpecialFlowerBlock(
                effect, effectDuration, props, beType
        );
    }

    static FlowerPotBlock flowerPot(Block block, int lightLevel) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY);
        return new FlowerPotBlock(block, lightLevel > 0 ? properties.lightLevel(blockState -> lightLevel) : properties);
    }

    private static ResourceLocation getId(Block b) {
        return BuiltInRegistries.BLOCK.getKey(b);
    }

    public static void registerBlocks(BiConsumer<Block, ResourceLocation> r)
    {
        r.accept(dayBloom, prefix(SUBblock_DAYBLOOM));
        r.accept(dayBloomFloating, floating(SUBblock_DAYBLOOM));
        r.accept(dayBloomPotted, potted(SUBblock_DAYBLOOM));
        r.accept(dayBloomPrime, prefix(SUBblock_DAYBLOOM_PRIME));
        r.accept(dayBloomPrimeFloating, floating(SUBblock_DAYBLOOM_PRIME));
        r.accept(dayBloomPrimePotted, potted(SUBblock_DAYBLOOM_PRIME));

        r.accept(nightShade, prefix(SUBblock_NIGHTSHADE));
        r.accept(nightShadeFloating, floating(SUBblock_NIGHTSHADE));
        r.accept(nightShadePotted, potted(SUBblock_NIGHTSHADE));
        r.accept(nightShadePrime, prefix(SUBblock_NIGHTSHADE_PRIME));
        r.accept(nightShadePrimeFloating, floating(SUBblock_NIGHTSHADE_PRIME));
        r.accept(nightShadePrimePotted, potted(SUBblock_NIGHTSHADE_PRIME));
    }

    public static void registerItemBlocks(BiConsumer<Item, ResourceLocation> r) {
        Item.Properties props = BotaniaItems.defaultBuilder();
        r.accept(new SpecialFlowerBlockItem(dayBloom, props), getId(dayBloom));
        r.accept(new SpecialFlowerBlockItem(dayBloomFloating, props), getId(dayBloomFloating));
        r.accept(new SpecialFlowerBlockItem(dayBloomPrime, props), getId(dayBloomPrime));
        r.accept(new SpecialFlowerBlockItem(dayBloomPrimeFloating, props), getId(dayBloomPrimeFloating));
        r.accept(new SpecialFlowerBlockItem(nightShade, props), getId(nightShade));
        r.accept(new SpecialFlowerBlockItem(nightShadeFloating, props), getId(nightShadeFloating));
        r.accept(new SpecialFlowerBlockItem(nightShadePrime, props), getId(nightShadePrime));
        r.accept(new SpecialFlowerBlockItem(nightShadePrimeFloating, props), getId(nightShadePrimeFloating));
    }

    public static void registerTEs(BiConsumer<BlockEntityType<?>, ResourceLocation> r) {
        r.accept(DAYBLOOM, getId(dayBloom));
        r.accept(DAYBLOOMPRIME, getId(dayBloomPrime));
        r.accept(NIGHTSHADE, getId(nightShade));
        r.accept(NIGHTSHADEPRIME, getId(nightShadePrime));
    }

    public static void registerWandHudCaps(BotaniaBlockEntities.BECapConsumer<WandHUD> consumer) {
        consumer.accept(be -> new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>((GeneratingFlowerBlockEntity) be),
        DAYBLOOM, DAYBLOOMPRIME, NIGHTSHADE, NIGHTSHADEPRIME);
    }
}