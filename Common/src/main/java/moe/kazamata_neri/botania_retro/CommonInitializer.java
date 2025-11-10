package moe.kazamata_neri.botania_retro;

import net.minecraft.resources.ResourceLocation;

public final class CommonInitializer {
    public static final String MOD_ID = "botania_retro";

    public static void init() {
    }

    public static ResourceLocation RL(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
