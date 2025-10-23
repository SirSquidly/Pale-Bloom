package com.sirsquidly.palebloom.common.world.feature;

import com.sirsquidly.palebloom.init.JTPGBlocks;
import net.minecraft.block.BlockBush;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenEyeblossom extends WorldGenerator
{
    BlockBush eyeblossomClosed = JTPGBlocks.EYEBLOSSOM_CLOSED;
    BlockBush eyeblossomOpen = JTPGBlocks.EYEBLOSSOM_OPEN;

    int quantity = 10;

    public WorldGenEyeblossom() {}

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        BlockBush block = worldIn.isDaytime() ? eyeblossomClosed : eyeblossomOpen;

        for (int i = 0; i < 64; ++i)
        {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.isNether() || blockpos.getY() < worldIn.getHeight() - 1) && block.canBlockStay(worldIn, blockpos, block.getDefaultState()))
            { worldIn.setBlockState(blockpos, block.getDefaultState(), 2); }
        }

        return true;
    }
}