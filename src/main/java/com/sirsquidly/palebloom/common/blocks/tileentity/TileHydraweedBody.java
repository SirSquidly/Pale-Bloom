
package com.sirsquidly.palebloom.common.blocks.tileentity;

import com.sirsquidly.palebloom.common.blocks.BlockHydraweedBody;
import com.sirsquidly.palebloom.common.entity.EntityHydraweedJaw;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.paleBloom;
import com.sirsquidly.palebloom.common.world.WorldPaleGarden;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.*;

public class TileHydraweedBody extends TileEntity implements ITickable
{
    /** The minimum blocks required for a Conduit to be able to Hunt/Defend. */
    public int spawnXZDistance = 9;
    public int spawnYDistance = 7;
    public int resinPlacementCooldown = 100;

    public int ticksExisted;
    public int requiredPlayerRange = 16;
    public int lastCreakingHurtTime;

    @Nullable
    private UUID jawUUID;

    @Override
    public void update()
    {
        if(world == null) return;

        if (this.ticksExisted++ < lastCreakingHurtTime)
        {
            spawnTrailParticles(3);
        }

        if(world.getTotalWorldTime() % 20L == 0L)
        {
            IBlockState state = world.getBlockState(pos);
            BlockHydraweedBody.EnumBodyState newState = ((BlockHydraweedBody)state.getBlock()).getCurrentBodyState(world, pos, world.getBlockState(pos));

            if (state.getValue(BlockHydraweedBody.HEART_STATE) != newState && (newState != BlockHydraweedBody.EnumBodyState.UPROOTED || getJawUUID() == null))
            {
                world.setBlockState(pos, state.withProperty(BlockHydraweedBody.HEART_STATE, newState));

                if (newState == BlockHydraweedBody.EnumBodyState.UPROOTED) return;
            }

            if (world.isRemote) return;

            if (getJawUUID() == null)
            {
                if (newState == BlockHydraweedBody.EnumBodyState.AWAKE) summonJaw();
            }
            else checkCreaking(WorldPaleGarden.isNight(world));
        }
    }

    /* All logic for summoning the Creaking. */
    public void summonJaw()
    {
        /* Early exit. */
        if (!world.isAnyPlayerWithinRangeAt(pos.getX(), pos.getY(), pos.getZ(), requiredPlayerRange) || world.getDifficulty() == EnumDifficulty.PEACEFUL || !world.getGameRules().getBoolean("doMobSpawning")) return;

        EntityHydraweedJaw creaking = new EntityHydraweedJaw(world);

        int offsetX = world.rand.nextInt(spawnXZDistance * 2) - spawnXZDistance;
        int offsetZ = world.rand.nextInt(spawnXZDistance * 2) - spawnXZDistance;
        int offsetY = world.rand.nextInt(spawnYDistance * 2) - spawnYDistance;
        BlockPos spawnPos = pos.add(offsetX, offsetY, offsetZ);
        creaking.setLocationAndAngles(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, world.rand.nextFloat() * 360.0F, 0.0F);

        if (world.checkNoEntityCollision(creaking.getEntityBoundingBox(), creaking) && !world.collidesWithAnyBlock(creaking.getEntityBoundingBox()))
        {
            BlockPos groundPos = spawnPos.down();

            if (world.getBlockState(groundPos).isSideSolid(world, groundPos, EnumFacing.UP))
            {
                world.playSound(null, pos, JTPGSounds.BLOCK_CREAKING_HEART_SPAWN_MOB, SoundCategory.BLOCKS, 1.0F, 1.0F);
                creaking.setHomePosAndDistance(pos, spawnXZDistance);
                world.spawnEntity(creaking);
                world.playSound(null, creaking.getPosition(), JTPGSounds.ENTITY_CREAKING_SPAWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                /* Save this Creaking to the Heart, and mark the tile entity as dirty, so it saves properly. */
                this.setJawUUID(creaking.getUniqueID());
                this.markDirty();
            }
        }
    }

    public void checkCreaking(boolean isNightIn)
    {
        if (world.isRemote) return;

        EntityHydraweedJaw jaw = (EntityHydraweedJaw)getJaw();

        if (jaw == null || jaw.isDead)
        {
            this.setJawUUID(null);
            this.markDirty();
            return;
        }

        if (!isNightIn && !jaw.hasCustomName())
        { jaw.setDead(); }
    }

    /** This method specifically gets called by the EntityCreaking class, whenever the Creaking is hit. */
    public void preformHitReact(DamageSource source)
    {
        world.playSound(null, pos, JTPGSounds.BLOCK_CREAKING_HEART_TRAIL, SoundCategory.BLOCKS, 1.0F, 1.0F);

        if (!world.isRemote && WorldPaleGarden.isNight(world) &&  source.getTrueSource() instanceof EntityPlayer)

        if (ticksExisted >= lastCreakingHurtTime)
        {
            lastCreakingHurtTime = ticksExisted + resinPlacementCooldown;
        }
    }

    /** Spawns Trail Particles to and from the Creaking. */
    public void spawnTrailParticles(int quantityIn)
    {
        Entity entity = this.getJaw();
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

    /**
     *  This keeps the tile entity around even if the block gets its state changed.
     *
     *  The state is changed for the block using `setBlockState` when plugged or taken in/out of water.
     * */
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    { return oldState.getBlock() != newState.getBlock(); }

    public UUID getJawUUID() { return jawUUID; }
    public void setJawUUID(UUID uuidIn) { jawUUID = uuidIn; }

    /** Gets the Creaking from the stored UUID. */
    @Nullable
    public Entity getJaw()
    {
        try
        {
            UUID uuid = this.getJawUUID();
            return uuid == null ? null : ((WorldServer) this.world).getEntityFromUuid(uuid);
        }
        catch (IllegalArgumentException var2)
        { return null; }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if (getJawUUID() != null) compound.setUniqueId("SpawnedJaw", this.getJawUUID());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (compound.hasUniqueId("SpawnedJaw")) setJawUUID(compound.getUniqueId("SpawnedJaw"));
    }
}