package com.sirsquidly.creaturesfromdarkness.init;

import com.sirsquidly.creaturesfromdarkness.creaturesfromdarkness;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class CFDLootTables
{
    public static final ResourceLocation SHADOW_DROPS = new ResourceLocation(creaturesfromdarkness.MOD_ID, "entities/shadow.json");
    public static final ResourceLocation NIGHTMARE_DROPS = new ResourceLocation(creaturesfromdarkness.MOD_ID, "entities/nightmare");

    public static void registerLootTables()
    {
        LootTableList.register(SHADOW_DROPS);
        LootTableList.register(NIGHTMARE_DROPS);
    }
}