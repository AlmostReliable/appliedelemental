package com.almostreliable.appelem.element;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import com.almostreliable.appelem.data.AppElemLang;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;

import java.util.List;

public class ElementKey extends AEKey {

    private final ElementType elementType;

    public ElementKey(ElementType elementType) {
        this.elementType = elementType;
    }

    @Override
    public AEKeyType getType() {
        return ElementKeyType.INSTANCE;
    }

    @Override
    public AEKey dropSecondary() {
        return this;
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString(ECNames.ELEMENT_TYPE, elementType.getSerializedName());
        return tag;
    }

    @Override
    public Object getPrimaryKey() {
        return elementType;
    }

    @Override
    public ResourceLocation getId() {
        // use the ElementalCraft id here, so players know where these keys are from
        return ElementalCraftApi.createRL(elementType.getSerializedName());
    }

    @Override
    public void writeToPacket(FriendlyByteBuf data) {
        data.writeUtf(elementType.getSerializedName());
    }

    @Override
    public Component computeDisplayName() {
        return AppElemLang.ELEMENT_NAMES.get(elementType.getSerializedName()).get();
    }

    @Override
    public void addDrops(long amount, List<ItemStack> drops, Level level, BlockPos pos) {
        // nothing -> voided
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return elementType == ((ElementKey) o).elementType;
    }

    @Override
    public int hashCode() {
        return elementType.hashCode();
    }
}
