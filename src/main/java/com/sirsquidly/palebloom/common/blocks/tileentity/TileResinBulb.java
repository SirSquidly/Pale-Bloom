package com.sirsquidly.palebloom.common.blocks.tileentity;

import com.sirsquidly.palebloom.common.blocks.BlockCreakingHeart;
import com.sirsquidly.palebloom.common.blocks.BlockResinBulb;
import com.sirsquidly.palebloom.config.ConfigCache;
import com.sirsquidly.palebloom.config.ConfigParser;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.init.JTPGItems;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.common.world.WorldPaleGarden;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class TileResinBulb extends TileEntity implements ITickable
{
    public ItemStack dropStack = new ItemStack(JTPGItems.RESIN_CLUMP);

    /** The minimum blocks required for a Conduit to be able to Hunt/Defend. */
    public int bulbCheckDistanceXZ = 8;
    public int bulbCheckDistanceY = 10;
    public int maxResin = 64;

    public int storedResin;

    public int bulbLevel = 0;
    public int cachedBulbLevel = 0;

    public BlockPos cachedHeartPos;
    public int heartSearchCooldown = 0;

    /** This is used to prevent every Resin Bulb from acting synced in function. */
    private int randomTickOffset = -1;
    private int heartTimer;

    @Override
    public void update()
    {
        if(world == null) return;

        if (--randomTickOffset > 0) return;
        randomTickOffset = 60;

        if (!WorldPaleGarden.isNight(world))
        {
            if (this.getStoredResin() < maxResin) tryResinHarvest(world, pos, world.rand);
            else
            { WorldPaleGarden.spawnParticles(world, pos, pos.up(1 + world.rand.nextInt(3)), 1, 2); }
        }
        else if (++heartTimer >= 5)
        {
            heartTimer = 0;
            if (this.getStoredResin() < this.maxResin) tryActiveHeartHarvest(world, pos);
        }

        bulbLevel = Math.min(3, Math.max(0, this.getStoredResin() / 18));

        if (cachedBulbLevel != bulbLevel)
        {
            this.world.setBlockState(this.pos, world.getBlockState(pos).withProperty(BlockResinBulb.LEVEL, bulbLevel), 3);

            SoundEvent sound = bulbLevel > cachedBulbLevel ? JTPGSounds.BLOCK_RESIN_BULB_INFLATE : JTPGSounds.BLOCK_RESIN_BULB_DEFLATE;
            world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 0.5F + (float) this.getStoredResin() / maxResin);
            cachedBulbLevel = bulbLevel;
            this.markDirty();
        }
    }

    public void tryResinHarvest(World world, BlockPos pos, Random random)
    {
        int offsetX = random.nextInt(bulbCheckDistanceXZ * 2) - bulbCheckDistanceXZ;
        int offsetY = random.nextInt(bulbCheckDistanceY * 2) - bulbCheckDistanceY;
        int offsetZ = random.nextInt(bulbCheckDistanceXZ * 2) - bulbCheckDistanceXZ;

        BlockPos checkPos = pos.add(offsetX, offsetY, offsetZ);

        int index = ConfigParser.blockResinBulbCollectFROM.indexOf(world.getBlockState(checkPos));
        if (index < 0) return;
        int resinCollected = ConfigParser.blockResinBulbCollectQUANITTY.get(index);

        this.setStoredResin(Math.min(maxResin, this.getStoredResin() + resinCollected));
        this.markDirty();

        world.playSound(null, pos, JTPGSounds.BLOCK_RESIN_PLACE, SoundCategory.BLOCKS, 0.25F, (world.rand.nextFloat() * 0.4F) + 0.8F);

        WorldPaleGarden.spawnParticles(world, checkPos, pos, 1, 2);
    }

    /** Collects a great amount of Resin if there is an active Creaking Heart nearby. */
    public void tryActiveHeartHarvest(World world, BlockPos pos)
    {
        if (ConfigCache.rsnBlb_creakingHeartResinReap == 0) return;

        TileCreakingHeart heart = getCachedHeart(world, pos);

        /* If the cached heart fails the check, we need to scan for a new one*/
        if (heart == null)
        {
            if (--heartSearchCooldown > 0) return;
            heart = findNearbyHeart(world, pos);

            if (heart == null)
            {
                heartSearchCooldown = 3;
                return;
            }
            cachedHeartPos = heart.getPos();
        }

        /* We need the Heart ACTIVE with a CREAKING to draw Resin. */
        if (heart.getCreaking() == null) return;
        this.setStoredResin(Math.min(maxResin, this.getStoredResin() + ConfigCache.rsnBlb_creakingHeartResinReap));
        this.markDirty();

        world.playSound(null, pos, JTPGSounds.BLOCK_RESIN_PLACE, SoundCategory.BLOCKS, 0.25F, (world.rand.nextFloat() * 0.4F) + 0.8F);

        WorldPaleGarden.spawnParticles(world, heart.getPos(), pos, 8, 2);
    }

    @Nullable
    private TileCreakingHeart findNearbyHeart(World world, BlockPos pos)
    {
        int radius = 3;

        for (BlockPos.MutableBlockPos checkPos : BlockPos.getAllInBoxMutable(pos.add(-radius, -radius, -radius), pos.add(radius, radius, radius)))
        {
            if (pos.distanceSq(checkPos) > (radius * radius)) continue;

            if (!world.isBlockLoaded(checkPos)) continue;

            /* Check if the Block is NOT an Uprooted Creaking Heart. */
            IBlockState state = world.getBlockState(checkPos);
            if (state.getBlock() == JTPGBlocks.CREAKING_HEART)
            {
                BlockCreakingHeart.EnumHeartState heartState = ((BlockCreakingHeart)state.getBlock()).getCurrentHeartState(world, checkPos, state);
                if (heartState == BlockCreakingHeart.EnumHeartState.UPROOTED) continue;
            }

            TileEntity te = world.getTileEntity(checkPos);
            if (!(te instanceof TileCreakingHeart)) continue;
            return (TileCreakingHeart) te;
        }
        return null;
    }


    /** Used to verify the cached Creaking Heart. */
    @Nullable
    private TileCreakingHeart getCachedHeart(World world, BlockPos pos)
    {
        if (cachedHeartPos == null) return null;

        TileEntity te = world.getTileEntity(cachedHeartPos);

        if (!(te instanceof TileCreakingHeart))
        {
            cachedHeartPos = null;
            return null;
        }

        /* Check if the Block is NOT an Uprooted Creaking Heart. */
        IBlockState state = world.getBlockState(cachedHeartPos);
        if (state.getBlock() == JTPGBlocks.CREAKING_HEART)
        {
            BlockCreakingHeart.EnumHeartState heartState =  ((BlockCreakingHeart) state.getBlock()).getCurrentHeartState(world, cachedHeartPos, state);
            if (heartState == BlockCreakingHeart.EnumHeartState.UPROOTED)
            {
                cachedHeartPos = null;
                return null;
            }
        }

        return (TileCreakingHeart) te;
    }

    public int getStoredResin()
    { return this.storedResin; }

    public void setStoredResin(int resinIn)
    { this.storedResin = resinIn; }

    /** Keeps the tile entity around even if the block gets its state changed. */
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    { return oldState.getBlock() != newState.getBlock(); }

    @Override
    public void onLoad()
    {
        super.onLoad();
        if (!world.isRemote && randomTickOffset < 0) randomTickOffset = world.rand.nextInt(60);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("StoredResin", this.getStoredResin());
        if (cachedHeartPos != null)
        {
            compound.setInteger("CachedHeartX", cachedHeartPos.getX());
            compound.setInteger("CachedHeartY", cachedHeartPos.getY());
            compound.setInteger("CachedHeartZ", cachedHeartPos.getZ());
        }
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.setStoredResin(compound.getInteger("StoredResin"));
        if (compound.hasKey("CachedHeartX") && compound.hasKey("CachedHeartY") && compound.hasKey("CachedHeartZ"))
        {
            cachedHeartPos = new BlockPos(compound.getInteger("CachedHeartX"), compound.getInteger("CachedHeartY"), compound.getInteger("CachedHeartZ"));
        }
        else cachedHeartPos = null;
    }

    private final IItemHandler handler = new ResinHandler();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    { return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing); }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        { return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler); }
        return super.getCapability(capability, facing);
    }


    private class ResinHandler implements IItemHandler
    {
        public int getSlots() { return 1; }

        public @Nonnull ItemStack getStackInSlot(int slot) { return ItemStack.EMPTY; }

        @Override
        public @Nonnull ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
        {
            if (stack.getItem() == JTPGItems.RESIN_CLUMP && getStoredResin() < maxResin)
            {
                int insertCount = stack.getCount();
                if (!simulate)
                {
                    setStoredResin(getStoredResin() + insertCount);
                    markDirty();
                }
                return ItemStack.EMPTY;
            }
            return stack;
        }

        @Override
        public @Nonnull ItemStack extractItem(int slot, int amount, boolean simulate) { return ItemStack.EMPTY; }

        @Override
        public int getSlotLimit(int slot) { return maxResin; }
    }
}