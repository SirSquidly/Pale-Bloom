package com.sirsquidly.palebloom.common.blocks;

import com.sirsquidly.palebloom.init.JTPGLootTables;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockSuckerRootNodule extends Block
{
    public BlockSuckerRootNodule()
    {
        super(Material.WOOD, MapColor.ADOBE);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    protected boolean canSilkHarvest() { return true; }

    /** Drops are handled via loot table if not Silk Touched. */
    public Item getItemDropped(IBlockState state, Random rand, int fortune) { return null; }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) <= 0)
        { dropFromBlockLootTable(worldIn, pos); }
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    /** Spawns the item from the Shell Sand Loot table. */
    public void dropFromBlockLootTable(World worldIn, BlockPos pos)
    {
        if (!worldIn.isRemote)
        {
            Random rand = worldIn.rand;
            LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)worldIn);
            List<ItemStack> result = worldIn.getLootTableManager().getLootTableFromLocation(JTPGLootTables.BLOCKS_SUCKER_ROOT_NODULE).generateLootForPools(rand, lootcontext$builder.build());

            for (ItemStack lootItem : result)
            {
                double d0 = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
                double d1 = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
                double d2 = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;

                EntityItem entityitem = new EntityItem(worldIn, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, lootItem);
                entityitem.setDefaultPickupDelay();
                worldIn.spawnEntity(entityitem);
            }
        }
    }
}