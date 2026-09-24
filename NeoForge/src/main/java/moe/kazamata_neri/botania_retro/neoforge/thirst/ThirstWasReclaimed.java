package moe.kazamata_neri.botania_retro.neoforge.thirst;

import cn.mlus.thirst.Thirst;
import cn.mlus.thirst.content.thirst.PlayerThirst;
import cn.mlus.thirst.foundation.common.capability.IThirst;
import cn.mlus.thirst.foundation.common.capability.ModAttachment;
import moe.kazamata_neri.botania_retro.api.ThirstLike;
import net.minecraft.world.entity.player.Player;

public final class ThirstWasReclaimed extends ThirstLike {
    public static final String MOD_ID = Thirst.ID;

    @Override
    public boolean canDrink(Player player) {
        return ((PlayerThirst) player.getData(ModAttachment.PLAYER_THIRST.get())).getThirst() < 20;
    }

    @Override
    public void drink(Player player) {
        IThirst thirst = player.getData(ModAttachment.PLAYER_THIRST.get());
        thirst.drink(2, 2);
        float exhaustion = thirst.getExhaustion();
        if (exhaustion >= 1.6F) {
            thirst.setExhaustion(exhaustion - 1.6F);
        } else {
            thirst.drink(0, 1);
            thirst.setExhaustion(exhaustion + 2.4F);
        }
    }
}