package moe.kazamata_neri.botania_retro.common.crafting;

import moe.kazamata_neri.botania_retro.common.crafting.recipe.ShapelessRelicBindRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.BiConsumer;

import static moe.kazamata_neri.botania_retro.CommonInitializer.RL;

public class ExtraBotaniaRecipeTypes {
    public static void submitRecipeSerializers(BiConsumer<RecipeSerializer<?>, ResourceLocation> r) {
        r.accept(ShapelessRelicBindRecipe.SERIALIZER, RL("relic_bind_shapeless"));
    }
}
