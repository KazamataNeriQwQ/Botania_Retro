package moe.kazamata_neri.botania_retro.fabric.thirst;

import com.example.hungerandthirst.ThirstManager;
import moe.kazamata_neri.botania_retro.api.ThirstLike;
import net.minecraft.world.entity.player.Player;

public final class HungerAndThirst extends ThirstLike {
    public static final String MOD_ID = "hungerandthirst";

    @Override
    public void drink(Player player) {
        ThirstManager.setThirst(player.getUUID(), ThirstManager.getThirst(player.getUUID()) + 2.0F);
    }

    @Override
    public boolean canDrink(Player player) {
        return ThirstManager.getThirst(player.getUUID()) < 20.0F;
    }
}