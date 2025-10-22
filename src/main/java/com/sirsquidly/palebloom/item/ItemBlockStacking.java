package com.sirsquidly.palebloom.item;

import com.sirsquidly.palebloom.blocks.BlockPalePetals;
import com.sirsquidly.palebloom.blocks.BlockSuckerRoots;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockStacking extends ItemBlock
{
	public ItemBlockStacking(Block block)
	{
        super(block);
        this.setRegistryName(block.getRegistryName());
        this.setMaxDamage(0);
    }

	/**
     * This is mostly copied from ItemSnow
     */
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack))
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();
            BlockPos blockpos = pos;

            if ((block != this.block) && !block.isReplaceable(worldIn, pos))
            {
                blockpos = pos.offset(facing);
                iblockstate = worldIn.getBlockState(blockpos);
                block = iblockstate.getBlock();
            }

            if (block == this.block)
            {
                if (!isMatchAtMax(iblockstate))
                {
                    int i = iblockstate.getValue(getPropertyStacking());
                    IBlockState iblockstate1 = iblockstate.withProperty(getPropertyStacking(), i + 1);
                    AxisAlignedBB axisalignedbb = iblockstate1.getCollisionBoundingBox(worldIn, blockpos);

                    if ((axisalignedbb == Block.NULL_AABB || worldIn.checkNoEntityCollision(axisalignedbb.offset(blockpos))) && worldIn.setBlockState(blockpos, iblockstate1, 10))
                    {
                        SoundType soundtype = this.block.getSoundType(iblockstate1, worldIn, pos, player);
                        worldIn.playSound(player, blockpos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

                        if (player instanceof EntityPlayerMP)
                        { CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, itemstack); }
                        /* Runs any `onBlockPlacedBy` logic associated with the given block*/
                        iblockstate.getBlock().onBlockPlacedBy(worldIn, pos, iblockstate, player, itemstack);

                        itemstack.shrink(1);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }

            return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }
        else
        { return EnumActionResult.FAIL; }
    }

    /** Gives us the proper Property to be checking and adjusting. */
    public PropertyInteger getPropertyStacking()
    {
        if (block == JTPGBlocks.PALE_PETALS) return BlockPalePetals.AMOUNT;
        if (block == JTPGBlocks.SUCKER_ROOTS) return BlockSuckerRoots.LAYERS;
        return null;
    }

    /**
     * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is
     * placed as a Block (mostly used with ItemBlocks).
     */
    public int getMetadata(int damage)
    { return damage; }

    /** If this given state (which matches the current block) is at the max value. */
    public boolean isMatchAtMax(IBlockState state)
    {
        if (state.getBlock() != this.block) return false;

        int i = state.getValue(getPropertyStacking());
    	
    	if (this.block == JTPGBlocks.PALE_PETALS) return i >= 4;
    	else if (this.block == JTPGBlocks.SUCKER_ROOTS) return i >= 2;
    	else
    	{ return false; }
    }

    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack)
    {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == this.block && !isMatchAtMax(state) || super.canPlaceBlockOnSide(world, pos, side, player, stack);
    }
}
