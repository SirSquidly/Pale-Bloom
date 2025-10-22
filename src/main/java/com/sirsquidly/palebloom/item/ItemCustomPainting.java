package com.sirsquidly.palebloom.item;

import com.sirsquidly.palebloom.entity.item.EntityPalePainting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemHangingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemCustomPainting extends ItemHangingEntity
{
    public ItemCustomPainting()
    {
        super(EntityPalePainting.class);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        BlockPos offsetPos = pos.offset(facing);

        if (facing != EnumFacing.DOWN && facing != EnumFacing.UP && player.canPlayerEdit(offsetPos, facing, itemstack))
        {
            EntityHanging entity = new EntityPalePainting(world, offsetPos, facing);

            if (entity != null && entity.onValidSurface())
            {
                if (!world.isRemote)
                {
                    entity.playPlaceSound();
                    world.spawnEntity(entity);
                }

                itemstack.shrink(1);

                return EnumActionResult.SUCCESS;
            }
            else
            { return EnumActionResult.FAIL; }
        }
        else
        { return EnumActionResult.FAIL; }
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.COMMON;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
    {
        tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.palebloom.painting_1.name"));
        tooltip.add(TextFormatting.GRAY + I18n.translateToLocal("description.palebloom.painting_1.artist"));
        tooltip.add(TextFormatting.WHITE + I18n.translateToLocal("description.palebloom.painting_1.dimensions"));
    }
}
