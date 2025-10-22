package com.sirsquidly.palebloom.blocks.base;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockJTPGPumpkin extends BlockHorizontal
{
    public BlockJTPGPumpkin()
    {
        super(Material.GOURD, MapColor.WHITE_STAINED_HARDENED_CLAY);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    { return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && worldIn.isSideSolid(pos.down(), EnumFacing.UP); }

    public IBlockState withRotation(IBlockState state, Rotation rot)
    { return state.withProperty(FACING, rot.rotate(state.getValue(FACING))); }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    { return state.withRotation(mirrorIn.toRotation(state.getValue(FACING))); }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    { return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()); }

    public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta)); }

    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }
}