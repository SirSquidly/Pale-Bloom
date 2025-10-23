package com.sirsquidly.palebloom.common.blocks;

import com.sirsquidly.palebloom.init.JTPGSounds;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockPaleMossCarpet extends Block implements IGrowable
{
    protected static final AxisAlignedBB CARPET_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
    protected static final AxisAlignedBB LOWER_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D);
    protected static final AxisAlignedBB FULL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public static final PropertyBool BOTTOM = PropertyBool.create("bottom");

    public static final PropertyEnum<EnumFacingHeight> NORTH = PropertyEnum.create("north", EnumFacingHeight.class);
    public static final PropertyEnum<EnumFacingHeight> EAST = PropertyEnum.create("east", EnumFacingHeight.class);
    public static final PropertyEnum<EnumFacingHeight> SOUTH = PropertyEnum.create("south", EnumFacingHeight.class);
    public static final PropertyEnum<EnumFacingHeight> WEST = PropertyEnum.create("west", EnumFacingHeight.class);

    public final float doubleChance = 0.25F;

    public BlockPaleMossCarpet()
    {
        super(Material.CARPET, MapColor.SILVER);
        this.setSoundType(JTPGSounds.MOSS);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setTickRandomly(true);
        this.getDefaultState().withProperty(BOTTOM, true).withProperty(NORTH, EnumFacingHeight.NONE).withProperty(EAST, EnumFacingHeight.NONE).withProperty(SOUTH, EnumFacingHeight.NONE).withProperty(WEST, EnumFacingHeight.NONE);
        //setSoundType(BOMDSoundTypes.MOSS);
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState upperState = worldIn.getBlockState(pos.up());
        boolean hasUpper = upperState.getBlock() == this && !upperState.getValue(BOTTOM);

        boolean blockNSolid = worldIn.getBlockState(pos.north()).isSideSolid(worldIn, pos.north(), EnumFacing.SOUTH);
        boolean blockESolid = worldIn.getBlockState(pos.east()).isSideSolid(worldIn, pos.east(), EnumFacing.WEST);
        boolean blockSSolid = worldIn.getBlockState(pos.south()).isSideSolid(worldIn, pos.south(), EnumFacing.NORTH);
        boolean blockWSolid = worldIn.getBlockState(pos.west()).isSideSolid(worldIn, pos.west(), EnumFacing.EAST);

        EnumFacingHeight north = blockNSolid ? EnumFacingHeight.SHORT : EnumFacingHeight.NONE;
        EnumFacingHeight east = blockESolid ? EnumFacingHeight.SHORT : EnumFacingHeight.NONE;
        EnumFacingHeight south = blockSSolid ? EnumFacingHeight.SHORT : EnumFacingHeight.NONE;
        EnumFacingHeight west = blockWSolid ? EnumFacingHeight.SHORT : EnumFacingHeight.NONE;

        if (hasUpper)
        {
            boolean upNSolid = worldIn.getBlockState(pos.up().north()).isSideSolid(worldIn, pos.up().north(), EnumFacing.SOUTH);
            boolean upESolid = worldIn.getBlockState(pos.up().east()).isSideSolid(worldIn, pos.up().east(), EnumFacing.WEST);
            boolean upSSolid = worldIn.getBlockState(pos.up().south()).isSideSolid(worldIn, pos.up().south(), EnumFacing.NORTH);
            boolean upWSolid = worldIn.getBlockState(pos.up().west()).isSideSolid(worldIn, pos.up().west(), EnumFacing.EAST);

            if (upNSolid) north = EnumFacingHeight.TALL;
            if (upESolid) east  = EnumFacingHeight.TALL;
            if (upSSolid) south = EnumFacingHeight.TALL;
            if (upWSolid) west  = EnumFacingHeight.TALL;
        }

        return state
                .withProperty(NORTH, north)
                .withProperty(EAST,  east)
                .withProperty(SOUTH, south)
                .withProperty(WEST,  west);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        state = getActualState(state, source, pos); // ensure it's updated

        boolean hasTall = false;
        boolean hasShort = false;

        for (PropertyEnum prop : new PropertyEnum[]{NORTH, EAST, SOUTH, WEST})
        {
            EnumFacingHeight height = (EnumFacingHeight) state.getValue(prop);
            if (height == EnumFacingHeight.TALL) hasTall = true;
            else if (height == EnumFacingHeight.SHORT) hasShort = true;
        }

        if (hasTall) return FULL_AABB;
        if (hasShort) return LOWER_AABB;
        return CARPET_AABB;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    { return blockState.getValue(BOTTOM) ? CARPET_AABB : null; }

    /** Gives Pale Moss Carpets a chance to get placed tall. */
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    { if (canGrow(worldIn, pos, state, false) && worldIn.rand.nextFloat() < doubleChance) grow(worldIn, worldIn.rand, pos, state);}

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        this.checkAndDropBlock(worldIn, pos, state);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    { this.checkAndDropBlock(worldIn, pos, state); }

    /** Requires non-Air below to be placed. This fits the rules of the `Bottom` Moss Carpet, which is the only one players should be placing anyway! */
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    { return super.canPlaceBlockAt(worldIn, pos) && !worldIn.isAirBlock(pos.down()); }

    /** If this is a top block, remove if there is none below, or if there are zero touching sides. */
    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!state.getValue(BOTTOM) && (worldIn.getBlockState(pos.down()).getBlock() != this || !hasAnySides(worldIn, pos)) || state.getValue(BOTTOM) && worldIn.isAirBlock(pos.down()))
        { worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3); }
    }

    /** Top parts do not drop, as they exist just to render. */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    { return state.getValue(BOTTOM) ? Item.getItemFromBlock(this): null; }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        if (side == EnumFacing.UP)
        { return true; }
        else
        { return blockAccess.getBlockState(pos.offset(side)).getBlock() == this || super.shouldSideBeRendered(blockState, blockAccess, pos, side); }
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    { return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED; }

    /** Used for the side textures. */
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    { return BlockRenderLayer.CUTOUT_MIPPED; }

    public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(BOTTOM, meta == 0); }

    public int getMetaFromState(IBlockState state)
    { return state.getValue(BOTTOM) ? 0 : 1; }

    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, BOTTOM, NORTH, EAST, SOUTH, WEST); }


    /** Quick math to see if ANY horizontal facings from the given position are a full block.*/
    public boolean hasAnySides(World world, BlockPos pos)
    {
        for (EnumFacing facing : EnumFacing.HORIZONTALS)
        {
            BlockPos offset = pos.offset(facing);

            if (world.getBlockState(offset).isSideSolid(world, offset, facing.getOpposite()))
            { return true; }
        }
        return false;
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    { return state.getValue(BOTTOM) && worldIn.isAirBlock(pos.up()) && hasAnySides(worldIn, pos.up()); }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    { return state.getValue(BOTTOM) && worldIn.isAirBlock(pos.up()) && hasAnySides(worldIn, pos.up()); }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    { worldIn.setBlockState(pos.up(), this.getDefaultState().withProperty(BOTTOM, false), 3); }

    /** An Enum to determine how tall the side vines are on each side. */
    public static enum EnumFacingHeight implements IStringSerializable
    {
        NONE("none"),
        SHORT("short"),
        TALL("tall");
        private final String name;

        private EnumFacingHeight(String name)
        { this.name = name; }

        public String toString()
        { return this.name; }
        public String getName()
        { return this.name; }
    }
}