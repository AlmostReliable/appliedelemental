package com.almostreliable.appelem.content;

import com.almostreliable.appelem.core.AppElemConfig;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.block.container.AbstractElementContainerBlock;

import java.util.List;

public class MeElementContainerBlock extends AbstractElementContainerBlock {

    public static final MapCodec<MeElementContainerBlock> CODEC = simpleCodec(MeElementContainerBlock::new);

    private static final VoxelShape BASE = box(0, 0, 0, 16, 2, 16);
    private static final VoxelShape GLASS = box(2, 2, 2, 14, 15, 14);
    private static final VoxelShape PILLAR_1 = box(1, 2, 1, 3, 16, 3);
    private static final VoxelShape PILLAR_2 = box(13, 2, 1, 15, 16, 3);
    private static final VoxelShape PILLAR_3 = box(1, 2, 13, 3, 16, 15);
    private static final VoxelShape PILLAR_4 = box(13, 2, 13, 15, 16, 15);
    private static final VoxelShape SOCKET = box(6, 15, 6, 10, 16, 10);
    private static final VoxelShape PORT_1 = box(5, 5, 0, 11, 11, 2);
    private static final VoxelShape PORT_2 = box(5, 5, 14, 11, 11, 16);
    private static final VoxelShape PORT_3 = box(0, 5, 5, 2, 11, 11);
    private static final VoxelShape PORT_4 = box(14, 5, 5, 16, 11, 11);

    private static final VoxelShape SHAPE = Shapes.or(
        BASE, GLASS, PILLAR_1, PILLAR_2, PILLAR_3, PILLAR_4, SOCKET, PORT_1, PORT_2, PORT_3, PORT_4
    );

    public MeElementContainerBlock(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public int getDefaultCapacity() {
        return AppElemConfig.COMMON.containerCapacity.get();
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("WIP").withStyle(ChatFormatting.DARK_RED));
    }
}
