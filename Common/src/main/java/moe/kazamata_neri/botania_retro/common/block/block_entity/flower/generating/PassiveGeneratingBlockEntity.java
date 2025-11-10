package moe.kazamata_neri.botania_retro.common.block.block_entity.flower.generating;

import moe.kazamata_neri.botania_retro.common.component.ExtraBotaniaDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.component.BotaniaDataComponents;

public abstract class PassiveGeneratingBlockEntity extends GeneratingFlowerBlockEntity {
    public static final int DECAY_TIME = 72000;

    public static final String TAG_DECAY_TICKS = "decay_ticks";
    public static final String TAG_TICK_COUNT = "tick_count";

    protected int decayTicks;
    protected int perManaGenerationTick;

    protected int tickCount = 0;

    protected BlockPos primePosition;

    public PassiveGeneratingBlockEntity(BlockEntityType<?> entityType, BlockPos pos, BlockState state, int tick) {
        super(entityType, pos, state);
        perManaGenerationTick = tick;
        setPrimusPosition(pos);
    }

    public void setPrimusPosition(BlockPos pos) {
        primePosition = pos;
    }

    @Override
    public void tickFlower() {
        super.tickFlower();
        tickCount++;

        if(canGenerate()) {
            if (getLevel() != null && getLevel().isClientSide)
            {
                for (int i = 0; i < 3; i++) {
                    WispParticleData data = WispParticleData.wisp((float) Math.random() / 6, 0.1F, 0.1F, 0.1F, 1);
                    emitParticle(data, 0.5 + Math.random() * 0.2 - 0.1, 0.5 + Math.random() * 0.2 - 0.1, 0.5 + Math.random() * 0.2 - 0.1, 0, (float) Math.random() / 30, 0);
                }
            }

            if (getLevel() != null && !getLevel().isClientSide) {
                if (tickCount - perManaGenerationTick == 0) {
                    tickCount = 0;
                    addMana(2);
                }
            }
        }

        if (getLevel() != null && !getLevel().isClientSide) {
            if (!isPrime() && ++decayTicks > DECAY_TIME)
            {
                getLevel().destroyBlock(getBlockPos(), false);
                if (Blocks.DEAD_BUSH.defaultBlockState().canSurvive(getLevel(), getBlockPos())) {
                    getLevel().setBlockAndUpdate(getBlockPos(), Blocks.DEAD_BUSH.defaultBlockState());
                }
            }
            else if(isPrime() && !getBlockPos().equals(primePosition))
            {
                getLevel().destroyBlock(getBlockPos(), false);
            }
        }

        sync();
    }

    @Override
    public int getMaxMana() {
        return 160;
    }

    public boolean isPrime() {
        return false;
    }

    public abstract boolean canGenerate();

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Circle(primePosition,1);
    }

    @Override
    public void readFromPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
        super.readFromPacketNBT(cmp, registries);
        this.decayTicks = cmp.getInt(TAG_DECAY_TICKS);
        this.tickCount = cmp.getInt(TAG_TICK_COUNT);
    }

    @Override
    public void writeToPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
        super.writeToPacketNBT(cmp, registries);
        cmp.putInt(TAG_DECAY_TICKS, decayTicks);
        cmp.putInt(TAG_TICK_COUNT, tickCount);
    }

    @Override
    public boolean isOvergrowthAffected() {
        return false;
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        if (decayTicks > 0) {
            components.set(BotaniaDataComponents.DECAY_TICKS, decayTicks);
        }
        if (tickCount > 0) {
            components.set(ExtraBotaniaDataComponents.TICK_COUNT, tickCount);
        }
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        decayTicks = componentInput.getOrDefault(BotaniaDataComponents.DECAY_TICKS, 0);
        tickCount = componentInput.getOrDefault(ExtraBotaniaDataComponents.TICK_COUNT, 0);
    }
}
