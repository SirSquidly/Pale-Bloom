package com.sirsquidly.palebloom.common.blocks.base;

import net.minecraft.block.BlockFence;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockJTPGFence extends BlockFence
{
    public BlockJTPGFence(Material materialIn, MapColor mapColorIn, SoundType soundIn)
    {
        super(materialIn, mapColorIn);
        this.setSoundType(soundIn);
    }
}