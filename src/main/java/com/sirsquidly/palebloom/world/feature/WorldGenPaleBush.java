package com.sirsquidly.palebloom.world.feature;

import com.sirsquidly.palebloom.init.JTPGBlocks;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

public class WorldGenPaleBush extends WorldGenAbstractTree
{
    public static final IBlockState LEAF = JTPGBlocks.PALE_OAK_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE).withProperty(BlockLeaves.DECAYABLE, Boolean.TRUE);

    public WorldGenPaleBush()
    { super(false); }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos pos)
    {
        BlockPos blockpos = pos.down();
        IBlockState state = worldIn.getBlockState(blockpos);
        boolean isSoil = state.getBlock().canSustainPlant(state, worldIn, blockpos, EnumFacing.UP, ((BlockSapling)Blocks.SAPLING));

        int i = rand.nextInt(3) + rand.nextInt(2) + 6;

        if (pos.getY() < 1 || pos.getY() >= worldIn.getHeight() || !worldIn.isAirBlock(pos)) return false;

        if (!(isSoil && pos.getY() < worldIn.getHeight() - i - 1))
        { return false; }


        if (rand.nextBoolean())
        {
            placeLeafAt(worldIn, pos);
            placeLeafAt(worldIn, pos.up());

            for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL)
            {
                placeLeafAt(worldIn, pos.offset(facing));
            }

            BlockPos[] possiblePositions = { pos.north().up(), pos.east().up(), pos.south().up(), pos.west().up(), pos.north().east(), pos.north().west(), pos.south().east(), pos.south().west() };

            for (BlockPos possiblePos : possiblePositions)
            {
                if (rand.nextBoolean()) placeLeafAt(worldIn, possiblePos);
            }
        }
        else
        {
            placeLeafAt(worldIn, pos);
            placeLeafAt(worldIn, pos.north());
            placeLeafAt(worldIn, pos.east());
            placeLeafAt(worldIn, pos.north().east());

            placeLeafAt(worldIn, pos.south());
            placeLeafAt(worldIn, pos.south().east());
            placeLeafAt(worldIn, pos.west());
            placeLeafAt(worldIn, pos.north().west());
            placeLeafAt(worldIn, pos.north(2));
            placeLeafAt(worldIn, pos.north(2).east());
            placeLeafAt(worldIn, pos.east(2));
            placeLeafAt(worldIn, pos.north().east(2));

            placeLeafAt(worldIn, pos.up());
            placeLeafAt(worldIn, pos.up().north().east());

            BlockPos[] possiblePositions = { pos.up().north(), pos.up().east(), pos.south().west(), pos.east(2).south(), pos.north(2).west(), pos.north(2).east(2) };

            for (BlockPos possiblePos : possiblePositions)
            {
                if (rand.nextBoolean()) placeLeafAt(worldIn, possiblePos);
            }
        }


        return true;
    }

    private void placeLeafAt(World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);
        if (state.getMaterial().isReplaceable() && !(state.getBlock() instanceof BlockDoublePlant))
        { this.setBlockAndNotifyAdequately(worldIn, pos, LEAF); }
    }
}