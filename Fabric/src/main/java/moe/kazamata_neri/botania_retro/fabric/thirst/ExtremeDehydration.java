package moe.kazamata_neri.botania_retro.fabric.thirst;

import moe.kazamata_neri.botania_retro.api.ThirstLike;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.stereometric.extremedehydration.network.thirst.ThirstNetworking;
import org.stereometric.extremedehydration.util.thirst.ThirstHolder;

public final class ExtremeDehydration extends ThirstLike {
    public static final String MOD_ID = "extremedehydration";

    @Override
    public void drink(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ThirstHolder holder = (ThirstHolder) serverPlayer;
            holder.extremeDehydration$setThirst(holder.extremeDehydration$getThirst() + 2);
            ThirstNetworking.sync(serverPlayer);
        }
    }

    @Override
    public boolean canDrink(Player player) {
        return player instanceof ServerPlayer serverPlayer
                && ((ThirstHolder) serverPlayer).extremeDehydration$getThirst() < 20;
    }
}
