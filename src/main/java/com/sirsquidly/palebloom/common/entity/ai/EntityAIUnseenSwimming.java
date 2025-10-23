package com.sirsquidly.palebloom.common.entity.ai;

import com.sirsquidly.palebloom.common.entity.AbstractCreaking;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;

public class EntityAIUnseenSwimming extends EntityAISwimming
{
    private final EntityLiving entity;

    public EntityAIUnseenSwimming(EntityLiving entityIn)
    {
        super(entityIn);
        entity = entityIn;
    }

    public boolean shouldExecute()
    { return !((AbstractCreaking)this.entity).getSEEN() && super.shouldExecute(); }
}