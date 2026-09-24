package moe.kazamata_neri.botania_retro.mixin;

import moe.kazamata_neri.botania_retro.common.item.ExtraBotaniaItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.relic.RingOfThorItem;

@Mixin(RingOfThorItem.class)
public abstract class MixinRingOfThorItem {
    @Inject(method = "getThorRing", at = @At("HEAD"), cancellable = true, remap = false)
    private static void fixGetThorRing(Player player, CallbackInfoReturnable<ItemStack> cir)
    {
        ItemStack aesirRing = EquipmentHandler.findOrEmpty(ExtraBotaniaItems.aesirRing, player);
        if (!aesirRing.isEmpty())
        {
            cir.setReturnValue(aesirRing);
        }
    }
}