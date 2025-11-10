package moe.kazamata_neri.botania_retro.neoforge.thirst;

import dev.ghen.thirst.content.thirst.PlayerThirst;
import dev.ghen.thirst.foundation.common.capability.ModAttachment;
import moe.kazamata_neri.botania_retro.api.IThirstLike;
import net.minecraft.world.entity.player.Player;

public final class ThirstWasTaken extends IThirstLike {

    public static final String ModID = "thirst";
    public boolean CanDrink(Player player)
    {
        return ((PlayerThirst)player.getData(ModAttachment.PLAYER_THIRST)).getThirst() < 20;
    }
    public void Drink(Player player)
    {
        PlayerThirst playerThirst = ((PlayerThirst)player.getData(ModAttachment.PLAYER_THIRST));
        playerThirst.drink(2, 2);
        float exhaustion = playerThirst.getExhaustion();
        if(exhaustion >= 1.6F)
        {
            playerThirst.setExhaustion(exhaustion - 1.6F);
        }
        else
        {
            playerThirst.drink(0, 1);
            playerThirst.setExhaustion(exhaustion + 2.4F);
        }
    }
}

