package com.sirsquidly.palebloom.item;

import com.sirsquidly.palebloom.world.WorldPaleGarden;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPaleShovel extends ItemSpade
{
    boolean isNight;

    public ItemPaleShovel(ToolMaterial material)
    {
        super(material);

        this.addPropertyOverride(new ResourceLocation("awake"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            { return isNight ? 1 : 0; }
        });
    }

    /** Called each Inventory Tick, same thing the Map does. This auto-sets the random sound if it doesn't have one.*/
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (entityIn.ticksExisted % 20 != 0) return;

        if (WorldPaleGarden.isNight(worldIn)) isNight = true;
        else isNight = false;

        if (stack.getItemDamage() > 0)
        {
            if (WorldPaleGarden.requestBulbResin(worldIn, entityIn.getPosition().up(), 1, true).resinPulled == 1) this.setDamage(stack, this.getDamage(stack) - 1);
        }
    }

    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        float base = super.getDestroySpeed(stack, state);

        if (this.isNight) return base * 2.0F;
        return base;
    }

    public int getHarvestLevel(ItemStack stack, String toolClass, EntityPlayer player, IBlockState blockState)
    {
        int base = super.getHarvestLevel(stack, toolClass,  player, blockState);

        if (isNight) return base + 1;
        return base;
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        if (attacker.world.isRemote) return false;
        if (!this.isNight) return true;


        return true;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add("");

        if (isNight)
        {
            tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.night_title"));
            tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.night_harvest"));
            tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.night_mine_speed"));
            tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.night_repair"));
            tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_shovel.night"));
        }
        else
        { tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.day")); }

    }
}