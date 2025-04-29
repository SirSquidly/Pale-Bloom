package com.sirsquidly.creaturesfromdarkness.item;

import com.google.common.collect.Multimap;
import com.sirsquidly.creaturesfromdarkness.init.CFDSounds;
import com.sirsquidly.creaturesfromdarkness.entity.EntityNightmare;
import com.sirsquidly.creaturesfromdarkness.util.CFDCapabilityUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemNightmareLeg extends Item
{
    private final float attackDamage;

    public ItemNightmareLeg()
    {
        this.maxStackSize = 1;
        this.setMaxDamage(1);
        this.attackDamage = 1.0F;

        this.setCreativeTab(CreativeTabs.TOOLS);
    }

    /** Functions like `hitEntity`, but seperate as to run through the `onAttackWithSpine` event. */
    public static void doHitEffect(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        if (target instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)target;

            target.world.playSound(null, new BlockPos(target), SoundEvents.ENTITY_HORSE_ANGRY, SoundCategory.PLAYERS, 1.0F, 0.2F);
            CFDCapabilityUtils.addNightmarePackage(player);
        }
        else if (target instanceof EntityNightmare)
        {
            spawnNightmareNearPlayer(attacker, 32, 16);
        }

        if (!(attacker instanceof EntityPlayer) || !((EntityPlayer) attacker).capabilities.isCreativeMode)
        {
            attacker.renderBrokenItemStack(stack);
            stack.shrink(1);

            if (attacker instanceof EntityPlayer) ((EntityPlayer) attacker).addStat(StatList.getObjectBreakStats(stack.getItem()));
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (playerIn.isSneaking())
        {
            CFDCapabilityUtils.addNightmarePackage(playerIn);

            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        else
        {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
    }

    public static void spawnNightmareNearPlayer(EntityLivingBase user, int spawnMaxDistance, int spawnMinDistance)
    {
        World world = user.world;
        if (world.isRemote) return;
        EntityNightmare nightmare = new EntityNightmare(world);
        BlockPos pos = new BlockPos(user);
        int maxAttempts = 80;

        for (int i = 0; i < maxAttempts; i++)
        {
            int offsetX = world.rand.nextInt(spawnMaxDistance * 2) - spawnMaxDistance;
            int offsetZ = world.rand.nextInt(spawnMaxDistance * 2) - spawnMaxDistance;
            int offsetY = world.rand.nextInt(5) - 2;
            BlockPos spawnPos = pos.add(offsetX, offsetY, offsetZ);

            if (spawnPos.distanceSq(pos) < spawnMinDistance * spawnMinDistance) continue;
            nightmare.setLocationAndAngles(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, world.rand.nextFloat() * 360.0F, 0.0F);

            if (world.checkNoEntityCollision(nightmare.getEntityBoundingBox(), nightmare) && world.getCollisionBoxes(nightmare, nightmare.getEntityBoundingBox()).isEmpty())
            {
                BlockPos groundPos = spawnPos.down();
                if (world.getBlockState(groundPos).isSideSolid(world, groundPos, EnumFacing.UP))
                {
                    world.playSound(null, user.posX, user.posY, user.posZ, CFDSounds.ENTITY_NIGHTMARE_WARN, SoundCategory.HOSTILE, 1.0F, 1.0F);
                    world.spawnEntity(nightmare);
                    break;
                }
            }
        }
    }


    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    { return true; }

    @SuppressWarnings("deprecated")
    public EnumRarity getRarity(ItemStack stack)
    { return EnumRarity.UNCOMMON; }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.attackDamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
        }

        return multimap;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    { return true; }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    { tooltip.add(TextFormatting.BLUE + I18n.format("description.creaturesfromdarkness.nightmare_spine.name")); }
}