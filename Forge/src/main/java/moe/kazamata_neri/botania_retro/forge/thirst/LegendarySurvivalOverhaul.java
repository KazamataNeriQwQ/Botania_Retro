package moe.kazamata_neri.botania_retro.forge.thirst;

import moe.kazamata_neri.botania_retro.api.ThirstLike;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public final class LegendarySurvivalOverhaul extends ThirstLike {
    public static final String MOD_ID = sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul.MOD_ID;

    @Override
    public void drink(Player player) {
        ThirstUtil.takeDrink(player, 2, 2.4F);
    }

    @Override
    public boolean canDrink(Player player) {
        return !CapabilityUtil.getThirstCapability(player).isHydrationLevelAtMax();
    }
}
