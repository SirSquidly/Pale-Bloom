package com.sirsquidly.palebloom.blocks;

import com.sirsquidly.palebloom.blocks.base.BlockJTPGLog;
import com.sirsquidly.palebloom.blocks.tileentity.TilePaleOakHollow;
import com.sirsquidly.palebloom.init.JTPGItems;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.world.WorldPaleGarden;
import net.minecraft.block.BlockLog;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockPaleOakHollow extends BlockJTPGLog implements ITileEntityProvider, IEyeblossomListener
{
    public static final PropertyBool AWAKE = PropertyBool.create("awake");
    public static final PropertyInteger SAP_LEVEL = PropertyInteger.create("sap_level", 0, 4);

    public BlockPaleOakHollow()
    {
        super();
        this.setTickRandomly(true);
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity tile = worldIn instanceof ChunkCache ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);

        if (tile instanceof TilePaleOakHollow) { return state.withProperty(SAP_LEVEL, ((TilePaleOakHollow) tile).getSapLevel()); }

        return state;
    }

    /** Prevents the hollow from being awake automatically when placed. */
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    { return this.getStateFromMeta(meta).withProperty(LOG_AXIS, BlockLog.EnumAxis.fromFacingAxis(facing.getAxis())).withProperty(AWAKE, false); }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = playerIn.getHeldItem(hand);

        if (stack.getItem() != Items.GLASS_BOTTLE) return false;

        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TilePaleOakHollow)
        {
            if (((TilePaleOakHollow) tile).getSapLevel() < 4) return false;

            playerIn.swingArm(hand);
            worldIn.playSound(null, pos, JTPGSounds.BLOCK_RESIN_PLACE, SoundCategory.BLOCKS, 0.8F, (worldIn.rand.nextFloat() * 0.4F) + 0.8F);
            worldIn.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.BLOCKS, 1.0F, 1.0F);

            if (!worldIn.isRemote)
            {
                playerIn.addStat(StatList.getObjectUseStats(Items.GLASS_BOTTLE));

                if (!playerIn.isCreative()) stack.shrink(1);

                ItemStack newStack = new ItemStack(JTPGItems.PALE_OAK_SAP);
                if (stack.isEmpty())
                { playerIn.setHeldItem(EnumHand.MAIN_HAND, newStack); }
                else if (!playerIn.inventory.addItemStackToInventory(newStack))
                { playerIn.dropItem(newStack, false); }

                ((TilePaleOakHollow) tile).setSapLevel(0);
                ((TilePaleOakHollow) tile).storedResin = 0;
                tile.markDirty();
                worldIn.notifyBlockUpdate(pos, state, state, 3);
            }

            return true;
        }

        return false;
    }

    /** Occurs randomly, meaning this Eyeblossom is NOT responding. */
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);
        preformSwapping(worldIn, pos, false);
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

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    { return new TilePaleOakHollow(); }

    @Override
    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, LOG_AXIS, AWAKE, SAP_LEVEL); }
}