package com.sirsquidly.palebloom.blocks;

import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.world.feature.WorldGenMoss;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.EnumPlantType;

import java.util.Random;

public class BlockPaleMossBlock extends Block implements IGrowable
{
    public BlockPaleMossBlock()
    {
        super(Material.GRASS, MapColor.SILVER);
        this.setSoundType(JTPGSounds.MOSS);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    /** Allow anything but a Cactus */
    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable)
    {
        if (plantable.getPlantType(world, pos) == EnumPlantType.Plains) return true;
        /* We love Dead Bushes in this household */
        if (plantable.getPlant(world, pos).getBlock() == Blocks.DEADBUSH) return true;
        /* Beach-type plants like Sugar Cane have a hardcoded check, so we need to implement our own water check */
        if (plantable.getPlantType(world, pos) == EnumPlantType.Beach)
        {
            for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL)
            { if (world.getBlockState(pos.offset(facing)).getMaterial() == Material.WATER) return true; }
        }
        return super.canSustainPlant(state, world, pos, direction, plantable);
    }

    /** Pistons break it. */
    @Override
    public EnumPushReaction getPushReaction(IBlockState state)
    { return EnumPushReaction.DESTROY; }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    { return worldIn.isAirBlock(pos.up()); }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    { return worldIn.isAirBlock(pos.up()); }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        WorldGenerator mossGen = new WorldGenMoss(7, 0.6F);

        mossGen.generate(worldIn, rand, pos);
    }
}