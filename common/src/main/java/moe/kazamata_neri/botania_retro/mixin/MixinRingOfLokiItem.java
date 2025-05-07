package moe.kazamata_neri.botania_retro.mixin;

import com.google.common.collect.ImmutableList;
import moe.kazamata_neri.botania_retro.common.item.ExtraBotaniaItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.relic.RingOfLokiItem;

import java.util.List;

@Mixin(RingOfLokiItem.class)
public class MixinRingOfLokiItem {
    @Shadow
    private static BlockPos getBindingCenter(ItemStack stack)
    {
        return new BlockPos(0,0,0);
    }

    @Shadow
    private static List<BlockPos> getCursorList(ItemStack stack)
    {
        return ImmutableList.of();
    }

    @Inject(method = "getLokiRing", at = @At("HEAD"), cancellable = true, remap = false)
    private static void fixGetLokiRing(Player player, CallbackInfoReturnable<ItemStack> cir)
    {
        ItemStack aesirRing = EquipmentHandler.findOrEmpty(ExtraBotaniaItems.aesirRing, player);
        if (!aesirRing.isEmpty())
        {
            cir.setReturnValue(aesirRing);
        }
    }

    /**
     * 修复获取物品的逻辑。
     *
     * @author KazamataNeri
     * @reason 重定向签名有问题，不影响原版逻辑
     */
    @Overwrite
    public List<BlockPos> getWireframesToDraw(Player player, ItemStack stack) {
        if (EquipmentHandler.findOrEmpty(BotaniaItems.lokiRing ,player) != stack) {
            return ImmutableList.of();
        }

        HitResult lookPos = Minecraft.getInstance().hitResult;

        if (lookPos != null
                && lookPos.getType() == HitResult.Type.BLOCK
                && !player.level().isEmptyBlock(((BlockHitResult) lookPos).getBlockPos())) {
            List<BlockPos> list = getCursorList(stack);
            BlockPos origin = getBindingCenter(stack);

            for (int i = 0; i < list.size(); i++) {
                if (origin.getY() != Integer.MIN_VALUE) {
                    list.set(i, list.get(i).offset(origin));
                } else {
                    list.set(i, list.get(i).offset(((BlockHitResult) lookPos).getBlockPos()));
                }
            }

            return list;
        }

        return ImmutableList.of();
    }

    /**
     * 修复获取物品的逻辑。
     *
     * @author KazamataNeri
     * @reason 重定向签名有问题，不影响原版逻辑
     */
    @Overwrite
    public BlockPos getSourceWireframe(Player player, ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        if (EquipmentHandler.findOrEmpty(BotaniaItems.lokiRing, player) == stack) {
            BlockPos currentBuildCenter = getBindingCenter(stack);
            if (currentBuildCenter.getY() != Integer.MIN_VALUE) {
                return currentBuildCenter;
            } else if (mc.hitResult instanceof BlockHitResult hitRes
                    && mc.hitResult.getType() == HitResult.Type.BLOCK
                    && !getCursorList(stack).isEmpty()) {
                return hitRes.getBlockPos();
            }
        }

        return null;
    }
}
