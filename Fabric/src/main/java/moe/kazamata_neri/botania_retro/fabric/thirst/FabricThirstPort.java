package moe.kazamata_neri.botania_retro.fabric.thirst;

import moe.kazamata_neri.botania_retro.api.ThirstLike;
import moe.kazamata_neri.botania_retro.thirst.Homeostatic;
import moe.kazamata_neri.botania_retro.thirst.ToughAsNails;
import moe.kazamata_neri.botania_retro.thirst.YetAnotherThirst;
import net.fabricmc.loader.api.FabricLoader;

public class FabricThirstPort {
    public static void register(FabricLoader loader) {
        if (loader.isModLoaded(ToughAsNails.MOD_ID)) {
            ThirstLike.INSTANCE.add(new ToughAsNails());
        }
        if (loader.isModLoaded(Homeostatic.MOD_ID)) {
            ThirstLike.INSTANCE.add(new Homeostatic());
        }
        if (loader.isModLoaded(YetAnotherThirst.MOD_ID)) {
            ThirstLike.INSTANCE.add(new YetAnotherThirst());
        }
        if (loader.isModLoaded(Dehydration.MOD_ID)) {
            ThirstLike.INSTANCE.add(new Dehydration());
        }
        if (loader.isModLoaded(ExtremeDehydration.MOD_ID)) {
            ThirstLike.INSTANCE.add(new ExtremeDehydration());
        }
    }
}
