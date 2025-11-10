package moe.kazamata_neri.botania_retro.fabric.thirst;

import moe.kazamata_neri.botania_retro.api.IThirstLike;
import moe.kazamata_neri.botania_retro.thirst.Homeostatic;
import moe.kazamata_neri.botania_retro.thirst.Survive;
import moe.kazamata_neri.botania_retro.thirst.ToughAsNails;
import net.fabricmc.loader.api.FabricLoader;

public class FabricThirstPort {
    public static void register(FabricLoader loader)
    {
        if(loader.isModLoaded(ToughAsNails.ModID))
        {
            IThirstLike.INSTANCE.add(new ToughAsNails());
        }
        if(loader.isModLoaded(Survive.ModID))
        {
            IThirstLike.INSTANCE.add(new Survive());
        }
        if(loader.isModLoaded(Homeostatic.ModID))
        {
            IThirstLike.INSTANCE.add(new Homeostatic());
        }
        if(loader.isModLoaded(Dehydration.ModID))
        {
            IThirstLike.INSTANCE.add(new Dehydration());
        }
    }
}
