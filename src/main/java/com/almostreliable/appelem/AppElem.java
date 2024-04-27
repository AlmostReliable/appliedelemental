package com.almostreliable.appelem;

import com.almostreliable.appelem.core.AppElemRegistration;
import com.almostreliable.appelem.data.DataGeneration;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@SuppressWarnings("UtilityClassWithPublicConstructor")
@Mod(BuildConfig.MOD_ID)
public final class AppElem {

    public static final Logger LOGGER = LogUtils.getLogger();

    public AppElem(IEventBus modEventBus) {
        AppElemRegistration.init(modEventBus);
        modEventBus.addListener(DataGeneration::init);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(BuildConfig.MOD_ID, path);
    }
}
