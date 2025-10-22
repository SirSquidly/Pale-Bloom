package com.sirsquidly.palebloom.item;

import com.sirsquidly.palebloom.entity.EntityMannequin;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class ItemMannequin extends Item
{
    private static final String[] VARIANT_KEYS = new String[] { "mannequin", "mannequin_dead" };

    public ItemMannequin()
    {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.hasSubtypes = true;
    }

    /** Override name for the other head variants! */
    public String getTranslationKey(ItemStack stack)
    { return "item." + paleBloom.MOD_ID + "." + VARIANT_KEYS[Math.min(stack.getMetadata(), VARIANT_KEYS.length - 1)]; }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (facing == EnumFacing.DOWN)
        { return EnumActionResult.FAIL; }
        else
        {
            boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
            BlockPos blockpos = flag ? pos : pos.offset(facing);
            ItemStack itemstack = player.getHeldItem(hand);

            if (!player.canPlayerEdit(blockpos, facing, itemstack))
            { return EnumActionResult.FAIL; }
            else
            {
                BlockPos blockpos1 = blockpos.up();
                boolean flag1 = !worldIn.isAirBlock(blockpos) && !worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
                flag1 = flag1 | (!worldIn.isAirBlock(blockpos1) && !worldIn.getBlockState(blockpos1).getBlock().isReplaceable(worldIn, blockpos1));

                if (flag1)
                { return EnumActionResult.FAIL; }
                else
                {
                    double d0 = blockpos.getX();
                    double d1 = blockpos.getY();
                    double d2 = blockpos.getZ();
                    List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(d0, d1, d2, d0 + 1.0D, d1 + 2.0D, d2 + 1.0D));

                    if (!list.isEmpty())
                    { return EnumActionResult.FAIL; }
                    else
                    {
                        if (!worldIn.isRemote)
                        {
                            ItemStack stack = player.getHeldItem(hand);

                            worldIn.setBlockToAir(blockpos);
                            worldIn.setBlockToAir(blockpos1);

                            float f = (float) MathHelper.floor((MathHelper.wrapDegrees(player.rotationYaw - 180.0F) + 22.5F) / 45.0F) * 45.0F;

                            EntityMannequin entityMannequin = new EntityMannequin(worldIn, d0 + 0.5D, d1, d2 + 0.5D, f);
                            entityMannequin.setSEEN(true);

                            entityMannequin.setInactive(stack.getMetadata() > 0);
                            entityMannequin.setDropChance(EntityEquipmentSlot.MAINHAND, 100);
                            entityMannequin.setDropChance(EntityEquipmentSlot.OFFHAND, 100);
                            entityMannequin.setDropChance(EntityEquipmentSlot.HEAD, 100);
                            entityMannequin.setDropChance(EntityEquipmentSlot.CHEST, 100);
                            entityMannequin.setDropChance(EntityEquipmentSlot.LEGS, 100);
                            entityMannequin.setDropChance(EntityEquipmentSlot.FEET, 100);

                            ItemMonsterPlacer.applyItemEntityDataToEntity(worldIn, player, itemstack, entityMannequin);
                            worldIn.spawnEntity(entityMannequin);

                            worldIn.playSound(null, entityMannequin.getPosition(), JTPGSounds.ENTITY_MANNEQUIN_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        }

                        itemstack.shrink(1);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
    }

    /** Add both types to the Creative Tab. */
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            items.add(new ItemStack(this, 1, 1));
            items.add(new ItemStack(this, 1, 0));
        }
    }

    @Override
    public int getMetadata(int damage)
    { return damage; }
}