package moe.kazamata_neri.botania_retro.neoforge.thirst;

import moe.kazamata_neri.botania_retro.api.IThirstLike;
import net.neoforged.fml.ModList;

public class NeoForgeThirstPort {
    public static void register(ModList modList)
    {
        if(modList.isLoaded(ThirstWasTaken.ModID))
        {
            IThirstLike.INSTANCE.add(new ThirstWasTaken());
        }
    }
}
