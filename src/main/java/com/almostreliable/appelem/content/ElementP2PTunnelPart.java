package com.almostreliable.appelem.content;

import appeng.api.config.PowerUnits;
import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.items.parts.PartModels;
import appeng.parts.p2p.CapabilityP2PTunnelPart;
import appeng.parts.p2p.P2PModels;
import com.almostreliable.appelem.AppElem;
import com.almostreliable.appelem.element.ElementKeyType;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

import java.util.List;

public class ElementP2PTunnelPart extends CapabilityP2PTunnelPart<ElementP2PTunnelPart, IElementStorage> {

    private static final P2PModels MODELS = new P2PModels(AppElem.id("part/element_p2p_tunnel"));
    private static final IElementStorage EMPTY = new EmptyElementHandler();

    @PartModels
    public static List<IPartModel> getModels() {
        return MODELS.getModels();
    }

    @SuppressWarnings("AssignmentToSuperclassField")
    public ElementP2PTunnelPart(IPartItem<?> partItem) {
        super(partItem, ElementalCraftCapabilities.ElementStorage.BLOCK);
        emptyHandler = EMPTY;
        inputHandler = new InputElementHandler();
        outputHandler = new OutputElementHandler();
    }

    @Override
    public IPartModel getStaticModels() {
        return MODELS.getModel(isPowered(), isActive());
    }

    private static class EmptyElementHandler implements IElementStorage {

        @Override
        public int getElementAmount(ElementType type) {
            return 0;
        }

        @Override
        public int getElementCapacity(ElementType type) {
            return 0;
        }

        @Override
        public int insertElement(int count, ElementType type, boolean simulate) {
            return 0;
        }

        @Override
        public int extractElement(int count, ElementType type, boolean simulate) {
            return 0;
        }
    }

    private class InputElementHandler implements IElementStorage {

        @Override
        public int getElementAmount(ElementType type) {
            return 0;
        }

        @Override
        public int getElementCapacity(ElementType type) {
            return Integer.MAX_VALUE;
        }

        @Override
        public int insertElement(int count, ElementType type, boolean simulate) {
            int outputTunnels = getOutputs().size();
            if (outputTunnels == 0) return 0;

            int total = 0;
            int countPerOutput = count / outputTunnels;
            int overflow = countPerOutput == 0 ? count : count % outputTunnels;

            for (ElementP2PTunnelPart target : getOutputs()) {
                try (CapabilityGuard guard = target.getAdjacentCapability()) {
                    IElementStorage output = guard.get();
                    int toInsert = countPerOutput + overflow;

                    int inserted = output.insertElement(toInsert, type, simulate);

                    overflow = toInsert - inserted;
                    total += inserted;
                }
            }

            if (!simulate) {
                queueTunnelDrain(PowerUnits.FE, (double) total / ElementKeyType.INSTANCE.getAmountPerOperation());
            }

            return total;
        }

        @Override
        public int extractElement(int count, ElementType type, boolean simulate) {
            return 0;
        }
    }

    public class OutputElementHandler implements IElementStorage {

        @Override
        public int getElementAmount(ElementType type) {
            try (CapabilityGuard guard = getInputCapability()) {
                return guard.get().getElementAmount(type);
            }
        }

        @Override
        public int getElementCapacity(ElementType type) {
            try (CapabilityGuard guard = getInputCapability()) {
                return guard.get().getElementCapacity(type);
            }
        }

        @Override
        public int insertElement(int count, ElementType type, boolean simulate) {
            return 0;
        }

        @Override
        public int extractElement(int count, ElementType type, boolean simulate) {
            try (CapabilityGuard guard = getInputCapability()) {
                int result = guard.get().extractElement(count, type, simulate);
                if (!simulate) {
                    queueTunnelDrain(PowerUnits.FE, (double) result / ElementKeyType.INSTANCE.getAmountPerOperation());
                }
                return result;
            }
        }
    }
}
