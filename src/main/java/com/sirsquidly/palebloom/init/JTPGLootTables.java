package com.sirsquidly.palebloom.init;

import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class JTPGLootTables
{
    public static final ResourceLocation BLOCKS_SUCKER_ROOT_NODULE = new ResourceLocation(paleBloom.MOD_ID, "blocks/sucker_root_nodule");

    public static final ResourceLocation ENTITIES_CREAKING = new ResourceLocation(paleBloom.MOD_ID, "entities/creaking");
    public static final ResourceLocation ENTITIES_MANNEQUIN = new ResourceLocation(paleBloom.MOD_ID, "entities/mannequin");
    public static final ResourceLocation ENTITIES_PALE_CREEPER = new ResourceLocation(paleBloom.MOD_ID, "entities/pale_creeper");
    public static final ResourceLocation ENTITIES_REAPING_WILLOW = new ResourceLocation(paleBloom.MOD_ID, "entities/reaping_willow");

    public static void registerLootTables()
    {
        LootTableList.register(BLOCKS_SUCKER_ROOT_NODULE);

        LootTableList.register(ENTITIES_CREAKING);
        LootTableList.register(ENTITIES_MANNEQUIN);
        LootTableList.register(ENTITIES_PALE_CREEPER);
        LootTableList.register(ENTITIES_REAPING_WILLOW);
    }
}