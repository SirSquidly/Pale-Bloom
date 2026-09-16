package com.sirsquidly.palebloom.common.advancements;

import net.minecraft.advancements.CriteriaTriggers;

public class JTPGAdvancements
{
    public static advancementManualTrigger GROW_REAPING_WILLOW = new advancementManualTrigger("grow_reaping_willow");

    public static void init()
    {
        CriteriaTriggers.register(GROW_REAPING_WILLOW);
    }
}