package com.sirsquidly.palebloom.common.blocks;

import com.sirsquidly.palebloom.config.Config;
import com.sirsquidly.palebloom.common.blocks.tileentity.TileCreakingHeart;
import com.sirsquidly.palebloom.common.entity.EntityCreaking;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.init.JTPGItems;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.common.world.WorldPaleGarden;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockCreakingHeart extends BlockRotatedPillar implements ITileEntityProvider
{
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);
    public static final PropertyEnum<EnumHeartState> HEART_STATE = PropertyEnum.create("heart_state", EnumHeartState.class);
    public static final PropertyBool NATURAL = PropertyBool.create("natural");

    private static final boolean naturalAmberValveDrop = Config.block.creakingHeart.naturalAmberValveDrop;
    private static final boolean alertReapingWillows = Config.block.creakingHeart.alertReapingWillows;

    public BlockCreakingHeart(Material materialIn, MapColor color)
    {
        super(materialIn, color);
        this.setSoundType(JTPGSounds.CREAKING_HEART);
        this.setHarvestLevel("axe", 0);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setTickRandomly(true);
        this.getDefaultState().withProperty(HEART_STATE, EnumHeartState.UPROOTED).withProperty(NATURAL, false);
    }

    public EnumHeartState getCurrentHeartState(World world, BlockPos pos, IBlockState state)
    {
        if (hasSupportingBlocks(world, pos, state))
        { return WorldPaleGarden.isNight(world) ? EnumHeartState.AWAKE : EnumHeartState.DORMANT; }
        return EnumHeartState.UPROOTED;
    }

    public boolean hasSupportingBlocks(World world, BlockPos pos, IBlockState state)
    {
        EnumFacing.Axis axis = state.getValue(AXIS);
        EnumFacing positiveFacing = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis);

        return blockSupportsHeart(world.getBlockState(pos.offset(positiveFacing)).getBlock()) && blockSupportsHeart(world.getBlockState(pos.offset(positiveFacing.getOpposite())).getBlock());
    }

    /** If the given block can support a Creaking Heart. */
    public boolean blockSupportsHeart(Block block)
    { return block == JTPGBlocks.PALE_OAK_LOG; }

    /** Used for playing the ambient sounds. */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (rand.nextInt(16) == 0 && stateIn.getValue(HEART_STATE) != EnumHeartState.UPROOTED && WorldPaleGarden.isNight(worldIn))
        {
            worldIn.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, JTPGSounds.BLOCK_CREAKING_HEART_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing.Axis axis;

        switch (meta & 3)
        {
            case 0: default: axis = EnumFacing.Axis.Y; break;
            case 1: axis = EnumFacing.Axis.X; break;
            case 2: axis = EnumFacing.Axis.Z; break;
        }

        boolean natural = (meta & 4) != 0;

        return getDefaultState().withProperty(AXIS, axis).withProperty(NATURAL, natural);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = 0;

        switch (state.getValue(AXIS))
        {
            case X: meta = 1; break;
            case Z: meta = 2; break;
            case Y: default: meta = 0; break;
        }

        if (state.getValue(NATURAL)) meta |= 4;

        return meta;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileCreakingHeart)
        {
            EntityCreaking creaking = (EntityCreaking)((TileCreakingHeart) tileentity).getCreaking();

            if (creaking != null)
            {
                creaking.setLastAttackedEntity(player);
                creaking.preformTwitchingDeath(43);
            }
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    protected boolean canSilkHarvest() { return true; }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return JTPGItems.RESIN_CLUMP;
    }

    /** Drops 1-3 Resin Clumps, +1 per fortune level. */
    public int quantityDropped(Random random) { return 1 + random.nextInt(3); }

    public int quantityDroppedWithBonus(int fortune, Random random)
    { return this.quantityDropped(random) + random.nextInt(fortune + 1); }

    /** Natural Hearts drop the Amber Valve and EXP when mined. */
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        if (alertReapingWillows) WorldPaleGarden.alertReapingWillow(worldIn, pos, player, 16);

        if (state.getValue(NATURAL))
        {
            if (naturalAmberValveDrop) spawnAsEntity(worldIn, pos, new ItemStack(JTPGItems.AMBER_VALVE));

            /* Drop 20-24 EXP. */
            int i = MathHelper.getInt(worldIn.rand, 20, 24);
            while (i > 0)
            {
                int j = EntityXPOrb.getXPSplit(i);
                i -= j;
                worldIn.spawnEntity(new EntityXPOrb(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, j));
            }
        }
    }

    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileCreakingHeart) return ((TileCreakingHeart) tileentity).getComparatorOutput();
        return 0;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    { return new TileCreakingHeart(); }

    @Override
    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, AXIS, HEART_STATE, NATURAL); }

    /** An Enum to determine how tall the side vines are on each side. */
    public static enum EnumHeartState implements IStringSerializable
    {
        UPROOTED("uprooted"),
        DORMANT("dormant"),
        AWAKE("awake");
        private final String name;

        private EnumHeartState(String name)
        { this.name = name; }

        public String toString()
        { return this.name; }
        public String getName()
        { return this.name; }
    }
}