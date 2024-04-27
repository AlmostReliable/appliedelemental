package com.almostreliable.appelem;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.item.holder.AbstractElementHolderItem;

import java.util.List;

public final class Utils {

    private Utils() {}

    public static String getNameOrFormatId(String id, @Nullable String name) {
        if (name == null) {
            String[] parts = id.split("_");
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
            }
            return String.join(" ", parts);
        }
        return name;
    }

    public static MutableComponent translate(String type, String key, Object... args) {
        return Component.translatable(String.format("%s.%s.%s", type, BuildConfig.MOD_ID, key), args);
    }

    @Nullable
    public static IElementStorage getItemElementStorage(ItemStack stack) {
        if (stack.getItem() instanceof AbstractElementHolderItem elementHolderItem) {
            return elementHolderItem.getElementStorage(stack);
        }

        return null;
    }

    public static String translateAsString(String type, String key) {
        return translate(type, key).getString();
    }

    public static void addShiftInfoTooltip(List<Component> tooltip) {
        tooltip.add(Component.literal("» ").withStyle(ChatFormatting.AQUA).append(translate(
            "tooltip",
            "shift_for_more",
            InputConstants.getKey("key.keyboard.left.shift").getDisplayName()
        ).withStyle(ChatFormatting.GRAY)));
    }

    public static <T> T cast(Object o, Class<T> clazz) {
        return clazz.cast(o);
    }
}
