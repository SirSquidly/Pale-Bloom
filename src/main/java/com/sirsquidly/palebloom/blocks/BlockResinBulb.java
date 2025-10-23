package com.sirsquidly.palebloom.blocks;

import com.sirsquidly.palebloom.blocks.tileentity.TileResinBulb;
import com.sirsquidly.palebloom.init.JTPGItems;
import com.sirsquidly.palebloom.init.JTPGSounds;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockResinBulb extends Block implements ITileEntityProvider
{
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 3);
    protected static final AxisAlignedBB[] BULB_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.375D, 0.75D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.5D, 0.8125D), new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.625D, 0.875D), new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.8125D, 0.9375D)};

    public BlockResinBulb(Material blockMaterialIn, MapColor blockMapColorIn, SoundType soundIn)
    {
        super(blockMaterialIn, blockMapColorIn);
        this.setSoundType(soundIn);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

        //this.getDefaultState().withProperty(LEVEL, 0);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    { return new TileResinBulb(); }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote) return true;
        ItemStack stack = playerIn.getHeldItem(hand);

        boolean isPaleOakSap = stack.getItem() == JTPGItems.PALE_OAK_SAP;
        if (stack.getItem() != JTPGItems.RESIN_CLUMP && !isPaleOakSap) return false;

        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileResinBulb)
        {
            TileResinBulb tile = (TileResinBulb)tileentity;
            if (tile.getStoredResin() >= tile.maxResin) return false;

            if (!playerIn.isCreative())
            {
                stack.shrink(1);

                if (isPaleOakSap)
                {
                    ItemStack newStack = new ItemStack(Items.GLASS_BOTTLE);
                    if (stack.isEmpty())
                    { playerIn.setHeldItem(EnumHand.MAIN_HAND, newStack); }
                    else if (!playerIn.inventory.addItemStackToInventory(newStack))
                    { playerIn.dropItem(newStack, false); }
                }
            }

            playerIn.swingArm(hand);
            worldIn.playSound(null, pos, JTPGSounds.BLOCK_RESIN_PLACE, SoundCategory.BLOCKS, 0.8F, (worldIn.rand.nextFloat() * 0.4F) + 0.8F);

            tile.setStoredResin(isPaleOakSap ? Math.min(64, tile.getStoredResin() + 32) : tile.getStoredResin() + 1);
            return true;
        }

        return false;
    }

    protected boolean canSilkHarvest() { return true; }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.005F);

        if (this.canSilkHarvest(worldIn, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0)
        {
            if (te instanceof TileResinBulb)
            {
                TileResinBulb tile = (TileResinBulb)te;

                ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this), 1, Math.min(3, Math.max(0, tile.storedResin / 18)));
                NBTTagCompound nbt = new NBTTagCompound();
                NBTTagCompound blockEntityTag = new NBTTagCompound();

                blockEntityTag.setInteger("StoredResin", tile.storedResin);
                nbt.setTag("BlockEntityTag", blockEntityTag);
                itemstack.setTagCompound(nbt);

                spawnAsEntity(worldIn, pos, itemstack);
            }
        }
        else
        {
            if (te instanceof TileResinBulb)
            {
                TileResinBulb tile = (TileResinBulb)te;

                spawnAsEntity(worldIn, pos, new ItemStack(JTPGItems.RESIN_CLUMP, tile.storedResin));
                spawnAsEntity(worldIn, pos, new ItemStack(JTPGItems.AMBER_VALVE));
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    { return this.getDefaultState().withProperty(LEVEL, meta); }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    { return BULB_AABB[state.getValue(LEVEL)]; }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    { return BULB_AABB[state.getValue(LEVEL)]; }

    public boolean isFullCube(IBlockState state)
    { return false; }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    { return BlockFaceShape.UNDEFINED; }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public boolean isOpaqueCube(IBlockState state) { return false; }

    public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(LEVEL, meta); }

    public int getMetaFromState(IBlockState state)
    { return state.getValue(LEVEL); }

    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, LEVEL); }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        NBTTagCompound nbttagcompound = stack.getTagCompound();

        if (nbttagcompound != null && nbttagcompound.hasKey("BlockEntityTag", 10))
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("BlockEntityTag");

            if (nbttagcompound1.hasKey("StoredResin") && nbttagcompound1.getInteger("StoredResin") > 0)
            { tooltip.add(I18n.translateToLocalFormatted("description.palebloom.resin_bulb.quantity", nbttagcompound1.getInteger("StoredResin"))); }
        }

        tooltip.add(I18n.translateToLocalFormatted("description.palebloom.resin_bulb.desc1"));
        tooltip.add(I18n.translateToLocalFormatted("description.palebloom.resin_bulb.desc2"));
    }
}