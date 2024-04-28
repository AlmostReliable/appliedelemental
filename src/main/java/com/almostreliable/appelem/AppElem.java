package com.almostreliable.appelem;

import com.almostreliable.appelem.core.AppElemRegistration;
import com.almostreliable.appelem.data.DataGeneration;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@SuppressWarnings("UtilityClassWithPublicConstructor")
@Mod(BuildConfig.MOD_ID)
public final class AppElem {

    public AppElem(IEventBus modEventBus) {
        AppElemRegistration.init(modEventBus);
        modEventBus.addListener(DataGeneration::init);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(BuildConfig.MOD_ID, path);
    }
}
