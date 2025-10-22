package com.sirsquidly.palebloom.item;

import com.sirsquidly.palebloom.Config;
import com.sirsquidly.palebloom.init.JTPGItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemJTPGFood extends ItemFood
{
    /** Number of ticks to run while 'EnumAction'ing until result. */
    public int useDuration;

    private static final int configNightlightBulbEffectLength = Config.item.foods.nightlightBulbEffectLength;
    private static final int configPalePumpkinPieEffectLength = Config.item.foods.palePumpkinPieEffectLength;

    public ItemJTPGFood(int amount, float saturation, boolean isWolfFood)
    { this(amount, saturation, isWolfFood, 32); }

    public ItemJTPGFood(int amount, float saturation, boolean isWolfFood, int useDuration)
    {
        super(amount, saturation, isWolfFood);
        this.useDuration = useDuration;
    }

    /** Effects of the Live Root are hard-coded, similar to how `ItemAppleGold` works. */
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
        super.onFoodEaten(stack, worldIn, player);

        if (!worldIn.isRemote && stack.getItem() == JTPGItems.LIVE_ROOT)
        {
            player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 300));
            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 300));
            player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 300));
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) { return this.useDuration; }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
    {
        if (stack.getItem() == JTPGItems.NIGHTLIGHT_BULB && configNightlightBulbEffectLength > 0)
        { tooltip.add(TextFormatting.BLUE + I18n.translateToLocalFormatted("description.palebloom.nightlight_bulb.effect", StringUtils.ticksToElapsedTime(configNightlightBulbEffectLength))); }
        else if (stack.getItem() == JTPGItems.PALE_PUMPKIN_PIE && configPalePumpkinPieEffectLength > 0)
        { tooltip.add(TextFormatting.BLUE + I18n.translateToLocalFormatted("description.palebloom.pale_pumpkin_pie.effect", StringUtils.ticksToElapsedTime(configPalePumpkinPieEffectLength))); }
    }
}