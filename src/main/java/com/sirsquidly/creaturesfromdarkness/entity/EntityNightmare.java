package com.sirsquidly.creaturesfromdarkness.entity;

import com.google.common.base.Optional;
import com.sirsquidly.creaturesfromdarkness.init.CFDLootTables;
import com.sirsquidly.creaturesfromdarkness.CommonProxy;
import com.sirsquidly.creaturesfromdarkness.Config;
import com.sirsquidly.creaturesfromdarkness.init.CFDSounds;
import com.sirsquidly.creaturesfromdarkness.capabilities.CapabilityNightmare;
import com.sirsquidly.creaturesfromdarkness.entity.ai.EntityAINightmareAttack;
import com.sirsquidly.creaturesfromdarkness.entity.ai.EntityAIPlayerStalk;
import com.sirsquidly.creaturesfromdarkness.util.CFDCapabilityUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class EntityNightmare extends AbstractShadowMob
{
    protected static final DataParameter<Optional<UUID>> PREY_UNIQUE_ID = EntityDataManager.<Optional<UUID>>createKey(EntityNightmare.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    private final EntityAIPlayerStalk aiPlayerStalk = new EntityAIPlayerStalk(this, 1.0D);
    private final EntityAINightmareAttack aiAttackMelee = new EntityAINightmareAttack(this, 1.0D);
    /** If this Nightmare is fully hostile and tries to attack. */
    private boolean isAttacking;
    /** How long a Nightmare sticks around, before despawning again. */
    private boolean limitedLifespan;
    private int limitedLifeTicks;

    private boolean configScareAnimals = Config.entity.nightmare.nightmareScaresAnimals;

    public EntityNightmare(World worldIn)
    {
        super(worldIn);
        this.setSize(0.7F, 1.95F);
        this.stepHeight = 1.5F;
        this.deathSmokeAmount = 60;
        this.minSeeReactLight = 0;
        this.experienceValue = 50;
        this.setBaseAi();
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(PREY_UNIQUE_ID, Optional.absent());
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
    }

    protected void applyEntityAI()
    { this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, false)); }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
    }

    /** Many Ai components are made to be toggled based on other factors, thus need to be setup differently! */
    public void setBaseAi()
    {
        if (this.world != null && !this.world.isRemote)
        {
            if (getIsAttacking())
            {
                this.tasks.addTask(2, this.aiAttackMelee);
            }
            else
            {
                this.tasks.addTask(4, this.aiPlayerStalk);
            }
        }
    }

    /** Reacts based on what the look reaction should be*/
    public void preformSeeReaction(EntityPlayer player)
    {
        if (player.hasCapability(CapabilityNightmare.RIPTIDE_CAP, null))
        {
            CapabilityNightmare.ICapabilityRiptide capNightmare = player.getCapability(CapabilityNightmare.RIPTIDE_CAP, null);

            if (!capNightmare.getNightmareOnPlayer())
            {
                CFDCapabilityUtils.addNightmarePackage(player);

                this.playSound(CFDSounds.ENTITY_NIGHTMARE_WARN, 1.0F, 1.0F);
            }
        }

        //this.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 2, 0, true, false));
        if (this.world != null && !this.world.isRemote)
        {
            this.setLimitedLife(200);
            this.tasks.addTask(2, this.aiAttackMelee);
            this.tasks.removeTask(this.aiPlayerStalk);
            setSEEN(true);
        }
    }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        float damage = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        boolean flag = entityIn.attackEntityFrom(CommonProxy.causeNightmareDamage(this), damage);

        if (flag)
        {
            if (entityIn instanceof EntityLivingBase)
            {
                ((EntityLivingBase)entityIn).knockBack(this, (float)damage * 0.05F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer) entityIn;
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

                if (itemstack1.getItem().isShield(itemstack1, entityplayer))
                {
                    entityplayer.getCooldownTracker().setCooldown(itemstack1.getItem(), 100);
                    this.world.setEntityState(entityplayer, (byte) 30);
                }
            }
        }

        return flag;
    }

    protected SoundEvent getAmbientSound() { return CFDSounds.ENTITY_NIGHTMARE_AMBIENT; }

    /** Sounds less often */
    public int getTalkInterval() { return 300; }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return CFDSounds.ENTITY_NIGHTMARE_HURT; }

    protected SoundEvent getDeathSound() { return CFDSounds.ENTITY_NIGHTMARE_DEATH; }

    protected void playStepSound(BlockPos pos, Block blockIn) { this.playSound(CFDSounds.ENTITY_NIGHTMARE_STEP, 0.15F, 0.5F); }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return CFDLootTables.NIGHTMARE_DROPS;
    }

    public void onUpdate()
    {
        super.onUpdate();
        this.setBaseAi();
        if (configScareAnimals) inflictPanic();

        EntityLivingBase attackTarget = this.getAttackTarget();

        if (this.limitedLifespan && --this.limitedLifeTicks <= 0)
        {
            /* This is used to set the Nightmare's reoccurring health when attacking the same player. */
            if (getPrey() != null && getPrey() instanceof EntityPlayer)
            {
                EntityPlayer player = ((EntityPlayer)getPrey());
                if (player.hasCapability(CapabilityNightmare.RIPTIDE_CAP, null))
                {
                    CapabilityNightmare.ICapabilityRiptide capNightmare = player.getCapability(CapabilityNightmare.RIPTIDE_CAP, null);

                    if (capNightmare.getNightmareOnPlayer())
                    { capNightmare.setNightmareHealth((int) this.getHealth()); }
                }
            }

            /* If this is supposed to be hunting Prey, but isn't neqr it, disappear far faster*/
            if (this.getPrey() != null && this.getPrey().getDistance(this) > 50) --this.limitedLifeTicks;


            this.setDead();
        }

        /* Smokes when in light */
        if (getCurrentLight() > 5 && world.getWorldTime() % 5 == 0)
        { spawnSmoke(getCurrentLight()/10); }
    }

    /** Causes nearby animals to PANIC */
    public void inflictPanic()
    {
        List<EntityCreature> checkNearbyAnimals = this.world.getEntitiesWithinAABB(EntityCreature.class, this.getEntityBoundingBox().grow(10, 10, 10));

        for (EntityCreature animal: checkNearbyAnimals)
        {
            for (EntityAITasks.EntityAITaskEntry taskEntry : animal.tasks.taskEntries)
            {
                if (taskEntry.action instanceof EntityAIPanic)
                {
                    animal.setRevengeTarget(this);
                    break;
                }
            }

            /* Tamable pets get locked down in fear, and some make angry sounds in fear. */
            if (animal instanceof EntityTameable)
            {
                animal.setRevengeTarget(null);
                animal.setAttackTarget(null);
                animal.getNavigator().clearPath();

                if (animal.ticksExisted % 10 + animal.getRNG().nextInt(20) == 0)
                {
                    SoundEvent sound = null;
                    if (animal instanceof EntityWolf)
                    { sound = SoundEvents.ENTITY_WOLF_GROWL; }
                    else if (animal instanceof EntityOcelot)
                    { sound = SoundEvents.ENTITY_CAT_HISS; }

                    if (sound != null) animal.playSound(sound, 0.4F, rand.nextFloat() * 0.4F + 0.8F + (animal.isChild() ? 0.4F : 0F));
                }
            }
        }
    }

    /** NEVER takes over 7 Damage from a Player */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        Entity entitySource = source.getTrueSource();

        if (entitySource != null && entitySource instanceof EntityPlayer && !((EntityPlayer) entitySource).capabilities.isCreativeMode)
        {
            super.attackEntityFrom(source, Math.min(amount, 7.0F));
            return true;
        }

        if (source instanceof EntityDamageSourceIndirect)
        {
            for (int i = 0; i < 64; ++i)
            {
                if (this.teleportRandomly())
                {
                    return true;
                }
            }

            return false;
        }


        super.attackEntityFrom(source, amount);
        return true;
    }

    protected boolean teleportRandomly()
    {
        double x = this.posX + (this.rand.nextDouble() - 0.5) * 8.0;
        double y = this.posY + (this.rand.nextInt(8) - 4);
        double z = this.posZ + (this.rand.nextDouble() - 0.5) * 8.0;
        return false;
        //return this.teleportTo(x, y, z);
    }

    /** Dis-allow ANY riding. */
    @Override
    public boolean startRiding(Entity entityIn, boolean force) { return false; }

    /** Zero fall damage. */
    public void fall(float distance, float damageMultiplier) {}

    /** Remove negative Potion Effects. */
    @Override
    public boolean isPotionApplicable(PotionEffect effect) { return !effect.getPotion().isBadEffect(); }

    protected float getWaterSlowDown() { return 0.99F; }

    /** Removes any tracked Nightmares for Nearby Players upon Death. Yes, Ranged Players will get screwed, lol */
    public boolean doDeathStuff()
    {
        List<EntityPlayer> checkForPlayers = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(10, 10, 10));

        if (!checkForPlayers.isEmpty())
        {
            for (EntityPlayer player: checkForPlayers)
            { CFDCapabilityUtils.removeNightmarePackage(player); }
        }

        return true;
    }

    @Override
    public boolean getCanSpawnHere()
    {
        int configYLevel = Config.entity.nightmare.nightmareSpawning.maxYLevel;

        if (this.world.getDifficulty() != EnumDifficulty.PEACEFUL && getCurrentLight() <= Config.entity.nightmare.nightmareSpawning.maxLightLevel && (configYLevel == 0 || this.posY < configYLevel))
        { return super.getCanSpawnHere(); }

        return false;
    }

    public float getBlockPathWeight(BlockPos pos)
    {
        return -this.world.getLightBrightness(pos);
    }

    public boolean isPushedByWater()
    {
        return false;
    }

    public boolean isPreventingPlayerRest(EntityPlayer playerIn)
    { return true; }

    @Nullable
    public EntityLivingBase getPrey()
    {
        try
        {
            UUID uuid = this.getPreyId();
            return uuid == null ? null : this.world.getPlayerEntityByUUID(uuid);
        }
        catch (IllegalArgumentException var2)
        { return null; }
    }

    @Nullable
    public UUID getPreyId()
    { return (UUID)((Optional)this.dataManager.get(PREY_UNIQUE_ID)).orNull(); }

    public void setPreyId(@Nullable UUID p_184754_1_) { this.dataManager.set(PREY_UNIQUE_ID, Optional.fromNullable(p_184754_1_)); }

    public boolean getIsAttacking()
    {
        return this.isAttacking;
    }

    public void setIsAttacking(boolean isAttackingIn)
    {
        this.isAttacking = isAttackingIn;
    }

    public void setLimitedLife(int limitedLifeTicksIn)
    {
        this.limitedLifespan = true;
        this.limitedLifeTicks = limitedLifeTicksIn;
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        compound.setBoolean("IsAttacking", this.getIsAttacking());

        if (this.getPreyId() == null)
        {  compound.setString("PreyUUID", ""); }
        else
        { compound.setString("PreyUUID", this.getPreyId().toString()); }

        if (this.limitedLifespan)
        { compound.setInteger("LifeTicks", this.limitedLifeTicks); }
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        String s;

        if (compound.hasKey("IsAttacking", 1)) this.setIsAttacking(compound.getBoolean("IsAttacking"));

        if (compound.hasKey("PreyUUID", 8))
        { s = compound.getString("PreyUUID"); }
        else
        {
            String s1 = compound.getString("Owner");
            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1);
        }
        if (!s.isEmpty())
        {
            try
            { this.setPreyId(UUID.fromString(s)); }
            catch (Throwable ignored) {}
        }


        if (compound.hasKey("LifeTicks"))
        { this.setLimitedLife(compound.getInteger("LifeTicks")); }
    }
}