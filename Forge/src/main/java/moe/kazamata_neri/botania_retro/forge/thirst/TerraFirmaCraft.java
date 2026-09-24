package moe.kazamata_neri.botania_retro.forge.thirst;

import moe.kazamata_neri.botania_retro.api.ThirstLike;
import net.dries007.tfc.common.capabilities.food.TFCFoodData;
import net.minecraft.world.entity.player.Player;

public final class TerraFirmaCraft extends ThirstLike {
    public static final String MOD_ID = "tfc";

    @Override
    public void drink(Player player) {
        if (player.getFoodData() instanceof TFCFoodData foodData) {
            foodData.addThirst(TFCFoodData.MAX_THIRST * 0.10F);
        }
    }

    @Override
    public boolean canDrink(Player player) {
        if (player.getFoodData() instanceof TFCFoodData foodData) {
            return foodData.getThirst() < TFCFoodData.MAX_THIRST;
        }
        return false;
    }
}
