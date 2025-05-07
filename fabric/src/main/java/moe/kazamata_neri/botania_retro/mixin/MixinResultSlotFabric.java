package moe.kazamata_neri.botania_retro.mixin;

import moe.kazamata_neri.botania_retro.common.item.relic.RingOfAesirItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ResultSlot.class)
public class MixinResultSlotFabric {
    @Shadow
    @Final
    private Player player;

    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;onCraftedBy(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;I)V"),
            method = "checkTakeAchievements"
    )
    private void onCraft(ItemStack itemStack, CallbackInfo ci)
    {
        RingOfAesirItem.onItemCrafted(player, itemStack);
    }
}
