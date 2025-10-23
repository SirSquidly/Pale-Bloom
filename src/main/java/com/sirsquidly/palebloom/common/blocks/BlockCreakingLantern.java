package com.sirsquidly.palebloom.common.blocks;

import com.sirsquidly.palebloom.common.blocks.base.BlockJTPGPumpkin;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.paleBloom;
import com.sirsquidly.palebloom.common.world.WorldPaleGarden;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCreakingLantern extends BlockJTPGPumpkin implements IEyeblossomListener
{
    public static final PropertyBool AWAKE = PropertyBool.create("awake");

    /** A multidimensional array for the offsets of the three Resin particles. From left to right. */
    private final Vec3d[][] particleSpot = new Vec3d[][]
    {
            new Vec3d[] {new Vec3d(0.66, 0.5, 1.05), new Vec3d(0.42, 0.2, 1.05), new Vec3d(0.35, 0.66, 1.05)},
            new Vec3d[] {new Vec3d(-0.05, 0.5, 0.66), new Vec3d(-0.05, 0.2, 0.42), new Vec3d(-0.05, 0.66, 0.35)},
            new Vec3d[] {new Vec3d(0.33, 0.5, -0.05), new Vec3d(0.58, 0.2, -0.05), new Vec3d(0.64, 0.66, -0.05)},
            new Vec3d[] {new Vec3d(1.05, 0.5, 0.33), new Vec3d(1.05, 0.2, 0.58), new Vec3d(1.05, 0.66, 0.64)}
    };

    public BlockCreakingLantern()
    {
        this.setTickRandomly(true);
        setDefaultState(blockState.getBaseState().withProperty(AWAKE, false));
    }

    /** Controls the 'awake' value of the double plant. */
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    { preformSwapping(worldIn, pos, false); }

    public void preformSwapping(World worldIn, BlockPos pos, boolean isResponding)
    {
        if (worldIn.isRemote) return;

        boolean isNight = WorldPaleGarden.isNight(worldIn);
        IBlockState state = worldIn.getBlockState(pos);

        /** Awake and Night are both a boolean, so check if they match to see if this needs updating. */
        if (state.getValue(AWAKE) == isNight) return;

        SoundEvent sound = isNight ? JTPGSounds.BLOCK_EYEBLOSSOM_OPEN : JTPGSounds.BLOCK_EYEBLOSSOM_CLOSE;

        worldIn.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
        worldIn.setBlockState(pos, state.withProperty(AWAKE, isNight), 2);

        int facingIndex = state.getValue(FACING).getHorizontalIndex();

        for (int i = 0; i <= 2; i++)
        {
            double hx = pos.getX() + particleSpot[facingIndex][i].x + (worldIn.rand.nextDouble() * 0.5 - 0.25);
            double hy = pos.getY() + particleSpot[facingIndex][i].y + 2 + (worldIn.rand.nextDouble() * 0.5 - 0.25);
            double hz = pos.getZ() + particleSpot[facingIndex][i].z + (worldIn.rand.nextDouble() * 0.5 - 0.25);

            paleBloom.proxy.spawnParticle(0, worldIn, pos.getX() + particleSpot[facingIndex][i].x, pos.getY() + particleSpot[facingIndex][i].y, pos.getZ() + particleSpot[facingIndex][i].z, hx, hy, hz, isNight ? 1 : 0);
        }
    }


    public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3)).withProperty(AWAKE, (meta & 4) != 0); }

    public int getMetaFromState(IBlockState state)
    {
        int i = state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(AWAKE))  i |= 4;
        return i;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, AWAKE, FACING);
    }
}