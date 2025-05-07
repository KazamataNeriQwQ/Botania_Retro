package moe.kazamata_neri.botania_retro.mixin;

import moe.kazamata_neri.botania_retro.common.item.ExtraBotaniaItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.relic.RingOfLokiItem;

@Mixin(RingOfLokiItem.class)
public abstract class MixinRingOfLokiItem {


    // Idea会报错不用理会
    @Redirect(method = "onPlayerInteract", at = @At(value = "INVOKE", target = "Lvazkii/botania/common/item/relic/RingOfLokiItem;getLokiRing(Lnet/minecraft/class_1657;)Lnet/minecraft/class_1799;", remap = false))
    private static ItemStack fixOnPlayerInteract(Player player)
    {
        var aesirRing = EquipmentHandler.findOrEmpty(ExtraBotaniaItems.aesirRing, player);
        if(!aesirRing.isEmpty())
        {
            return aesirRing;
        }
        return EquipmentHandler.findOrEmpty(BotaniaItems.lokiRing, player);
    }
}
