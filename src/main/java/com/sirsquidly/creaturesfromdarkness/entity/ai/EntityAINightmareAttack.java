package com.sirsquidly.creaturesfromdarkness.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.MobEffects;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class EntityAINightmareAttack extends EntityAIBase
{
    World world;
    private final EntityCreature entity;
    double movementSpeed;
    private int chargeCooldown = 0;
    private int cancelTry = 0;

    public EntityAINightmareAttack(EntityCreature entityIn, double speedIn)
    {
        this.entity = entityIn;
        this.world = entityIn.world;
        this.movementSpeed = speedIn;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    { return this.entity.getAttackTarget() != null && this.entity.getAttackTarget().isEntityAlive() && (this.world.getLightFor(EnumSkyBlock.BLOCK, this.entity.getAttackTarget().getPosition()) < 5 || this.entity.getAttackTarget().isPotionActive(MobEffects.BLINDNESS)); }

    public void resetTask()
    {
        this.chargeCooldown = 0;
    }

    public void updateTask()
    {
        EntityLivingBase target = this.entity.getAttackTarget();
        double distance = this.entity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);

        this.entity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);

        if (this.entity.isInWater())
        { this.doWaterCharge(target, distance); }
        else if (--this.chargeCooldown <= 0)
        {
            this.doCharge(target, distance);
            checkAndPerformMelee(target, distance);
        }
        else
        {
            this.entity.getMoveHelper().strafe(0.0F, 0.6F);
        }
    }

    /** Rushes straight to the entity */
    private void doCharge(EntityLivingBase target, double distance)
    {
        this.entity.getNavigator().clearPath();
        this.entity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        this.entity.getNavigator().tryMoveToEntityLiving(target, this.movementSpeed);
        ++this.cancelTry;
        if (this.cancelTry > 50)
        {
            this.chargeCooldown = this.entity.getRNG().nextInt(15) + 20;
            this.cancelTry = 0;
        }
    }

    /** A different form of charge when in water */
    private void doWaterCharge(EntityLivingBase target, double distance)
    {
        this.entity.getNavigator().clearPath();
        this.entity.setAIMoveSpeed((float) (this.movementSpeed * 0.5));
        this.entity.getMoveHelper().setMoveTo(target.posX, target.posY, target.posZ, this.movementSpeed);

        this.chargeCooldown = Math.max(this.chargeCooldown, 20);
    }

    protected void checkAndPerformMelee(EntityLivingBase target, double distance)
    {
        double attackReach = this.getAttackReachSqr(target);

        if (distance <= attackReach)
        {
            this.entity.getNavigator().clearPath();

            if (distance <= attackReach)
            { this.entity.attackEntityAsMob(target); }

            this.chargeCooldown = this.entity.getRNG().nextInt(15) + 20;
        }
    }

    protected double getAttackReachSqr(EntityLivingBase attackTarget)
    { return (double)(this.entity.width * 2.0F * this.entity.width * 2.0F + attackTarget.width); }
}