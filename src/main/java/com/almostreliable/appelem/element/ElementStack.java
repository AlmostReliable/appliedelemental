package com.almostreliable.appelem.element;

import appeng.api.stacks.GenericStack;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

/**
 * Utility class to get the first available element from an {@link IElementStorage}.
 *
 * @param elementType The element type that was found.
 * @param amount The amount of the element that was found.
 */
record ElementStack(ElementType elementType, int amount) {

    private static final ElementStack EMPTY = new ElementStack(ElementType.NONE, 0);

    static ElementStack of(IElementStorage handler) {
        if (handler.isEmpty()) return EMPTY;

        for (ElementType elementType : ElementType.values()) {
            int amount = handler.getElementAmount(elementType);
            if (amount <= 0) continue;
            return new ElementStack(elementType, amount);
        }

        return EMPTY;
    }

    ElementKey toKey() {
        return new ElementKey(elementType);
    }

    GenericStack toGenericStack() {
        return new GenericStack(toKey(), amount);
    }

    boolean isEmpty() {
        return this == EMPTY || elementType == ElementType.NONE || amount == 0;
    }
}
