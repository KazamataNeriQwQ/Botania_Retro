package moe.kazamata_neri.botania_retro.thirst;

import moe.kazamata_neri.botania_retro.api.ThirstLike;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.TANAPI;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;

public final class ToughAsNails extends ThirstLike {
    public static final String MOD_ID = TANAPI.MOD_ID;

    @Override
    public void drink(Player player) {
        IThirst thirst = ThirstHelper.getThirst(player);
        thirst.addThirst(2);
        thirst.addHydration(2.4F);
    }

    @Override
    public boolean canDrink(Player player) {
        return ThirstHelper.canDrink(player, false);
    }
}
