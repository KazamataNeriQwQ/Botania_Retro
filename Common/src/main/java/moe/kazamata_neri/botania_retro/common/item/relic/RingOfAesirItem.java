package moe.kazamata_neri.botania_retro.common.item.relic;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import moe.kazamata_neri.botania_retro.common.item.ExtraBotaniaItems;
import moe.kazamata_neri.botania_retro.common.lib.LibItemNames;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
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
import org.jetbrains.annotations.Unmodifiable;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.item.WireframeCoordinateListProvider;
import vazkii.botania.common.advancements.RelicBindTrigger;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.helper.DataComponentHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.relic.RelicBaubleItem;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.*;

import static moe.kazamata_neri.botania_retro.CommonInitializer.*;

public class RingOfAesirItem extends RelicBaubleItem implements WireframeCoordinateListProvider {

    public RingOfAesirItem(Item.Properties props) {
        super(props);
    }

    public static void OnDropped(ItemEntity itemEntity) {
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
                    GlobalPos blockPos = getBindingCenter(stack);
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
                            assert uuid != null;
                            relicRing.bindToUUID(uuid);
                            level.addFreshEntity(new ItemEntity(level, x, y -0.2f, z, ring));
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
            GlobalPos origin = getBindingCenter(stack);
            if (origin != null && origin.dimension() != player.level().dimension()) {
                // binding mode for different dimension
                return Collections.emptyList();
            }

            List<BlockPos> list = getCursorList(stack);
            List<BlockPos> result = new ArrayList<>(list.size());
            Vec3i offset = origin != null ? origin.pos() : ((BlockHitResult) lookPos).getBlockPos();
            for (BlockPos cursor : list) {
                result.add(cursor.offset(offset));
            }

            return result;
        }

        return ImmutableList.of();
    }

    @Nullable
    @Override
    public BlockPos getSourceWireframe(Player player, ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        if (getAesirRing(player) == stack) {
            GlobalPos currentBuildCenter = getBindingCenter(stack);
            if (currentBuildCenter != null && currentBuildCenter.dimension() == player.level().dimension()) {
                return currentBuildCenter.pos();
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
    public Multimap<Holder<Attribute>, AttributeModifier> getEquippedAttributeModifiers(ItemStack stack, ResourceLocation slotId) {
        Multimap<Holder<Attribute>, AttributeModifier> attributes = super.getEquippedAttributeModifiers(stack, slotId);
        attributes.put(Attributes.MAX_HEALTH,
                new AttributeModifier(slotId, 20, AttributeModifier.Operation.ADD_VALUE));
        return attributes;
    }

    @Nullable
    private static GlobalPos getBindingCenter(ItemStack stack) {
        return stack.get(BotaniaDataComponents.BINDING_POS);
    }

    private static void exitBindingMode(ItemStack stack) {
        stack.remove(BotaniaDataComponents.BINDING_POS);
    }

    private static void setBindingCenter(ItemStack stack, GlobalPos pos) {
        stack.set(BotaniaDataComponents.BINDING_POS, pos);
    }

    @Unmodifiable
    private static List<BlockPos> getCursorList(ItemStack stack) {
        return stack.getOrDefault(BotaniaDataComponents.LOKI_RING_OFFSET_LIST, Collections.emptyList());
    }

    private static void setCursorList(ItemStack stack, @Nullable List<BlockPos> cursors) {
        DataComponentHelper.setNonEmpty(stack, BotaniaDataComponents.LOKI_RING_OFFSET_LIST, cursors);
    }

    private static ItemStack getAesirRing(Player player) {
        return EquipmentHandler.findOrEmpty(ExtraBotaniaItems.aesirRing, player);
    }

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, RL("challenge/" + LibItemNames.AESIR_RING));
    }
}