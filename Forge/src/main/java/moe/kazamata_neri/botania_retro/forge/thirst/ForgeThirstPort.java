package moe.kazamata_neri.botania_retro.forge.thirst;

import moe.kazamata_neri.botania_retro.api.ThirstLike;
import moe.kazamata_neri.botania_retro.thirst.Homeostatic;
import moe.kazamata_neri.botania_retro.thirst.ToughAsNails;
import moe.kazamata_neri.botania_retro.thirst.YetAnotherThirst;
import net.minecraftforge.fml.ModList;

public class ForgeThirstPort {
    public static void register(ModList modList) {
        if (modList.isLoaded(ToughAsNails.MOD_ID)) {
            ThirstLike.INSTANCE.add(new ToughAsNails());
        }
        if (modList.isLoaded(Homeostatic.MOD_ID)) {
            ThirstLike.INSTANCE.add(new Homeostatic());
        }
        if (modList.isLoaded(YetAnotherThirst.MOD_ID)) {
            ThirstLike.INSTANCE.add(new YetAnotherThirst());
        }
        if (modList.isLoaded(Survive.MOD_ID)) {
            ThirstLike.INSTANCE.add(new Survive());
        }
        if (modList.isLoaded(TerraFirmaCraft.MOD_ID)) {
            ThirstLike.INSTANCE.add(new TerraFirmaCraft());
        }
        if (modList.isLoaded(ThirstWasTaken.MOD_ID)) {
            ThirstLike.INSTANCE.add(new ThirstWasTaken());
        }
        if (modList.isLoaded(LegendarySurvivalOverhaul.MOD_ID)) {
            ThirstLike.INSTANCE.add(new LegendarySurvivalOverhaul());
        }
        if (modList.isLoaded(RealisticNeeds.MOD_ID)) {
            ThirstLike.INSTANCE.add(new RealisticNeeds());
        }
    }
}
