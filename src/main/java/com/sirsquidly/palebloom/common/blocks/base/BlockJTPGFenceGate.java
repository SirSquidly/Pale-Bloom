package com.sirsquidly.palebloom.common.blocks.base;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockJTPGFenceGate extends BlockFenceGate
{
    public BlockJTPGFenceGate(BlockPlanks.EnumType p_i46394_1_, SoundType soundIn)
    {
        super(p_i46394_1_);
        this.setSoundType(soundIn);
    }

    @Deprecated
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return MapColor.QUARTZ;
    }
}