package moe.kazamata_neri.botania_retro;

import moe.kazamata_neri.botania_retro.common.crafting.recipe.ShapelessRelicBindRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.BiConsumer;

import static moe.kazamata_neri.botania_retro.common.helper.ResourceLocationHelper.prefix;

public final class Botania_retro {
    public static final String MOD_ID = "botania_retro";

    public static void registerRecipeSerializers(BiConsumer<RecipeSerializer<?>, ResourceLocation> r)
    {
        r.accept(ShapelessRelicBindRecipe.SERIALIZER, prefix("relic_bind_shapeless"));
    }

    public static void init() {
        // Write common init code here.
    }
}
