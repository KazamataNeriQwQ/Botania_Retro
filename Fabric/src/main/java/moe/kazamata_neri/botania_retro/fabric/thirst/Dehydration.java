package moe.kazamata_neri.botania_retro.fabric.thirst;

import moe.kazamata_neri.botania_retro.api.IThirstLike;
import net.dehydration.access.ThirstManagerAccess;
import net.dehydration.thirst.ThirstManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class Dehydration extends IThirstLike {
    public static String ModID = "dehydration";

    @Override
    public void Drink(Player player) {
        if (player instanceof ServerPlayer serverPlayer)
        {
            ThirstManager thirstManager = ((ThirstManagerAccess) serverPlayer).getThirstManager();
            thirstManager.add(2);
            thirstManager.addDehydration(-4.8F);
        }
    }

    @Override
    public boolean CanDrink(Player player) {
        if (player instanceof ServerPlayer serverPlayer)
        {
            return ((ThirstManagerAccess) serverPlayer).getThirstManager().getThirstLevel() < 20;
        }
        return false;
    }
}
