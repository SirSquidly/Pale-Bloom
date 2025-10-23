package com.sirsquidly.palebloom.blocks.tileentity;

import com.sirsquidly.palebloom.Config;
import com.sirsquidly.palebloom.blocks.BlockCreakingHeart;
import com.sirsquidly.palebloom.blocks.BlockResinClump;
import com.sirsquidly.palebloom.entity.EntityCreaking;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.paleBloom;
import com.sirsquidly.palebloom.world.WorldPaleGarden;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.*;

public class TileCreakingHeart extends TileEntity implements ITickable
{
    /** The minimum blocks required for a Conduit to be able to Hunt/Defend. */
    public int spawnXZDistance = 16;
    public int spawnYDistance = 8;
    public int resinPlacementCooldown = 100;

    public int ticksExisted;
    public int requiredPlayerRange = 32;
    public int lastCreakingHurtTime;

    @Nullable
    private UUID creakingUUID;
    private int comparatorOutput = 0;

    private static final boolean enableCreaking = Config.entity.creaking.enableCreaking;
    private static final boolean naturalResinClumps = Config.block.creakingHeart.naturalResinClumps;
    private static final boolean unnaturalResinClumps = Config.block.creakingHeart.unnaturalResinClumps;

    @Override
    public void update()
    {
        if(world == null) return;

        if (this.ticksExisted++ < lastCreakingHurtTime)
        {
            spawnTrailParticles(3);
        }

        /* Unlike vanilla, don't try recalculating the cpmparator output every single tick! */
        if (world.getTotalWorldTime() % 5L == 0L && !world.isRemote)
        {
            int comparatorSignal = this.getCreaking() != null ? (int) (15 - Math.floor(this.getCreakingDistance() / 32 * 15)) : 0;
            if (comparatorSignal != this.getComparatorOutput())
            {
                this.setComparatorOutput(comparatorSignal);
                world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
            }
        }

        if(world.getTotalWorldTime() % 20L == 0L)
        {
            IBlockState state = world.getBlockState(pos);
            BlockCreakingHeart.EnumHeartState newState = ((BlockCreakingHeart)state.getBlock()).getCurrentHeartState(world, pos, world.getBlockState(pos));

            if (state.getValue(BlockCreakingHeart.HEART_STATE) != newState && (newState != BlockCreakingHeart.EnumHeartState.UPROOTED || getCreakingUUID() == null))
            {
                world.setBlockState(pos, state.withProperty(BlockCreakingHeart.HEART_STATE, newState), 2);

                if (newState == BlockCreakingHeart.EnumHeartState.UPROOTED) return;
            }

            /* If the Creaking isn't enabled, no need to do spawning or checks. */
            if (world.isRemote || !enableCreaking) return;

            if (getCreakingUUID() == null)
            {
                if (newState == BlockCreakingHeart.EnumHeartState.AWAKE) summonCreaking(8);
            }
            else checkCreaking(WorldPaleGarden.isNight(world));
        }
    }

