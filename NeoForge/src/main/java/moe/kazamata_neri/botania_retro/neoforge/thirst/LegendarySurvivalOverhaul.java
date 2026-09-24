package moe.kazamata_neri.botania_retro.neoforge.thirst;

import moe.kazamata_neri.botania_retro.api.ThirstLike;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.thirst.IThirstAttachment;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.attachments.ModAttachments;

public final class LegendarySurvivalOverhaul extends ThirstLike {
    public static final String MOD_ID = "legendarysurvivaloverhaul";

    @Override
    public void drink(Player player) {
        ThirstUtil.takeDrink(player, 2, 2.4F);
    }

    @Override
    public boolean canDrink(Player player) {
        IThirstAttachment attachment = player.getData(ModAttachments.THIRST.get());
        return !attachment.isHydrationLevelAtMax();
    }
}