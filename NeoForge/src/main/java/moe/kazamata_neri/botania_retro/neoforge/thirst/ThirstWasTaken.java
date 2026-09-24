package moe.kazamata_neri.botania_retro.neoforge.thirst;

import moe.kazamata_neri.botania_retro.api.ThirstLike;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public final class ThirstWasTaken extends ThirstLike {
    private static final String THIRST_CLASS = "dev.ghen.thirst.Thirst";
    private static final String MOD_ATTACHMENT_CLASS = "dev.ghen.thirst.foundation.common.capability.ModAttachment";
    private static final String PLAYER_THIRST_FIELD = "PLAYER_THIRST";

    public static final String MOD_ID = "thirst";

    public static boolean isAvailable() {
        try {
            Class.forName(THIRST_CLASS);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean canDrink(Player player) {
        Object thirst = getThirst(player);
        if (thirst == null) {
            return false;
        }
        return (int) invoke(thirst, "getThirst") < 20;
    }

    @Override
    public void drink(Player player) {
        Object thirst = getThirst(player);
        if (thirst == null) {
            return;
        }
        invoke(thirst, "drink", 2, 2);
        float exhaustion = (float) invoke(thirst, "getExhaustion");
        if (exhaustion >= 1.6F) {
            invoke(thirst, "setExhaustion", exhaustion - 1.6F);
        } else {
            invoke(thirst, "drink", 0, 1);
            invoke(thirst, "setExhaustion", exhaustion + 2.4F);
        }
    }

    private static Object getThirst(Player player) {
        try {
            Class<?> modAttachmentClass = Class.forName(MOD_ATTACHMENT_CLASS);
            Field field = modAttachmentClass.getField(PLAYER_THIRST_FIELD);
            Supplier<? extends AttachmentType<?>> supplier = (Supplier<? extends AttachmentType<?>>) field.get(null);
            return player.getData(supplier.get());
        } catch (Exception e) {
            return null;
        }
    }

    private static Object invoke(Object target, String name, Object... args) {
        try {
            Class<?>[] paramTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof Integer) {
                    paramTypes[i] = int.class;
                } else if (arg instanceof Float) {
                    paramTypes[i] = float.class;
                }
            }
            Method method = target.getClass().getMethod(name, paramTypes);
            return method.invoke(target, args);
        } catch (Exception e) {
            return null;
        }
    }
}