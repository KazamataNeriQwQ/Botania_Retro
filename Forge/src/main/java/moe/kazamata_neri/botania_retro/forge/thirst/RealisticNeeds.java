package moe.kazamata_neri.botania_retro.forge.thirst;

import de.Folfi.Luna.realisticneeds.network.RealisticneedsVariables;
import moe.kazamata_neri.botania_retro.api.ThirstLike;
import net.minecraft.world.entity.player.Player;

public final class RealisticNeeds extends ThirstLike {
    public static final String MOD_ID = de.Folfi.Luna.realisticneeds.Realisticneeds.MODID;

    @Override
    public void drink(Player player) {
        player.getCapability(RealisticneedsVariables.PLAYER_VARIABLES_CAPABILITY).ifPresent(variables -> {
            variables.Water += 2.0D;
            variables.syncPlayerVariables(player);
        });
    }

    @Override
    public boolean canDrink(Player player) {
        return true;
    }
}
