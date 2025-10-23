package com.sirsquidly.palebloom.common.item.cultivartools;

import com.sirsquidly.palebloom.common.world.WorldPaleGarden;
import com.sirsquidly.palebloom.config.ConfigCache;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/** Functions are preformed using Events within 'CommonEvents'.
 * `onLeftClickWithPaleHoe` = Triggers when a player left-clicks a block using the Pale Hoe, at night
 * */
public class ItemPaleHoe extends ItemHoe
{
    static boolean isNight;

    public ItemPaleHoe(ToolMaterial material)
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

        if (ConfigCache.cultHoeAwakeBulbHealing && stack.getItemDamage() > 0)
        {
            if (WorldPaleGarden.requestBulbResin(worldIn, entityIn.getPosition().up(), 1, true).resinPulled == 1) this.setDamage(stack, this.getDamage(stack) - 1);
        }
    }

    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        float base = super.getDestroySpeed(stack, state);

        if (isNight) return base * ConfigCache.cultHoeAwakeMiningSpeed;
        return base;
    }

    public int getHarvestLevel(ItemStack stack, String toolClass, EntityPlayer player, IBlockState blockState)
    {
        int base = super.getHarvestLevel(stack, toolClass,  player, blockState);

        if (isNight) return base + ConfigCache.cultHoeAwakeHarvestLevel;
        return base;
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        if (attacker.world.isRemote) return false;
        if (!ConfigCache.cultHoeAwakeGardenCall || !this.isNight) return super.hitEntity(stack, target, attacker);

        if (attacker instanceof EntityPlayer)
        {
            if (((EntityPlayer)attacker).getCooldownTracker().getCooldown(stack.getItem(), 0) > 0) return super.hitEntity(stack, target, attacker);
            ((EntityPlayer)attacker).getCooldownTracker().setCooldown(stack.getItem(), ConfigCache.cultHoeAwakeGardenCallCooldown);
        }

        int aidDistance = 64;
        boolean flag = true;

        for (EntityLiving entity : attacker.world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(attacker.getPosition().add(-aidDistance, -aidDistance, -aidDistance), attacker.getPosition().add(aidDistance + 1, aidDistance + 1, aidDistance + 1))))
        {
            if (!WorldPaleGarden.isPaleEntity(entity)) continue;

            if (entity.getAttackTarget() == null && !entity.isOnSameTeam(target))
            {
                entity.setRevengeTarget(target);
                WorldPaleGarden.preformGardenCallEffects(attacker.world, entity, target, flag);
                flag = false;
            }
        }

        return super.hitEntity(stack, target, attacker);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add("");

        if (isNight)
        {
            tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.night_title"));
            tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.night_harvest", ConfigCache.cultHoeAwakeHarvestLevel));
            tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.night_mine_speed", ConfigCache.cultHoeAwakeMiningSpeed));
            if (ConfigCache.cultHoeAwakeBulbHealing) tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.night_repair"));
            if (ConfigCache.cultHoeAwakeGardenCall) tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_hoe.night"));
        }
        else
        { tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.day")); }

    }
}