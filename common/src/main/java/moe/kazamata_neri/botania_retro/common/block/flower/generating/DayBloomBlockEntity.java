package moe.kazamata_neri.botania_retro.common.block.flower.generating;

import moe.kazamata_neri.botania_retro.common.block.ExtraBotaniaFlowerBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DayBloomBlockEntity extends PassiveGeneratingBlockEntity  {


    public DayBloomBlockEntity(BlockPos pos, BlockState state) {
        super(ExtraBotaniaFlowerBlocks.DAYBLOOM, pos, state, 3);
    }

    @Override
    public int getColor()
    {
        return 0xFFFF00;
    }

    public boolean canGenerate()
    {
        Level level = getLevel();
        if (level != null) {
            return !(level.isRaining() || level.isThundering()) && level.isDay() && level.canSeeSky(getBlockPos());
        }
        return false;
    }
}
