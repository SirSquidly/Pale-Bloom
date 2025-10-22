package com.sirsquidly.palebloom.item;

import com.sirsquidly.palebloom.blocks.BlockResinClump;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.world.WorldPaleGarden;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ItemPalePickaxe extends ItemPickaxe
{
    boolean isNight;

    public ItemPalePickaxe(ToolMaterial material)
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
        if (!this.isNight) return super.hitEntity(stack, target, attacker);

        if (target.getHealth() <= 0)
        {
            if (attacker instanceof EntityPlayer)
            {
                if (((EntityPlayer)attacker).getCooldownTracker().getCooldown(stack.getItem(), 0) > 0) return super.hitEntity(stack, target, attacker);
                ((EntityPlayer)attacker).getCooldownTracker().setCooldown(stack.getItem(), 120);
            }

            tryPlaceResin(target.world, target.getPosition(), target.getRNG());
        }

        return super.hitEntity(stack, target, attacker);
    }

    public void tryPlaceResin(World world, BlockPos pos, Random rand)
    {
        int resinRange = 1;
        int placements = rand.nextInt(2) + 2;

        List<BlockPos> goodPositions = new ArrayList<>();
        for (BlockPos checkPos : BlockPos.getAllInBoxMutable( pos.add(-resinRange, -resinRange, -resinRange), pos.add(resinRange, resinRange, resinRange)))
        {
            if (Math.abs(checkPos.getX() - pos.getX()) + Math.abs(checkPos.getY() - pos.getY()) + Math.abs(checkPos.getZ() - pos.getZ()) > resinRange) continue;
            goodPositions.add(new BlockPos(checkPos));
        }
        Collections.shuffle(goodPositions, rand);


        for (BlockPos currentPos : goodPositions)
        {
            for (EnumFacing facing : EnumFacing.values())
            {
                BlockPos outerPos = currentPos.offset(facing);
                IBlockState adjacentState = world.getBlockState(outerPos);

                if (!adjacentState.isSideSolid(world, outerPos, facing.getOpposite())) continue;

                IBlockState targetState = world.getBlockState(currentPos);

                if (targetState.getBlock().isAir(targetState, world, currentPos))
                {
                    world.setBlockState(currentPos, BlockResinClump.getBlockState(facing));
                    world.playSound(null, pos, JTPGSounds.BLOCK_RESIN_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    placements--;
                }
                else if (targetState.getBlock() instanceof BlockResinClump)
                {
                    EnumFacing[] existingFacings = BlockResinClump.getFacings(targetState);
                    if (!ArrayUtils.contains(existingFacings, facing))
                    {
                        world.setBlockState(currentPos, BlockResinClump.getBlockState(ArrayUtils.add(existingFacings, facing)));
                        world.playSound(null, pos, JTPGSounds.BLOCK_RESIN_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        placements--;
                    }
                }

                if (placements <= 0) return;
            }
        }
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
            tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_pickaxe.night"));
        }
        else
        { tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.day")); }

    }
}