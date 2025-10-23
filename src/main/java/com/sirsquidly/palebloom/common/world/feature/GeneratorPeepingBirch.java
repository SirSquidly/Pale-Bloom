package com.sirsquidly.palebloom.common.world.feature;

import com.google.common.collect.Lists;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneratorPeepingBirch extends WorldGenAbstractTree
{
    public static final IBlockState LOG = JTPGBlocks.PEEPING_BIRCH_LOG.getDefaultState();
    public static final IBlockState LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH).withProperty(BlockOldLeaf.CHECK_DECAY, false);

    public GeneratorPeepingBirch()
    { super(false); }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos pos)
    {
        BlockPos blockpos = pos.down();
        IBlockState state = worldIn.getBlockState(blockpos);
        boolean isSoil = state.getBlock().canSustainPlant(state, worldIn, blockpos, net.minecraft.util.EnumFacing.UP, ((net.minecraft.block.BlockSapling) Blocks.SAPLING));

        int i = rand.nextInt(6) + 6;

        if (pos.getY() < 1 || pos.getY() >= worldIn.getHeight() || !this.placeTreeOfHeight(worldIn, pos, i)) return false;

        if (!(isSoil && pos.getY() < worldIn.getHeight() - i - 1))
        { return false; }

        state.getBlock().onPlantGrow(state, worldIn, blockpos, pos);

        for (int i2 = pos.getY() - 3 + i; i2 <= pos.getY() + i; ++i2)
        {
            int k2 = i2 - (pos.getY() + i);
            int l2 = 1 - k2 / 2;

            for (int i3 = pos.getX() - l2; i3 <= pos.getX() + l2; ++i3)
            {
                int j1 = i3 - pos.getX();

                for (int k1 = pos.getZ() - l2; k1 <= pos.getZ() + l2; ++k1)
                {
                    int l1 = k1 - pos.getZ();

                    if (Math.abs(j1) != l2 || Math.abs(l1) != l2 || rand.nextInt(2) != 0 && k2 != 0)
                    {
                        BlockPos topPos = new BlockPos(i3, i2, k1);
                        IBlockState state2 = worldIn.getBlockState(topPos);

                        if (state2.getBlock().isAir(state2, worldIn, topPos) || state2.getBlock().isAir(state2, worldIn, topPos))
                        {
                            this.placeLeafAt(worldIn, topPos);
                        }
                    }
                }
            }
        }

        for (int j2 = 0; j2 < i; ++j2)
        {
            BlockPos upN = pos.up(j2);
            IBlockState state2 = worldIn.getBlockState(upN);

            if (state2.getBlock().isAir(state2, worldIn, upN) || state2.getBlock().isLeaves(state2, worldIn, upN))
            {
                this.placeLogAt(worldIn, upN, BlockLog.EnumAxis.Y);

                if (j2 > 3 && j2 < i - 3)
                {
                    if (rand.nextInt(4) == 0) continue;

                    List<EnumFacing> list = Lists.newArrayList(EnumFacing.Plane.HORIZONTAL);
                    Collections.shuffle(list, rand);

                    /* Includes an extra check below, to prevent branch ends from generating stacked. */
                    if ((state2.getBlock().isAir(state2, worldIn, upN.offset(list.get(0))) || state2.getBlock().isLeaves(state2, worldIn, upN.offset(list.get(0)))) && state2.getBlock().isAir(state2, worldIn, upN.offset(list.get(0)).down()))
                    {
                        this.placeLogAt(worldIn, upN.offset(list.get(0)), BlockLog.EnumAxis.fromFacingAxis(list.get(0).getAxis()));
                    }
                }
            }
        }

        return true;
    }

    private boolean placeTreeOfHeight(World worldIn, BlockPos pos, int height)
    {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int l = 0; l <= height + 1; ++l)
        {
            int i1 = 1;

            if (l == 0)
            { i1 = 0; }

            if (l >= pos.getY() + 1 + height - 2)
            { i1 = 2; }

            for (int j1 = -i1; j1 <= i1; ++j1)
            {
                for (int k1 = -i1; k1 <= i1; ++k1)
                {
                    if (j >= 0 && j < worldIn.getHeight())
                    {
                        if (!this.isReplaceable(worldIn, blockpos$mutableblockpos.setPos(i + j1, j + l, k + k1)))
                        { return false; }
                    }
                    else
                    { return false; }
                }
            }
        }

        return true;
    }


    private void placeLogAt(World worldIn, BlockPos pos, BlockLog.EnumAxis axis)
    { this.setBlockAndNotifyAdequately(worldIn, pos, LOG.withProperty(BlockLog.LOG_AXIS, axis)); }

    private void placeLeafAt(World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);
        if (state.getMaterial().isReplaceable())
        { this.setBlockAndNotifyAdequately(worldIn, pos, LEAF); }
    }
}