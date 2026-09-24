package moe.kazamata_neri.botania_retro.common.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.UUID;

public class ShapelessRelicBindRecipe extends ShapelessRecipe {
    public ShapelessRelicBindRecipe(ShapelessRecipe compose) {
        super(compose.getId(), compose.getGroup(), CraftingBookCategory.EQUIPMENT,
                compose.getResultItem(RegistryAccess.EMPTY),
                compose.getIngredients());
    }

    private static UUID getUUID(ItemStack stack)
    {
        var relic = XplatAbstractions.INSTANCE.findRelic(stack);
        if (relic != null)
        {
            return relic.getSoulbindUUID();
        }
        return null;
    }

    @Override
    public boolean matches(@NotNull CraftingContainer inv,@NotNull Level level) {
        if (!super.matches(inv, level)) {
            return false;
        }

        UUID requiredUUID = null;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            UUID currentUUID = getUUID(stack);
            if (requiredUUID == null) {
                requiredUUID = currentUUID;
            } else if (currentUUID != null && !requiredUUID.equals(currentUUID)) {
                return false;
            }
        }
        return true;
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull CraftingContainer container, @NotNull RegistryAccess registryAccess) {
        ItemStack result = super.assemble(container, registryAccess);
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.getTag() != null) {
                result.getOrCreateTag().merge(stack.getTag().copy());
            }
        }
        return result;
    }

    @NotNull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
    public static final RecipeSerializer<ShapelessRelicBindRecipe> SERIALIZER = new Serializer();

    private static class Serializer implements RecipeSerializer<ShapelessRelicBindRecipe> {
        @NotNull
        @Override
        public ShapelessRelicBindRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            return new ShapelessRelicBindRecipe(SHAPELESS_RECIPE.fromJson(recipeId, json));
        }

        @NotNull
        @Override
        public ShapelessRelicBindRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            return new ShapelessRelicBindRecipe(SHAPELESS_RECIPE.fromNetwork(recipeId, buffer));
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull ShapelessRelicBindRecipe recipe) {
            SHAPELESS_RECIPE.toNetwork(buffer, recipe);
        }
    }
}

