package com.sirsquidly.palebloom.entity.ai;

import com.sirsquidly.palebloom.entity.AbstractCreaking;
import com.sirsquidly.palebloom.entity.EntityCreaking;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackMelee;

public class EntityAIUnseenMelee extends EntityAIAttackMelee implements AbstractHeartRangeCheck
{
    boolean useHeartRangeCheck;

    public EntityAIUnseenMelee(EntityCreature creature, double speedIn, boolean useLongMemory, boolean useHeartRangeCheckIn)
    {
        super(creature, speedIn, useLongMemory);
        useHeartRangeCheck = useHeartRangeCheckIn;
    }

    public boolean shouldExecute()
    {
        if (this.attacker instanceof EntityCreaking && useHeartRangeCheck)
        {
            return !((AbstractCreaking)this.attacker).getSEEN() && entityInHeartRange((EntityCreaking)this.attacker, this.attacker.getAttackTarget()) && entityInHeartRange((EntityCreaking)this.attacker, this.attacker) && super.shouldExecute();
        }

        return !((AbstractCreaking)this.attacker).getSEEN() && super.shouldExecute();
    }

    public boolean shouldContinueExecuting()
    { return !((AbstractCreaking)this.attacker).getSEEN() && super.shouldContinueExecuting(); }
}