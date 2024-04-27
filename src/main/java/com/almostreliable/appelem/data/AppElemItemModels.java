package com.almostreliable.appelem.data;

import appeng.core.AppEng;
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
//        singleTexture(AppElemItems.ELEMENT_CELL_HOUSING);

        for (var cell : AppElemItems.getCells()) {
            singleTexture(cell).texture("layer1", AppEng.makeId("item/storage_cell_led"));
        }
        for (var cell : AppElemItems.getPortableCells()) {
            singleTexture(cell).texture("layer1", AppEng.makeId("item/portable_cell_led"));
        }
    }

    private ItemModelBuilder singleTexture(DeferredItem<? extends Item> item) {
        String path = item.getId().getPath();
        return singleTexture(path, mcLoc("item/generated"), "layer0", AppElem.id("item/" + path));
    }
}
