package com.sirsquidly.palebloom.blocks;

import com.google.common.collect.Lists;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.world.WorldPaleGarden;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlockBramble extends BlockBush implements IGrowable
{
    protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    public BlockBramble()
    {
        super(Material.PLANTS, MapColor.GREEN_STAINED_HARDENED_CLAY);
        this.setSoundType(SoundType.PLANT);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return SAPLING_AABB;
    }

    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!(entityIn instanceof EntityLivingBase) || WorldPaleGarden.isPaleEntity(entityIn)) return;

        double dx = entityIn.posX - entityIn.prevPosX;
        double dy = entityIn.posY - entityIn.prevPosY;
        double dz = entityIn.posZ - entityIn.prevPosZ;
        boolean isMoving = (dx * dx + dy * dy + dz * dz) > 0.0001D;

        if (isMoving)
        {
            entityIn.attackEntityFrom(DamageSource.CACTUS, 1.0F);
        }
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL)
        { if (checkWithinColumn(worldIn, pos.offset(facing)) != null) return true; }
        return false;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL)
        { if (checkWithinColumn(worldIn, pos.offset(facing)) != null) return true; }
        return false;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        List<EnumFacing> list = Lists.newArrayList(EnumFacing.Plane.HORIZONTAL);
        Collections.shuffle(list, rand);

        for (EnumFacing facing : list)
        {
            BlockPos checkPos = checkWithinColumn(worldIn, pos.offset(facing));
            if (checkPos != null)
            {
                worldIn.playSound(null, checkPos, this.getSoundType().getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                worldIn.setBlockState(checkPos, this.getDefaultState());
                return;
            }
        }
    }

    /** Returns the best position within a 3 block column. Returns null if no good position is in this column. */
    public BlockPos checkWithinColumn(World world, BlockPos pos)
    {
        if (world.isAirBlock(pos))
        {
            if (world.isAirBlock(pos.down()))
            {
                return canPlaceBlockAt(world, pos.down()) ? pos.down() : null;
            }
            else return canPlaceBlockAt(world, pos) ? pos : null;
        }
        else return canPlaceBlockAt(world, pos.up()) ? pos.up() : null;
    }

    public EnumOffsetType getOffsetType()
    {
        return EnumOffsetType.XYZ;
    }
}