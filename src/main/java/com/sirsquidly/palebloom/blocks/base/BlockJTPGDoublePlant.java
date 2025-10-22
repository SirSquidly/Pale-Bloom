package com.sirsquidly.palebloom.blocks.base;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

import java.util.Random;

public class BlockJTPGDoublePlant extends BlockBush
{
    public static final PropertyEnum<EnumBlockHalf> HALF = PropertyEnum.create("half", BlockJTPGDoublePlant.EnumBlockHalf.class);

    public BlockJTPGDoublePlant()
    {
        super(Material.GRASS);
        this.setSoundType(SoundType.PLANT);
    }

    /** If the same block is below, then this is a top block, simple. */
    public boolean isTop(IBlockAccess world, BlockPos pos)
    { return world.getBlockState(pos.down()).getBlock() == this; }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    { return isTop(worldIn, pos) || super.canBlockStay(worldIn, pos, state) && worldIn.getBlockState(pos.up()).getBlock() == this; }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    { return super.canBlockStay(worldIn, pos, this.getDefaultState()) && worldIn.isAirBlock(pos.up()); }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    { return state.getValue(HALF) == EnumBlockHalf.LOWER ? super.getItemDropped(state, rand, fortune) : null; }

    public EnumOffsetType getOffsetType()
    { return EnumOffsetType.XZ; }

    @Override
    public net.minecraftforge.common.EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
    { return EnumPlantType.Plains; }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos)
    {
        return this.getDefaultState();
    }

    public static enum EnumBlockHalf implements IStringSerializable
    {
        UPPER,
        LOWER;

        public String toString()
        {
            return this.getName();
        }
        public String getName()
        { return this == UPPER ? "upper" : "lower"; }
    }
}