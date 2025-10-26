package com.sirsquidly.palebloom.common.world.feature;

import com.sirsquidly.palebloom.config.ConfigCache;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

public class GeneratorPeepingBirch extends WorldGenAbstractTree
{
    public static final IBlockState PEEPING_LOG = JTPGBlocks.PEEPING_BIRCH_LOG.getDefaultState();
    public static final IBlockState FALLBACK_LOG = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH);
    public static final IBlockState PEEPING_LEAF = JTPGBlocks.PEEPING_BIRCH_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE).withProperty(BlockLeaves.DECAYABLE, Boolean.TRUE);
    public static final IBlockState FALLBACK_LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH).withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE).withProperty(BlockLeaves.DECAYABLE, Boolean.TRUE);

    public static final IBlockState LOG = ConfigCache.palOakWod_enabled ? PEEPING_LOG : FALLBACK_LOG;
    public static final IBlockState LEAF = ConfigCache.palOakLvs_enabled ? PEEPING_LEAF : FALLBACK_LEAF;

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

                if (j2 > 3 && j2 < i - 3 && rand.nextInt(3) == 0)
                {
                    EnumFacing randomFacing = EnumFacing.Plane.HORIZONTAL.random(rand);
                    BlockPos offsetPos = upN.offset(randomFacing);
                    IBlockState offsetState = worldIn.getBlockState(offsetPos);
                    IBlockState offsetDownState = worldIn.getBlockState(offsetPos.down());

                    if ((offsetState.getBlock().isAir(offsetState, worldIn, offsetPos) || offsetState.getBlock().isLeaves(offsetState, worldIn, offsetPos)) && offsetDownState.getBlock().isAir(offsetDownState, worldIn, offsetPos.down()))
                    {
                        this.placeLogAt(worldIn, offsetPos, BlockLog.EnumAxis.fromFacingAxis(randomFacing.getAxis()));
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