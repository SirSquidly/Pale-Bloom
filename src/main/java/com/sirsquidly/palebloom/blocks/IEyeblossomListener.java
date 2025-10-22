package com.sirsquidly.palebloom.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public interface IEyeblossomListener
{

    /** Called via `BlockEyeblossom`'s scheduleUpdate. */
    default void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    { preformSwapping(worldIn, pos, true); }

    default void preformSwapping(World worldIn, BlockPos pos, boolean isResponding)
    {}
}