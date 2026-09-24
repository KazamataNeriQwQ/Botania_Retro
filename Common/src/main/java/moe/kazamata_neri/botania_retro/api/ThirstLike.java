package moe.kazamata_neri.botania_retro.api;

import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class ThirstLike {
    public static final List<ThirstLike> INSTANCE = new ArrayList<>();
    public abstract void drink(Player player);
    public abstract boolean canDrink(Player player);
}