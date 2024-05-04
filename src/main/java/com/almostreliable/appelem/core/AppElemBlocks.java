package com.almostreliable.appelem.core;

import com.almostreliable.appelem.BuildConfig;
import com.almostreliable.appelem.Utils;
import com.almostreliable.appelem.block.NetworkElementContainerBlock;
import com.almostreliable.appelem.data.AppElemLang;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.property.ECProperties;

import java.util.function.Supplier;

public final class AppElemBlocks {

    static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(BuildConfig.MOD_ID);

    // @formatter:off

    public static final DeferredBlock<NetworkElementContainerBlock> NETWORK_CONTAINER = register("network_element_container", () -> new NetworkElementContainerBlock(ECProperties.Blocks.CONTAINER));

    // @formatter:on

    private AppElemBlocks() {}

    private static <T extends Block> DeferredBlock<T> register(String id, Supplier<T> blockSupplier, @Nullable String name) {
        var deferredBlock = REGISTRY.register(id, blockSupplier);
        AppElemItems.REGISTRY.register(id, name, p -> new BlockItem(deferredBlock.get(), p));
        AppElemLang.LangEntry.of("block", id, Utils.getNameOrFormatId(id, name));
        return deferredBlock;
    }

    private static <T extends Block> DeferredBlock<T> register(String id, Supplier<T> blockSupplier) {
        return register(id, blockSupplier, null);
    }
}
