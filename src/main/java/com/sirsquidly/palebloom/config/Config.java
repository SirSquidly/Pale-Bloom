package com.sirsquidly.palebloom.config;

import com.sirsquidly.palebloom.paleBloom;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@net.minecraftforge.common.config.Config(modid = paleBloom.MOD_ID, name = paleBloom.CONFIG_NAME)
@net.minecraftforge.common.config.Config.LangKey("config.palebloom.title")
@Mod.EventBusSubscriber(modid = paleBloom.MOD_ID)
public class Config
{
    @net.minecraftforge.common.config.Config.LangKey("config.palebloom.configVersion")
    @net.minecraftforge.common.config.Config.Comment({
            "Config Versions help inform modpack makers/config users if changes have been made to the config between updates. These differ from main versioning, since the config file is static.",
            "Basically, you compare the current default of this value, to the default of when you generated it.",
            "",
            "The versioning follows:",
            "0.0.x - Default values have been slightly adjusted.",
            "0.x.0 - Config options have been added.",
            "x.0.0 - Previous Config Options have been completely overhauled and/or removed. Creating a fresh file is recommended."
    })
    public static String configVersion = "0.0.0";

    @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden")
    @net.minecraftforge.common.config.Config.Comment("Pale Garden Config")
    public static configPaleGarden paleGarden = new configPaleGarden();

    public static class configPaleGarden
    {
        @net.minecraftforge.common.config.Config.RequiresMcRestart
        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden.enablePaleGarden")
        @net.minecraftforge.common.config.Config.Comment("Enables the Pale Garden biome.")
        public boolean enablePaleGarden = true;

        @net.minecraftforge.common.config.Config.RequiresMcRestart
        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden.subbiomeOfDarkOak")
        @net.minecraftforge.common.config.Config.Comment("Adds the Pale Garden as a sub-biome of the vanilla Dark Forest/Roofed Forest.")
        public boolean subbiomeOfDarkOak = true;

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden.treeGen")
        @net.minecraftforge.common.config.Config.Comment("All config related to Trees in the Pale Garden")
        public configTreeGen treeGen = new configTreeGen();

        public static class configTreeGen
        {
            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden.treeGen.darkOakChance")
            @net.minecraftforge.common.config.Config.Comment("The chance for a Dark Oak tree to generate within a Pale Garden. 1 / this number. 0 disables this.")
            public int darkOakChance = 20;

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden.treeGen.paleOakTree")
            @net.minecraftforge.common.config.Config.Comment("All config related to Trees")
            public configTreeGen.configPaleOakTree paleOakTree = new configTreeGen.configPaleOakTree();

