package com.sirsquidly.palebloom.blocks.base;

import com.sirsquidly.palebloom.init.JTPGBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockJTPGBlock extends Block
{
    public BlockJTPGBlock(Material blockMaterialIn, MapColor blockMapColorIn, SoundType soundIn)
    {
        super(blockMaterialIn, blockMapColorIn);
        this.setSoundType(soundIn);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        Item item = itemstack.getItem();
        Block thisBlock = state.getBlock();

        if (thisBlock != JTPGBlocks.PALE_PUMPKIN) return false;

        if (item instanceof ItemShears)
        {
            if (facing != EnumFacing.UP && facing != EnumFacing.DOWN)
            {
                worldIn.setBlockState(pos, JTPGBlocks.PALE_CARVED_PUMPKIN.getDefaultState().withProperty(BlockHorizontal.FACING, facing));
            }
            else
            {
                worldIn.setBlockState(pos, JTPGBlocks.PALE_CARVED_PUMPKIN.getDefaultState().withProperty(BlockHorizontal.FACING, playerIn.getHorizontalFacing().getOpposite()));
            }

            worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_WOOD_HIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return true;
        }
        return false;
    }
}
