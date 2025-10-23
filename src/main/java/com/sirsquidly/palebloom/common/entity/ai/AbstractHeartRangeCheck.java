package com.sirsquidly.palebloom.common.entity.ai;

import com.sirsquidly.palebloom.common.entity.EntityCreaking;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public interface AbstractHeartRangeCheck
{
    default boolean entityInHeartRange(EntityCreaking heart, Entity target)
    { return target != null && (target.getDistanceSqToCenter(heart.getHeartPos()) <= (32) * (32) || heart.getHeartPos() == BlockPos.ORIGIN); }
}