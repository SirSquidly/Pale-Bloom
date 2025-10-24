package com.sirsquidly.palebloom.config;

/**
 * 	This is simply a static form of the config, for reference throughout the mod.
 */
public class ConfigCache
{
    /** Blocks */
    public static final double paleBloomCreakingHeartChance = Config.block.treeBlocks.saplings.bloomingPaleOakSapling.creakingHeartChance;
    public static final double paleBloomNaturalCreakingHeartChance = Config.block.treeBlocks.saplings.bloomingPaleOakSapling.naturalCreakingHeartChance;
    public static final double paleOakCreakingHeartChance = Config.block.treeBlocks.saplings.paleOakSapling.creakingHeartChance;
    public static final double paleOakNaturalCreakingHeartChance = Config.block.treeBlocks.saplings.paleOakSapling.naturalCreakingHeartChance;

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