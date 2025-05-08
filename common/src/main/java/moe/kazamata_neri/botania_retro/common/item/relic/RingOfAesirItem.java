package moe.kazamata_neri.botania_retro.common.item.relic;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import moe.kazamata_neri.botania_retro.common.item.ExtraBotaniaItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block.Bound;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.item.WireframeCoordinateListProvider;
import vazkii.botania.common.advancements.RelicBindTrigger;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.relic.RelicBaubleItem;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class RingOfAesirItem extends RelicBaubleItem implements WireframeCoordinateListProvider {


    private static final String TAG_CURSOR_LIST = "cursorList";
    private static final String TAG_CURSOR_PREFIX = "cursor";
    private static final String TAG_CURSOR_COUNT = "cursorCount";
    private static final String TAG_X_OFFSET = "xOffset";
    private static final String TAG_Y_OFFSET = "yOffset";
    private static final String TAG_Z_OFFSET = "zOffset";
    private static final String TAG_X_ORIGIN = "xOrigin";
    private static final String TAG_Y_ORIGIN = "yOrigin";
    private static final String TAG_Z_ORIGIN = "zOrigin";


    public RingOfAesirItem(Item.Properties props)
    {
        super(props);
    }

    public static void OnDropped(ItemEntity itemEntity)
    {
        if (!itemEntity.level().isClientSide)
        {
            ItemStack stack = itemEntity.getItem();
            if (stack.getItem() == ExtraBotaniaItems.aesirRing)
            {
                var relic = XplatAbstractions.INSTANCE.findRelic(stack);
                if (relic != null)
                {
                    UUID uuid = relic.getSoulbindUUID();
                    double x = itemEntity.getX();
                    double y = itemEntity.getY();
                    double z = itemEntity.getZ();
                    Level level = itemEntity.level();
                    BlockPos blockPos = getBindingCenter(stack);
                    List<BlockPos> blockPosList = getCursorList(stack);
                    ItemStack lokiRing = new ItemStack(BotaniaItems.lokiRing);
                    ItemStack odinRing = new ItemStack(BotaniaItems.odinRing);
                    ItemStack thorRing = new ItemStack(BotaniaItems.thorRing);
                    setBindingCenter(lokiRing, blockPos);
                    setCursorList(lokiRing, blockPosList);
                    ItemStack[] rings = {lokiRing, odinRing, thorRing};
                    for (ItemStack ring : rings) {
                        var relicRing = XplatAbstractions.INSTANCE.findRelic(ring);
                        if(relicRing != null)
                        {
                            relicRing.bindToUUID(uuid);
                            level.addFreshEntity(new ItemEntity(level, x, y, z, ring));
                        }
                    }
                    itemEntity.remove(Entity.RemovalReason.KILLED);
                }
            }
        }
    }

    public static void onCrafted(Player player, ItemStack stack) {
        if (!player.level().isClientSide)
        {
            if (stack.getItem() == ExtraBotaniaItems.aesirRing)
            {
                var relic = XplatAbstractions.INSTANCE.findRelic(stack);
                if (relic != null && player instanceof ServerPlayer serverPlayer && serverPlayer.getUUID().equals(relic.getSoulbindUUID()))
                {
                    RelicBindTrigger.INSTANCE.trigger(serverPlayer, stack);
                }
            }
        }
    }

    @Override
    public void onUnequipped(ItemStack stack, LivingEntity living) {
        setCursorList(stack, null);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean held) {
        super.inventoryTick(stack, world, entity, slot, held);
        if (slot >= 0) {
            exitBindingMode(stack);
        }
    }

    @Override
    public List<BlockPos> getWireframesToDraw(Player player, ItemStack stack) {
        if (getAesirRing(player) != stack) {
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

    @Override
    public BlockPos getSourceWireframe(Player player, ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        if (getAesirRing(player) == stack) {
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

    @Override
    public void onValidPlayerWornTick(Player player) {
        if (player.isOnFire()) {
            player.clearFire();
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getEquippedAttributeModifiers(ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
        attributes.put(Attributes.MAX_HEALTH,
                new AttributeModifier(getBaubleUUID(stack), "Aesir Ring", 20, AttributeModifier.Operation.ADDITION));
        return attributes;
    }

    private static BlockPos getBindingCenter(ItemStack stack) {
        int x = ItemNBTHelper.getInt(stack, TAG_X_ORIGIN, 0);
        int y = ItemNBTHelper.getInt(stack, TAG_Y_ORIGIN, Integer.MIN_VALUE);
        int z = ItemNBTHelper.getInt(stack, TAG_Z_ORIGIN, 0);
        return new BlockPos(x, y, z);
    }

    private static void exitBindingMode(ItemStack stack) {
        setBindingCenter(stack, Bound.UNBOUND_POS);
    }

    private static void setBindingCenter(ItemStack stack, BlockPos pos) {
        ItemNBTHelper.setInt(stack, TAG_X_ORIGIN, pos.getX());
        ItemNBTHelper.setInt(stack, TAG_Y_ORIGIN, pos.getY());
        ItemNBTHelper.setInt(stack, TAG_Z_ORIGIN, pos.getZ());
    }

    private static List<BlockPos> getCursorList(ItemStack stack) {
        CompoundTag cmp = ItemNBTHelper.getCompound(stack, TAG_CURSOR_LIST, false);
        List<BlockPos> cursors = new ArrayList<>();

        int count = cmp.getInt(TAG_CURSOR_COUNT);
        for (int i = 0; i < count; i++) {
            CompoundTag cursorCmp = cmp.getCompound(TAG_CURSOR_PREFIX + i);
            int x = cursorCmp.getInt(TAG_X_OFFSET);
            int y = cursorCmp.getInt(TAG_Y_OFFSET);
            int z = cursorCmp.getInt(TAG_Z_OFFSET);
            cursors.add(new BlockPos(x, y, z));
        }

        return cursors;
    }

    private static void setCursorList(ItemStack stack, @Nullable List<BlockPos> cursors) {
        CompoundTag cmp = new CompoundTag();
        if (cursors != null) {
            int i = 0;
            for (BlockPos cursor : cursors) {
                CompoundTag cursorCmp = cursorToCmp(cursor);
                cmp.put(TAG_CURSOR_PREFIX + i, cursorCmp);
                i++;
            }
            cmp.putInt(TAG_CURSOR_COUNT, i);
        }

        ItemNBTHelper.setCompound(stack, TAG_CURSOR_LIST, cmp);
    }

    private static CompoundTag cursorToCmp(BlockPos pos) {
        CompoundTag cmp = new CompoundTag();
        cmp.putInt(TAG_X_OFFSET, pos.getX());
        cmp.putInt(TAG_Y_OFFSET, pos.getY());
        cmp.putInt(TAG_Z_OFFSET, pos.getZ());
        return cmp;
    }

    private static ItemStack getAesirRing(Player player)
    {
        return EquipmentHandler.findOrEmpty(ExtraBotaniaItems.aesirRing, player);
    }

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, prefix("challenge/loki_ring"));
    }
}