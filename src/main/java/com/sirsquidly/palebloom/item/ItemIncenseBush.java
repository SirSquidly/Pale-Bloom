package com.sirsquidly.palebloom.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemIncenseBush  extends ItemBlock
{
    public ItemIncenseBush(Block block)
    {
        super(block);
        this.setRegistryName(block.getRegistryName());
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
    {
        NBTTagCompound nbttagcompound = stack.getTagCompound();

        if (nbttagcompound != null && nbttagcompound.hasKey("potion"))
        {
            Potion potion = Potion.getPotionFromResourceLocation(nbttagcompound.getString("potion"));

            tooltip.add((potion.isBadEffect() ? TextFormatting.RED : TextFormatting.BLUE) + I18n.translateToLocal(potion.getName()));
        }
        else
        {
            tooltip.add(TextFormatting.GRAY + I18n.translateToLocal("effect.none").trim());
        }
    }
}
