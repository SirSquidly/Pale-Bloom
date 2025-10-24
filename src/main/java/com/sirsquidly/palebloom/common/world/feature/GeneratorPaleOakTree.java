package com.sirsquidly.palebloom.common.world.feature;

import com.sirsquidly.palebloom.common.blocks.BlockCreakingHeart;
import com.sirsquidly.palebloom.config.ConfigCache;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import net.minecraft.block.*;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneratorPaleOakTree extends WorldGenAbstractTree
{
    public static final IBlockState LOG = JTPGBlocks.PALE_OAK_LOG.getDefaultState();
    public static final IBlockState CREAKING_HEART = JTPGBlocks.CREAKING_HEART.getDefaultState().withProperty(BlockCreakingHeart.NATURAL, true).withProperty(BlockCreakingHeart.AXIS, EnumFacing.Axis.Y);
    public static final IBlockState LEAF = JTPGBlocks.PALE_OAK_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE).withProperty(BlockLeaves.DECAYABLE, Boolean.TRUE);

    public static final IBlockState HANGING_MOSS = JTPGBlocks.PALE_HANGING_MOSS.getDefaultState();

    /** The chance for this tree to ATTEMPT to generate a Creaking Heart. */
    public float placeCreakingHeartChance;
    public float creakingHeartNaturalChance;
    public float dyingTreeChance;

    /** The chance to place a vine on a generated leaf. */
    public float placeHangingMossChance = 0.1F;
    /** A stored list of Leaf Positions, to later have Hanging Pale Moss randomly placed below them. */
    private final List<BlockPos> storedLeafPositions = new ArrayList<>();
    /** A stored list of Log Positions, to later have a Creaking Heart placed randomly within them. */
    private final List<BlockPos> storedLogPositions = new ArrayList<>();

    public GeneratorPaleOakTree(float placeCreakingHeartChanceIn, float creakingHeartNaturalChanceIn)
    { this(placeCreakingHeartChanceIn, creakingHeartNaturalChanceIn, 0F); }

    public GeneratorPaleOakTree(float placeCreakingHeartChanceIn, float creakingHeartNaturalChanceIn, float dyingChanceIn)
    {
        super(false);
        placeCreakingHeartChance = ConfigCache.crkHrt_enabled ? placeCreakingHeartChanceIn : 0.0F;
        creakingHeartNaturalChance = creakingHeartNaturalChanceIn;
        dyingTreeChance = dyingChanceIn;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos pos)
    {
        BlockPos blockpos = pos.down();
        IBlockState state = worldIn.getBlockState(blockpos);

        boolean isSoil = state.getBlock().canSustainPlant(state, worldIn, blockpos, net.minecraft.util.EnumFacing.UP, ((net.minecraft.block.BlockSapling)Blocks.SAPLING));

        int i = rand.nextInt(3) + rand.nextInt(2) + 6;
        int j = pos.getX();
        int k = pos.getY();
        int l = pos.getZ();

        if (pos.getY() < 1 || pos.getY() >= worldIn.getHeight() || !this.placeTreeOfHeight(worldIn, pos, i)) return false;

        if (!(isSoil && pos.getY() < worldIn.getHeight() - i - 1))
        { return false; }

        /* Mimics the Bedrock Edition Dyding Trees. */
        boolean isDying = rand.nextFloat() < this.dyingTreeChance;

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
            BlockPos[] trunkPositions = {blockpos1, blockpos1.east(), blockpos1.south(), blockpos1.east().south()};

            for (BlockPos trunkPos : trunkPositions)
            {
                IBlockState currentState = worldIn.getBlockState(trunkPos);
                if (currentState.getBlock().isAir(currentState, worldIn, trunkPos) || currentState.getBlock().isLeaves(currentState, worldIn, trunkPos))
                {
                    this.placeLogAt(worldIn, trunkPos, false);
                }
            }

            /* If this is a Dying Tree, move around the trunk and place Vines on every available side. */
            if (isDying)
            {
                for (BlockPos logPos : trunkPositions)
                {
                    for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL)
                    {
                        BlockPos vinePos = logPos.offset(facing);

                        IBlockState stateAt = worldIn.getBlockState(vinePos);
                        if (stateAt.getBlock().isAir(stateAt, worldIn, vinePos))
                        {
                            this.addVine(worldIn, vinePos, BlockVine.getPropertyFor(facing.getOpposite()));
                        }
                    }
                }
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
                if ((k3 < 0 || k3 > 1 || j4 < 0 || j4 > 1) && rand.nextInt(3) <= 0)
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
            { this.addHangingMoss(worldIn, pos.down(), rand); }
        }
        storedLeafPositions.clear();
    }

    /** Places Hanging Pale Moss, with some randomized length down.*/
    private void addHangingMoss(World worldIn, BlockPos pos, Random rand)
    {
        this.setBlockAndNotifyAdequately(worldIn, pos, HANGING_MOSS);

        int i = rand.nextInt(4);
        for (BlockPos blockpos = pos.down(); worldIn.isAirBlock(blockpos) && i > 0; --i)
        {
            this.setBlockAndNotifyAdequately(worldIn, blockpos, HANGING_MOSS);
            blockpos = blockpos.down();
        }
    }

    /** Places a Vanilla Vine.*/
    private void addVine(World worldIn, BlockPos pos, PropertyBool prop)
    {
        IBlockState iblockstate = Blocks.VINE.getDefaultState().withProperty(prop, Boolean.TRUE);
        this.setBlockAndNotifyAdequately(worldIn, pos, iblockstate);
    }

    /** Places a Creaking Heart, anywhere within the position of placed logs, where logs would surround all sides of the position. */
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