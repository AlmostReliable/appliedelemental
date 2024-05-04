package com.almostreliable.appelem.core;

import appeng.core.definitions.AEItems;
import appeng.items.materials.MaterialItem;
import appeng.items.storage.BasicStorageCell;
import appeng.items.storage.StorageTier;
import appeng.items.tools.powered.PortableCellItem;
import appeng.menu.me.common.MEStorageMenu;
import com.almostreliable.appelem.BuildConfig;
import com.almostreliable.appelem.data.AppElemLang;
import com.almostreliable.appelem.element.ElementKeyType;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public final class AppElemItems {

    static final DeferredItemRegister REGISTRY = new DeferredItemRegister();
    private static final List<DeferredItem<BasicStorageCell>> CELLS = new ArrayList<>();
    private static final List<DeferredItem<PortableCellItem>> PORTABLE_CELLS = new ArrayList<>();

    // @formatter:off

    // housing
    public static final DeferredItem<MaterialItem> ELEMENT_CELL_HOUSING = REGISTRY.register("element_cell_housing", "ME Element Cell Housing", MaterialItem::new);

    // cells
    public static final DeferredItem<BasicStorageCell> ELEMENT_CELL_1K = REGISTRY.registerCell("element_storage_cell_1k", "1k ME Element Storage Cell", p -> new BasicStorageCell(p.stacksTo(1), AEItems.CELL_COMPONENT_1K, ELEMENT_CELL_HOUSING, .5, 1, 8, 4, ElementKeyType.INSTANCE));
    public static final DeferredItem<BasicStorageCell> ELEMENT_CELL_4K = REGISTRY.registerCell("element_storage_cell_4k", "4k ME Element Storage Cell", p -> new BasicStorageCell(p.stacksTo(1), AEItems.CELL_COMPONENT_4K, ELEMENT_CELL_HOUSING, 1, 4, 32, 4, ElementKeyType.INSTANCE));
    public static final DeferredItem<BasicStorageCell> ELEMENT_CELL_16K = REGISTRY.registerCell("element_storage_cell_16k", "16k ME Element Storage Cell", p -> new BasicStorageCell(p.stacksTo(1), AEItems.CELL_COMPONENT_16K, ELEMENT_CELL_HOUSING, 1.5, 16, 128, 4, ElementKeyType.INSTANCE));
    public static final DeferredItem<BasicStorageCell> ELEMENT_CELL_64K = REGISTRY.registerCell("element_storage_cell_64k", "64k ME Element Storage Cell", p -> new BasicStorageCell(p.stacksTo(1), AEItems.CELL_COMPONENT_64K, ELEMENT_CELL_HOUSING, 2.0, 64, 512, 4, ElementKeyType.INSTANCE));
    public static final DeferredItem<BasicStorageCell> ELEMENT_CELL_256K = REGISTRY.registerCell("element_storage_cell_256k", "256k ME Element Storage Cell", p -> new BasicStorageCell(p.stacksTo(1), AEItems.CELL_COMPONENT_256K, ELEMENT_CELL_HOUSING, 2.5, 256, 2_048, 4, ElementKeyType.INSTANCE));

    // portable cells
    public static final DeferredItem<PortableCellItem> PORTABLE_ELEMENT_CELL_1K = REGISTRY.registerPortableCell("portable_element_cell_1k", "1k Portable Element Cell", p -> new PortableCellItem(ElementKeyType.INSTANCE, 4, MEStorageMenu.PORTABLE_FLUID_CELL_TYPE, StorageTier.SIZE_1K, p.stacksTo(1), 0xFFE5_FF36));
    public static final DeferredItem<PortableCellItem> PORTABLE_ELEMENT_CELL_4K = REGISTRY.registerPortableCell("portable_element_cell_4k", "4k Portable Element Cell", p -> new PortableCellItem(ElementKeyType.INSTANCE, 4, MEStorageMenu.PORTABLE_FLUID_CELL_TYPE, StorageTier.SIZE_4K, p.stacksTo(1), 0xFFE5_FF36));
    public static final DeferredItem<PortableCellItem> PORTABLE_ELEMENT_CELL_16K = REGISTRY.registerPortableCell("portable_element_cell_16k", "16k Portable Element Cell", p -> new PortableCellItem(ElementKeyType.INSTANCE, 4, MEStorageMenu.PORTABLE_FLUID_CELL_TYPE, StorageTier.SIZE_16K, p.stacksTo(1), 0xFFE5_FF36));
    public static final DeferredItem<PortableCellItem> PORTABLE_ELEMENT_CELL_64K = REGISTRY.registerPortableCell("portable_element_cell_64k", "64k Portable Element Cell", p -> new PortableCellItem(ElementKeyType.INSTANCE, 4, MEStorageMenu.PORTABLE_FLUID_CELL_TYPE, StorageTier.SIZE_64K, p.stacksTo(1), 0xFFE5_FF36));
    public static final DeferredItem<PortableCellItem> PORTABLE_ELEMENT_CELL_256K = REGISTRY.registerPortableCell("portable_element_cell_256k", "256k Portable Element Cell", p -> new PortableCellItem(ElementKeyType.INSTANCE, 4, MEStorageMenu.PORTABLE_FLUID_CELL_TYPE, StorageTier.SIZE_256K, p.stacksTo(1), 0xFFE5_FF36));

    // @formatter:on

    private AppElemItems() {}

    public static Collection<DeferredItem<BasicStorageCell>> getCells() {
        return Collections.unmodifiableCollection(CELLS);
    }

    public static Collection<DeferredItem<PortableCellItem>> getPortableCells() {
        return Collections.unmodifiableCollection(PORTABLE_CELLS);
    }

    static final class DeferredItemRegister {

        private final DeferredRegister.Items registry = DeferredRegister.createItems(BuildConfig.MOD_ID);

        private DeferredItemRegister() {}

        <T extends Item> DeferredItem<T> register(String id, @Nullable String name, Function<Item.Properties, T> factory) {
            DeferredItem<T> deferredItem = registry.register(id, () -> factory.apply(new Item.Properties()));
            AppElemLang.LangEntry.of("item", id, AppElemRegistration.getNameOrFormatId(id, name));
            AppElemTab.add(deferredItem);
            return deferredItem;
        }

        private DeferredItem<BasicStorageCell> registerCell(String id, String name, Function<Item.Properties, BasicStorageCell> factory) {
            var deferredItem = register(id, name, factory);
            CELLS.add(deferredItem);
            return deferredItem;
        }

        private DeferredItem<PortableCellItem> registerPortableCell(
            String id, String name, Function<Item.Properties, PortableCellItem> factory
        ) {
            var deferredItem = register(id, name, factory);
            PORTABLE_CELLS.add(deferredItem);
            return deferredItem;
        }

        void register(IEventBus modEventBus) {
            registry.register(modEventBus);
        }
    }
}
