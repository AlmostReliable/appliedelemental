package com.almostreliable.appelem.data;

import appeng.core.AppEng;
import appeng.items.storage.BasicStorageCell;
import appeng.items.tools.powered.PortableCellItem;
import com.almostreliable.appelem.AppElem;
import com.almostreliable.appelem.BuildConfig;
import com.almostreliable.appelem.core.AppElemItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class AppElemBlockModels extends BlockModelProvider {

    AppElemBlockModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BuildConfig.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (var cell : AppElemItems.getCells()) {
            cell(cell);
        }
    }

    private void cell(DeferredItem<BasicStorageCell> cell) {
        String path = cell.getId().getPath();
        String name = path.substring(path.lastIndexOf('_') + 1) + "_element_cell";
        singleTexture(name, AppEng.makeId("block/drive/drive_cell"), "cell", AppElem.id("block/" + name));
    }
}
