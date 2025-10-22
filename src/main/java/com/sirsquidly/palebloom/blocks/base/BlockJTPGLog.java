package com.sirsquidly.palebloom.blocks.base;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockJTPGLog extends BlockLog
{
    public static final PropertyEnum<EnumAxis> LOG_AXIS = PropertyEnum.create("axis", EnumAxis.class);

    public BlockJTPGLog()
    {
        super();
        this.setHarvestLevel("axe", 0);
    }

    @Deprecated
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return MapColor.QUARTZ;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(LOG_AXIS, EnumAxis.values()[meta & 3]); }

    @Override
    public int getMetaFromState(IBlockState state) { return state.getValue(LOG_AXIS).ordinal(); }

    @Override
    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, LOG_AXIS); }

    public ImmutableList<IBlockState> getProperties()
    {
        return this.blockState.getValidStates();
    }
}