package com.almostreliable.appelem.core;

import appeng.api.parts.IPartItem;
import appeng.api.parts.PartModels;
import appeng.items.materials.MaterialItem;
import appeng.items.parts.PartItem;
import appeng.items.parts.PartModelsHelper;
import appeng.items.storage.StorageTier;
import appeng.items.tools.powered.PortableCellItem;
import appeng.menu.me.common.MEStorageMenu;
import appeng.parts.p2p.CapabilityP2PTunnelPart;
import com.almostreliable.appelem.BuildConfig;
import com.almostreliable.appelem.content.ElementP2PTunnelPart;
import com.almostreliable.appelem.content.ElementStorageCell;
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
    private static final List<DeferredItem<ElementStorageCell>> CELLS = new ArrayList<>();
    private static final List<DeferredItem<PortableCellItem>> PORTABLE_CELLS = new ArrayList<>();

    // @formatter:off

    // housing
    public static final DeferredItem<MaterialItem> ELEMENT_CELL_HOUSING = REGISTRY.register("element_cell_housing", "ME Element Cell Housing", MaterialItem::new);

    // cells
    public static final DeferredItem<ElementStorageCell> ELEMENT_CELL_1K = REGISTRY.registerCell("element_storage_cell_1k", "1k ME Element Storage Cell", p -> new ElementStorageCell(p.stacksTo(1), StorageTier.SIZE_1K));
    public static final DeferredItem<ElementStorageCell> ELEMENT_CELL_4K = REGISTRY.registerCell("element_storage_cell_4k", "4k ME Element Storage Cell", p -> new ElementStorageCell(p.stacksTo(1), StorageTier.SIZE_4K));
    public static final DeferredItem<ElementStorageCell> ELEMENT_CELL_16K = REGISTRY.registerCell("element_storage_cell_16k", "16k ME Element Storage Cell", p -> new ElementStorageCell(p.stacksTo(1), StorageTier.SIZE_16K));
    public static final DeferredItem<ElementStorageCell> ELEMENT_CELL_64K = REGISTRY.registerCell("element_storage_cell_64k", "64k ME Element Storage Cell", p -> new ElementStorageCell(p.stacksTo(1), StorageTier.SIZE_64K));
    public static final DeferredItem<ElementStorageCell> ELEMENT_CELL_256K = REGISTRY.registerCell("element_storage_cell_256k", "256k ME Element Storage Cell", p -> new ElementStorageCell(p.stacksTo(1), StorageTier.SIZE_256K));

    // portable cells
    public static final DeferredItem<PortableCellItem> PORTABLE_ELEMENT_CELL_1K = REGISTRY.registerPortableCell("portable_element_cell_1k", "1k Portable Element Cell", p -> new PortableCellItem(ElementKeyType.INSTANCE, 4, MEStorageMenu.PORTABLE_FLUID_CELL_TYPE, StorageTier.SIZE_1K, p.stacksTo(1), 0xFFE5_FF36));
    public static final DeferredItem<PortableCellItem> PORTABLE_ELEMENT_CELL_4K = REGISTRY.registerPortableCell("portable_element_cell_4k", "4k Portable Element Cell", p -> new PortableCellItem(ElementKeyType.INSTANCE, 4, MEStorageMenu.PORTABLE_FLUID_CELL_TYPE, StorageTier.SIZE_4K, p.stacksTo(1), 0xFFE5_FF36));
    public static final DeferredItem<PortableCellItem> PORTABLE_ELEMENT_CELL_16K = REGISTRY.registerPortableCell("portable_element_cell_16k", "16k Portable Element Cell", p -> new PortableCellItem(ElementKeyType.INSTANCE, 4, MEStorageMenu.PORTABLE_FLUID_CELL_TYPE, StorageTier.SIZE_16K, p.stacksTo(1), 0xFFE5_FF36));
    public static final DeferredItem<PortableCellItem> PORTABLE_ELEMENT_CELL_64K = REGISTRY.registerPortableCell("portable_element_cell_64k", "64k Portable Element Cell", p -> new PortableCellItem(ElementKeyType.INSTANCE, 4, MEStorageMenu.PORTABLE_FLUID_CELL_TYPE, StorageTier.SIZE_64K, p.stacksTo(1), 0xFFE5_FF36));
    public static final DeferredItem<PortableCellItem> PORTABLE_ELEMENT_CELL_256K = REGISTRY.registerPortableCell("portable_element_cell_256k", "256k Portable Element Cell", p -> new PortableCellItem(ElementKeyType.INSTANCE, 4, MEStorageMenu.PORTABLE_FLUID_CELL_TYPE, StorageTier.SIZE_256K, p.stacksTo(1), 0xFFE5_FF36));

    // p2p
    public static final DeferredItem<PartItem<ElementP2PTunnelPart>> ELEMENT_P2P_TUNNEL = REGISTRY.registerP2P("element_p2p_tunnel", "Element P2P Tunnel", ElementP2PTunnelPart.class, ElementP2PTunnelPart::new);

    // @formatter:on

    private AppElemItems() {}

    public static List<DeferredItem<ElementStorageCell>> getCells() {
        return Collections.unmodifiableList(CELLS);
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

        private DeferredItem<ElementStorageCell> registerCell(
            String id, String name, Function<Item.Properties, ElementStorageCell> factory
        ) {
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

        private <T extends CapabilityP2PTunnelPart<T, ?>> DeferredItem<PartItem<T>> registerP2P(
            String id, String name, Class<T> clazz, Function<IPartItem<T>, T> factory
        ) {
            PartModels.registerModels(PartModelsHelper.createModels(clazz));
            return register(id, name, props -> new PartItem<>(props, clazz, factory));
        }

        void register(IEventBus modEventBus) {
            registry.register(modEventBus);
        }
    }
}
