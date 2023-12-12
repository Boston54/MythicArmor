package net.boston.mythicarmor.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ImbuingStationBlock extends Block {
    public ImbuingStationBlock(Properties pProperties) {
        super(pProperties);
    }

    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 12, 15);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
}
