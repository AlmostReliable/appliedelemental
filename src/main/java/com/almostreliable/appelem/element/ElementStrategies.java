package com.almostreliable.appelem.element;

import appeng.api.behaviors.ContainerItemStrategy;
import appeng.api.behaviors.ExternalStorageStrategy;
import appeng.api.behaviors.StackExportStrategy;
import appeng.api.behaviors.StackImportStrategy;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.me.storage.ExternalStorageFacade;
import appeng.parts.automation.ForgeExternalStorageStrategy;
import appeng.parts.automation.HandlerStrategy;
import appeng.parts.automation.StorageExportStrategy;
import appeng.parts.automation.StorageImportStrategy;
import com.google.common.primitives.Ints;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.common.SoundActions;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public final class ElementStrategies {

    private ElementStrategies() {}

    public static void register() {
        ContainerItemStrategy.register(ElementKeyType.INSTANCE, ElementKey.class, ElementContainerStrategy.INSTANCE);
        ExternalStorageStrategy.register(ElementKeyType.INSTANCE, ElementStrategies::createExternalStorageStrategy);
        StackExportStrategy.register(ElementKeyType.INSTANCE, ElementStrategies::createStackExportStrategy);
        StackImportStrategy.register(ElementKeyType.INSTANCE, ElementStrategies::createStackImportStrategy);
    }

    private static ExternalStorageStrategy createExternalStorageStrategy(ServerLevel level, BlockPos pos, Direction fromSide) {
        return new ForgeExternalStorageStrategy<>(
            ElementalCraftCapabilities.ElementStorage.BLOCK,
            ELEMENT_HANDLER,
            level,
            pos,
            fromSide
        );
    }

    private static StackExportStrategy createStackExportStrategy(ServerLevel level, BlockPos pos, Direction fromSide) {
        return new ElementStorageExportStrategy(
            ElementalCraftCapabilities.ElementStorage.BLOCK,
            ELEMENT_HANDLER,
            level,
            pos,
            fromSide
        );
    }

    private static StackImportStrategy createStackImportStrategy(ServerLevel level, BlockPos pos, Direction fromSide) {
        return new StorageImportStrategy<>(
            ElementalCraftCapabilities.ElementStorage.BLOCK,
            ELEMENT_HANDLER,
            level,
            pos,
            fromSide
        );
    }

    private static final HandlerStrategy<IElementStorage, ElementStack> ELEMENT_HANDLER = new HandlerStrategy<>(ElementKeyType.INSTANCE) {

        @Override
        public boolean isSupported(AEKey what) {
            return what instanceof ElementKey;
        }

        @Override
        public ExternalStorageFacade getFacade(IElementStorage handler) {
            return new ElementHandlerFacade(handler);
        }

        @Nullable
        @Override
        public ElementStack getStack(AEKey what, long amount) {
            if (what instanceof ElementKey) {
                ElementType elementType = (ElementType) what.getPrimaryKey();
                return elementType == ElementType.NONE ? null : new ElementStack(elementType, Ints.saturatedCast(amount));
            }

            return null;
        }

        @Override
        public long insert(IElementStorage handler, AEKey what, long amount, Actionable mode) {
            if (what instanceof ElementKey && amount > 0) {
                ElementStack stack = ElementStack.of(handler);
                if (!stack.isEmpty() && !stack.toKey().equals(what)) return 0;
                return amount - handler.insertElement(Ints.saturatedCast(amount), (ElementType) what.getPrimaryKey(), mode.isSimulate());
            }

            return 0;
        }
    };

    private static final class ElementHandlerFacade extends ExternalStorageFacade {

        private final IElementStorage handler;

        private ElementHandlerFacade(IElementStorage handler) {
            this.handler = handler;
        }

        @Override
        public int getSlots() {
            return 1;
        }

        @Nullable
        @Override
        public GenericStack getStackInSlot(int slot) {
            ElementStack stack = ElementStack.of(handler);
            if (stack.isEmpty()) return null;
            return stack.toGenericStack();
        }

        @Override
        public AEKeyType getKeyType() {
            return ElementKeyType.INSTANCE;
        }

        @Override
        protected int insertExternal(AEKey what, int amount, Actionable mode) {
            if (what instanceof ElementKey && amount > 0) {
                ElementStack stack = ElementStack.of(handler);
                if (!stack.isEmpty() && !stack.toKey().equals(what)) return 0;
                return amount - handler.insertElement(amount, (ElementType) what.getPrimaryKey(), mode.isSimulate());
            }

            return 0;
        }

        @Override
        protected int extractExternal(AEKey what, int amount, Actionable mode) {
            if (what instanceof ElementKey && amount > 0) {
                return handler.extractElement(amount, (ElementType) what.getPrimaryKey(), mode.isSimulate());
            }

            return 0;
        }

        @Override
        public boolean containsAnyFuzzy(Set<AEKey> keys) {
            ElementStack stack = ElementStack.of(handler);
            return !stack.isEmpty() && keys.contains(stack.toKey());
        }

        @Override
        public void getAvailableStacks(KeyCounter out) {
            ElementStack stack = ElementStack.of(handler);
            if (stack.isEmpty()) return;
            out.add(stack.toKey(), stack.amount());
        }

        @Override
        public boolean isPreferredStorageFor(AEKey what, IActionSource source) {
            if (what instanceof ElementKey) {
                ElementType elementType = (ElementType) what.getPrimaryKey();
                return elementType != ElementType.NONE && handler.getElementAmount(elementType) > 0;
            }

            return false;
        }
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    public static final class ElementContainerStrategy implements ContainerItemStrategy<ElementKey, ElementContainerStrategy.Context> {

        private static final ElementContainerStrategy INSTANCE = new ElementContainerStrategy();

        private ElementContainerStrategy() {}

        @Nullable
        @Override
        public GenericStack getContainedStack(ItemStack stack) {
            if (stack.isEmpty()) return null;

            IElementStorage handler = stack.getCapability(ElementalCraftCapabilities.ElementStorage.ITEM);
            if (handler == null) return null;

            ElementStack elementStack = ElementStack.of(handler);
            if (elementStack.isEmpty()) return null;
            return elementStack.toGenericStack();
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
            ItemStack copy = stack.copyWithCount(1);

            IElementStorage handler = copy.getCapability(ElementalCraftCapabilities.ElementStorage.ITEM);
            if (handler == null) return 0;

            int extracted = handler.extractElement(Ints.saturatedCast(amount), (ElementType) what.getPrimaryKey(), mode.isSimulate());
            if (mode == Actionable.MODULATE) {
                stack.shrink(1);
                context.addOverflow(copy);
            }

            return extracted;
        }

        @Override
        public long insert(Context context, ElementKey what, long amount, Actionable mode) {
            ItemStack stack = context.getStack();
            ItemStack copy = stack.copyWithCount(1);

            IElementStorage handler = copy.getCapability(ElementalCraftCapabilities.ElementStorage.ITEM);
            if (handler == null) return 0;

            int notInserted = handler.insertElement(Ints.saturatedCast(amount), (ElementType) what.getPrimaryKey(), mode.isSimulate());

            if (mode == Actionable.MODULATE) {
                stack.shrink(1);
                context.addOverflow(copy);
            }

            return amount - notInserted;
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

    private static final class ElementStorageExportStrategy extends StorageExportStrategy<IElementStorage, ElementStack> {

        private ElementStorageExportStrategy(
            BlockCapability<IElementStorage, Direction> capability, HandlerStrategy<IElementStorage, ElementStack> handlerStrategy,
            ServerLevel level, BlockPos fromPos, Direction fromSide
        ) {
            super(capability, handlerStrategy, level, fromPos, fromSide);
        }
    }
}
