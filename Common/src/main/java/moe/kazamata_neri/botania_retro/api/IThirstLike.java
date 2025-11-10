package moe.kazamata_neri.botania_retro.api;

import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class IThirstLike {
    public static final List<IThirstLike> INSTANCE = new ArrayList<>();
    public abstract void Drink(Player player);
    public abstract boolean CanDrink(Player player);
}
