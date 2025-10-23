package com.sirsquidly.palebloom.common.blocks;

import com.sirsquidly.palebloom.config.Config;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.init.JTPGItems;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.paleBloom;
import com.sirsquidly.palebloom.common.world.WorldPaleGarden;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
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

public class BlockNightlight extends BlockBush implements IShearable, IGrowable, IEyeblossomListener
{
    protected static final AxisAlignedBB NIGHTLIGHT_AABB = new AxisAlignedBB(0.125D, 0.625D, 0.125D, 0.875D, 1.0D, 0.875D);
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);
    public static final PropertyBool AWAKE = PropertyBool.create("awake");

    private static final boolean configNightlightBulbsEnabled = Config.item.foods.enableNightligtBulb;

    public BlockNightlight()
    {
        super(Material.GRASS, MapColor.SILVER);
        this.setSoundType(JTPGSounds.MOSS);
        this.setTickRandomly(true);

        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0).withProperty(AWAKE, false));
        //setSoundType(BOMDSoundTypes.MOSS);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getLightValue(IBlockState state)
    {
        if (state.getValue(AWAKE))
        {
            switch (state.getValue(AGE))
            {
                case 0: return 0;
                case 1: return 3;
                case 2: return 9;
                case 3: return 13;
            }
        }
        return super.getLightValue(state);
    }

    /** Interacting will always harvest the berries if this vine has berries, otherwise allows clipping the bottom with Shears */
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (state.getValue(AWAKE) && state.getValue(AGE) == 3)
        {
            worldIn.setBlockState(pos, state.withProperty(AGE, 0), 2);
            worldIn.playSound(null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (configNightlightBulbsEnabled) spawnAsEntity(worldIn, pos, new ItemStack(JTPGItems.NIGHTLIGHT_BULB));
            return true;
        }
        return false;
    }

    /** Occurs randomly, meaning this Eyeblossom is NOT responding. */
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);
        preformSwapping(worldIn, pos, false);
        if (worldIn.isRemote) return;

        if (state.getValue(AWAKE))
        {
            //if (rand.nextInt(66) != 0) return;
            int age = state.getValue(AGE);

            if (age < 3)
            {
                if (WorldPaleGarden.requestBulbResin(worldIn, pos, 10, true).resinPulled == 10)
                {
                    worldIn.playSound(null, pos, JTPGSounds.BLOCK_EYEBLOSSOM_OPEN_LONG, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    worldIn.setBlockState(pos, state.withProperty(AGE, age + 1), 2);
                }
            }
            else
            {
                double hx = pos.getX() + 0.5 + (worldIn.rand.nextDouble() * 0.5 - 0.25);
                double hy = pos.getY() - 3 + (worldIn.rand.nextDouble() * 0.5 - 0.25);
                double hz = pos.getZ() + 0.5 + (worldIn.rand.nextDouble() * 0.5 - 0.25);
                paleBloom.proxy.spawnParticle(0, worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, hx, hy, hz, 1);
            }
        }
    }

    public void preformSwapping(World worldIn, BlockPos pos, boolean isResponding)
    {
        if (worldIn.isRemote) return;

        boolean isNight = WorldPaleGarden.isNight(worldIn);
        IBlockState state = worldIn.getBlockState(pos);

        /** Awake and Night are both a boolean, so check if they match to see if this needs updating. */
        if (state.getValue(AWAKE) == isNight) return;

        SoundEvent sound = isNight ? JTPGSounds.BLOCK_NIGHTLIGHT_OPEN : JTPGSounds.BLOCK_NIGHTLIGHT_CLOSE;

        worldIn.playSound(null, pos, sound, SoundCategory.BLOCKS, 0.5F, (worldIn.rand.nextFloat() * 0.4F) + 0.8F);
        worldIn.setBlockState(pos, state.withProperty(AWAKE, isNight), 2);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    { return NIGHTLIGHT_AABB; }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    { return canBlockStay(world, pos, world.getBlockState(pos)); }

    /** Checks based on BlockFaceShape, as it is more generous than `isSideSolid` (such as allowing Leaves) */
    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    { return worldIn.getBlockState(pos.up()).getBlockFaceShape(worldIn, pos.up(), EnumFacing.DOWN) == BlockFaceShape.SOLID || worldIn.getBlockState(pos.up()).getBlock() == JTPGBlocks.PALE_HANGING_MOSS; }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() { return BlockRenderLayer.CUTOUT_MIPPED; }

    /** Void drops, so Shears are required. */
    public Item getItemDropped(IBlockState state, Random rand, int fortune) { return null; }

    @Override
    public boolean isShearable(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos) { return true; }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
    { return NonNullList.withSize(1, new ItemStack(this, 1)); }

    /** Returns the bottom block of a length og Hanging Pale Moss. Will stop if there is no moss of course. */
    public BlockPos findBottomPos(World worldIn, BlockPos pos)
    {
        BlockPos next = pos;

        while (worldIn.getBlockState(next).getBlock() == JTPGBlocks.PALE_HANGING_MOSS)
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
        {
            worldIn.setBlockState(bottomPos, JTPGBlocks.PALE_HANGING_MOSS.getDefaultState(), 2);
            worldIn.setBlockState(bottomPos.down(), state, 2);
        }
    }

    public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(AGE, meta & 3).withProperty(AWAKE, (meta & 4) != 0); }

    public int getMetaFromState(IBlockState state) { return (state.getValue(AGE) & 3)| (state.getValue(AWAKE) ? 4 : 0); }

    @Override
    protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, AGE, AWAKE); }
}