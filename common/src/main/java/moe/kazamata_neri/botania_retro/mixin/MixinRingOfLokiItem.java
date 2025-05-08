package moe.kazamata_neri.botania_retro.mixin;

import moe.kazamata_neri.botania_retro.common.item.ExtraBotaniaItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.relic.RingOfLokiItem;

@Mixin(RingOfLokiItem.class)
public abstract class MixinRingOfLokiItem {
    @Redirect(method = "onPlayerInteract", at = @At(value = "INVOKE", target = "Lvazkii/botania/common/item/relic/RingOfLokiItem;getLokiRing(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/item/ItemStack;"))
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