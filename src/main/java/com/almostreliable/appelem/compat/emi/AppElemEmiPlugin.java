package com.almostreliable.appelem.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import net.neoforged.fml.ModList;

@EmiEntrypoint
public class AppElemEmiPlugin implements EmiPlugin {

    static {
        if (ModList.get().isLoaded("jei")) {
            ElementalCraftJemiAdapter.init();
        }
    }

    @Override
    public void register(EmiRegistry registry) {
        // no-op
    }
}
