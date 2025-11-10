package moe.kazamata_neri.botania_retro.mixin;

import moe.kazamata_neri.botania_retro.common.item.ExtraBotaniaItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.relic.RingOfOdinItem;
import vazkii.botania.common.lib.BotaniaTags;

@Mixin(RingOfOdinItem.class)
public abstract class MixinRingOfOdinItem {
    @Inject(method = "onPlayerAttacked", at = @At("HEAD"), cancellable = true)
    private static void fixOnPlayerAttacked(Player player, DamageSource src, CallbackInfoReturnable<Boolean> cir) {
        boolean isAesirRing = src.is(BotaniaTags.DamageTypes.RING_OF_ODIN_IMMUNE) && !EquipmentHandler.findOrEmpty(ExtraBotaniaItems.aesirRing, player).isEmpty();
        if (isAesirRing)
        {
            cir.setReturnValue(true);
        }
    }
}
