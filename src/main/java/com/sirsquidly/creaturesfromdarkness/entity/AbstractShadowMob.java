package com.sirsquidly.creaturesfromdarkness.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import java.util.List;

public class AbstractShadowMob extends EntityCreature implements IMob
{
    /** Used to make sure the look reaction only occurs once per entity! */
    private static final DataParameter<Boolean> BEEN_SEEN = EntityDataManager.<Boolean>createKey(AbstractShadowMob.class, DataSerializers.BOOLEAN);
    /** How much smoke to spawn on death. */
    public int deathSmokeAmount;
    /** The minimum required blocklight for this shadow.json to consider itself 'visible', and thus reacting to being seen. */
    public int minSeeReactLight;

    public AbstractShadowMob(World worldIn)
    {
        super(worldIn);
        this.deathSmokeAmount = 20;
        this.minSeeReactLight = 5;
    }

    protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(BEEN_SEEN, false);
    }

    /** Returns the light level at the entity, not including Skylight. */
    public int getCurrentLight()
    { return this.world.getLightFor(EnumSkyBlock.BLOCK, this.getPosition()); }

    public boolean isSeen(EntityPlayer player)
    {
        Vec3d playerLook = player.getLook(1.0F).normalize();
        Vec3d vec3d1 = new Vec3d(this.posX - player.posX, this.getEntityBoundingBox().minY + (this.height / 2.0) - (player.posY + (double)player.getEyeHeight()), this.posZ - player.posZ);
        double d0 = vec3d1.length();
        vec3d1 = vec3d1.normalize();
        double d1 = playerLook.dotProduct(vec3d1);
        return d1 > 0.99D && player.canEntityBeSeen(this);
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        this.updateArmSwingProgress();

        if (!getSEEN() && (minSeeReactLight == 0 || getCurrentLight() > minSeeReactLight))
        {
            List<Entity> checkPlayerSight = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(20));

            if(!checkPlayerSight.isEmpty())
            {
                for (Entity player: checkPlayerSight)
                {
                    if (this.isSeen((EntityPlayer)player) && !((EntityPlayer) player).isCreative() && !((EntityPlayer) player).isSpectator())
                    {
                        this.preformSeeReaction((EntityPlayer)player);
                    }
                }
            }
        }
    }

    /** Occurs when the check within `onLivingUpdate` passes! */
    public void preformSeeReaction(EntityPlayer player) {}

    /** Mostly a copy of `onDeathUpdate` for the sake of skipping the death animation, and changing the particles. */
    @Override
    protected void onDeathUpdate()
    {
        if (!this.doDeathStuff()) return;

        if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")))
        {
            int xpDrop = this.getExperiencePoints(this.attackingPlayer);
            xpDrop = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, xpDrop);

            while (xpDrop > 0)
            {
                int j = EntityXPOrb.getXPSplit(xpDrop);
                xpDrop -= j;
                this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
            }
        }
        this.setDead();

        spawnSmoke(this.deathSmokeAmount);
    }

    /** An open method for running any logic on Death, without copying a bunch of `onDeathUpdate` again. */
    public boolean doDeathStuff() { return true; }

    /** Spawns Smoke around the entity. */
    public void spawnSmoke(int amount)
    {
        for (int k = 0; k < 20; ++k)
        {
            double d2 = this.rand.nextGaussian() * 0.02D;
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (double)(this.rand.nextFloat() * this.width) - (this.rand.nextFloat() * this.width), this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width) - (this.rand.nextFloat() * this.width), d2, d0, d1);
        }
    }

    public boolean getSEEN()
    { return (Boolean)this.dataManager.get(BEEN_SEEN); }

    public void setSEEN(boolean bool)
    { this.dataManager.set(BEEN_SEEN, bool); }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Seen", getSEEN());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setSEEN(compound.getBoolean("Seen"));
    }
}