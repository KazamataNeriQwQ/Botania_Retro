package moe.kazamata_neri.botania_retro.thirst;

import dev.minhnh.yetanotherthirst.api.YetAnotherThirstAPI;
import moe.kazamata_neri.botania_retro.api.ThirstLike;
import net.minecraft.world.entity.player.Player;

public final class YetAnotherThirst extends ThirstLike {
    public static final String MOD_ID = "yet_another_thirst";

    @Override
    public void drink(Player player) {
        YetAnotherThirstAPI.drink(player, 2, 2);
    }

    @Override
    public boolean canDrink(Player player) {
        return YetAnotherThirstAPI.isThirstEnabled(player) && YetAnotherThirstAPI.getThirst(player) < 20;
    }
}