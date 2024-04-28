package com.almostreliable.appelem.data;

import appeng.core.AppEng;
import appeng.items.storage.BasicStorageCell;
import appeng.items.tools.powered.PortableCellItem;
import com.almostreliable.appelem.AppElem;
import com.almostreliable.appelem.BuildConfig;
import com.almostreliable.appelem.core.AppElemItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class AppElemItemModels extends ItemModelProvider {

    AppElemItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BuildConfig.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(AppElemItems.ELEMENT_CELL_HOUSING);

        for (var cell : AppElemItems.getCells()) {
            cell(cell);
        }
        for (var portableCell : AppElemItems.getPortableCells()) {
            portableCell(portableCell);
        }
    }

    private ItemModelBuilder singleTexture(DeferredItem<? extends Item> item, String prefix) {
        String path = item.getId().getPath();
        return singleTexture(path, mcLoc("item/generated"), "layer0", AppElem.id(prefix + "/" + path));
    }

    private void simpleItem(DeferredItem<? extends Item> item) {
        singleTexture(item, "item");
    }

    private void cell(DeferredItem<BasicStorageCell> cell) {
        singleTexture(cell, "item").texture("layer1", AppEng.makeId("item/storage_cell_led"));
    }

    private void portableCell(DeferredItem<PortableCellItem> portableCell) {
        String path = portableCell.getId().getPath();
        String tier = path.substring(path.lastIndexOf('_') + 1);
        singleTexture(path, mcLoc("item/generated"), "layer0", AppEng.makeId("item/portable_cell_screen"))
            .texture("layer1", AppEng.makeId("item/portable_cell_led"))
            .texture("layer2", AppElem.id("item/portable_element_cell_housing"))
            .texture("layer3", AppEng.makeId("item/portable_cell_side_" + tier));
    }
}
