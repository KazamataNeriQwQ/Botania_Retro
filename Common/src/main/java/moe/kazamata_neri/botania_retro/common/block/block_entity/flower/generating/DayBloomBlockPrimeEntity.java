package moe.kazamata_neri.botania_retro.common.block.block_entity.flower.generating;

import moe.kazamata_neri.botania_retro.common.block.block_entity.ExtraBotaniaBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DayBloomBlockPrimeEntity extends PassiveGeneratingBlockEntity {
    public DayBloomBlockPrimeEntity(BlockPos pos, BlockState state) {
        super(ExtraBotaniaBlockEntities.DAYBLOOM_PRIME, pos, state, 2);
    }

    @Override
    public int getColor() {
        return 0xFFFF00;
    }

    @Override
    public boolean isPrime() {
        return true;
    }

    public boolean canGenerate() {
        Level level = getLevel();
        if (level != null) {
            return !(level.isRaining() || level.isThundering()) && level.isDay() && level.canSeeSky(getBlockPos());
        }
        return false;
    }
}
