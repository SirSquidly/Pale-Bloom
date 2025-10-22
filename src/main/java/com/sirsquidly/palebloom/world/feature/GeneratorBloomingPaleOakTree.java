package com.sirsquidly.palebloom.world.feature;

import com.sirsquidly.palebloom.Config;
import com.sirsquidly.palebloom.blocks.BlockCreakingHeart;
import com.sirsquidly.palebloom.blocks.BlockSuckerRoots;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneratorBloomingPaleOakTree extends WorldGenAbstractTree
{
    public static final IBlockState LOG = JTPGBlocks.PALE_OAK_LOG.getDefaultState();
    public static final IBlockState CREAKING_HEART = JTPGBlocks.CREAKING_HEART.getDefaultState().withProperty(BlockCreakingHeart.AXIS, EnumFacing.Axis.Y);
    public static final IBlockState LEAF = JTPGBlocks.BLOOMING_PALE_OAK_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE).withProperty(BlockLeaves.DECAYABLE, Boolean.TRUE);
    public static final IBlockState ROOT = JTPGBlocks.SUCKER_ROOTS.getDefaultState();
    public static final IBlockState ROOT_NODULE = JTPGBlocks.SUCKER_ROOT_NODULE.getDefaultState();

    public static final IBlockState HANGING_MOSS = JTPGBlocks.PALE_HANGING_MOSS.getDefaultState();

    /** The chance for this tree to ATTEMPT to generate a Creaking Heart. */
    public float placeCreakingHeartChance;
    /** If this tree has placed a Nodule. Each Pale Oak Tree only gets 1, if the random checks pass. */
    public boolean placedNodule = false;
    public float creakingHeartNaturalChance;

    /** The chance to place a vine on a generated leaf. */
    public float placeHangingMossChance = 0.1F;
    /** A stored list of Leaf Positions, to later have Vines randomly applied to them. */
    private final List<BlockPos> storedLeafPositions = new ArrayList<>();
    /** A stored list of Leaf Positions, to later have Vines randomly applied to them. */
    private final List<BlockPos> storedLogPositions = new ArrayList<>();

    private static final boolean enableCreakingHeart = Config.block.creakingHeart.enableCreakingHeart;

    public GeneratorBloomingPaleOakTree(float placeCreakingHeartChanceIn, float creakingHeartNaturalChanceIn)
    {
        super(false);
        placeCreakingHeartChance = enableCreakingHeart ? placeCreakingHeartChanceIn : 0.0F;
        creakingHeartNaturalChance = creakingHeartNaturalChanceIn;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos pos)
    {
        BlockPos blockpos = pos.down();
        IBlockState state = worldIn.getBlockState(blockpos);

        boolean isSoil = state.getBlock().canSustainPlant(state, worldIn, blockpos, EnumFacing.UP, ((BlockSapling)Blocks.SAPLING));

        int i = rand.nextInt(2) + rand.nextInt(2) + 8;
        int j = pos.getX();
        int k = pos.getY();
        int l = pos.getZ();

        if (pos.getY() < 1 || pos.getY() >= worldIn.getHeight() || !this.placeTreeOfHeight(worldIn, pos, i)) return false;

        if (!(isSoil && pos.getY() < worldIn.getHeight() - i - 1))
        { return false; }

        EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
        int i1 = i - rand.nextInt(4);
        int j1 = 2 - rand.nextInt(3);
        int k1 = j;
        int l1 = l;
        int i2 = k + i - 1;

        this.onPlantGrow(worldIn, blockpos, pos);
        this.onPlantGrow(worldIn, blockpos.east(), pos);
        this.onPlantGrow(worldIn, blockpos.south(), pos);
        this.onPlantGrow(worldIn, blockpos.south().east(), pos);

        for (int j2 = 0; j2 < i; ++j2)
        {
            if (j2 >= i1 && j1 > 0)
            {
                k1 += enumfacing.getXOffset();
                l1 += enumfacing.getZOffset();
                --j1;
            }

            int k2 = k + j2;
            BlockPos blockpos1 = new BlockPos(k1, k2, l1);
            state = worldIn.getBlockState(blockpos1);

            if (state.getBlock().isAir(state, worldIn, blockpos1) || state.getBlock().isLeaves(state, worldIn, blockpos1))
            {
                this.placeLogAt(worldIn, blockpos1, false);
                this.placeLogAt(worldIn, blockpos1.east(), false);
                this.placeLogAt(worldIn, blockpos1.south(), false);
                this.placeLogAt(worldIn, blockpos1.east().south(), false);
            }
        }

        for (int i3 = -2; i3 <= 0; ++i3)
        {
            for (int l3 = -2; l3 <= 0; ++l3)
            {
                int k4 = -1;
                this.placeLeafAt(worldIn, k1 + i3, i2 + k4, l1 + l3);
                this.placeLeafAt(worldIn, 1 + k1 - i3, i2 + k4, l1 + l3);
                this.placeLeafAt(worldIn, k1 + i3, i2 + k4, 1 + l1 - l3);
                this.placeLeafAt(worldIn, 1 + k1 - i3, i2 + k4, 1 + l1 - l3);

                if ((i3 > -2 || l3 > -1) && (i3 != -1 || l3 != -2))
                {
                    k4 = 1;
                    this.placeLeafAt(worldIn, k1 + i3, i2 + k4, l1 + l3);
                    this.placeLeafAt(worldIn, 1 + k1 - i3, i2 + k4, l1 + l3);
                    this.placeLeafAt(worldIn, k1 + i3, i2 + k4, 1 + l1 - l3);
                    this.placeLeafAt(worldIn, 1 + k1 - i3, i2 + k4, 1 + l1 - l3);
                }
            }
        }

        if (rand.nextBoolean())
        {
            this.placeLeafAt(worldIn, k1, i2 + 2, l1);
            this.placeLeafAt(worldIn, k1 + 1, i2 + 2, l1);
            this.placeLeafAt(worldIn, k1 + 1, i2 + 2, l1 + 1);
            this.placeLeafAt(worldIn, k1, i2 + 2, l1 + 1);
        }

        for (int j3 = -3; j3 <= 4; ++j3)
        {
            for (int i4 = -3; i4 <= 4; ++i4)
            {
                if ((j3 != -3 || i4 != -3) && (j3 != -3 || i4 != 4) && (j3 != 4 || i4 != -3) && (j3 != 4 || i4 != 4) && (Math.abs(j3) < 3 || Math.abs(i4) < 3))
                {
                    this.placeLeafAt(worldIn, k1 + j3, i2, l1 + i4);
                }
            }
        }

        for (int k3 = -1; k3 <= 2; ++k3)
        {
            for (int j4 = -1; j4 <= 2; ++j4)
            {
                if ((k3 < 0 || k3 > 1 || j4 < 0 || j4 > 1) && rand.nextInt(3) == 0)
                {
                    int l4 = rand.nextInt(3) + 2;

                    for (int i5 = 0; i5 < l4; ++i5)
                    {
                        this.placeLogAt(worldIn, new BlockPos(j + k3, i2 - i5 - 1, l + j4), false);
                    }

                    for (int j5 = -1; j5 <= 1; ++j5)
                    {
                        for (int l2 = -1; l2 <= 1; ++l2)
                        {
                            this.placeLeafAt(worldIn, k1 + k3 + j5, i2, l1 + j4 + l2);
                        }
                    }

                    for (int k5 = -2; k5 <= 2; ++k5)
                    {
                        for (int l5 = -2; l5 <= 2; ++l5)
                        {
                            if (Math.abs(k5) != 2 || Math.abs(l5) != 2)
                            {
                                this.placeLeafAt(worldIn, k1 + k3 + k5, i2 - 1, l1 + j4 + l5);
                            }
                        }
                    }
                }
            }
        }


        BlockPos posLol = new BlockPos(j, i2, l);
        placeRandomHangingLeaves(worldIn, rand, posLol.down(1), 4, 3);
        placeRandomHangingLeaves(worldIn, rand, posLol.down(2), 4, 3);
        placeRandomHangingLeaves(worldIn, rand, posLol.down(3), 4, 2);

        placeStump(worldIn, rand, pos);
        if (rand.nextBoolean()) placeRoots(worldIn, rand, pos, EnumFacing.NORTH);
        if (rand.nextBoolean()) placeRoots(worldIn, rand, pos, EnumFacing.WEST);
        if (rand.nextBoolean()) placeRoots(worldIn, rand, pos.east(), EnumFacing.NORTH);
        if (rand.nextBoolean()) placeRoots(worldIn, rand, pos.east(), EnumFacing.EAST);
        if (rand.nextBoolean()) placeRoots(worldIn, rand, pos.south(), EnumFacing.SOUTH);
        if (rand.nextBoolean()) placeRoots(worldIn, rand, pos.south(), EnumFacing.WEST);
        if (rand.nextBoolean()) placeRoots(worldIn, rand, pos.south().east(), EnumFacing.SOUTH);
        if (rand.nextBoolean()) placeRoots(worldIn, rand, pos.south().east(), EnumFacing.EAST);

        if (!placedNodule && rand.nextInt(10) == 0)
        { placeRootNoduleAt(worldIn, pos.down()); }
        if (!placedNodule && rand.nextInt(10) == 0)
        { placeRootNoduleAt(worldIn, pos.east().down()); }
        if (!placedNodule && rand.nextInt(10) == 0)
        { placeRootNoduleAt(worldIn, pos.south().down()); }
        if (!placedNodule && rand.nextInt(10) == 0)
        { placeRootNoduleAt(worldIn, pos.south().east().down()); }

        /* Places all the Hanging Moss. */
        placeHangingMoss(worldIn, rand);

        /* Gambling if we TRY to place a heart! */
        if (rand.nextFloat() < this.placeCreakingHeartChance) placeCreakingHeart(worldIn, rand);

        /* Safety cleanup of the lists. */
        if (!storedLeafPositions.isEmpty()) storedLeafPositions.clear();
        if (!storedLogPositions.isEmpty()) storedLogPositions.clear();

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
            {
                i1 = 0;
            }

            if (l >= height - 1)
            {
                i1 = 2;
            }

            for (int j1 = -i1; j1 <= i1; ++j1)
            {
                for (int k1 = -i1; k1 <= i1; ++k1)
                {
                    if (!this.isReplaceable(worldIn, blockpos$mutableblockpos.setPos(i + j1, j + l, k + k1)))
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /** Generates an entire Roots around the base of a Blooming Pale Oak tree. */
    private void placeStump(World worldIn, Random rand, BlockPos pos)
    {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(pos);

        for (int k3 = -1; k3 <= 2; ++k3)
        {
            for (int j4 = -1; j4 <= 2; ++j4)
            {
                if ((k3 < 0 || k3 > 1 || j4 < 0 || j4 > 1) && rand.nextInt(3) == 0)
                {
                    /* The height of the stump to generate. */
                    int stumpHeight = rand.nextInt(2) + 1;

                    blockpos$mutableblockpos.setPos(pos.getX() + k3, pos.getY(), pos.getZ() + j4);

                    if (!worldIn.getBlockState(blockpos$mutableblockpos.down()).isFullBlock())
                    {
                        if (worldIn.isAirBlock(blockpos$mutableblockpos.down()) && worldIn.getBlockState(blockpos$mutableblockpos.down(2)).isFullBlock())
                        {
                            this.placeLogAt(worldIn, blockpos$mutableblockpos.down(), false);
                        }
                        else { continue; }
                    }

                    for (int i5 = 0; i5 < stumpHeight; ++i5)
                    {
                        this.placeLogAt(worldIn, blockpos$mutableblockpos, false);
                        blockpos$mutableblockpos.move(EnumFacing.UP);
                    }
                }
            }
        }
    }

    private void placeRoots(World worldIn, Random rand, BlockPos pos, EnumFacing facing)
    {
        /* How far out the roots will generate. */
        int rootReach = rand.nextInt(3) + 1;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(pos);

        for (int i = 0; i <= rootReach; i++)
        {
            if (rand.nextInt(2) == 0)
            {
                /* WHY is `rotateX` PRIVATE??? */
                blockpos$mutableblockpos.move(rand.nextBoolean() ? facing.rotateY() : facing.rotateY().getOpposite());
            }
            else
            { blockpos$mutableblockpos.move(facing); }

            /* Root Placement requires the below position to be solid, if it isn't, check if the direct lower position IS.*/
            if (!worldIn.getBlockState(blockpos$mutableblockpos.down()).isFullBlock())
            {
                if (worldIn.getBlockState(blockpos$mutableblockpos.down(2)).isFullBlock())
                { blockpos$mutableblockpos.move(EnumFacing.DOWN); }
                else { return; }
            }

            /* If the root crawler collides with a solid surface, then consider it finished.
            * This check ignores the first run, since the Tree can generate a thick stump around its base.
            * */
            if ( (!worldIn.getBlockState(blockpos$mutableblockpos).getMaterial().isReplaceable() || i != 0 && worldIn.getBlockState(blockpos$mutableblockpos) == LOG) || worldIn.getBlockState(blockpos$mutableblockpos).getMaterial().isLiquid()) return;

            if (this.placeRootAt(worldIn, blockpos$mutableblockpos, rand.nextInt(2 + i) == 0 ? 2 : 1))
            {
                if (!placedNodule && rand.nextInt(8 + i) == 0)
                { placeRootNoduleAt(worldIn, blockpos$mutableblockpos.down()); }
            }
        }
    }

    /* Places a Root Nodule at this position, and checks the flag. */
    private boolean placeRootNoduleAt(World worldIn, BlockPos pos)
    {
        if (blockNoduleReplaceable(worldIn.getBlockState(pos).getBlock()))
        {
            this.setBlockAndNotifyAdequately(worldIn, pos, ROOT_NODULE);
            placedNodule = true;
            return true;
        }

        return false;
    }

    /** If the given block is allowed to be replaced by a Nodule. */
    public boolean blockNoduleReplaceable(Block block)
    { return block == JTPGBlocks.PALE_MOSS || block == Blocks.GRASS || block == Blocks.DIRT; }

    /* Used for placing the actual Root Blocks. Returns 'true' if the block can be placed. */
    private boolean placeRootAt(World worldIn, BlockPos pos, int layerHeight)
    {
        IBlockState state = worldIn.getBlockState(pos);

        if (state.getMaterial().isReplaceable())
        {
            this.setBlockAndNotifyAdequately(worldIn, pos, ROOT.withProperty(BlockSuckerRoots.LAYERS, layerHeight));
            return true;
        }

        return false;
    }


    private void placeLogAt(World worldIn, BlockPos pos, boolean fullBark)
    {
        BlockLog.EnumAxis getAxis = fullBark ? BlockLog.EnumAxis.NONE : BlockLog.EnumAxis.Y;
        this.setBlockAndNotifyAdequately(worldIn, pos, LOG.withProperty(BlockLog.LOG_AXIS, getAxis));

        storedLogPositions.add(pos);
    }

    private void placeLeafAt(World worldIn, int x, int y, int z)
    { this.placeLeafAt(worldIn, worldIn.rand, new BlockPos(x,y,z)); }

    private void placeLeafAt(World worldIn, Random rand, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);

        if (state.getMaterial().isReplaceable())
        { this.setBlockAndNotifyAdequately(worldIn, pos, LEAF); }

        if (rand.nextFloat() < placeHangingMossChance) storedLeafPositions.add(pos);
    }

    /** Adds random leaves hanging from the bottom */
    private void placeRandomHangingLeaves(World worldIn, Random rand, BlockPos pos, int discWidth, int genChance)
    {
        for (int j3 = -discWidth; j3 <= discWidth; ++j3)
        {
            for (int i4 = -discWidth; i4 <= discWidth; ++i4)
            {
                if (worldIn.rand.nextInt(genChance) == 0 && worldIn.getBlockState(pos.add(j3, 1, i4)).isFullBlock())
                { placeLeafAt(worldIn, rand, pos.add(j3, 0, i4)); }
            }
        }
    }

    private void placeHangingMoss(World worldIn, Random rand)
    {
        /* If no leaves were saved to the list, just skip any Hanging Vine placement! */
        if (storedLeafPositions.isEmpty()) return;

        /* DO NOT utilize an enhanced For Loop, live alterations to the storedLeadPositions list WILL cause a ConcurrentModificationException! */
        for (int i = 0; i < storedLeafPositions.size(); i++)
        {
            BlockPos pos = storedLeafPositions.get(i);
            if (!worldIn.getBlockState(pos).getBlock().isLeaves(worldIn.getBlockState(pos), worldIn, pos)) continue;

            IBlockState state = worldIn.getBlockState(pos.down());
            if (state.getBlock().isAir(state, worldIn, pos.down()))
            { this.addHangingVine(worldIn, pos.down(), rand); }
        }
        storedLeafPositions.clear();
    }

    private void addHangingVine(World worldIn, BlockPos pos, Random rand)
    {
        this.setBlockAndNotifyAdequately(worldIn, pos, HANGING_MOSS);

        int i = rand.nextInt(4);
        for (BlockPos blockpos = pos.down(); worldIn.isAirBlock(blockpos) && i > 0; --i)
        {
            this.setBlockAndNotifyAdequately(worldIn, blockpos, HANGING_MOSS);
            blockpos = blockpos.down();
        }
    }

    private void placeCreakingHeart(World worldIn, Random rand)
    {
        /* If no leaves were saved to the list, just skip any Hanging Vine placement! */
        if (storedLogPositions.isEmpty()) return;

        /* DO NOT utilize an enhanced For Loop, live alterations to the storedLeadPositions list WILL cause a ConcurrentModificationException! */
        for (int i = 0; i < storedLogPositions.size(); i++)
        {
            boolean goodHeartSpot = true;
            BlockPos pos = storedLogPositions.get(i);

            for (EnumFacing facing: EnumFacing.values())
            {
                if (worldIn.getBlockState(pos.offset(facing)).getBlock() != JTPGBlocks.PALE_OAK_LOG) goodHeartSpot = false;
            }

            if (goodHeartSpot)
            {
                this.setBlockAndNotifyAdequately(worldIn, pos, CREAKING_HEART.withProperty(BlockCreakingHeart.NATURAL, rand.nextFloat() < this.creakingHeartNaturalChance));
                storedLogPositions.clear();
                return;
            }
        }
        storedLogPositions.clear();
    }

    private void onPlantGrow(World world, BlockPos pos, BlockPos source)
    {
        IBlockState state = world.getBlockState(pos);
        state.getBlock().onPlantGrow(state, world, pos, source);
    }
}