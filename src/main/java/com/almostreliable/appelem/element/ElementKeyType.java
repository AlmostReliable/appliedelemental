package com.almostreliable.appelem.element;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import com.almostreliable.appelem.AppElem;
import com.almostreliable.appelem.core.AppElemConfig;
import com.almostreliable.appelem.data.AppElemLang;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;

public final class ElementKeyType extends AEKeyType {

    public static final ElementKeyType INSTANCE = new ElementKeyType();

    private ElementKeyType() {
        super(AppElem.id("element"), ElementKey.class, AppElemLang.ELEMENTAL_DESCRIPTION.get());
    }

    @Override
    public int getAmountPerOperation() {
        return AppElemConfig.COMMON.amountPerOperation.get();
    }

    @Override
    public int getAmountPerByte() {
        return AppElemConfig.COMMON.amountPerByte.get();
    }

    @Override
    public AEKey readFromPacket(FriendlyByteBuf input) {
        ElementType elementType = ElementType.byName(input.readUtf());
        return new ElementKey(elementType);
    }

    @Override
    public AEKey loadKeyFromTag(CompoundTag tag) {
        ElementType elementType = ElementType.byName(tag.getString(ECNames.ELEMENT_TYPE));
        return new ElementKey(elementType);
    }
}
