package com.sirsquidly.palebloom.world.feature;

import com.sirsquidly.palebloom.blocks.BlockDoublePalePlant;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import net.minecraft.block.BlockBush;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenDoublePalePlant extends WorldGenerator
{
    private int plantType = 0;

    BlockBush block = JTPGBlocks.PALE_DOUBLE_PLANT;
    int quantity = 10;

    public WorldGenDoublePalePlant() {}

    public void setPalePlantType(int plantTypeIn)
    {
        this.plantType = plantTypeIn;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        for (int i = 0; i < 64; ++i)
        {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.isNether() || blockpos.getY() < worldIn.getHeight() - 1) && JTPGBlocks.PALE_DOUBLE_PLANT.canPlaceBlockAt(worldIn, blockpos))
            {
                ((BlockDoublePalePlant)this.block).placeDoubleAt(worldIn, blockpos, this.plantType, false, 2);

                return true;
            }
        }

        return true;
    }
}