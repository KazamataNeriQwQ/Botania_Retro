package moe.kazamata_neri.botania_retro.neoforge.thirst;

import moe.kazamata_neri.botania_retro.api.ThirstLike;
import net.dries007.tfc.common.player.IPlayerInfo;
import net.dries007.tfc.common.player.PlayerInfo;
import net.minecraft.world.entity.player.Player;

public final class TerraFirmaCraft extends ThirstLike {
    public static final String MOD_ID = "tfc";

    @Override
    public void drink(Player player) {
        IPlayerInfo info = IPlayerInfo.get(player);
        info.addThirst(PlayerInfo.MAX_THIRST * 0.1f);
        info.onDrink();
    }

    @Override
    public boolean canDrink(Player player) {
        return IPlayerInfo.get(player).getThirst() < PlayerInfo.MAX_THIRST;
    }
}