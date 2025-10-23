package com.sirsquidly.palebloom.common.blocks;

import com.sirsquidly.palebloom.common.blocks.base.BlockJTPGDoublePlant;
import com.sirsquidly.palebloom.common.blocks.tileentity.TileReapingWillowSapling;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockReapingWillowSapling extends BlockJTPGDoublePlant
{
    public static final PropertyBool AWAKE = PropertyBool.create("awake");
    public static final PropertyEnum<BlockReapingWillowSapling.EnumBlockHalf> HALF = PropertyEnum.create("half", BlockReapingWillowSapling.EnumBlockHalf.class);
    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 7);

    protected static final AxisAlignedBB BUSH_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);
    protected static final AxisAlignedBB BUSH_SLAB_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.5D, 0.875D);

    public BlockReapingWillowSapling()
    {
        this.setTickRandomly(true);

        setDefaultState(blockState.getBaseState().withProperty(AWAKE, false).withProperty(HALF, EnumBlockHalf.LOWER).withProperty(STAGE, 0));
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        int i = state.getValue(STAGE);
        return state.getValue(HALF) == EnumBlockHalf.UPPER && (i == 4 || i == 5) ? BUSH_SLAB_AABB : BUSH_AABB;
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    { return state.getValue(STAGE) > 3 ? super.canBlockStay(worldIn, pos, state) : this.canSustainBush(worldIn.getBlockState(pos.down())); }

    /** Does the placeAt when placed. */
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    { return this.getDefaultState().withProperty(AWAKE, false).withProperty(HALF, EnumBlockHalf.LOWER).withProperty(STAGE, 0); }

    /** Places the double tall plant. */
    public void placeDoubleAt(World worldIn, BlockPos lowerPos, boolean awake, int stage, int flags)
    {
        worldIn.setBlockState(lowerPos, this.getDefaultState().withProperty(AWAKE, awake).withProperty(HALF, EnumBlockHalf.LOWER).withProperty(STAGE, stage), flags);
        if (stage > 3) worldIn.setBlockState(lowerPos.up(), this.getDefaultState().withProperty(AWAKE, awake).withProperty(HALF, EnumBlockHalf.UPPER).withProperty(STAGE, stage), flags);
    }

    public boolean hasTileEntity(IBlockState state) { return true; }

    @Nullable
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        if (state.getValue(HALF) == EnumBlockHalf.LOWER) return new TileReapingWillowSapling();
        return null;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumBlockHalf half = (meta & 8) == 0 ? EnumBlockHalf.LOWER : EnumBlockHalf.UPPER;
        int stage = meta & 7;
        return this.getDefaultState().withProperty(HALF, half).withProperty(STAGE, stage);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = state.getValue(STAGE);
        if (state.getValue(HALF) == EnumBlockHalf.UPPER) meta |= 8;
        return meta;
    }

    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, AWAKE, HALF, STAGE); }
}