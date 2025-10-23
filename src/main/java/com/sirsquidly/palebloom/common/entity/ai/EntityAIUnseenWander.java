package com.sirsquidly.palebloom.common.entity.ai;

import com.sirsquidly.palebloom.common.entity.AbstractCreaking;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;

public class EntityAIUnseenWander extends EntityAIWander implements AbstractHeartRangeCheck
{
    public EntityAIUnseenWander(EntityCreature creatureIn, double speedIn)
    { super(creatureIn, speedIn); }

    public boolean shouldExecute()
    { return !((AbstractCreaking)this.entity).getSEEN() && super.shouldExecute(); }

    public boolean shouldContinueExecuting()
    { return !((AbstractCreaking)this.entity).getSEEN() && super.shouldContinueExecuting(); }
}