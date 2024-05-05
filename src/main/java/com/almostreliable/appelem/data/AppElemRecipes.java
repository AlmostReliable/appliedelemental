package com.almostreliable.appelem.data;

import appeng.core.definitions.AEBlocks;
import appeng.datagen.providers.tags.ConventionTags;
import appeng.recipes.game.StorageCellUpgradeRecipe;
import com.almostreliable.appelem.AppElem;
import com.almostreliable.appelem.content.ElementStorageCell;
import com.almostreliable.appelem.core.AppElemItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.tag.ECTags;

class AppElemRecipes extends RecipeProvider {

    AppElemRecipes(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        // element housing
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AppElemItems.ELEMENT_CELL_HOUSING)
            .pattern("aba")
            .pattern("b b")
            .pattern("ded")
            .define('a', AEBlocks.QUARTZ_GLASS)
            .define('b', ConventionTags.REDSTONE)
            .define('d', ECTags.Items.POWERFUL_SHARDS)
            .define('e', ECItems.CONTAINED_CRYSTAL.get())
            .unlockedBy("has_dusts/redstone", has(ConventionTags.REDSTONE))
            .save(recipeOutput, AppElem.id("network/cells/element_cell_housing"));

        // element cells
        var cells = AppElemItems.getCells();
        for (int i = 0; i < cells.size(); i++) {
            var cellSupplier = cells.get(i);
            ElementStorageCell cell = cellSupplier.get();
            ItemLike cellCore = cell.getCoreItem();
            ItemLike cellHousing = cell.getHousingItem();
            String tierName = cell.getTier().namePrefix();

            // all in one
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, cell)
                .pattern("aba")
                .pattern("bcb")
                .pattern("ded")
                .define('a', AEBlocks.QUARTZ_GLASS)
                .define('b', ConventionTags.REDSTONE)
                .define('c', cellCore)
                .define('d', ECTags.Items.POWERFUL_SHARDS)
                .define('e', ECItems.CONTAINED_CRYSTAL.get())
                .unlockedBy("has_cell_component_" + tierName, has(cellCore))
                .save(recipeOutput, AppElem.id("network/cells/element_storage_cell_" + tierName));

            // housing + core
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, cell)
                .requires(cellHousing)
                .requires(cellCore)
                .unlockedBy("has_cell_component_" + tierName, has(cellCore))
                .unlockedBy("has_item_cell_housing", has(cellHousing))
                .save(recipeOutput, AppElem.id("network/cells/element_storage_cell_" + tierName + "_storage"));

            // upgrade recipes
            var cellId = cellSupplier.getId();
            for (int j = i + 1; j < cells.size(); j++) {
                var upgradeCellSupplier = cells.get(j);
                ElementStorageCell upgradeCell = upgradeCellSupplier.get();
                Item upgradeCellCore = upgradeCell.getCoreItem().asItem();
                String upgradeTierName = upgradeCell.getTier().namePrefix();

                var recipeId = cellId.withPath(path -> "upgrade/" + path + "_to_" + upgradeTierName);

                recipeOutput.accept(
                    recipeId,
                    new StorageCellUpgradeRecipe(cell, upgradeCellCore, upgradeCell, cellCore.asItem()),
                    null
                );
            }
        }
    }
}
