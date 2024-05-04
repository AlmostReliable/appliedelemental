package com.almostreliable.appelem.core;

import com.almostreliable.appelem.BuildConfig;
import com.almostreliable.appelem.content.MeElementContainerBlock;
import com.almostreliable.appelem.data.AppElemLang;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.property.ECProperties;

import java.util.function.Supplier;

public final class AppElemBlocks {

    static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(BuildConfig.MOD_ID);

    // @formatter:off

    public static final DeferredBlock<MeElementContainerBlock> CONTAINER = register("me_element_container", "ME Element Container", () -> new MeElementContainerBlock(ECProperties.Blocks.CONTAINER));

    // @formatter:on

    private AppElemBlocks() {}

    private static <T extends Block> DeferredBlock<T> register(String id, String name, Supplier<T> blockSupplier) {
        var deferredBlock = REGISTRY.register(id, blockSupplier);
        AppElemItems.REGISTRY.register(id, name, p -> new BlockItem(deferredBlock.get(), p));
        AppElemLang.LangEntry.of("block", id, AppElemRegistration.getNameOrFormatId(id, name));
        return deferredBlock;
    }
}
