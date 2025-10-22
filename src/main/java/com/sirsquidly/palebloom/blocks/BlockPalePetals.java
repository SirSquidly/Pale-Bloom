package com.sirsquidly.palebloom.blocks;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockPalePetals extends BlockBush
{
    public static final PropertyInteger AMOUNT = PropertyInteger.create("amount", 1, 4);
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    /* Bounding Boxes for 1 or 2 `amount` Petals, while 3 or 4 uses the full box. Sorted by EnumFacing index (S W N E). */
    private final AxisAlignedBB[] PETALS_1_AABB = new AxisAlignedBB[]
            {
                    new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 0.1875D, 1.0D),
                    new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 0.1875D, 1.0D),
                    new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.1875D, 0.5D),
                    new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.1875D, 0.5D)
            };
    private final AxisAlignedBB[] PETALS_2_AABB = new AxisAlignedBB[]
            {
                    new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D),
                    new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 0.1875D, 1.0D),
                    new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.1875D, 1.0D),
                    new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 0.5D)
            };
    protected static final AxisAlignedBB PETALS_FULL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);


    public BlockPalePetals()
    {
        super(Material.PLANTS, MapColor.ADOBE);
        this.setSoundType(SoundType.PLANT);
        this.setTickRandomly(true);
        //setSoundType(BOMDSoundTypes.MOSS);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        int i = state.getValue(AMOUNT);
        if (i > 2) return PETALS_FULL_AABB;

        int facingInd = state.getValue(FACING).getHorizontalIndex();
        return i == 1 ? PETALS_1_AABB[facingInd] : PETALS_2_AABB[facingInd];
    }

    @Override public int quantityDropped(IBlockState state, int fortune, Random random){ return state.getValue(AMOUNT); }

    public IBlockState withRotation(IBlockState state, Rotation rot)
    { return state.withProperty(FACING, rot.rotate(state.getValue(FACING))); }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    { return state.withRotation(mirrorIn.toRotation(state.getValue(FACING))); }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    { return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()); }

    public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(AMOUNT, (meta & 3) + 1).withProperty(FACING, EnumFacing.byHorizontalIndex(meta >> 2)); }

    public int getMetaFromState(IBlockState state)
    {
        int i = state.getValue(AMOUNT) - 1;
        i |= state.getValue(FACING).getHorizontalIndex() << 2;
        return i;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, AMOUNT, FACING);
    }
}