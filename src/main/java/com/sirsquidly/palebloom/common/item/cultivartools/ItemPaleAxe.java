package com.sirsquidly.palebloom.common.item.cultivartools;

import com.sirsquidly.palebloom.common.entity.item.EntityThorn;
import com.sirsquidly.palebloom.common.world.WorldPaleGarden;
import com.sirsquidly.palebloom.config.ConfigCache;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPaleAxe extends ItemAxe
{
    boolean isNight;

    public ItemPaleAxe(ToolMaterial material)
    {
        super(material, 8, -3.2F);

        this.attackDamage = isNight ? 9 : 8;

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
        else
        {
            isNight = false;
            return;
        }

        if (ConfigCache.cultAxeAwakeBulbHealing && stack.getItemDamage() > 0)
        {
            if (WorldPaleGarden.requestBulbResin(worldIn, entityIn.getPosition().up(), 1, true).resinPulled == 1) this.setDamage(stack, this.getDamage(stack) - 1);
        }
    }

    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        float base = super.getDestroySpeed(stack, state);

        if (this.isNight) return base * ConfigCache.cultAxeAwakeMiningSpeed;
        return base;
    }

    public int getHarvestLevel(ItemStack stack, String toolClass, EntityPlayer player, IBlockState blockState)
    {
        int base = super.getHarvestLevel(stack, toolClass,  player, blockState);

        if (isNight) return base + ConfigCache.cultAxeAwakeHarvestLevel;
        return base;
    }

    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        if (state.getBlock() instanceof BlockLog && !entityLiving.isSneaking() && ConfigCache.cultAxeAwakeThornBurst)
        {
            if (entityLiving instanceof EntityPlayer)
            {
                if (((EntityPlayer)entityLiving).getCooldownTracker().getCooldown(stack.getItem(), 0) > 0) return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
                ((EntityPlayer)entityLiving).getCooldownTracker().setCooldown(stack.getItem(), ConfigCache.cultAxeAwakeThornBurstCooldown);
            }

            worldIn.setBlockToAir(pos);

            worldIn.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, SoundCategory.BLOCKS, 0.7F, 1.0F);
            for (int i = 0; i < 30; i++)
            {
                spawnThorn(worldIn, pos, entityLiving);
            }
        }

        return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }

    public void spawnThorn(World world, BlockPos pos, EntityLivingBase attacker)
    {
        if (world.isRemote) return;
        EntityThorn thorn = new EntityThorn(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, attacker);

        double shootX = attacker.getRNG().nextDouble() * 2.0 - 1.0;
        double shootY = attacker.getRNG().nextDouble() * 2.0 - 1.0;
        double shootZ = attacker.getRNG().nextDouble() * 2.0 - 1.0;

        thorn.shoot(shootX, shootY, shootZ, 0.5F, 0);

        world.spawnEntity(thorn);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add("");

        if (isNight)
        {
            tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.night_title"));
            tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.night_harvest", ConfigCache.cultAxeAwakeHarvestLevel));
            tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.night_mine_speed", ConfigCache.cultAxeAwakeMiningSpeed));
            if (ConfigCache.cultAxeAwakeBulbHealing) tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.night_repair"));
            if (ConfigCache.cultAxeAwakeThornBurst) tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_axe.night"));
        }
        else
        { tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.day")); }

    }
}