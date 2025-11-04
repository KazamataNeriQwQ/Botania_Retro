package moe.kazamata_neri.botania_retro.common.crafting.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.UUID;

public class ShapelessRelicBindRecipe extends ShapelessRecipe {
    public static final RecipeSerializer<ShapelessRelicBindRecipe> SERIALIZER = new Serializer();

    final String group;
    final CraftingBookCategory category;
    final ItemStack result;
    final NonNullList<Ingredient> ingredients;

    public ShapelessRelicBindRecipe(String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(group, category, result, ingredients);
        this.group = group;
        this.category = category;
        this.result = result;
        this.ingredients = ingredients;
    }

    private static UUID getUUID(ItemStack stack) {
        var relic = XplatAbstractions.INSTANCE.findRelic(stack);
        if (relic != null)
        {
            return relic.getSoulbindUUID();
        }
        return null;
    }

    @Override
    public boolean matches(CraftingInput inv, Level level) {
        if (!super.matches(inv, level)) {
            return false;
        }

        UUID requiredUUID = null;
        for (int i = 0; i < inv.size(); i++) {
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
    public @NotNull ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
        ItemStack result = super.assemble(inv, registries);
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            for(var c : stack.getComponents())
            {
                result.set((DataComponentType)c.type(), c.value());
            }
        }
        return result;
    }
    private static class Serializer implements RecipeSerializer<ShapelessRelicBindRecipe> {
        private static final MapCodec<ShapelessRelicBindRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Codec.STRING.optionalFieldOf("group", "").forGetter((shapelessRecipe) -> shapelessRecipe.group), CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter((shapelessRecipe) -> shapelessRecipe.category), ItemStack.STRICT_CODEC.fieldOf("result").forGetter((shapelessRecipe) -> shapelessRecipe.result), Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap((list) -> {
            Ingredient[] ingredients = (Ingredient[])list.stream().filter((ingredient) -> !ingredient.isEmpty()).toArray((i) -> new Ingredient[i]);
            if (ingredients.length == 0) {
                return DataResult.error(() -> "No ingredients for shapeless recipe");
            } else {
                return ingredients.length > 9 ? DataResult.error(() -> "Too many ingredients for shapeless recipe") : DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredients));
            }
        }, DataResult::success).forGetter((shapelessRecipe) -> shapelessRecipe.ingredients)).apply(instance, ShapelessRelicBindRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessRelicBindRecipe> STREAM_CODEC = StreamCodec.of(ShapelessRelicBindRecipe.Serializer::toNetwork, ShapelessRelicBindRecipe.Serializer::fromNetwork);

        public MapCodec<ShapelessRelicBindRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, ShapelessRelicBindRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static ShapelessRelicBindRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            String string = buffer.readUtf();
            CraftingBookCategory craftingBookCategory = (CraftingBookCategory)buffer.readEnum(CraftingBookCategory.class);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonNullList = NonNullList.withSize(i, Ingredient.EMPTY);
            nonNullList.replaceAll((ingredient) -> (Ingredient)Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            ItemStack itemStack = (ItemStack)ItemStack.STREAM_CODEC.decode(buffer);
            return new ShapelessRelicBindRecipe(string, craftingBookCategory, itemStack, nonNullList);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, ShapelessRelicBindRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeEnum(recipe.category);
            buffer.writeVarInt(recipe.ingredients.size());

            for(Ingredient ingredient : recipe.ingredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
        }
    }
}

