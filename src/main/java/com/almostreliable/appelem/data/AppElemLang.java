package com.almostreliable.appelem.data;

import com.almostreliable.appelem.BuildConfig;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.neoforge.common.data.LanguageProvider;
import sirttas.elementalcraft.api.element.ElementType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
public final class AppElemLang extends LanguageProvider {

    public static final Map<String, LangEntry> ELEMENT_NAMES = new HashMap<>();
    public static final LangEntry ELEMENTAL_DESCRIPTION = LangEntry.of("label", "elements", "Elements");

    static {
        for (ElementType elementType : ElementType.values()) {
            if (elementType == ElementType.NONE) continue;
            String serializedName = elementType.getSerializedName();
            String uppercaseName = serializedName.substring(0, 1).toUpperCase() + serializedName.substring(1);
            ELEMENT_NAMES.put(
                elementType.getSerializedName(),
                LangEntry.of("element", serializedName, uppercaseName + " Element")
            );
        }
    }

    AppElemLang(PackOutput output) {
        super(output, BuildConfig.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        for (LangEntry entry : LangEntry.ENTRIES) {
            add(entry.key, entry.value);
        }
    }

    public record LangEntry(String key, String value) implements Supplier<MutableComponent> {

        private static final Set<LangEntry> ENTRIES = new HashSet<>();

        public static LangEntry of(String prefix, String id, String value) {
            LangEntry entry = new LangEntry(String.format("%s.%s.%s", prefix, BuildConfig.MOD_ID, id), value);
            ENTRIES.add(entry);
            return entry;
        }

        @Override
        public MutableComponent get() {
            return Component.translatable(key, value);
        }
    }
}
