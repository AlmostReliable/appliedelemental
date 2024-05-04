package com.almostreliable.appelem.compat.emi;

import appeng.api.integrations.emi.EmiStackConverters;

final class ElementalCraftJemiAdapter {

    private ElementalCraftJemiAdapter() {}

    static void init() {
        EmiStackConverters.register(new ElementStackConverter());
    }
}
