package moe.kazamata_neri.botania_retro.thirst;

import com.stereowalker.survive.api.needs.PlayerNeeds;
import com.stereowalker.survive.config.ServerConfig;
import com.stereowalker.survive.needs.WaterData;
import moe.kazamata_neri.botania_retro.api.ThirstLike;
import net.minecraft.world.entity.player.Player;

public final class Survive extends ThirstLike {
    public static final String MOD_ID = com.stereowalker.survive.Survive.MOD_ID;

    @Override
    public void drink(Player player) {
        WaterData water = (WaterData) PlayerNeeds.api().getWater(player);
        int level;
        float hydration;
        switch (ServerConfig.stomachCapacity) {
            case DOUBLED:
                level = 4;
                hydration = 4.8F;
                break;
            default:
                level = 2;
                hydration = 2.4F;
                break;
        }
        water.setWaterLevel(water.getWaterLevel() + level);
        water.setWaterHydrationLevel(water.getHydrationLevel() + hydration);
    }

    @Override
    public boolean canDrink(Player player) {
        int waterLevel = ((WaterData) PlayerNeeds.api().getWater(player)).getWaterLevel();
        switch (ServerConfig.stomachCapacity) {
            case DOUBLED:
                return waterLevel < 40;
            default:
                return waterLevel < 20;
        }
    }
}