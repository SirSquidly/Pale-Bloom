package com.sirsquidly.palebloom.common.blocks.base;

import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockJTPGTrapDoor extends BlockTrapDoor
{
    public BlockJTPGTrapDoor()
    {
        super(Material.WOOD);
    }

    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return MapColor.QUARTZ;
    }
}