package com.sirsquidly.palebloom.common.blocks;

import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.init.JTPGSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class BlockPaleHangingMoss extends BlockBush implements IShearable, IGrowable
{
    protected static final AxisAlignedBB HANGING_MID = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 1.0D, 0.9D);
    protected static final AxisAlignedBB HANGING_BOTTOM = new AxisAlignedBB(0.1D, 0.125D, 0.1D, 0.9D, 1.0D, 0.9D);

    public static final PropertyBool BOTTOM = PropertyBool.create("bottom");

    public BlockPaleHangingMoss()
    {
        super(Material.GRASS, MapColor.SILVER);
        this.setSoundType(JTPGSounds.MOSS);
        this.setTickRandomly(true);
        this.getDefaultState().withProperty(BOTTOM, true);
        //setSoundType(BOMDSoundTypes.MOSS);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    { return isBottom(source, pos) ? HANGING_BOTTOM : HANGING_MID; }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    { return state.withProperty(BOTTOM, isBottom(worldIn, pos)); }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    { return canBlockStay(world, pos, world.getBlockState(pos)); }

    /** Checks based on BlockFaceShape, as it is more generous than `isSideSolid` (such as allowing Leaves) */
    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    { return worldIn.getBlockState(pos.up()).getBlockFaceShape(worldIn, pos.up(), EnumFacing.DOWN) == BlockFaceShape.SOLID || worldIn.getBlockState(pos.up()).getBlock() == this; }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        this.checkAndDropBlock(worldIn, pos, state);
    }

    /** If below this Hanging Moss is NOT another Hanging Moss block. Used for getting the state, and for growth logic. */
    public boolean isBottom(IBlockAccess worldIn, BlockPos pos)
    { return worldIn.getBlockState(pos.down()).getBlock() != this && worldIn.getBlockState(pos.down()).getBlock() != JTPGBlocks.NIGHTLIGHT; }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() { return BlockRenderLayer.CUTOUT_MIPPED; }

    /** Used for playing the ambient sounds. */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (rand.nextInt(512) == 0 && blockCausesAmbience(worldIn.getBlockState(pos.up()).getBlock()))
        {
            worldIn.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, JTPGSounds.BLOCK_PALE_HANGING_MOSS_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
        }
    }

    /** If the given block is one that makes the Pale Hanging Moss play ambient sounds. */
    public boolean blockCausesAmbience(Block block)
    { return block == JTPGBlocks.PALE_OAK_LEAVES || block == JTPGBlocks.PALE_OAK_LOG; }

    public IBlockState getStateFromMeta(int meta) { return this.getDefaultState(); }

    public int getMetaFromState(IBlockState state) { return 0; }

    @Override
    protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, BOTTOM); }

    /** Void drops, so Shears are required. */
    public Item getItemDropped(IBlockState state, Random rand, int fortune) { return null; }

    @Override
    public boolean isShearable(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos) { return true; }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos, int fortune) { return NonNullList.withSize(1, new ItemStack(this, 1)); }

    /** Returns the bottom block of a length og Hanging Pale Moss. Will stop if there is no moss of course. */
    public BlockPos findBottomPos(World worldIn, BlockPos pos)
    {
        BlockPos next = pos;

        while (worldIn.getBlockState(next).getBlock() == this && !isBottom(worldIn, next))
        { next = next.down(); }

        return next;
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    { return worldIn.isAirBlock(findBottomPos(worldIn, pos).down()); }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    { return worldIn.isAirBlock(findBottomPos(worldIn, pos).down()); }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        BlockPos bottomPos = findBottomPos(worldIn, pos);

        if (worldIn.isAirBlock(bottomPos.down()))
        { worldIn.setBlockState(bottomPos.down(), this.getDefaultState(), 2); }
    }
}