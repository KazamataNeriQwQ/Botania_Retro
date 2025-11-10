package moe.kazamata_neri.botania_retro.thirst;

import com.stereowalker.survive.api.needs.PlayerNeeds;
import com.stereowalker.survive.config.ServerConfig;
import com.stereowalker.survive.needs.WaterData;
import moe.kazamata_neri.botania_retro.api.IThirstLike;
import net.minecraft.world.entity.player.Player;


public final class Survive extends IThirstLike {
    public static String ModID = com.stereowalker.survive.Survive.MOD_ID;

    @Override
    public void Drink(Player player) {
        WaterData water = (WaterData)PlayerNeeds.api().getWater(player);
        water.setWaterLevel(water.getWaterLevel() + 2);
        water.setWaterHydrationLevel(water.getHydrationLevel() + 2.4F);
    }

    @Override
    public boolean CanDrink(Player player) {
        int waterLevel = ((WaterData)PlayerNeeds.api().getWater(player)).getWaterLevel();
        switch (ServerConfig.stomachCapacity) {
            case DOUBLED:
                return waterLevel < 40;
            default:
                return waterLevel < 20;
        }
    }
}
