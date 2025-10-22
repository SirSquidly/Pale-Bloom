package com.sirsquidly.palebloom.item;

import com.sirsquidly.palebloom.blocks.BlockResinClump;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.init.JTPGSounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

public class ItemResinClump extends Item
{

    public ItemResinClump() { super(); }
    
    //I have looked into the deeper depths and sculk veins have stared back
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking() |! player.canPlayerEdit(pos, side, stack) || stack.getItem() != this)  return EnumActionResult.PASS;
        pos = pos.offset(side);
        IBlockState state = world.getBlockState(pos);
        EnumFacing facing = side.getOpposite();
        if (state.getBlock() instanceof BlockResinClump)
        {
            if (BlockResinClump.hasFacing(state, facing)) return EnumActionResult.PASS;
            /* Added a bonus check, so facing only gets updated if the face can allow the Resin to be placed against it. */
            if (JTPGBlocks.RESIN_CLUMPS[0].canPlaceBlockOnSide(world, pos, side)) state = BlockResinClump.getBlockState(ArrayUtils.add(BlockResinClump.getFacings(state), facing));
        }
        else if (!world.mayPlace(JTPGBlocks.RESIN_CLUMPS[0], pos, false, side, player)) return EnumActionResult.PASS;
        else state = BlockResinClump.getBlockState(facing);
        if (!world.setBlockState(pos, state, 11)) return EnumActionResult.PASS;

        SoundType sound = JTPGSounds.RESIN;
        world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1) / 2f, sound.getPitch() * 0.8f);
        stack.shrink(1);
        if (player instanceof EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
        return EnumActionResult.SUCCESS;
    }
    
}
