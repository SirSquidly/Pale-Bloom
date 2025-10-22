package com.sirsquidly.palebloom.blocks.base;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;

/**
 * OH WHYYY ARE STAIRS `protected`, WHY
 * */
public class BlockJTPGStairs extends BlockStairs
{
    public BlockJTPGStairs(IBlockState modelState, SoundType soundIn)
    {
        super(modelState);
        this.setSoundType(soundIn);
        this.useNeighborBrightness = true;
        this.setLightOpacity(255);
    }
}