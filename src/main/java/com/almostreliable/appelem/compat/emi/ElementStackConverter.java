package com.almostreliable.appelem.compat.emi;

import appeng.api.behaviors.GenericSlotCapacities;
import appeng.api.integrations.emi.EmiStackConverter;
import appeng.api.stacks.GenericStack;
import com.almostreliable.appelem.AppElem;
import com.almostreliable.appelem.element.ElementKey;
import com.almostreliable.appelem.element.ElementKeyType;
import com.google.common.primitives.Ints;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.jemi.JemiStack;
import dev.emi.emi.jemi.JemiUtil;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.interaction.jei.ingredient.source.IngredientSource;

public class ElementStackConverter implements EmiStackConverter {

    @Override
    public Class<?> getKeyType() {
        return ElementType.class;
    }

    @Nullable
    @Override
    public EmiStack toEmiStack(GenericStack stack) {
        try {
            if (stack.what() instanceof ElementKey elementKey) {
                ElementType elementType = (ElementType) elementKey.getPrimaryKey();
                IngredientElementType elementIngredient = new IngredientElementType(elementType, getCapacityAmount());
                return JemiUtil.getStack(elementIngredient);
            }
        } catch (Exception e) {
            AppElem.LOGGER.error("Failed to convert GenericStack to EmiStack", e);
        }

        return null;
    }

    @Nullable
    @Override
    public GenericStack toGenericStack(EmiStack stack) {
        try {
            if (stack instanceof JemiStack<?> jemiStack) {
                Object ingredient = jemiStack.ingredient;
                if (ingredient instanceof IngredientElementType elementIngredient) {
                    ElementKey elementKey = new ElementKey(elementIngredient.elementType());
                    return new GenericStack(elementKey, getCapacityAmount());
                }
                if (ingredient instanceof IngredientSource sourceIngredient) {
                    ElementKey elementKey = new ElementKey(sourceIngredient.elementType());
                    return new GenericStack(elementKey, getCapacityAmount());
                }
            }
        } catch (Exception e) {
            AppElem.LOGGER.error("Failed to convert EmiStack to GenericStack", e);
        }

        return null;
    }

    @SuppressWarnings("UnstableApiUsage")
    private int getCapacityAmount() {
        return Ints.saturatedCast(GenericSlotCapacities.getMap().get(ElementKeyType.INSTANCE));
    }
}
