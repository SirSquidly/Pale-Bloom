package com.sirsquidly.creaturesfromdarkness.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Used for following the player
 * */
public class EntityAIPlayerStalk extends EntityAIBase
{
    World world;
    private final EntityCreature entity;
    /** Speed to walk while stalking */
    double stalkSpeed;
    /** How close to move towards the player, before stopping */
    float stalkRange = 18.0F;

    public EntityAIPlayerStalk(EntityCreature entityIn, double stalkSpeedIn)
    {
        this.entity = entityIn;
        this.world = entityIn.world;
        this.stalkSpeed = stalkSpeedIn;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    { return this.entity.getAttackTarget() instanceof EntityPlayer && !this.entity.isInWater(); }

    public void resetTask()
    { this.entity.getNavigator().clearPath(); }

    public void updateTask()
    {
        EntityLivingBase target = this.entity.getAttackTarget();
        double d0 = this.entity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);

        this.entity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);

        if (d0 > (double)((stalkRange * stalkRange)* 0.25F))
        {
            this.entity.getNavigator().tryMoveToEntityLiving(target, this.stalkSpeed);
        }
        else
        { this.entity.getNavigator().clearPath(); }
    }
}