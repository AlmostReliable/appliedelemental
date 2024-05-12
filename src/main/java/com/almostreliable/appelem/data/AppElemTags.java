package com.almostreliable.appelem.data;

import appeng.api.features.P2PTunnelAttunement;
import com.almostreliable.appelem.BuildConfig;
import com.almostreliable.appelem.core.AppElemBlocks;
import com.almostreliable.appelem.core.AppElemItems;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.tag.ECTags;

import java.util.concurrent.CompletableFuture;

final class AppElemTags {

    private AppElemTags() {}

    static void initTagProviders(boolean run, DataGenerator generator, PackOutput output, ExistingFileHelper existingFileHelper) {
        var lookupFilter = CompletableFuture.supplyAsync(VanillaRegistries::createLookup, Util.backgroundExecutor());
        var blockTags = generator.addProvider(run, new Blocks(output, lookupFilter, existingFileHelper));
        generator.addProvider(run, new Items(output, lookupFilter, blockTags.contentsGetter()));
    }

    private static final class Items extends ItemTagsProvider {

        private Items(
            PackOutput output, CompletableFuture<HolderLookup.Provider> lookupFilter, CompletableFuture<TagLookup<Block>> blockTags
        ) {
            super(output, lookupFilter, blockTags);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            var elementP2PTag = tag(P2PTunnelAttunement.getAttunementTag(AppElemItems.ELEMENT_P2P_TUNNEL));
            elementP2PTag.addOptionalTag(ECTags.Items.SHARDS)
                .addOptionalTag(ECTags.Items.CRYSTALS)
                .addOptionalTag(ECTags.Items.LENSES)
                .addOptionalTag(ECTags.Items.PIPES)
                .addOptional(ECBlocks.SMALL_CONTAINER.getId())
                .addOptional(ECBlocks.CONTAINER.getId())
                .addOptional(ECBlocks.AIR_RESERVOIR.getId())
                .addOptional(ECBlocks.EARTH_RESERVOIR.getId())
                .addOptional(ECBlocks.FIRE_RESERVOIR.getId())
                .addOptional(ECBlocks.WATER_RESERVOIR.getId())
                .addOptional(AppElemBlocks.CONTAINER.getId());
        }
    }

    private static final class Blocks extends BlockTagsProvider {

        private Blocks(
            PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper
        ) {
            super(output, lookupProvider, BuildConfig.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).addOptional(AppElemBlocks.CONTAINER.getId());
        }
    }
}
