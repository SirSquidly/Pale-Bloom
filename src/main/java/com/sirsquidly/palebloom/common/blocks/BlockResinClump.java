package com.sirsquidly.palebloom.common.blocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.init.JTPGItems;
import com.sirsquidly.palebloom.init.JTPGSounds;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;


/**
 * Thank you incredibly kindly to the wonderful and beautiful SmileyCorp for this code,
 * as lifted from Deeper Depths with permission.
 *
 * It probably cost her a golf ball-sized hole in her sanity, but... it works.
 * */
public class BlockResinClump extends Block
{
    private static final EnumMap<EnumFacing, Integer> DIR_FLAGS = getFlags();

    private static EnumMap<EnumFacing, Integer> getFlags() {
        EnumMap<EnumFacing, Integer> flags = Maps.newEnumMap(EnumFacing.class);
        for (EnumFacing facing : EnumFacing.values()) flags.put(facing, (int) Math.pow(2, facing.ordinal()));
        return flags;
    }

    protected static final AxisAlignedBB EMPTY_AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    protected static final AxisAlignedBB[] AABBS = {
            new AxisAlignedBB(0, 0, 0, 1, 0.1875D, 1),
            new AxisAlignedBB(0, 0.8125, 0, 1, 1, 1),
            new AxisAlignedBB(0, 0, 0, 1, 1, 0.1875D),
            new AxisAlignedBB(0, 0, 0.8125, 1, 1, 1),
            new AxisAlignedBB(0, 0, 0, 0.1875D, 1, 1),
            new AxisAlignedBB(0.8125, 0, 0, 1, 1, 1)
    };

    public static final PropertyInteger META = PropertyInteger.create("meta", 0, 15);

    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool EAST = PropertyBool.create("east");
    private static final PropertyBool[] DIRECTIONS = {DOWN, UP, NORTH, SOUTH, WEST, EAST};

    private final int ordinal;

    public BlockResinClump(int ordinal)
    {
        super(Material.PLANTS);
        this.setSoundType(JTPGSounds.RESIN);

        this.ordinal = ordinal;
        setDefaultState(blockState.getBaseState().withProperty(UP, false).withProperty(DOWN, false).withProperty(NORTH, false)
                .withProperty(SOUTH, false).withProperty(EAST, false).withProperty(WEST, false));
    }

    @Override
    protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, META, DOWN, UP, NORTH, SOUTH, WEST, EAST); }

    //the simple conversions
    @Override
    public IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(META, meta); }

    @Override
    public int getMetaFromState(IBlockState state) { return state.getValue(META); }

    //again probably not needed but makes statemapping easier and f3 look better
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        IBlockState state1 = getDefaultState();
        for (EnumFacing facing : getFacings(state)) state1 = state1.withProperty(getProperty(facing), true);
        return state1;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos from)
    {
        int facesLost = 0;
        List<EnumFacing> facings = Lists.newArrayList(getFacings(state));
        Iterator<EnumFacing> iterator = facings.iterator();
        while (iterator.hasNext())
        {
            EnumFacing facing = iterator.next();
            BlockPos pos1 = pos.offset(facing);
            IBlockState state1 = world.getBlockState(pos1);

            if (state1.getBlockFaceShape(world, pos.offset(facing.getOpposite()), facing) == BlockFaceShape.SOLID) continue;
            iterator.remove();
            facesLost++;
        }
        if (facesLost > 0)
        {
            world.setBlockState(pos, getBlockState(facings.toArray(new EnumFacing[facings.size()])));
            spawnAsEntity(world, pos, new ItemStack(JTPGItems.RESIN_CLUMP, facesLost));
        }
    }

    /**
     * This real short bunch are stuff I had to edit to work for Resin specifically.
     * */
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) { return MapColor.ORANGE_STAINED_HARDENED_CLAY; }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) { return new ItemStack(JTPGItems.RESIN_CLUMP); }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) { return new ItemStack(JTPGItems.RESIN_CLUMP).getItem(); }

    /** YEEEEAAAAHHHH RETURN THE LENGTH AS QUANTITY WE ARE CODING */
    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random)
    { return getFacings(state).length; }

    /** Apparently Resin is Replaceable??? */
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) { return true; }

    //no dynamic bounding boxes so we just go fullblock if we have more than one face
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        AxisAlignedBB aabb = EMPTY_AABB;
        for (EnumFacing facing : getFacings(state)) {
            if (aabb != EMPTY_AABB) return FULL_BLOCK_AABB;
            else aabb = AABBS[facing.ordinal()];
        }
        return aabb;
    }

    //jank ass shit to let you place stuff through the open faces
    //(you can't put more than 2 sculk veins in the block without doing this)
    //(adding right click functionality instead would mean we need to do vector math, which I really don't want to do)
    @Override
    public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end)
    {
        for (EnumFacing facing : getFacings(state)) {
            RayTraceResult result = rayTrace(pos, start, end, AABBS[facing.ordinal()]);
            if (result != null) return result;
        }
        return null;
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing facing) { return world.getBlockState(pos.offset(facing.getOpposite())).getBlockFaceShape(world, pos.offset(facing.getOpposite()), facing) == BlockFaceShape.SOLID; }

    /** Properly tell the game that this is NOT a solid shape. */
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    { return BlockFaceShape.UNDEFINED; }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) { return NULL_AABB; }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() { return BlockRenderLayer.CUTOUT; }

    @Override
    public boolean isFullCube(IBlockState state) { return false; }

    @Override
    public boolean isOpaqueCube(IBlockState state) { return false; }

    //spin
    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing facing)
    {
        return world.setBlockState(pos, getBlockState(Arrays.stream(getFacings(world.getBlockState(pos)))
                .map(f -> f.rotateAround(facing.getAxis())).toArray(EnumFacing[]::new)));
    }

    //YOU, YOU'RE WHY WE CAN'T HAVE NICE THINGS LIKE TILE ENTITIES
    //(ngl this stuff being tiles probably would break something else, but still)
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirror) {  return getBlockState(Arrays.stream(getFacings(state)).map(f -> mirror.mirror(f)).toArray(EnumFacing[]::new)); }

    //used for item registry jank
    public int getOrdinal() { return ordinal; }

    //normal function
    public static PropertyBool getProperty(EnumFacing facing) { return DIRECTIONS[facing.ordinal()]; }

    //ignore everything below here it's all jank and bad
    public static boolean hasFacing(IBlockState state, EnumFacing facing) { return (getMeta(state) & DIR_FLAGS.get(facing)) > 0; }

    public static EnumFacing[] getFacings(IBlockState state) { return getFacings(getMeta(state)); }

    public static EnumFacing[] getFacings(int meta) { return Arrays.stream(EnumFacing.values()).filter(f -> (DIR_FLAGS.get(f) & meta) > 0).toArray(EnumFacing[]::new); }

    private static int getMeta(IBlockState state)
    {
        if (!(state.getBlock() instanceof BlockResinClump)) return 0;
        return ((BlockResinClump) state.getBlock()).getOrdinal() * 16 + state.getValue(META);
    }

    public static IBlockState getBlockState(int meta)
    { return meta == 0 ? Blocks.AIR.getDefaultState() : JTPGBlocks.RESIN_CLUMPS[meta / 16].getDefaultState().withProperty(META, meta % 16); }

    public static IBlockState getBlockState(EnumFacing... facings) { return getBlockState(getMeta(facings)); }

    public static int getMeta(EnumFacing... facings)
    {
        int meta = 0;
        for (EnumFacing facing : facings) meta += DIR_FLAGS.get(facing);
        return meta;
    }
}