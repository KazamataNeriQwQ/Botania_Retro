package moe.kazamata_neri.botania_retro.common.block.flower.generating;

import moe.kazamata_neri.botania_retro.common.block.ExtraBotaniaFlowerBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class NightShadePrimeBlockEntity extends PassiveGeneratingBlockEntity {
    public NightShadePrimeBlockEntity(BlockPos pos, BlockState state) {
        super(ExtraBotaniaFlowerBlocks.NIGHTSHADEPRIME, pos, state, 2);
    }

    @Override
    public int getColor()
    {
        return 0x3D2A90;
    }

    @Override
    public boolean canGenerate()
    {
        Level level= getLevel();
        if (level != null) {
            return level.isNight();
        }
        return false;
    }
}
