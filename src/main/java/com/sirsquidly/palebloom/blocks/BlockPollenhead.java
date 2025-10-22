package com.sirsquidly.palebloom.blocks;

import com.sirsquidly.palebloom.blocks.base.BlockJTPGDoublePlant;
import com.sirsquidly.palebloom.blocks.tileentity.TilePollenhead;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.world.WorldPaleGarden;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockPollenhead extends BlockJTPGDoublePlant implements IGardenState, IEyeblossomListener
{
    public static final PropertyEnum<BlockDoublePalePlant.EnumBlockHalf> HALF = PropertyEnum.create("half", BlockDoublePalePlant.EnumBlockHalf.class);
    protected static final AxisAlignedBB BUSH_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);

    public BlockPollenhead()
    {
        this.setSoundType(SoundType.PLANT);
        this.setTickRandomly(true);

        setDefaultState(blockState.getBaseState().withProperty(LUCIDITY_STATE, EnumLucidityState.DORMANT));
    }

    /** Render texture at fullbright, if open, and the config option is true. */
    @SideOnly(Side.CLIENT)
    public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos)
    { return state.getValue(HALF) == EnumBlockHalf.UPPER && state.getValue(LUCIDITY_STATE) == EnumLucidityState.AWAKE ? 15728880 : super.getPackedLightmapCoords(state, source, pos); }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    { return BUSH_AABB; }

    /** Controls the 'awake' value of the double plant. */
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    { preformSwapping(worldIn, pos, false); }

    public void preformSwapping(World worldIn, BlockPos pos, boolean isResponding)
    {
        IBlockState state = worldIn.getBlockState(pos);
        if (worldIn.isRemote || state.getValue(HALF) != EnumBlockHalf.LOWER) return;

        boolean isNight = WorldPaleGarden.isNight(worldIn);
        SoundEvent sound = isNight ? JTPGSounds.BLOCK_POLLENHEAD_OPEN : JTPGSounds.BLOCK_POLLENHEAD_CLOSE;
        EnumLucidityState lucidity = isNight ? EnumLucidityState.LUCID : EnumLucidityState.DORMANT;

        if (state.getValue(LUCIDITY_STATE) == lucidity || state.getValue(LUCIDITY_STATE) == EnumLucidityState.AWAKE) return;

        //sound = lucidity == EnumLucidityState.DORMANT ? JTPGSounds.BLOCK_POLLENHEAD_CLOSE_LUCID : sound;

        worldIn.playSound(null, pos, sound, SoundCategory.BLOCKS, 0.25F, 1.0F);
        this.placeDoubleAt(worldIn, pos, lucidity, 2);
    }


    /** Does the placeAt when placed. */
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    { this.placeDoubleAt(worldIn, pos, EnumLucidityState.DORMANT, 2); }

    /** Places the double tall palnt. */
    public void placeDoubleAt(World worldIn, BlockPos lowerPos, EnumLucidityState state, int flags)
    {
        worldIn.setBlockState(lowerPos, this.getDefaultState().withProperty(HALF, EnumBlockHalf.LOWER).withProperty(LUCIDITY_STATE, state), flags);
        worldIn.setBlockState(lowerPos.up(), this.getDefaultState().withProperty(HALF, EnumBlockHalf.UPPER).withProperty(LUCIDITY_STATE, state), flags);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (rand.nextInt(512) == 0 && stateIn.getValue(LUCIDITY_STATE) == EnumLucidityState.AWAKE)
        {
            worldIn.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, JTPGSounds.BLOCK_POLLENHEAD_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumLucidityState state = EnumLucidityState.values()[meta & 3];
        EnumBlockHalf half = (meta & 4) == 0 ? EnumBlockHalf.LOWER : EnumBlockHalf.UPPER;
        return this.getDefaultState().withProperty(LUCIDITY_STATE, state).withProperty(HALF, half);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = 0;
        meta |= state.getValue(LUCIDITY_STATE).ordinal();
        if (state.getValue(HALF) == EnumBlockHalf.UPPER) meta |= 4;
        return meta;
    }

    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, HALF, LUCIDITY_STATE); }


    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    { return true; }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        if (state.getValue(HALF) == EnumBlockHalf.UPPER)
        { return new TilePollenhead(); }
        return null;
    }
}