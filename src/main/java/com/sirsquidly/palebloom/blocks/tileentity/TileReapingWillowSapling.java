package com.sirsquidly.palebloom.blocks.tileentity;

import com.sirsquidly.palebloom.blocks.BlockReapingWillowSapling;
import com.sirsquidly.palebloom.entity.EntityReapingWillow;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.world.WorldPaleGarden;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TileReapingWillowSapling extends TileEntity implements ITickable
{
    public int storedResin;
    public int resinPullQuantity = 4;
    public int resinMaxForGrowth = 256;

    /* If the growth is currently being blocked. 1 = 2 tall is blocked (3 -> 4), 2 = Reaping Spawn is Blocked (256 Resin) */
    public int blockedType = 0;

    public int saplingStage = 0;
    public boolean isAwake = false;

    public int cachedSaplingStage = 0;
    public boolean cachedAwake = false;

    @Override
    public void update()
    {
        if(world.isRemote || world.getTotalWorldTime() % 20L != 0L) return;
        isAwake = WorldPaleGarden.isNight(world);

        /* Early exit if it has been detected as blocked prior. */
        if (blockedType > 0 && checkGrowthBlocked(pos.up(blockedType), isAwake && world.rand.nextBoolean())) return;

        if (cachedSaplingStage != saplingStage || cachedAwake != isAwake) updatePlantState(pos);

        if (isAwake)
        {
            if (this.storedResin < resinMaxForGrowth)
            {
                storedResin += WorldPaleGarden.requestBulbResin(world, pos, resinPullQuantity).resinPulled;
                saplingStage = Math.min(7, Math.max(0, storedResin / 36));
                markDirty();
            }
            else
            { preformReapingSpawn(); }
        }
    }

    /** Updates the Sapling by replacing it with the proper state values. */
    public void updatePlantState(BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if ((saplingStage == 4 || cachedSaplingStage == 3) && checkGrowthBlocked(pos.up(), true)) return;

        ((BlockReapingWillowSapling)block).placeDoubleAt(world, pos, isAwake, saplingStage, 3);

        if (cachedSaplingStage != saplingStage)
        {
            world.playSound(null, pos, JTPGSounds.BLOCK_MOSS_HIT, SoundCategory.BLOCKS, 0.5F, 1.0F);
            world.playEvent(2005, pos, 0);
            if (saplingStage > 3) world.playEvent(2005, pos.up(), 0);
        }

        cachedSaplingStage = saplingStage;
        cachedAwake = isAwake;
        markDirty();
    }

    /** Updates the Sapling by replacing it with the proper state values. */
    public boolean checkGrowthBlocked(BlockPos blockPos, boolean doEffects)
    {
        if (!world.isAirBlock(blockPos))
        {
            if (doEffects)
            {
                world.playSound(null, blockPos, JTPGSounds.ENTITY_REAPING_WILLOW_ANGER, SoundCategory.BLOCKS, 0.2F, 1.0F);

                ((WorldServer)this.world).spawnParticle(EnumParticleTypes.VILLAGER_ANGRY, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2, 0.25, 0.25, 0.25, 0.01D);
            }


            blockedType = blockPos.getY() - pos.getY();
            return true;
        }

        blockedType = 0;
        return false;
    }


    /** Updates the Sapling by replacing it with the proper state values. */
    public void preformReapingSpawn()
    {
        if (checkGrowthBlocked(pos.up(2), true)) return;

        EntityReapingWillow reapingWillow = new EntityReapingWillow(world);

        world.playSound(null, pos, JTPGSounds.BLOCK_CREAKING_HEART_SPAWN_MOB, SoundCategory.BLOCKS, 1.0F, 1.0F);

        reapingWillow.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, world.rand.nextFloat() * 360.0F, 0.0F);

        world.spawnEntity(reapingWillow);
        world.playSound(null, reapingWillow.getPosition(), JTPGSounds.ENTITY_CREAKING_SPAWN, SoundCategory.BLOCKS, 1.0F, 1.0F);

        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2 | 4);
        world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 2 | 4);

        this.markDirty();
    }


    /** Keeps the tile entity around even if the block gets its state changed. */
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    { return oldState.getBlock() != newState.getBlock(); }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("StoredResin", this.storedResin);
        compound.setByte("SaplingStage", (byte) this.saplingStage);
        compound.setByte("CachedSaplingStage", (byte) this.cachedSaplingStage);
        compound.setByte("Blocked", (byte) this.blockedType);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.storedResin = compound.getInteger("StoredResin");
        this.saplingStage = compound.getByte("SaplingStage");
        this.cachedSaplingStage = compound.getByte("CachedSaplingStage");
        this.blockedType = compound.getByte("Blocked");
    }
}