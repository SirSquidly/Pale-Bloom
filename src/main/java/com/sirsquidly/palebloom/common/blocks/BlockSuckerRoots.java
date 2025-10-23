package com.sirsquidly.palebloom.common.blocks;

import com.sirsquidly.palebloom.common.world.WorldPaleGarden;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockSuckerRoots extends Block
{
    public static final PropertyInteger LAYERS = PropertyInteger.create("layers", 1, 2);
    protected static final AxisAlignedBB[] ROOTS_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)};


    public BlockSuckerRoots()
    {
        super(Material.PLANTS, MapColor.ADOBE);
        this.setSoundType(SoundType.PLANT);
        this.setCreativeTab(CreativeTabs.DECORATIONS);

        this.setDefaultState(this.blockState.getBaseState().withProperty(LAYERS, Integer.valueOf(1)));
        //setSoundType(BOMDSoundTypes.MOSS);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    { return ROOTS_AABB[state.getValue(LAYERS) - 1];  }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    { return NULL_AABB; }

    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!(entityIn instanceof EntityLivingBase) || WorldPaleGarden.isPaleEntity(entityIn)) return;

        int layers = state.getValue(LAYERS);
        double height = layers * 0.25D;

        double entityFeet = entityIn.posY;
        double blockTop = pos.getY() + height;

        if (entityFeet < blockTop)
        {
            double slowDown = (layers > 1) ? 0.1D : 0.4D;

            entityIn.motionX *= slowDown;
            entityIn.motionY *= 0.85D;
            entityIn.motionZ *= slowDown;

            if (layers > 1 && entityIn.ticksExisted % 20 == 0)
            {
                entityIn.attackEntityFrom(DamageSource.CACTUS, 1.0F);
            }
        }
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    { return worldIn.isSideSolid(pos.down(), EnumFacing.UP) && super.canPlaceBlockAt(worldIn, pos); }

    public boolean isOpaqueCube(IBlockState state)
    { return false; }

    public boolean isFullCube(IBlockState state)
    { return false; }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    { return BlockRenderLayer.CUTOUT; }

    public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(LAYERS, meta + 1); }

    public int getMetaFromState(IBlockState state)
    { return state.getValue(LAYERS) - 1; }

    protected boolean canSilkHarvest() { return true; }

    /** Scales the quantity dropped by Silk Touch with the number of Layers. */
    protected ItemStack getSilkTouchDrop(IBlockState state)
    { return new ItemStack(Item.getItemFromBlock(this), state.getValue(LAYERS)); }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.STICK;
    }

    @Override public int quantityDropped(IBlockState state, int fortune, Random random){ return random.nextInt(3 * state.getValue(LAYERS)); }

    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, LAYERS); }
}