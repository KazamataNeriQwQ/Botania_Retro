package moe.kazamata_neri.botania_retro.thirst;

import homeostatic.common.Hydration;
import homeostatic.network.IWater;
import homeostatic.platform.Services;
import homeostatic.util.WaterHelper;
import moe.kazamata_neri.botania_retro.api.ThirstLike;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public final class Homeostatic extends ThirstLike {
    public static final String MOD_ID = homeostatic.Homeostatic.MODID;

    @Override
    public void drink(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            WaterHelper.drink(serverPlayer, new Hydration(2, 2.4F, 0, 0, 0F), true);
        }
    }

    @Override
    public boolean canDrink(Player player) {
        Optional<? extends IWater> waterOpt = Services.PLATFORM.getWaterCapabilty(player);
        return waterOpt.map(data -> data.getWaterLevel() < 20).orElse(false);
    }
}