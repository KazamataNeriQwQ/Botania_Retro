package moe.kazamata_neri.botania_retro.forge.thirst;

import dev.ghen.thirst.foundation.common.capability.IThirst;
import dev.ghen.thirst.foundation.common.capability.ModCapabilities;
import moe.kazamata_neri.botania_retro.api.ThirstLike;
import net.minecraft.world.entity.player.Player;

public final class ThirstWasTaken extends ThirstLike {
    public static final String MOD_ID = dev.ghen.thirst.Thirst.ID;

    @Override
    public void drink(Player player) {
        player.getCapability(ModCapabilities.PLAYER_THIRST).ifPresent(thirst -> {
            thirst.drink(player, 2, 2);
            float exhaustion = thirst.getExhaustion();
            if (exhaustion >= 1.6F) {
                thirst.setExhaustion(exhaustion - 1.6F);
            } else {
                thirst.drink(player, 0, 1);
                thirst.setExhaustion(exhaustion + 2.4F);
            }
        });
    }

    @Override
    public boolean canDrink(Player player) {
        return player.getCapability(ModCapabilities.PLAYER_THIRST)
                .map(IThirst::getThirst)
                .map(thirst -> thirst < 20)
                .orElse(false);
    }
}
