package com.almostreliable.appelem.content;

import appeng.items.storage.BasicStorageCell;
import appeng.items.storage.StorageTier;
import com.almostreliable.appelem.core.AppElemItems;
import com.almostreliable.appelem.element.ElementKeyType;
import net.minecraft.world.level.ItemLike;

public class ElementStorageCell extends BasicStorageCell {

    private final StorageTier tier;

    public ElementStorageCell(Properties properties, StorageTier tier) {
        super(
            properties,
            tier.componentSupplier().get(),
            AppElemItems.ELEMENT_CELL_HOUSING,
            tier.idleDrain(),
            tier.bytes() / 1_024,
            tier.bytes() / 128,
            4,
            ElementKeyType.INSTANCE
        );

        this.tier = tier;
    }

    public ItemLike getCoreItem() {
        return coreItem;
    }

    public ItemLike getHousingItem() {
        return housingItem;
    }

    public StorageTier getTier() {
        return tier;
    }
}