    /**
     *  All logic for summoning the Creaking.
    * int maxAttempts = How many max times to re-roll the checks for a single spawn, stops when one succeeds or when max is reached
    * */
    public void summonCreaking(int maxAttempts)
    {
        /* Early exit. */
        if (!world.isAnyPlayerWithinRangeAt(pos.getX(), pos.getY(), pos.getZ(), requiredPlayerRange) || world.getDifficulty() == EnumDifficulty.PEACEFUL || !world.getGameRules().getBoolean("doMobSpawning")) return;

        for (int i = 0; i < maxAttempts; i++)
        {
            EntityCreaking creaking = new EntityCreaking(world);

            BlockPos spawnPos = getCreakingSpawnPosition();
            if (spawnPos == null) return;

            creaking.setLocationAndAngles(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, world.rand.nextFloat() * 360.0F, 0.0F);
            if (!creaking.isNotColliding()) return;

            world.playSound(null, pos, JTPGSounds.BLOCK_CREAKING_HEART_SPAWN_MOB, SoundCategory.BLOCKS, 1.0F, 1.0F);
            creaking.setHeartPos(pos);
            creaking.setHomePosAndDistance(pos, spawnXZDistance - 1);
            world.spawnEntity(creaking);
            world.playSound(null, creaking.getPosition(), JTPGSounds.ENTITY_CREAKING_SPAWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
            /* Save this Creaking to the Heart, and mark the tile entity as dirty, so it saves properly. */
            this.setCreakingUUID(creaking.getUniqueID());
            this.markDirty();
            break;
        }
    }

    /** Finds a block position for the Creaking to spawn at. */
    public BlockPos getCreakingSpawnPosition()
    {
        int offsetX = world.rand.nextInt(spawnXZDistance * 2) - spawnXZDistance;
        int offsetZ = world.rand.nextInt(spawnXZDistance * 2) - spawnXZDistance;
        BlockPos candidate = pos.add(offsetX, spawnYDistance, offsetZ);

        for (int y = spawnYDistance; y >= -spawnYDistance; --y)
        {
            candidate = candidate.down();
            IBlockState belowState = world.getBlockState(candidate.down());

            if (belowState.isSideSolid(world, candidate, EnumFacing.UP) && !(belowState.getBlock() instanceof BlockLeaves))
            { return candidate; }
        }
        return null;
    }

    public void checkCreaking(boolean isNightIn)
    {
        if (world.isRemote) return;

        EntityCreaking creaking = (EntityCreaking)getCreaking();

        if (creaking == null || creaking.isDead)
        {
            this.setCreakingUUID(null);
            this.markDirty();
            return;
        }

        if (getCreakingDistance() >= 34.0F) { creaking.preformTwitchingDeath(1); }

        if (!isNightIn && !creaking.hasCustomName())
        { creaking.preformTwitchingDeath(1); }
    }

    /** This method specifically gets called by the EntityCreaking class, whenever the Creaking is hit. */
    public void preformHitReact(DamageSource source)
    {
        world.playSound(null, pos, JTPGSounds.BLOCK_CREAKING_HEART_TRAIL, SoundCategory.BLOCKS, 1.0F, 1.0F);

        if (!world.isRemote && WorldPaleGarden.isNight(world) && source.getTrueSource() instanceof EntityPlayer)
        {
            if (ticksExisted >= lastCreakingHurtTime)
            {
                lastCreakingHurtTime = ticksExisted + resinPlacementCooldown;

                /* Check the config booleans to see if Resin can be placed, based on the Natural value. */
                if ((getNatural() && !naturalResinClumps) || (!getNatural() && !unnaturalResinClumps)) return;
                tryPlaceResin(world, pos, world.rand);
            }
        }
    }

    /** Spawns Trail Particles to and from the Creaking. */
    public void spawnTrailParticles(int quantityIn)
    {
        Entity entity = this.getCreaking();
        if (entity == null || entity.isDead) return;

        for (int i = 0; i < quantityIn; ++i)
        {
            double hx = this.pos.getX() + 0.5 + (world.rand.nextDouble() * 0.5 - 0.25);
            double hy = this.pos.getY() + 0.5 + (world.rand.nextDouble() * 0.5 - 0.25);
            double hz = this.pos.getZ() + 0.5 + (world.rand.nextDouble() * 0.5 - 0.25);

            double cX = entity.posX + (world.rand.nextDouble() * 0.5 - 0.25);
            double cY = entity.posY + (world.rand.nextDouble() * entity.height);
            double cZ = entity.posZ + (world.rand.nextDouble() * 0.5 - 0.25);

            paleBloom.proxy.spawnParticle(0, world, cX, cY, cZ, hx, hy, hz, 0);
            paleBloom.proxy.spawnParticle(0, world, hx, hy, hz, cX, cY, cZ, 1);
        }
    }

    /** Places Veins atop all open block faces of blocks neighboring the given position. */
    public void tryPlaceResin(World world, BlockPos pos, Random rand)
    {
        int resinRange = 3;
        int placements = rand.nextInt(2) + 2;

        List<BlockPos> goodPositions = new ArrayList<>();
        for (BlockPos checkPos : BlockPos.getAllInBoxMutable( pos.add(-resinRange, -resinRange, -resinRange), pos.add(resinRange, resinRange, resinRange)))
        {
            if (Math.abs(checkPos.getX() - pos.getX()) + Math.abs(checkPos.getY() - pos.getY()) + Math.abs(checkPos.getZ() - pos.getZ()) > resinRange) continue;
            goodPositions.add(new BlockPos(checkPos));
        }
        Collections.shuffle(goodPositions, rand);


        for (BlockPos currentPos : goodPositions)
        {
            for (EnumFacing facing : EnumFacing.values())
            {
                BlockPos outerPos = currentPos.offset(facing);
                IBlockState adjacentState = world.getBlockState(outerPos);

                if (!adjacentState.isSideSolid(world, outerPos, facing.getOpposite())) continue;
                if (adjacentState.getBlock() != JTPGBlocks.PALE_OAK_LOG) continue;

                IBlockState targetState = world.getBlockState(currentPos);

                if (targetState.getBlock().isAir(targetState, world, currentPos))
                {
                    world.setBlockState(currentPos, BlockResinClump.getBlockState(facing));
                    world.playSound(null, pos, JTPGSounds.BLOCK_RESIN_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    placements--;
                }
                else if (targetState.getBlock() instanceof BlockResinClump)
                {
                    EnumFacing[] existingFacings = BlockResinClump.getFacings(targetState);
                    if (!ArrayUtils.contains(existingFacings, facing))
                    {
                        world.setBlockState(currentPos, BlockResinClump.getBlockState(ArrayUtils.add(existingFacings, facing)));
                        world.playSound(null, pos, JTPGSounds.BLOCK_RESIN_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        placements--;
                    }
                }

                if (placements <= 0) return;
            }
        }
    }

    /**
     *  This keeps the tile entity around even if the block gets its state changed.
     *
     *  The state is changed for the block using `setBlockState` when plugged or taken in/out of water.
     * */
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    { return oldState.getBlock() != newState.getBlock(); }

    public UUID getCreakingUUID() { return creakingUUID; }
    public void setCreakingUUID(UUID uuidIn) { creakingUUID = uuidIn; }

    public double getCreakingDistance() { return pos.getDistance(getCreaking().getPosition().getX(), getCreaking().getPosition().getY(), getCreaking().getPosition().getZ()); }

    public int getComparatorOutput() { return comparatorOutput; }
    public void setComparatorOutput(int outputIn) { comparatorOutput = outputIn; }

    /** Returns if this Heart is Natural. Based on checking the actual block class. */
    public boolean getNatural()
    { return world.getBlockState(pos).getValue(BlockCreakingHeart.NATURAL); }

    /** Gets the Creaking from the stored UUID. */
    @Nullable
    public Entity getCreaking()
    {
        try
        {
            UUID uuid = this.getCreakingUUID();
            return uuid == null ? null : ((WorldServer) this.world).getEntityFromUuid(uuid);
        }
        catch (IllegalArgumentException var2)
        { return null; }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if (getCreakingUUID() != null) compound.setUniqueId("SpawnedCreaking", creakingUUID);
        compound.setInteger("ComparatorOutput", getComparatorOutput());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (compound.hasUniqueId("SpawnedCreaking")) setCreakingUUID(compound.getUniqueId("SpawnedCreaking"));
        setComparatorOutput(compound.getInteger("ComparatorOutput"));
    }
}