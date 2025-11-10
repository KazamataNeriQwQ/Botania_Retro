package moe.kazamata_neri.botania_retro.mixin;

import moe.kazamata_neri.botania_retro.api.IThirstLike;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.item.relic.FruitOfGrisaiaItem;

@Mixin(FruitOfGrisaiaItem.class)
public abstract class MixinFruitOfGrisaiaItem {
    @Redirect(method = {"use", "onUseTick"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;canEat(Z)Z"))
    private boolean fixCanEat(Player player, boolean canAlwaysEat) {
        for(IThirstLike thirstLike : IThirstLike.INSTANCE)
        {
            if (thirstLike.CanDrink(player))
            {
                return thirstLike.CanDrink(player);
            }
        }
        return player.canEat(canAlwaysEat);
    }

    @Inject(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V", shift = At.Shift.AFTER))
    public void fixOnUseTick(Level world, LivingEntity living, ItemStack stack, int count, CallbackInfo ci)
    {
        if (living instanceof Player player)
        {
            for(IThirstLike thirstLike : IThirstLike.INSTANCE)
            {
                thirstLike.Drink(player);
            }
        }
    }
}
