package com.sirsquidly.palebloom.common.blocks.tileentity;

import com.sirsquidly.palebloom.config.Config;
import com.sirsquidly.palebloom.common.blocks.BlockPaleOakHollow;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.common.world.WorldPaleGarden;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TilePaleOakHollow extends TileEntity implements ITickable
{
    public int storedResin;
    public int resinPullQuantity = 8;
    public int resinMaxForFull = Config.block.paleOakHollow.resinToSapQuantity;

    public boolean isAwake = false;
    public int sapLevel = 0;

    public boolean cachedAwake = false;
    public int cachedSapLevel = 0;

    @Override
    public void update()
    {
        if(world.isRemote || world.getTotalWorldTime() % 120L != 0L) return;
        isAwake = WorldPaleGarden.isNight(world);

        if (cachedSapLevel != getSapLevel() || cachedAwake != isAwake) updatePlantState(pos);

        if (isAwake)
        {
            if (this.storedResin < resinMaxForFull)
            {
                storedResin += WorldPaleGarden.requestBulbResin(world, pos, resinPullQuantity).resinPulled;
                setSapLevel(Math.min(4, Math.max(0, storedResin / (int) (resinMaxForFull * 0.25F))));
                markDirty();
                IBlockState state = world.getBlockState(pos);
                world.notifyBlockUpdate(pos, state, state, 3);
            }
        }
    }

    /** Updates the Sapling by replacing it with the proper state values. */
    public void updatePlantState(BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);

        world.playSound(null, pos, JTPGSounds.BLOCK_MOSS_HIT, SoundCategory.BLOCKS, 0.5F, 1.0F);
        world.setBlockState(pos, state.withProperty(BlockPaleOakHollow.AWAKE, true).withProperty(BlockPaleOakHollow.SAP_LEVEL, getSapLevel()));

        cachedSapLevel = getSapLevel();
        cachedAwake = isAwake;
        markDirty();
        world.notifyBlockUpdate(pos, state, state, 3);
    }

    /** Simply returns the Sap Level. */
    public int getSapLevel() { return this.sapLevel; }
    public void setSapLevel(int sapLevelIn) { this.sapLevel = sapLevelIn; }

    /** Keeps the tile entity around even if the block gets its state changed. */
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    { return oldState.getBlock() != newState.getBlock(); }


    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    { return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag()); }

    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.getNbtCompound());
        if (world != null) world.markBlockRangeForRenderUpdate(pos, pos);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("StoredResin", this.storedResin);
        compound.setByte("SapLevel", (byte) getSapLevel());
        compound.setByte("CachedSapLevel", (byte) this.cachedSapLevel);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.storedResin = compound.getInteger("StoredResin");
        setSapLevel(compound.getByte("SapLevel"));
        this.cachedSapLevel = compound.getByte("CachedSapLevel");
    }
}