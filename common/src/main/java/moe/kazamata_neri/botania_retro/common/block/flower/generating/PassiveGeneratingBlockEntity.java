package moe.kazamata_neri.botania_retro.common.block.flower.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.client.fx.WispParticleData;

import static vazkii.botania.common.block.flower.generating.HydroangeasBlockEntity.TAG_PASSIVE_DECAY_TICKS;

public abstract class PassiveGeneratingBlockEntity extends GeneratingFlowerBlockEntity {
    public static final int DECAY_TIME = 72000;

    protected int passiveDecayTicks;
    protected int perManaGenerationTick;

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

        if(canGenerate())
        {
            for (int i = 0; i < 3; i++) {
                WispParticleData data = WispParticleData.wisp((float) Math.random() / 6, 0.1F, 0.1F, 0.1F, 1);
                emitParticle(data, 0.5 + Math.random() * 0.2 - 0.1, 0.5 + Math.random() * 0.2 - 0.1, 0.5 + Math.random() * 0.2 - 0.1, 0, (float) Math.random() / 30, 0);
            }

            if (getLevel() != null && !getLevel().isClientSide) {
                if (ticksExisted % perManaGenerationTick == 0) {
                    addMana(2);
                    sync();
                }
            }
        }

        if (getLevel() != null && !getLevel().isClientSide) {
            if (!isPrime() && ++passiveDecayTicks > DECAY_TIME)
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
    }

    @Override
    public int getMaxMana()
    {
        return 160;
    }

    public boolean isPrime()
    {
        return false;
    }

    public abstract boolean canGenerate();

    @Override
    public @Nullable RadiusDescriptor getRadius()
    {
        return new RadiusDescriptor.Circle(primePosition,1);
    }

    @Override
    public void readFromPacketNBT(CompoundTag cmp) {
        super.readFromPacketNBT(cmp);
        passiveDecayTicks = cmp.getInt(TAG_PASSIVE_DECAY_TICKS);
    }

    @Override
    public void writeToPacketNBT(CompoundTag cmp) {
        super.writeToPacketNBT(cmp);
        cmp.putInt(TAG_PASSIVE_DECAY_TICKS, passiveDecayTicks);
    }

    @Override
    public boolean isOvergrowthAffected() {
        return false;
    }
}
