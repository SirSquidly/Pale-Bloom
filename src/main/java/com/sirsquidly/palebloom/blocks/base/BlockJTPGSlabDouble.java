package com.sirsquidly.palebloom.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.Random;

public class BlockJTPGSlabDouble extends BlockJTPGSlab
{
	private final Block block;

	public BlockJTPGSlabDouble(Block block, Material materialIn, SoundType soundIn, float hardnessIn, float resistenceIn)
    {
        super(materialIn, soundIn, hardnessIn, resistenceIn);
        this.block = block;
		this.setCreativeTab(null);
    }

	/** Why, yes, this IS a short useless file, but GOD I suffered too much trying to condense this in the normal slab class.*/
	public boolean isDouble()
	{
		return true;
	}

	/** Just return the base Slab when picking the Double Slab. */
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{ return new ItemStack(Item.getItemFromBlock(block)); }

	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    { return Item.getItemFromBlock(block); }
}