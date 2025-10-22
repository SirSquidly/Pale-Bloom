package com.sirsquidly.palebloom.blocks;

import com.sirsquidly.palebloom.blocks.tileentity.TileHydraweedBody;
import com.sirsquidly.palebloom.entity.EntityHydraweedJaw;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.init.JTPGItems;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.world.WorldPaleGarden;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockHydraweedBody extends Block implements ITileEntityProvider
{
    public static final PropertyEnum<EnumBodyState> HEART_STATE = PropertyEnum.create("heart_state", EnumBodyState.class);
    public static final PropertyBool NATURAL = PropertyBool.create("natural");

    public BlockHydraweedBody()
    {
        super(Material.GRASS, MapColor.SILVER);
        this.setSoundType(JTPGSounds.CREAKING_HEART);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setTickRandomly(true);
        this.getDefaultState().withProperty(HEART_STATE, EnumBodyState.UPROOTED).withProperty(NATURAL, false);
    }

    public EnumBodyState getCurrentBodyState(World world, BlockPos pos, IBlockState state)
    {
        if (hasSupportingBlocks(world, pos, state))
        { return WorldPaleGarden.isNight(world) ? EnumBodyState.AWAKE : EnumBodyState.DORMANT; }
        return EnumBodyState.UPROOTED;
    }

    public boolean hasSupportingBlocks(World world, BlockPos pos, IBlockState state)
    { return blockSupportsBody(world.getBlockState(pos.down()).getBlock()); }

    /** If the given block can support a Creaking Heart. */
    public boolean blockSupportsBody(Block block)
    { return block == JTPGBlocks.PALE_MOSS; }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumBodyState state = EnumBodyState.values()[meta & 3];
        return this.getDefaultState().withProperty(HEART_STATE, state).withProperty(NATURAL, (meta & 4) != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = 0;
        meta |= state.getValue(HEART_STATE).ordinal();
        if (state.getValue(NATURAL)) meta |= 4;
        return meta;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileHydraweedBody)
        {
            EntityHydraweedJaw jaw = (EntityHydraweedJaw)((TileHydraweedBody) tileentity).getJaw();

            if (jaw != null)
            {
                jaw.setDead();
            }
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    protected boolean canSilkHarvest() { return true; }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) { return null; }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        if (state.getValue(NATURAL)) spawnAsEntity(worldIn, pos, new ItemStack(JTPGItems.AMBER_VALVE));
    }

    /** Drop 20-24 XP when destroyed. */
    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this) && state.getValue(NATURAL))
        { return MathHelper.getInt(rand, 20, 24); }

        return 0;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    { return new TileHydraweedBody(); }

    @Override
    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, HEART_STATE, NATURAL); }

    /** An Enum to determine how tall the side vines are on each side. */
    public static enum EnumBodyState implements IStringSerializable
    {
        UPROOTED("uprooted"),
        DORMANT("dormant"),
        AWAKE("awake");
        private final String name;

        private EnumBodyState(String name)
        { this.name = name; }

        public String toString()
        { return this.name; }
        public String getName()
        { return this.name; }
    }
}