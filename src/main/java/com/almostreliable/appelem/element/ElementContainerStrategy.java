package com.almostreliable.appelem.element;

import appeng.api.behaviors.ContainerItemStrategy;
import appeng.api.config.Actionable;
import appeng.api.stacks.GenericStack;
import com.almostreliable.appelem.Utils;
import com.google.common.primitives.Ints;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.SoundActions;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.item.holder.AbstractElementHolderItem;

@SuppressWarnings({"UnstableApiUsage", "ClassEscapesDefinedScope"})
public class ElementContainerStrategy implements ContainerItemStrategy<ElementKey, ElementContainerStrategy.Context> {

    @Nullable
    @Override
    public GenericStack getContainedStack(ItemStack stack) {
        if (stack.isEmpty()) return null;

        IElementStorage elementStorage = stack.getCapability(ElementalCraftCapabilities.ElementStorage.ITEM);
        if (elementStorage == null) return null;

        for (ElementType elementType : ElementType.values()) {
            if (elementType == ElementType.NONE) continue;
            int canExtract = elementStorage.extractElement(Integer.MAX_VALUE, elementType, true);
            if (canExtract <= 0) continue;
            return new GenericStack(new ElementKey(elementType), canExtract);
        }

        return null;
    }

    @Nullable
    @Override
    public Context findCarriedContext(Player player, AbstractContainerMenu menu) {
        if (menu.getCarried().getCapability(ElementalCraftCapabilities.ElementStorage.ITEM) != null) {
            return new CarriedContext(player, menu);
        }

        return null;
    }

    @Override
    public @Nullable Context findPlayerSlotContext(Player player, int slot) {
        if (player.getInventory().getItem(slot).getCapability(ElementalCraftCapabilities.ElementStorage.ITEM) != null) {
            return new PlayerInvContext(player, slot);
        }

        return null;
    }

    @Override
    public long extract(Context context, ElementKey what, long amount, Actionable mode) {
        ItemStack stack = context.getStack();
        ItemStack copy = stack.copy();

        IElementStorage elementStorage = stack.getCapability(ElementalCraftCapabilities.ElementStorage.ITEM);
        if (elementStorage == null) return 0;

        int extracted = elementStorage.extractElement(Ints.saturatedCast(amount), (ElementType) what.getPrimaryKey(), mode.isSimulate());
        if (mode == Actionable.MODULATE) {
            stack.shrink(1);
            context.addOverflow(copy);
        }

        return extracted;
    }

    @Override
    public long insert(Context context, ElementKey what, long amount, Actionable mode) {
        ItemStack stack = context.getStack();
        ItemStack copy = stack.copy();

        IElementStorage elementStorage = stack.getCapability(ElementalCraftCapabilities.ElementStorage.ITEM);
        if (elementStorage == null) return 0;

        int inserted = Ints.saturatedCast(amount) -
            elementStorage.insertElement(Ints.saturatedCast(amount), (ElementType) what.getPrimaryKey(), mode.isSimulate());

        if (mode == Actionable.MODULATE) {
            stack.shrink(1);
            context.addOverflow(copy);
        }

        return inserted;
    }

    @Override
    public void playFillSound(Player player, ElementKey what) {
        SoundEvent fillSound = Fluids.WATER.getFluidType().getSound(player, SoundActions.BUCKET_FILL);
        if (fillSound == null) return;
        player.playNotifySound(fillSound, SoundSource.PLAYERS, 1, 1);
    }

    @Override
    public void playEmptySound(Player player, ElementKey what) {
        SoundEvent emptySound = Fluids.WATER.getFluidType().getSound(player, SoundActions.BUCKET_EMPTY);
        if (emptySound == null) return;
        player.playNotifySound(emptySound, SoundSource.PLAYERS, 1, 1);
    }

    @Nullable
    @Override
    public GenericStack getExtractableContent(Context context) {
        return getContainedStack(context.getStack());
    }

    interface Context {

        ItemStack getStack();

        void addOverflow(ItemStack stack);
    }

    private record CarriedContext(Player player, AbstractContainerMenu menu) implements Context {

        @Override
        public ItemStack getStack() {
            return menu.getCarried();
        }

        @Override
        public void addOverflow(ItemStack stack) {
            if (menu.getCarried().isEmpty()) {
                menu.setCarried(stack);
            } else {
                player.getInventory().placeItemBackInInventory(stack);
            }
        }
    }

    private record PlayerInvContext(Player player, int slot) implements Context {

        @Override
        public ItemStack getStack() {
            return player.getInventory().getItem(slot);
        }

        @Override
        public void addOverflow(ItemStack stack) {
            player.getInventory().placeItemBackInInventory(stack);
        }
    }
}
