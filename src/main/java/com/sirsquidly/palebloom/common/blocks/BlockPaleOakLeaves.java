package com.sirsquidly.palebloom.common.blocks;

import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockPaleOakLeaves extends BlockLeaves
{
    Block getSaplingDropped;
    int getDroppedMetadata;

    public BlockPaleOakLeaves(Block getSaplingDroppedIn, int metaIn)
    {
        super();
        setDefaultState(this.getDefaultState().withProperty(BlockLeaves.DECAYABLE, false).withProperty(BlockLeaves.CHECK_DECAY, false));
        getSaplingDropped = getSaplingDroppedIn;
        getDroppedMetadata = metaIn;
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    { return this.getDefaultState().withProperty(BlockLeaves.DECAYABLE, false).withProperty(BlockLeaves.CHECK_DECAY, false); }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta)
    { return null; }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    { return getSaplingDropped != null ? Item.getItemFromBlock(getSaplingDropped) : null; }

    public int damageDropped(IBlockState state)
    { return getDroppedMetadata; }

    @Deprecated
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return MapColor.IRON;
    }

    @Override
    public NonNullList<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    { return NonNullList.withSize(1, new ItemStack(this, 1)); }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (!worldIn.isRemote && stack.getItem() instanceof ItemShears)
        { player.addStat(StatList.getBlockStats(this)); }
        else
        { super.harvestBlock(worldIn, player, pos, state, te, stack); }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return Blocks.LEAVES.getRenderLayer();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return Blocks.LEAVES.isOpaqueCube(state);
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    { return Blocks.LEAVES.shouldSideBeRendered(state, world, pos, side); }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (rand.nextInt(64) == 0 && !worldIn.getBlockState(pos.down()).getMaterial().blocksMovement())
        {
            paleBloom.proxy.spawnParticle(1, worldIn, pos.getX() + 0.5 + (worldIn.rand.nextDouble() - 0.5), pos.getY(), pos.getZ() + 0.5 + (worldIn.rand.nextDouble() - 0.5), 0, 0, 0);
        }
    }

    public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(DECAYABLE, (meta & 4) == 0).withProperty(CHECK_DECAY, (meta & 8) > 0); }

    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        if (!(Boolean) state.getValue(DECAYABLE))
        { i |= 4; }
        if ((Boolean) state.getValue(CHECK_DECAY))
        { i |= 8; }
        return i;
    }

    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE); }
}