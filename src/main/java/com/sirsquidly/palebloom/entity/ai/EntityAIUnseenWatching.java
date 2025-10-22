package com.sirsquidly.palebloom.entity.ai;

import com.sirsquidly.palebloom.entity.AbstractCreaking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;

public class EntityAIUnseenWatching extends EntityAIWatchClosest
{
    public EntityAIUnseenWatching(EntityLiving entityIn, Class<? extends Entity> watchTargetClass, float maxDistance)
    { super(entityIn, watchTargetClass, maxDistance); }

    public boolean shouldExecute()
    { return !((AbstractCreaking)this.entity).getSEEN() && super.shouldExecute(); }

    public boolean shouldContinueExecuting()
    { return !((AbstractCreaking)this.entity).getSEEN() && super.shouldContinueExecuting(); }
}