package com.almostreliable.appelem.element;

import appeng.api.implementations.blockentities.IChestOrDrive;
import appeng.api.storage.cells.IBasicCellItem;
import appeng.api.storage.cells.ICellGuiHandler;
import appeng.api.storage.cells.ICellHandler;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import appeng.menu.me.items.BasicCellChestMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ElementCellGuiHandler implements ICellGuiHandler {

    @Override
    public boolean isSpecializedFor(ItemStack cell) {
        return cell.getItem() instanceof IBasicCellItem basicCellItem
            && basicCellItem.getKeyType() == ElementKeyType.INSTANCE;
    }

    @Override
    public void openChestGui(Player player, IChestOrDrive chest, ICellHandler cellHandler, ItemStack cell) {
        MenuOpener.open(BasicCellChestMenu.TYPE, player, MenuLocators.forBlockEntity((BlockEntity) chest));
    }
}
