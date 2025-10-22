package com.sirsquidly.palebloom.blocks;

import com.sirsquidly.palebloom.entity.item.EntitySeedBomb;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlockSeedBomb extends Block implements IShearable
{
    public BlockSeedBomb()
    {
        super(Material.TNT);
        this.setSoundType(SoundType.PLANT);
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    public boolean canDropFromExplosion(Explosion explosionIn)
    {
        return false;
    }

    public void explode(World worldIn, BlockPos pos, EntityLivingBase igniter)
    {
        if (!worldIn.isRemote)
        {
            EntitySeedBomb underwaterTNTPrimed = new EntitySeedBomb(worldIn, (float)pos.getX() + 0.5F, pos.getY(), (float)pos.getZ() + 0.5F, igniter);
            underwaterTNTPrimed.setFuse(40);
            worldIn.spawnEntity(underwaterTNTPrimed);
            worldIn.playSound(null, underwaterTNTPrimed.posX, underwaterTNTPrimed.posY, underwaterTNTPrimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = playerIn.getHeldItem(hand);

        if (!itemstack.isEmpty() && (itemstack.getItem() == Items.FLINT_AND_STEEL || itemstack.getItem() == Items.FIRE_CHARGE))
        {
            this.explode(worldIn, pos, playerIn);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);

            if (itemstack.getItem() == Items.FLINT_AND_STEEL)
            { itemstack.damageItem(1, playerIn); }
            else if (!playerIn.capabilities.isCreativeMode)
            { itemstack.shrink(1); }

            return true;
        }
        else
        { return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ); }
    }

    /** Vanilla doesn't actually set a harvest tool for Hoes, so this is more a bonus in case other mods do. */
    public String getHarvestTool(IBlockState state) { return "hoe"; }


    public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
    {
        ItemStack held = player.getHeldItemMainhand();
        if (held.getItem() instanceof ItemHoe)
        {
            return super.getPlayerRelativeBlockHardness(state, player, worldIn, pos) * 3.0F;
        }

        return super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
    }



    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }

    protected boolean canSilkHarvest() { return true; }

    public boolean isShearable(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos) { return true; }

    public List<ItemStack> onSheared(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
    { return Collections.singletonList(new ItemStack(this, 1)); }

    /** When broken without Silk Touch or Shears, it gets ignited. */
    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) <= 0 && !(stack.getItem() instanceof ItemShears))
        { this.explode(worldIn, pos, null); }
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }
}