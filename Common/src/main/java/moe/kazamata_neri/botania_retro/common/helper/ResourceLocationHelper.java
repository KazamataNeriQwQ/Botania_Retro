package moe.kazamata_neri.botania_retro.common.helper;

import net.minecraft.resources.ResourceLocation;

import static moe.kazamata_neri.botania_retro.CommonInitializer.MOD_ID;

public class ResourceLocationHelper {
    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
    public static ResourceLocation floating(String path)
    {
        return new ResourceLocation(MOD_ID, "floating_" + path);
    }
    public static ResourceLocation potted(String path)
    {
        return new ResourceLocation(MOD_ID, "potted_" + path);
    }
    public static final String AESIR_RING = "aesir_ring";
    public static final String SUBblock_DAYBLOOM = "day_bloom";
    public static final String SUBblock_DAYBLOOM_PRIME = "day_bloom_prime";
    public static final String SUBblock_NIGHTSHADE = "night_shade";
    public static final String SUBblock_NIGHTSHADE_PRIME = "night_shade_prime";
}