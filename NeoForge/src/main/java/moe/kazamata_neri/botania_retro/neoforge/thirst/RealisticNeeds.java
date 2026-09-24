package moe.kazamata_neri.botania_retro.neoforge.thirst;

import de.Folfi.Luna.realisticneeds.network.RealisticneedsVariables;
import moe.kazamata_neri.botania_retro.api.ThirstLike;
import net.minecraft.world.entity.player.Player;

public final class RealisticNeeds extends ThirstLike {
    public static final String MOD_ID = "realisticneeds";

    @Override
    public void drink(Player player) {
        RealisticneedsVariables.PlayerVariables variables = player.getData(RealisticneedsVariables.PLAYER_VARIABLES.get());
        variables.Water += 2.0D;
        variables.syncPlayerVariables(player);
    }

    @Override
    public boolean canDrink(Player player) {
        return true;
    }
}