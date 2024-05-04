package com.almostreliable.appelem.data;

import appeng.core.AppEng;
import appeng.items.storage.BasicStorageCell;
import appeng.items.tools.powered.PortableCellItem;
import com.almostreliable.appelem.AppElem;
import com.almostreliable.appelem.BuildConfig;
import com.almostreliable.appelem.core.AppElemBlocks;
import com.almostreliable.appelem.core.AppElemItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

class AppElemModels extends BlockStateProvider {

    AppElemModels(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BuildConfig.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithExistingModel(AppElemBlocks.NETWORK_CONTAINER.get());
        itemModels().basicItem(AppElemItems.ELEMENT_CELL_HOUSING.get());

        for (var cell : AppElemItems.getCells()) {
            cell(cell);
        }
        for (var portableCell : AppElemItems.getPortableCells()) {
            portableCell(portableCell);
        }
    }

    private void simpleBlockWithExistingModel(Block block) {
        ModelFile modelFile = models().getExistingFile(blockTexture(block));
        simpleBlockWithItem(block, modelFile);
    }

    private void cell(DeferredItem<BasicStorageCell> cell) {
        String path = cell.getId().getPath();
        String name = path.substring(path.lastIndexOf('_') + 1) + "_element_cell";
        models().singleTexture(name, AppEng.makeId("block/drive/drive_cell"), "cell", AppElem.id("block/" + name));
        itemModels().basicItem(cell.get()).texture("layer1", AppEng.makeId("item/storage_cell_led"));
    }

    private void portableCell(DeferredItem<PortableCellItem> portableCell) {
        String path = portableCell.getId().getPath();
        String tier = path.substring(path.lastIndexOf('_') + 1);
        itemModels().singleTexture(path, mcLoc("item/generated"), "layer0", AppEng.makeId("item/portable_cell_screen"))
            .texture("layer1", AppEng.makeId("item/portable_cell_led"))
            .texture("layer2", AppElem.id("item/portable_element_cell_housing"))
            .texture("layer3", AppEng.makeId("item/portable_cell_side_" + tier));
    }
}
