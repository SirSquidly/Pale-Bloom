package com.sirsquidly.palebloom.entity;

import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.init.JTPGSounds;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityHydraweedJaw extends EntityCreature
{
    public EntityHydraweedJaw(World worldIn)
    {
        super(worldIn);
        setSize(0.9F, 0.5F);
        this.stepHeight = 2F;
    }

    @Override
    protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAITrapper(this));
        this.tasks.addTask(2, new EntityAIJawPanic(this, 1.0D));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    protected SoundEvent getDeathSound() { return JTPGSounds.ENTITY_HYDRAWEED_JAW_DEATH; }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return JTPGSounds.ENTITY_HYDRAWEED_JAW_HURT; }

    public void onLivingUpdate()
    {
        this.updateArmSwingProgress();

        if (!this.getPassengers().isEmpty())
        {
            if (this.getPassengers().get(0).isSneaking())
            { this.getPassengers().get(0).setSneaking(false); }
        }

        super.onLivingUpdate();
    }

    /** No death animation, just spawn particles. */
    protected void onDeathUpdate()
    {
        this.setDead();

        //this.world.playSound(null, this.getPosition(), SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, SoundCategory.HOSTILE, 1.0F, 1.0F);

        if (this.world instanceof WorldServer)
        {
            ((WorldServer)this.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY + (double)this.height / 1.5D, this.posZ, 30, this.width / 4.0F, this.height / 4.0F, (double)(this.width / 4.0F), 0.05D, Block.getStateId(JTPGBlocks.PALE_OAK_LOG.getDefaultState()));
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity)
    {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();

        if (entity.attackEntityFrom(DamageSource.causeMobDamage(this), f))
        {
            if (this.getPassengers().isEmpty() && !entity.isRiding())
            { entity.startRiding(this); }

            return true;
        }

        return super.attackEntityAsMob(entity);
    }

    public double getMountedYOffset() { return 0.8D; }

    public boolean shouldRiderSit() { return false; }
    public boolean canRiderInteract() { return true; }


    public class EntityAITrapper extends EntityAIBase
    {
        World world;
        protected EntityCreature attacker;
        protected int attackTick;

        public EntityAITrapper(EntityCreature creature)
        {
            this.attacker = creature;
            this.world = creature.world;
        }

        public boolean shouldExecute()
        {
            EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();

            if (attacker.isSprinting() || entitylivingbase == null || !entitylivingbase.isEntityAlive())
            { return false; }
            else
            {
                return this.getAttackReachSqr(entitylivingbase) >= this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
            }
        }

        public boolean shouldContinueExecuting()
        {
            EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();

            if (entitylivingbase instanceof EntityPlayer && (((EntityPlayer)entitylivingbase).isSpectator() || ((EntityPlayer)entitylivingbase).isCreative()))
            { return false; }

            return shouldExecute();
        }

        public void startExecuting()
        {
            //this.delayCounter = 0;
        }

        public void resetTask()
        {
            EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();

            if (entitylivingbase instanceof EntityPlayer && (((EntityPlayer)entitylivingbase).isSpectator() || ((EntityPlayer)entitylivingbase).isCreative()))
            { this.attacker.setAttackTarget(null); }

            this.attacker.getNavigator().clearPath();
        }

        public void updateTask()
        {
            EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
            double d0 = this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);

            if (this.getAttackReachSqr(entitylivingbase) >= d0)
            {
                this.attackTick = Math.max(this.attackTick - 1, 0);

                if (this.attackTick <= 0)
                {
                    this.attackTick = 20;
                    this.attacker.swingArm(EnumHand.MAIN_HAND);
                    this.attacker.attackEntityAsMob(entitylivingbase);
                }
            }
        }

        protected double getAttackReachSqr(EntityLivingBase attackTarget)
        { return this.attacker.width * 2.0F * this.attacker.width * 2.0F + attackTarget.width; }
    }

    /** Panic/Fleeing AI ONLY when not holding an entity, makes the Jaw invisible and sprinting, and ends by centering it. */
    public class EntityAIJawPanic extends EntityAIPanic
    {
        public EntityAIJawPanic(EntityCreature creature, double speedIn)
        { super(creature, speedIn);  }

        public boolean shouldExecute()
        { return super.shouldExecute() && creature.getPassengers().isEmpty(); }

        public void startExecuting()
        {
            super.startExecuting();
            creature.setInvisible(true);
            creature.setSprinting(true);
        }

        public void resetTask()
        {
            super.resetTask();

            creature.setInvisible(false);
            creature.setSprinting(false);
            BlockPos pos = new BlockPos(creature.posX, 0, creature.posZ);
            creature.setPosition(pos.getX() + 0.5D, creature.posY, pos.getZ() + 0.5D);
            creature.rotationPitch = 0;
            creature.prevRotationPitch = 0;
            creature.motionX = 0;
            creature.motionZ = 0;
        }
    }
}