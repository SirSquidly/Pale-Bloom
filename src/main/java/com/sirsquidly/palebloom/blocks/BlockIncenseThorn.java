package com.sirsquidly.palebloom.blocks;

import com.sirsquidly.palebloom.blocks.tileentity.TileIncenseThorn;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.world.WorldPaleGarden;
import net.minecraft.block.BlockBush;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockIncenseThorn extends BlockBush implements ITileEntityProvider, IGardenState, IEyeblossomListener
{
    protected static final AxisAlignedBB BUSH_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);

    public BlockIncenseThorn()
    {
        this.setSoundType(SoundType.PLANT);
        this.setTickRandomly(true);

        this.setCreativeTab(CreativeTabs.BREWING);
        this.setTickRandomly(true);
        setDefaultState(blockState.getBaseState().withProperty(LUCIDITY_STATE, EnumLucidityState.DORMANT));
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) { return BUSH_AABB; }

    /** Controls the 'awake' value of the double plant. */
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    { preformSwapping(worldIn, pos, false); }

    public void preformSwapping(World worldIn, BlockPos pos, boolean isResponding)
    {
        IBlockState state = worldIn.getBlockState(pos);

        boolean isNight = WorldPaleGarden.isNight(worldIn);
        SoundEvent sound = isNight ? JTPGSounds.BLOCK_POLLENHEAD_OPEN : JTPGSounds.BLOCK_POLLENHEAD_CLOSE;
        EnumLucidityState lucidity = isNight ? EnumLucidityState.LUCID : EnumLucidityState.DORMANT;

        if (state.getValue(LUCIDITY_STATE) == lucidity || state.getValue(LUCIDITY_STATE) == EnumLucidityState.AWAKE) return;

        worldIn.playSound(null, pos, sound, SoundCategory.BLOCKS, 0.25F, 1.0F);
        worldIn.setBlockState(pos, state.withProperty(LUCIDITY_STATE, lucidity));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (world.isRemote) return;

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileIncenseThorn && stack.hasTagCompound())
        {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag.hasKey("potion"))
            {
                ((TileIncenseThorn) tile).setPotion(Potion.getPotionFromResourceLocation(tag.getString("potion")));

                /** If the given item does not have color pre-defined via NBT, attempt to get one from the defined Potion Effect. */
                if (tag.hasKey("color")) ((TileIncenseThorn) tile).setColor(tag.getInteger("color"));
                else ((TileIncenseThorn) tile).setupPotionColor();
            }
        }
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (te instanceof TileIncenseThorn)
        {
            TileIncenseThorn tile = (TileIncenseThorn)te;
            ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
            NBTTagCompound nbt = new NBTTagCompound();

            if (tile.getPotion() != null) nbt.setString("potion", tile.getPotion().getRegistryName().toString());
            itemstack.setTagCompound(nbt);
            spawnAsEntity(worldIn, pos, itemstack);
        }
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }


    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumLucidityState state = EnumLucidityState.values()[meta & 3];
        return this.getDefaultState().withProperty(LUCIDITY_STATE, state);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = 0;
        meta |= state.getValue(LUCIDITY_STATE).ordinal();
        return meta;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {  return new TileIncenseThorn(); }

    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, LUCIDITY_STATE); }
}