            public static class configPaleOakTree
            {
                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden.treeGen.paleOakTree.dyingTreeChance")
                @net.minecraftforge.common.config.Config.Comment("The percent chance for a generated Pale Oak to be dying. This causes Vines to generate on all sides of the main trunk.")
                @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 100)
                public double dyingTreeChance = 2.0D;

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden.treeGen.paleOakTree.creakingHeartChance")
                @net.minecraftforge.common.config.Config.Comment("The percent chance for a generated Pale Oak to have a Creaking Heart. Note Creaking Hearts need to be surrounded on all sides, so this may fail even at 100%.")
                @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 100)
                public double creakingHeartChance = 20.0D;

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden.treeGen.paleOakTree.naturalCreakingHeartChance")
                @net.minecraftforge.common.config.Config.Comment("The percent chance for a generated Creaking Heart to be Natural.")
                @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 100)
                public double naturalCreakingHeartChance = 100.0D;
            }
        }

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden.understoryGen")
        @net.minecraftforge.common.config.Config.Comment("All config for plants below the tree canopy")
        public configUnderstoryGen understoryGen = new configUnderstoryGen();

        public static class configUnderstoryGen
        {
            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden.understoryGen.eyeblossomChance")
            @net.minecraftforge.common.config.Config.Comment("The chance for an Eyeblossom Patch to generate per chunk. 1 / this number. 0 disables this.")
            public int eyeblossomChance = 8;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden.understoryGen.brambleChance")
            @net.minecraftforge.common.config.Config.Comment("The chance for a Bramble Patch to generate per chunk. 1 / this number. 0 disables this.")
            public int brambleChance = 8;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden.understoryGen.doublePalePlantChance")
            @net.minecraftforge.common.config.Config.Comment("The chance for a single Double Pale Plant generate per chunk. 1 / this number. 0 disables this.")
            public int doublePalePlantChance = 8;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden.understoryGen.shrubChance")
            @net.minecraftforge.common.config.Config.Comment("The chance for a Pale Shrub to generate per chunk. 1 / this number. 0 disables this.")
            public int shrubChance = 4;
        }

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden.fog")
        @net.minecraftforge.common.config.Config.Comment("Config related to Entities")
        public configFog fog = new configFog();

        public static class configFog
        {
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden.fog.enableFog")
            @net.minecraftforge.common.config.Config.Comment("Enables the custom Heavy Fog.")
            public boolean enableFog = true;

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden.fog.creativeIgnoresFog")
            @net.minecraftforge.common.config.Config.Comment("Makes Fog not render for players in Creative Mode.")
            public boolean creativeIgnoresFog = true;

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden.fog.fogFarDistance")
            @net.minecraftforge.common.config.Config.Comment("The furthest, or 'end' point, of the Fog. Nothing past this is visible.")
            @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 9999)
            public double fogFarDistance = 40.0D;
        }
    }

    @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block")
    @net.minecraftforge.common.config.Config.Comment("Config related to Entities")
    public static configBlock block = new configBlock();

    public static class configBlock
    {
        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.treeBlocks")
        @net.minecraftforge.common.config.Config.Comment("All config for blocks related to Trees")
        public configBlock.configTreeBlocks treeBlocks = new configBlock.configTreeBlocks();

        public static class configTreeBlocks
        {
            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.treeBlocks.enablePaleSaplings")
            @net.minecraftforge.common.config.Config.Comment("Enables all Pale Saplings (Pale, Blooming, Peeping).")
            public boolean enablePaleSaplings = true;

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.treeBlocks.paleOak")
            @net.minecraftforge.common.config.Config.Comment("Config for all Pale Oak Blocks")
            public configTreeBlocks.configPaleOak paleOak = new configTreeBlocks.configPaleOak();

            public static class configPaleOak
            {
                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.treeBlocks.paleOak.enablePaleOakLeaves")
                @net.minecraftforge.common.config.Config.Comment("Enables Pale Oak Leaves. If disabled, Pale Oak Trees will generate with Dark Oak Leaves.")
                public boolean enablePaleOakLeaves = true;

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.treeBlocks.paleOak.enablePaleOakWoods")
                @net.minecraftforge.common.config.Config.Comment("Enables all Pale Oak wood blocks (Log, Planks, Stairs, ect). If disabled, Pale Oak Trees will generate with Dark Oak Logs.")
                public boolean enablePaleOakWoods = true;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.treeBlocks.paleOak.paleOakSapling")
                @net.minecraftforge.common.config.Config.Comment("Config related to Entities")
                public configPaleOak.configPaleOakSapling paleOakSapling = new configPaleOak.configPaleOakSapling();

                public static class configPaleOakSapling
                {
                    @net.minecraftforge.common.config.Config.RequiresMcRestart
                    @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.treeBlocks.paleOak.paleOakSapling.creakingHeartChance")
                    @net.minecraftforge.common.config.Config.Comment("The chance for a player-grown Pale Oak to have a Creaking Heart. Note Creaking Hearts need to be surrounded on all sides, so this may fail even at 100%.")
                    @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 100)
                    public double creakingHeartChance = 0.0D;

                    @net.minecraftforge.common.config.Config.RequiresMcRestart
                    @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.treeBlocks.paleOak.creakingHeart.naturalCreakingHeartChance")
                    @net.minecraftforge.common.config.Config.Comment("The chance for a generated Creaking Heart to be Natural.")
                    @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 100)
                    public double naturalCreakingHeartChance = 0.0D;
                }
            }

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.treeBlocks.bloomingPaleOak")
            @net.minecraftforge.common.config.Config.Comment("Config for all Blooming Pale Oak Blocks")
            public configTreeBlocks.configBloomingPaleOak bloomingPaleOak = new configTreeBlocks.configBloomingPaleOak();

            public static class configBloomingPaleOak
            {
                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.treeBlocks.bloomingPaleOak.enableBloomingPaleOakLeaves")
                @net.minecraftforge.common.config.Config.Comment("Enables Blooming Pale Oak Leaves.")
                public boolean enableBloomingPaleOakLeaves = true;

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.treeBlocks.bloomingPaleOak.enableSuckerRoots")
                @net.minecraftforge.common.config.Config.Comment("Enables Sucker Roots.")
                public boolean enableSuckerRoots = true;

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.treeBlocks.bloomingPaleOak.bloomingLeavesDropSapling")
                @net.minecraftforge.common.config.Config.Comment("If Blooming Pale Oak Leaves should drop Blooming Saplings. If disabled, instead they drop the normal Pale Oak Sapling.")
                public boolean bloomingLeavesDropSapling = false;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.treeBlocks.bloomingPaleOak.bloomingSapling")
                @net.minecraftforge.common.config.Config.Comment("Config related to Entities")
                public configBloomingPaleOak.configBloomingPaleOakSapling bloomingPaleOakSapling = new configBloomingPaleOak.configBloomingPaleOakSapling();

                public static class configBloomingPaleOakSapling
                {
                    @net.minecraftforge.common.config.Config.RequiresMcRestart
                    @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.treeBlocks.bloomingPaleOak.bloomingSapling.creakingHeartChance")
                    @net.minecraftforge.common.config.Config.Comment("The chance for a player-grown Pale Oak to have a Creaking Heart. Note Creaking Hearts need to be surrounded on all sides, so this may fail even at 100%.")
                    @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 100)
                    public double creakingHeartChance = 20.0D;

                    @net.minecraftforge.common.config.Config.RequiresMcRestart
                    @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.treeBlocks.bloomingPaleOak.bloomingSapling.naturalCreakingHeartChance")
                    @net.minecraftforge.common.config.Config.Comment("The chance for a generated Creaking Heart to be Natural.")
                    @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 100)
                    public double naturalCreakingHeartChance = 100.0D;
                }
            }

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.treeBlocks.peepingBirch")
            @net.minecraftforge.common.config.Config.Comment("Config for all Peeping Birch Blocks")
            public configTreeBlocks.configPeepingBirch peepingBirch = new configTreeBlocks.configPeepingBirch();

            public static class configPeepingBirch
            {
                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.treeBlocks.peepingBirch.enablePeepingBirchLog")
                @net.minecraftforge.common.config.Config.Comment("Enables the Peeping Birch Log.")
                public boolean enablePeepingBirchLog = true;
            }
        }

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks")
        @net.minecraftforge.common.config.Config.Comment("All config for blocks related to specialized Flora")
        public configBlock.configAwakenedFloraBlocks awakenedFloraBlocks = new configBlock.configAwakenedFloraBlocks();

        public static class configAwakenedFloraBlocks
        {
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.creakingHeart")
            @net.minecraftforge.common.config.Config.Comment("Config related to Entities")
            public configAwakenedFloraBlocks.configCreakingHeart creakingHeart = new configAwakenedFloraBlocks.configCreakingHeart();

            public static class configCreakingHeart
            {
                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.creakingHeart.enableCreakingHeart")
                @net.minecraftforge.common.config.Config.Comment("Enables the Creaking Heart.")
                public boolean enableCreakingHeart = true;

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.creakingHeart.alertReapingWillows")
                @net.minecraftforge.common.config.Config.Comment("If mining a Creaking Heart will anger Reaping Willows.")
                public boolean alertReapingWillows = true;

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.creakingHeart.unnaturalResinClumps")
                @net.minecraftforge.common.config.Config.Comment("Non-Natural Creaking Hearts (Player crafted) can generate Resin Clumps.")
                public boolean unnaturalResinClumps = true;

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.creakingHeart.naturalResinClumps")
                @net.minecraftforge.common.config.Config.Comment("Natural Creaking Hearts (Naturally generated or Grown) can generate Resin Clumps.")
                public boolean naturalResinClumps = true;

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.creakingHeart.naturalAmberValveDrop")
                @net.minecraftforge.common.config.Config.Comment("Natural Creaking Hearts will drop an Amber Valve when mined.")
                public boolean naturalAmberValveDrop = true;
            }

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.creakingLantern")
            @net.minecraftforge.common.config.Config.Comment("Creaking Lantern config")
            public configAwakenedFloraBlocks.configCreakingLantern creakingLantern = new configAwakenedFloraBlocks.configCreakingLantern();

            public static class configCreakingLantern
            {
                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.creakingLantern.enableCreakingLantern")
                @net.minecraftforge.common.config.Config.Comment("Enables Creaking Lanterns.")
                public boolean enableCreakingLantern = true;

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.creakingLantern.creakingsIgnoreWearer")
                @net.minecraftforge.common.config.Config.Comment("Creakings ignore Creaking Lantern wearers.")
                public boolean creakingsIgnoreWearer = true;
            }

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.hydraweed_body")
            @net.minecraftforge.common.config.Config.Comment("Hydraweed Body config")
            public configAwakenedFloraBlocks.configHydraweedBody hydraweed_body = new configAwakenedFloraBlocks.configHydraweedBody();

            public static class configHydraweedBody
            {
                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.hydraweed_body.enableHydraweedBody")
                @net.minecraftforge.common.config.Config.Comment("Enables the Hydraweed Body. Currently this also enables the Hydraweed Jaw.")
                public boolean enableHydraweedBody = false;
            }

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.incenseThorns")
            @net.minecraftforge.common.config.Config.Comment("Incense Thorns config")
            public configAwakenedFloraBlocks.configIncenseThorns incenseThorns = new configAwakenedFloraBlocks.configIncenseThorns();

            public static class configIncenseThorns
            {
                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.incenseThorns.enableIncenseThorns")
                @net.minecraftforge.common.config.Config.Comment("Enables Incense Thorns.")
                public boolean enableIncenseThorns = true;
            }


            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.nightlight")
            @net.minecraftforge.common.config.Config.Comment("Nightlight config")
            public configAwakenedFloraBlocks.configNightlight nightlight = new configAwakenedFloraBlocks.configNightlight();

            public static class configNightlight
            {
                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.nightlight.enableNightlight")
                @net.minecraftforge.common.config.Config.Comment("Enables the Nightlight.")
                public boolean enableNightlight = true;
            }

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.paleOakHollow")
            @net.minecraftforge.common.config.Config.Comment("Pale Oak Hollow config")
            public configAwakenedFloraBlocks.configPaleOakHollow paleOakHollow = new configAwakenedFloraBlocks.configPaleOakHollow();

            public static class configPaleOakHollow
            {
                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.paleOakHollow.enablePaleOakHollow")
                @net.minecraftforge.common.config.Config.Comment("Enables Pale Oak Hollows.")
                public boolean enablePaleOakHollow = true;

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.paleOakHollow.resinToSapQuantity")
                @net.minecraftforge.common.config.Config.Comment("How much resin the Hollow takes to become full of Sap.")
                public int resinToSapQuantity = 32;
            }


            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.pollenhead")
            @net.minecraftforge.common.config.Config.Comment("Pollenhead config")
            public configAwakenedFloraBlocks.configPollenhead pollenhead = new configAwakenedFloraBlocks.configPollenhead();

            public static class configPollenhead
            {
                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.pollenhead.enablePollenhead")
                @net.minecraftforge.common.config.Config.Comment("Enables Pollenheads.")
                public boolean enablePollenhead = true;

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.pollenhead.pollenheadHybridizeList")
                @net.minecraftforge.common.config.Config.Comment("Blocks that can be replaced by Pale Moss when growing.")
                public String[] pollenheadHybridizeList = {
                        "minecraft:double_plant:1=palebloom:pale_plant_double:1",
                        "minecraft:double_plant:4=palebloom:pale_plant_double:0",
                        "minecraft:double_plant:5=palebloom:pale_plant_double:2",
                        "minecraft:sapling:2=palebloom:pale_sapling:2",
                        "minecraft:sapling:5=palebloom:pale_sapling:0",
                        "palebloom:pale_sapling:0=palebloom:pale_sapling:1",
                        "minecraft:yellow_flower=palebloom:pale_petals",
                        "minecraft:red_flower:*=palebloom:pale_petals",
                        "minecraft:pumpkin:*=palebloom:pale_pumpkin"
                };
            }

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.reapingWillowSapling")
            @net.minecraftforge.common.config.Config.Comment("Reaping Willow Sapling config")
            public configAwakenedFloraBlocks.configReapingWillowSapling reapingWillowSapling = new configAwakenedFloraBlocks.configReapingWillowSapling();

            public static class configReapingWillowSapling
            {
                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.reapingWillowSapling.enableReapingWillowSapling")
                @net.minecraftforge.common.config.Config.Comment("Enables the Reaping Willow Sapling.")
                public boolean enableReapingWillowSapling = true;
            }

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.resinBulb")
            @net.minecraftforge.common.config.Config.Comment("Resin Bulb config")
            public configAwakenedFloraBlocks.configResinBulb resinBulb = new configAwakenedFloraBlocks.configResinBulb();

            public static class configResinBulb
            {
                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.resinBulb.enableResinBulb")
                @net.minecraftforge.common.config.Config.Comment("Enables the Resin Bulb. Disabling this will break MANY parts of the mod currently.")
                public boolean enableResinBulb = true;

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.resinBulb.resinBulbCollectList")
                @net.minecraftforge.common.config.Config.Comment("Blocks the Resin Bulb can obtain Resin from, and how much.")
                public String[] resinBulbCollectList = {
                        "palebloom:eyeblossom_closed=1",
                        "palebloom:pale_hanging_moss:*=1",
                        "palebloom:pale_moss_block=1",
                        "palebloom:pale_oak_leaves:*=1",
                        "palebloom:pale_oak_log:*=1",
                        "palebloom:pale_petals:*=1",
                        "palebloom:pale_pumpkin:*=1",
                        "palebloom:pale_sapling:*=1",
                        "palebloom:eyeblossom_open=2",
                        "palebloom:blooming_pale_oak_leaves:*=2",
                        "palebloom:pale_plant_double:*=2",
                        "palebloom:creaking_heart:*=3"
                };

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.resinBulb.activeCreakingHeartResinAmount")
                @net.minecraftforge.common.config.Config.Comment("How much Resin a Resin Bulb will pull from an active Creaking Heart at night. Setting to 0 disables this.")
                @net.minecraftforge.common.config.Config.RangeInt(min = 0, max = 64)
                public int activeCreakingHeartResinAmount = 8;
            }

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.awakenedFloraBlocks.enableSuckerRootNodule")
            @net.minecraftforge.common.config.Config.Comment("Enables the Sucker Root Nodule.")
            public boolean enableSuckerRootNodule = true;
        }

        @net.minecraftforge.common.config.Config.RequiresMcRestart
        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.paleMossReplacable")
        @net.minecraftforge.common.config.Config.Comment("Blocks that can be replaced by Pale Moss when growing.")
        public String[] paleMossReplacable = {
                "minecraft:dirt:*",
                "minecraft:grass",
                "minecraft:mycelium",
                "minecraft:stone",
                "minecraft:stone:1",
                "minecraft:stone:3",
                "minecraft:stone:5"
        };

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.floraBlocks")
        @net.minecraftforge.common.config.Config.Comment("All config for blocks related to Flora")
        public configBlock.configFloraBlocks floraBlocks = new configBlock.configFloraBlocks();

        public static class configFloraBlocks
        {
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.floraBlocks.enableEyeblossom")
            @net.minecraftforge.common.config.Config.Comment("Enables Eyeblossom3")
            public boolean enableEyeblossom = true;

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.floraBlocks.eyeblossomFullbright")
            @net.minecraftforge.common.config.Config.Comment("Renders the Open Eyeblossom at fullbright. This is Clientside, altering this requires the lightmap to be reloaded!")
            public boolean eyeblossomFullbright = true;

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.floraBlocks.enablePalePetals")
            @net.minecraftforge.common.config.Config.Comment("Enables Pale Petals.")
            public boolean enablePalePetals = true;

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.floraBlocks.enableBramble")
            @net.minecraftforge.common.config.Config.Comment("Enables Bramble.")
            public boolean enableBramble = true;

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.floraBlocks.enableDoublePalePlants")
            @net.minecraftforge.common.config.Config.Comment("Enables all the Double-tall Pale Plants (Eyeblossom Bush, Stiffpod, Pallid).")
            public boolean enableDoublePalePlants = true;

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.floraBlocks.palePumpkin")
            @net.minecraftforge.common.config.Config.Comment("All config for every Pale Pumpkin block, except the Creaking Lantern (which is under Awakened Flora)")
            public configFloraBlocks.configPalePumpkin palePumpkin = new configFloraBlocks.configPalePumpkin();

            public static class configPalePumpkin
            {
                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.floraBlocks.palePumpkin.enablePalePumpkin")
                @net.minecraftforge.common.config.Config.Comment("Enables Pale Pumpkin. Unlike vanilla 1.12, this refers to the uncarved form.")
                public boolean enablePalePumpkin = true;

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.floraBlocks.palePumpkin.enablePaleCarvedPumpkin")
                @net.minecraftforge.common.config.Config.Comment("Enables the Pale Carved Pumpkin.")
                public boolean enablePaleCarvedPumpkin = true;

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.floraBlocks.palePumpkin.enablePaleJackoLantern")
                @net.minecraftforge.common.config.Config.Comment("Enables the Pale Jack o' Lantern.")
                public boolean enablePaleJackoLantern = true;

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.floraBlocks.palePumpkin.enablePaleSoulJackoLantern")
                @net.minecraftforge.common.config.Config.Comment("Enables the Pale Soul Jack o' Lantern. Recipes utilize Soul Torches from Unseen's Nether Backport.")
                public boolean enablePaleSoulJackoLantern = true;
            }
        }

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.resinBlocks")
        @net.minecraftforge.common.config.Config.Comment("Nightlight config")
        public configBlock.configResinBlocks resinBlocks = new configBlock.configResinBlocks();

        public static class configResinBlocks
        {
            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.resinBlocks.enableResinClump")
            @net.minecraftforge.common.config.Config.Comment("Enables the Resin Clump.")
            public boolean enableResinClump = true;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.resinBlocks.enableResinBlock")
            @net.minecraftforge.common.config.Config.Comment("Enables the Resin Block.")
            public boolean enableResinBlock = true;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.resinBlocks.enableResinBricks")
            @net.minecraftforge.common.config.Config.Comment("Enables all Resin Bricks-type Blocks.")
            public boolean enableResinBricks = true;
        }

        @net.minecraftforge.common.config.Config.RequiresMcRestart
        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.enableSeedBomb")
        @net.minecraftforge.common.config.Config.Comment("Enables the Seed Bomb. This also enables the associated entity.")
        public boolean enableSeedBomb = true;
    }

    @net.minecraftforge.common.config.Config.RequiresMcRestart
    @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity")
    @net.minecraftforge.common.config.Config.Comment("Entity Config")
    public static configEntity entity = new configEntity();

    public static class configEntity
    {
        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.creaking")
        @net.minecraftforge.common.config.Config.Comment("Config related to Entities")
        public configCreaking creaking = new configCreaking();

        public static class configCreaking
        {
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.creaking.enableCreaking")
            @net.minecraftforge.common.config.Config.Comment("Enables the Creaking.")
            public boolean enableCreaking = true;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.creaking.chaseSpeedMult")
            @net.minecraftforge.common.config.Config.Comment("Multiplies the Creaking's speed when it has an attack target.")
            @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 9999)
            public double chaseSpeedMult = 5.0D;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.creaking.attackDamage")
            @net.minecraftforge.common.config.Config.Comment("How much damage a Creaking deals. Note that Vanilla is 3.")
            @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 9999)
            public double attackDamage = 4.0D;
        }

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.mannequin")
        @net.minecraftforge.common.config.Config.Comment("Config related to Entities")
        public configMannequin mannequin = new configMannequin();

        public static class configMannequin
        {
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.mannequin.enableMannequin")
            @net.minecraftforge.common.config.Config.Comment("Enables the Mannequin. This also enables the associated item.")
            public boolean enableMannequin = true;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.mannequin.chaseSpeedMult")
            @net.minecraftforge.common.config.Config.Comment("Multiplies the Mannequin's speed when it has an attack target.")
            @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 9999)
            public double chaseSpeedMult = 5.0D;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.mannequin.attackDamage")
            @net.minecraftforge.common.config.Config.Comment("How much damage a Mannequin deals.")
            @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 9999)
            public double attackDamage = 2.0D;
        }

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.pale_creeper")
        @net.minecraftforge.common.config.Config.Comment("Pale Creeper Config")
        public configPaleCreeper paleCreeper = new configPaleCreeper();

        public static class configPaleCreeper
        {
            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.pale_creeper.enablePaleCreeper")
            @net.minecraftforge.common.config.Config.Comment("Enables the Pale Creeper")
            public boolean enablePaleCreeper = true;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.pale_creeper.paleCreeperReplacementChance")
            @net.minecraftforge.common.config.Config.Comment("The percent chance a Pale Creeper replaces a normal Creeper spawn, within the Pale Garden.")
            @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 100)
            public double paleCreeperReplacementChance = 50;
        }

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.reapingWillow")
        @net.minecraftforge.common.config.Config.Comment("Config related to Entities")
        public configReapingWillow reapingWillow = new configReapingWillow();

        public static class configReapingWillow
        {
            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.reapingWillow.enableReapingWillow")
            @net.minecraftforge.common.config.Config.Comment("Enables the Reaping Willow.")
            public boolean enableReapingWillow = true;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.reapingWillow.reapingWillowIsntApathetic")
            @net.minecraftforge.common.config.Config.Comment({
                    "If the Reaping Willow will defend any harmed Pale Garden entities.",
                    "Note that this is overridden by the `reapingWillowsIgnoreCrimes` config option, for Pale Moss Cloak wearers."
            })
            public boolean reapingWillowIsntApathetic = true;
        }
    }

    @net.minecraftforge.common.config.Config.RequiresMcRestart
    @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item")
    @net.minecraftforge.common.config.Config.Comment("Config related to Items")
    public static configItem item = new configItem();

    public static class configItem
    {
        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.enableAmberValve")
        @net.minecraftforge.common.config.Config.Comment("Enables the Amber Valve. Note this is used for a lot of recipes. ")
        public boolean enableAmberValve = true;

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.enableEeriePainting")
        @net.minecraftforge.common.config.Config.Comment("Enables the Eerie Painting. This also enables the associated entity.")
        public boolean enableEeriePainting = true;

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.enableLiveRoot")
        @net.minecraftforge.common.config.Config.Comment("Enables the Live Root.")
        public boolean enableLiveRoot = true;

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.enablePaleCreeperHusk")
        @net.minecraftforge.common.config.Config.Comment("Enables the Pale Creeper Husk.")
        public boolean enablePaleCreeperHusk = true;

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.enablePaleOakBoat")
        @net.minecraftforge.common.config.Config.Comment("Enables the Pale Oak Boat. This also enables the associated entity.")
        public boolean enablePaleOakBoat = true;

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.enablePaleOakSap")
        @net.minecraftforge.common.config.Config.Comment("Enables Pale Oak Sap.")
        public boolean enablePaleOakSap = true;

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.enableResinBrick")
        @net.minecraftforge.common.config.Config.Comment("Enables the Resin Brick.")
        public boolean enableResinBrick = true;

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.foods")
        @net.minecraftforge.common.config.Config.Comment("Config for all food items")
        public configItem.configFoods foods = new configItem.configFoods();

        public static class configFoods
        {
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.foods.enableNightligtBulb")
            @net.minecraftforge.common.config.Config.Comment("Enables Nightlight Bulbs. Note, this causes Nightlights to drop nothing when harvested. ")
            public boolean enableNightligtBulb = true;

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.foods.nightlightBulbEffectLength")
            @net.minecraftforge.common.config.Config.Comment("How long (in game ticks [20 = 1 second]) the Amber Eyes effect is given, by eating a Nightlight Bulb.")
            public int nightlightBulbEffectLength = 310;

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.foods.enablePalePumpkinPie")
            @net.minecraftforge.common.config.Config.Comment("Enables the Pale Pumpkin Pie.")
            public boolean enablePalePumpkinPie = true;

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.foods.palePumpkinPieEffectLength")
            @net.minecraftforge.common.config.Config.Comment("How long (in game ticks [20 = 1 second]) the Amber Eyes effect is given, by eating a Pale Pumpkin Pie.")
            public int palePumpkinPieEffectLength = 2410;
        }

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools")
        @net.minecraftforge.common.config.Config.Comment("Config for all Gardengrafted Tools and Weapons")
        public configItem.configGardengraftedTools gardengraftedTools = new configItem.configGardengraftedTools();

        public static class configGardengraftedTools
        {
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarAxe")
            public configGardengraftedTools.configCultivarAxe cultivarAxe = new configGardengraftedTools.configCultivarAxe();

            public static class configCultivarAxe
            {
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarAxe.enableCultivarAxe")
                @net.minecraftforge.common.config.Config.Comment("Enables the Gardengrafted Axe.")
                public boolean enableCultivarAxe = true;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarAxe.awakenedThornBurst")
                @net.minecraftforge.common.config.Config.Comment("Mined Logs explode into a burst of Thorns, when Awakened.")
                public boolean awakenedThornBurst = true;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarAxe.awakenedThornBurstCooldown")
                @net.minecraftforge.common.config.Config.Comment("The cooldown (in ticks) applied for the Thorn Burst Ability.")
                public int awakenedThornBurstCooldown = 60;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarAxe.awakenedBulbHealing")
                @net.minecraftforge.common.config.Config.Comment("Repairs self using Resin from nearby Resin Bulbs.")
                public boolean awakenedBulbHealing = true;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarAxe.awakenedHarvestLevel")
                @net.minecraftforge.common.config.Config.Comment("Adds this value to the Harvest Level when Awakened.")
                public int awakenedHarvestLevel = 1;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarAxe.awakenedMiningSpeed")
                @net.minecraftforge.common.config.Config.Comment("Multiplies this value to the Mining Speed when Awakened.")
                public float awakenedMiningSpeed = 2.0F;
            }

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarHoe")
            public configGardengraftedTools.configCultivarHoe cultivarHoe = new configGardengraftedTools.configCultivarHoe();

            public static class configCultivarHoe
            {
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarHoe.enableCultivarHoe")
                @net.minecraftforge.common.config.Config.Comment("Enables the Gardengrafted Hoe.")
                public boolean enableCultivarHoe = true;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarHoe.awakenedGardenCall")
                @net.minecraftforge.common.config.Config.Comment("Calls all Pale Entities to attack your target, when Awakened.")
                public boolean awakenedGardenCall = true;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarHoe.awakenedGardenCallCooldown")
                @net.minecraftforge.common.config.Config.Comment("The cooldown (in ticks) applied for the Garden Call Ability.")
                public int awakenedGardenCallCooldown = 120;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarHoe.awakenedBulbHealing")
                @net.minecraftforge.common.config.Config.Comment("Repairs self using Resin from nearby Resin Bulbs.")
                public boolean awakenedBulbHealing = true;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarHoe.awakenedHarvestLevel")
                @net.minecraftforge.common.config.Config.Comment("Adds this value to the Harvest Level when Awakened.")
                public int awakenedHarvestLevel = 1;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarHoe.awakenedMiningSpeed")
                @net.minecraftforge.common.config.Config.Comment("Multiplies this value to the Mining Speed when Awakened.")
                public float awakenedMiningSpeed = 2.0F;
            }

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarPickaxe")
            public configGardengraftedTools.configCultivarPickaxe cultivarPickaxe = new configGardengraftedTools.configCultivarPickaxe();

            public static class configCultivarPickaxe
            {
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarPickaxe.enableCultivarPickaxe")
                @net.minecraftforge.common.config.Config.Comment("Enables the Gardengrafted Pickaxe.")
                public boolean enableCultivarPickaxe = true;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarPickaxe.awakenedResinBlood")
                @net.minecraftforge.common.config.Config.Comment("Killed entities place 2-3 Resin Clumps, when Awakened.")
                public boolean awakenedResinBlood = true;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarPickaxe.awakenedResinBloodCooldown")
                @net.minecraftforge.common.config.Config.Comment("The cooldown (in ticks) applied for the Resin Blood Ability.")
                public int awakenedResinBloodCooldown = 60;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarPickaxe.awakenedBulbHealing")
                @net.minecraftforge.common.config.Config.Comment("Repairs self using Resin from nearby Resin Bulbs.")
                public boolean awakenedBulbHealing = true;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarPickaxe.awakenedHarvestLevel")
                @net.minecraftforge.common.config.Config.Comment("Adds this value to the Harvest Level when Awakened.")
                public int awakenedHarvestLevel = 1;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarPickaxe.awakenedMiningSpeed")
                @net.minecraftforge.common.config.Config.Comment("Multiplies this value to the Mining Speed when Awakened.")
                public float awakenedMiningSpeed = 2.0F;
            }

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarShovel")
            public configGardengraftedTools.configCultivarShovel cultivarShovel = new configGardengraftedTools.configCultivarShovel();

            public static class configCultivarShovel
            {
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarShovel.enableCultivarShovel")
                @net.minecraftforge.common.config.Config.Comment("Enables the Gardengrafted Shovel.")
                public boolean enableCultivarShovel = true;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarShovel.awakenedThornStroke")
                @net.minecraftforge.common.config.Config.Comment("Enables the Gardengrafted Sword.")
                public boolean awakenedThornStroke = true;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarShovel.awakenedThornStrokeCooldown")
                @net.minecraftforge.common.config.Config.Comment("The cooldown (in ticks) applied for the Resin Blood Ability.")
                public int awakenedThornStrokeCooldown = 120;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarShovel.awakenedBulbHealing")
                @net.minecraftforge.common.config.Config.Comment("Repairs self using Resin from nearby Resin Bulbs.")
                public boolean awakenedBulbHealing = true;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarShovel.awakenedHarvestLevel")
                @net.minecraftforge.common.config.Config.Comment("Adds this value to the Harvest Level when Awakened.")
                public int awakenedHarvestLevel = 1;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarShovel.awakenedMiningSpeed")
                @net.minecraftforge.common.config.Config.Comment("Multiplies this value to the Mining Speed when Awakened.")
                public float awakenedMiningSpeed = 2.0F;
            }

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarSword")
            public configGardengraftedTools.configCultivarSword cultivarSword = new configGardengraftedTools.configCultivarSword();

            public static class configCultivarSword
            {
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarSword.enableCultivarSword")
                @net.minecraftforge.common.config.Config.Comment("Enables the Gardengrafted Sword. ")
                public boolean enableCultivarSword = true;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarSword.awakenedTransfer")
                @net.minecraftforge.common.config.Config.Comment("Poison and/or Wither is transferred from yourself to your target, when Awakened.")
                public boolean awakenedTransfer = true;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarSword.awakenedTransferCooldown")
                @net.minecraftforge.common.config.Config.Comment("The cooldown (in ticks) applied for the Transfer Ability.")
                public int awakenedTransferCooldown = 120;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarSword.awakenedBulbHealing")
                @net.minecraftforge.common.config.Config.Comment("Repairs self using Resin from nearby Resin Bulbs.")
                public boolean awakenedBulbHealing = true;

                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.cultivarTools.cultivarSword.awakenedDamage")
                @net.minecraftforge.common.config.Config.Comment("Enables the Gardengrafted Sword. ")
                public double awakenedDamage = 6.0D;
            }
        }

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.paleMossCloak")
        @net.minecraftforge.common.config.Config.Comment("Config for the Pale Moss Cloak")
        public configItem.configPaleMossCloak paleMossCloak = new configItem.configPaleMossCloak();

        public static class configPaleMossCloak
        {
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.paleMossCloak.enablePaleMossCloak")
            @net.minecraftforge.common.config.Config.Comment("Enables the Pale Moss Cloak.")
            public boolean enablePaleMossCloak = true;

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.paleMossCloak.reapingWillowsDefendCloakWearers")
            @net.minecraftforge.common.config.Config.Comment("Reaping Willows will defend Pale Moss Cloak wearers.")
            public boolean reapingWillowsDefendCloakWearers = true;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.creakingHeart.reapingWillowsIgnoreCrimes")
            @net.minecraftforge.common.config.Config.Comment("If Reaping Willows will ignore Garden Calls (harm to Pale Entities) against Pale Moss Cloak wearers.")
            public boolean reapingWillowsIgnoreCrimes = true;
        }
    }



    @Mod.EventBusSubscriber(modid = paleBloom.MOD_ID)
    public static class ConfigSyncHandler
    {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if(event.getModID().equals(paleBloom.MOD_ID))
            { ConfigManager.sync(paleBloom.MOD_ID, net.minecraftforge.common.config.Config.Type.INSTANCE); }
        }
    }
}