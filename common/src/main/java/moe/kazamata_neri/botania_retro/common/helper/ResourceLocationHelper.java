package moe.kazamata_neri.botania_retro.common.helper;

import net.minecraft.resources.ResourceLocation;

import static moe.kazamata_neri.botania_retro.Botania_retro.MOD_ID;

public class ResourceLocationHelper {
    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}