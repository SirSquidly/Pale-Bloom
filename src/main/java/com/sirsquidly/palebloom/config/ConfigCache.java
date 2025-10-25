package com.sirsquidly.palebloom.config;

/**
 * 	This is simply a static form of the config, for reference throughout the mod.
 */
public class ConfigCache
{
    /** Blocks */
    public static final double blmPalOakSpl_creakingHeartChance = Config.block.treeBlocks.bloomingPaleOak.bloomingPaleOakSapling.creakingHeartChance;
    public static final double blmPalOakSpl_naturalCreakingHeartChance = Config.block.treeBlocks.bloomingPaleOak.bloomingPaleOakSapling.naturalCreakingHeartChance;
    public static final double palOakSpl_creakingHeartChance = Config.block.treeBlocks.paleOak.paleOakSapling.creakingHeartChance;
    public static final double palOakSpl_naturalCreakingHeartChance = Config.block.treeBlocks.paleOak.paleOakSapling.naturalCreakingHeartChance;

    public static final boolean crkHrt_enabled = Config.block.awakenedFloraBlocks.creakingHeart.enableCreakingHeart;
    public static final boolean crkHrt_genResinNatural = Config.block.awakenedFloraBlocks.creakingHeart.naturalResinClumps;
    public static final boolean crkHrt_genResinUnnatural = Config.block.awakenedFloraBlocks.creakingHeart.unnaturalResinClumps;
    public static final boolean crkHrt_dropAmberValveNatural = Config.block.awakenedFloraBlocks.creakingHeart.naturalAmberValveDrop;
    public static final boolean crkHrt_alertReapingWillows = Config.block.awakenedFloraBlocks.creakingHeart.alertReapingWillows;
    public static final boolean crkLtn_creakingIgnored = Config.block.awakenedFloraBlocks.creakingLantern.creakingsIgnoreWearer;
    public static final int palOakHlw_resinSapAmount = Config.block.awakenedFloraBlocks.paleOakHollow.resinToSapQuantity;
    public static final boolean rsnBlb_enabled = Config.block.awakenedFloraBlocks.resinBulb.enableResinBulb;
    public static final int rsnBlb_creakingHeartResinReap = Config.block.awakenedFloraBlocks.resinBulb.activeCreakingHeartResinAmount;

    public static final boolean eyeblm_fullbright = Config.block.floraBlocks.eyeblossomFullbright;
    public static final boolean eyeblm_enabled = Config.block.floraBlocks.enableEyeblossom;
    public static final boolean brmbl_enabled = Config.block.floraBlocks.enableBramble;
    public static final boolean dblPalPnt_enabled = Config.block.floraBlocks.enableDoublePalePlants;

    public static final boolean palOakLvs_enabled = Config.block.treeBlocks.paleOak.enablePaleOakLeaves;
    public static final boolean palOakWod_enabled = Config.block.treeBlocks.paleOak.enablePaleOakWoods;
    public static final boolean blmPalOakLvs_enabled = Config.block.treeBlocks.bloomingPaleOak.enableBloomingPaleOakLeaves;
    public static final boolean blmPalOakLvs_saplingDrop = Config.block.treeBlocks.bloomingPaleOak.bloomingLeavesDropSapling;
    public static final boolean sucRot_enabled = Config.block.treeBlocks.bloomingPaleOak.enableSuckerRoots;

    /** Entities */
    public static final boolean crk_enabled = Config.entity.creaking.enableCreaking;

    /** Items */
    public static final boolean ctvAxe_awakeBulbHealing = Config.item.gardengraftedTools.cultivarAxe.awakenedBulbHealing;
    public static final boolean ctvAxe_awakeThornBurst = Config.item.gardengraftedTools.cultivarAxe.awakenedThornBurst;
    public static final float ctvAxe_awakeMiningSpeed = Config.item.gardengraftedTools.cultivarAxe.awakenedMiningSpeed;
    public static final int ctvAxe_awakeHarvestLevel = Config.item.gardengraftedTools.cultivarAxe.awakenedHarvestLevel;
    public static final int ctvAxe_awakeThornBurstCooldown = Config.item.gardengraftedTools.cultivarAxe.awakenedThornBurstCooldown;

    public static final boolean ctvHoe_awakeBulbHealing = Config.item.gardengraftedTools.cultivarHoe.awakenedBulbHealing;
    public static final boolean ctvHoe_awakeGardenCall = Config.item.gardengraftedTools.cultivarHoe.awakenedGardenCall;
    public static final float ctvHoe_awakeMiningSpeed = Config.item.gardengraftedTools.cultivarHoe.awakenedMiningSpeed;
    public static final int ctvHoe_awakeHarvestLevel = Config.item.gardengraftedTools.cultivarHoe.awakenedHarvestLevel;
    public static final int ctvHoe_awakeGardenCallCooldown = Config.item.gardengraftedTools.cultivarHoe.awakenedGardenCallCooldown;

    public static final boolean ctvPik_awakeBulbHealing = Config.item.gardengraftedTools.cultivarPickaxe.awakenedBulbHealing;
    public static final boolean ctvPik_awakeResinBlood = Config.item.gardengraftedTools.cultivarPickaxe.awakenedResinBlood;
    public static final float ctvPik_awakeMiningSpeed = Config.item.gardengraftedTools.cultivarPickaxe.awakenedMiningSpeed;
    public static final int ctvPik_awakeHarvestLevel = Config.item.gardengraftedTools.cultivarPickaxe.awakenedHarvestLevel;
    public static final int ctvPik_awakeResinBloodCooldown = Config.item.gardengraftedTools.cultivarPickaxe.awakenedResinBloodCooldown;

    public static final boolean ctvSvl_awakeBulbHealing = Config.item.gardengraftedTools.cultivarShovel.awakenedBulbHealing;
    public static final boolean ctvSvl_awakeThornStroke = Config.item.gardengraftedTools.cultivarShovel.awakenedThornStroke;
    public static final float ctvSvl_awakeMiningSpeed = Config.item.gardengraftedTools.cultivarShovel.awakenedMiningSpeed;
    public static final int ctvSvl_awakeHarvestLevel = Config.item.gardengraftedTools.cultivarShovel.awakenedHarvestLevel;
    public static final int ctvSvl_awakeThornStrokeCooldown = Config.item.gardengraftedTools.cultivarShovel.awakenedThornStrokeCooldown;

    public static final boolean clvSwd_awakeBulbHealing = Config.item.gardengraftedTools.cultivarSword.awakenedBulbHealing;
    public static final boolean clvSwd_awakeTransfer = Config.item.gardengraftedTools.cultivarSword.awakenedTransfer;
    public static final double clvSwd_awakeDamage = Config.item.gardengraftedTools.cultivarSword.awakenedDamage;
    public static final int clvSwd_awakeTransferCooldown = Config.item.gardengraftedTools.cultivarSword.awakenedTransferCooldown;
}