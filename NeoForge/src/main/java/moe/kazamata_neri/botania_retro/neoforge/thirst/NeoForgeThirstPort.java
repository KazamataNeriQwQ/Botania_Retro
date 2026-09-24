package moe.kazamata_neri.botania_retro.neoforge.thirst;

import moe.kazamata_neri.botania_retro.api.ThirstLike;
import moe.kazamata_neri.botania_retro.thirst.Homeostatic;
import moe.kazamata_neri.botania_retro.thirst.Survive;
import moe.kazamata_neri.botania_retro.thirst.ToughAsNails;
import moe.kazamata_neri.botania_retro.thirst.YetAnotherThirst;
import net.neoforged.fml.ModList;

public class NeoForgeThirstPort {
    public static void register(ModList modList) {
        if (modList.isLoaded(ToughAsNails.MOD_ID)) {
            ThirstLike.INSTANCE.add(new ToughAsNails());
        }
        if (modList.isLoaded(Survive.MOD_ID)) {
            ThirstLike.INSTANCE.add(new Survive());
        }
        if (modList.isLoaded(Homeostatic.MOD_ID)) {
            ThirstLike.INSTANCE.add(new Homeostatic());
        }
        if (modList.isLoaded(YetAnotherThirst.MOD_ID)) {
            ThirstLike.INSTANCE.add(new YetAnotherThirst());
        }
        if (modList.isLoaded(ThirstWasReclaimed.MOD_ID)) {
            if (ThirstWasTaken.isAvailable()) {
                ThirstLike.INSTANCE.add(new ThirstWasTaken());
            } else {
                ThirstLike.INSTANCE.add(new ThirstWasReclaimed());
            }
        }
        if (modList.isLoaded(LegendarySurvivalOverhaul.MOD_ID)) {
            ThirstLike.INSTANCE.add(new LegendarySurvivalOverhaul());
        }
        if (modList.isLoaded(RealisticNeeds.MOD_ID)) {
            ThirstLike.INSTANCE.add(new RealisticNeeds());
        }
    }
}