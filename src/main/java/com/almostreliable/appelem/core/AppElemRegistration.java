package com.almostreliable.appelem.core;

import appeng.api.behaviors.ContainerItemStrategy;
import appeng.api.client.AEKeyRendering;
import appeng.api.client.StorageCellModels;
import appeng.api.stacks.AEKeyTypes;
import appeng.api.storage.StorageCells;
import com.almostreliable.appelem.AppElem;
import com.almostreliable.appelem.element.ElementCellGuiHandler;
import com.almostreliable.appelem.element.ElementContainerStrategy;
import com.almostreliable.appelem.element.ElementKey;
import com.almostreliable.appelem.element.ElementKeyType;
import com.almostreliable.appelem.element.ElementRenderer;
import com.almostreliable.appelem.network.PacketHandler;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class AppElemRegistration {

    private AppElemRegistration() {}

    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(AppElemRegistration::onRegisterEvent);
        modEventBus.addListener(AppElemRegistration::registerCapabilities);
        modEventBus.addListener(AppElemTab::registerContents);
        modEventBus.addListener(PacketHandler::registerPackets);

//        AppElemBlocks.REGISTRY.register(modEventBus);
        AppElemItems.REGISTRY.register(modEventBus);

        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(AppElemClientRegistration::onClientSetup);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private static void onRegisterEvent(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.CREATIVE_MODE_TAB) {
            AppElemTab.register(event);
        }
        if (event.getRegistryKey() == Registries.ITEM) {
            AEKeyTypes.register(ElementKeyType.INSTANCE);
            ContainerItemStrategy.register(ElementKeyType.INSTANCE, ElementKey.class, new ElementContainerStrategy());
        }
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
    }

    private static class AppElemClientRegistration {

        private static void onClientSetup(FMLClientSetupEvent event) {
            AEKeyRendering.register(ElementKeyType.INSTANCE, ElementKey.class, new ElementRenderer());

            StorageCells.addCellGuiHandler(new ElementCellGuiHandler());

            StorageCellModels.registerModel(AppElemItems.ELEMENT_CELL_1K, AppElem.id("cells/1k_element_cell"));
            StorageCellModels.registerModel(AppElemItems.ELEMENT_CELL_4K, AppElem.id("cells/4k_element_cell"));
            StorageCellModels.registerModel(AppElemItems.ELEMENT_CELL_16K, AppElem.id("cells/16k_element_cell"));
            StorageCellModels.registerModel(AppElemItems.ELEMENT_CELL_64K, AppElem.id("cells/64k_element_cell"));
            StorageCellModels.registerModel(AppElemItems.ELEMENT_CELL_256K, AppElem.id("cells/256k_element_cell"));

            StorageCellModels.registerModel(AppElemItems.PORTABLE_ELEMENT_CELL_1K, AppElem.id("cells/1k_element_cell"));
            StorageCellModels.registerModel(AppElemItems.PORTABLE_ELEMENT_CELL_4K, AppElem.id("cells/4k_element_cell"));
            StorageCellModels.registerModel(AppElemItems.PORTABLE_ELEMENT_CELL_16K, AppElem.id("cells/16k_element_cell"));
            StorageCellModels.registerModel(AppElemItems.PORTABLE_ELEMENT_CELL_64K, AppElem.id("cells/64k_element_cell"));
            StorageCellModels.registerModel(AppElemItems.PORTABLE_ELEMENT_CELL_256K, AppElem.id("cells/256k_element_cell"));
        }
    }
}
