package com.almostreliable.appelem;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

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
}
