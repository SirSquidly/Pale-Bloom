package com.sirsquidly.palebloom;

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
        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.creakingHeart.naturalAmberValveDrop")
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
            @net.minecraftforge.common.config.Config.Comment("All config related to Trees in the Pale Garden")
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





        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.paleGarden.fog")
        @net.minecraftforge.common.config.Config.Comment("Config related to Entities")
        public configFog fog = new configFog();

        public static class configFog
        {
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.config.paleGarden.fog.enableFog")
            @net.minecraftforge.common.config.Config.Comment("Enables the custom Heavy Fog.")
            public boolean enableFog = true;

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.config.paleGarden.fog.creativeIgnoresFog")
            @net.minecraftforge.common.config.Config.Comment("Makes Fog not render for players in Creative Mode.")
            public boolean creativeIgnoresFog = true;

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.config.paleGarden.fog.fogFarDistance")
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
        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.creakingHeart")
        @net.minecraftforge.common.config.Config.Comment("Config related to Entities")
        public configBlock.configCreakingHeart creakingHeart = new configBlock.configCreakingHeart();

        public static class configCreakingHeart
        {
            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.creakingHeart.enableCreakingHeart")
            @net.minecraftforge.common.config.Config.Comment("Enables the Creaking Heart.")
            public boolean enableCreakingHeart = true;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.creakingHeart.naturalResinOnly")
            @net.minecraftforge.common.config.Config.Comment("Non-Natural Creaking Hearts (Player crafted) can generate Resin Clumps.")
            public boolean unnaturalResinClumps = true;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.creakingHeart.naturalResinOnly")
            @net.minecraftforge.common.config.Config.Comment("Natural Creaking Hearts (Naturally generated or Grown) can generate Resin Clumps.")
            public boolean naturalResinClumps = true;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.creakingHeart.naturalAmberValveDrop")
            @net.minecraftforge.common.config.Config.Comment("Natural Creaking Hearts will drop an Amber Valve when mined.")
            public boolean naturalAmberValveDrop = true;
        }

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.saplings")
        @net.minecraftforge.common.config.Config.Comment("Config related to Entities")
        public configBlock.configSaplings saplings = new configBlock.configSaplings();

        public static class configSaplings
        {
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.saplings.paleOakSapling")
            @net.minecraftforge.common.config.Config.Comment("Config related to Entities")
            public configSaplings.configPaleOakSapling paleOakSapling = new configSaplings.configPaleOakSapling();

            public static class configPaleOakSapling
            {
                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.saplings.paleOakSapling.creakingHeartChance")
                @net.minecraftforge.common.config.Config.Comment("The chance for a player-grown Pale Oak to have a Creaking Heart. Note Creaking Hearts need to be surrounded on all sides, so this may fail even at 100%.")
                @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 100)
                public double creakingHeartChance = 0.0D;

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.saplings.creakingHeart.naturalCreakingHeartChance")
                @net.minecraftforge.common.config.Config.Comment("The chance for a generated Creaking Heart to be Natural.")
                @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 100)
                public double naturalCreakingHeartChance = 0.0D;
            }

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.saplings.bloomingPaleOakSapling")
            @net.minecraftforge.common.config.Config.Comment("Config related to Entities")
            public configSaplings.configBloomingPaleOakSapling bloomingPaleOakSapling = new configSaplings.configBloomingPaleOakSapling();

            public static class configBloomingPaleOakSapling
            {
                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.saplings.bloomingPaleOakSapling.creakingHeartChance")
                @net.minecraftforge.common.config.Config.Comment("The chance for a player-grown Pale Oak to have a Creaking Heart. Note Creaking Hearts need to be surrounded on all sides, so this may fail even at 100%.")
                @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 100)
                public double creakingHeartChance = 20.0D;

                @net.minecraftforge.common.config.Config.RequiresMcRestart
                @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.saplings.bloomingPaleOakSapling.naturalCreakingHeartChance")
                @net.minecraftforge.common.config.Config.Comment("The chance for a generated Creaking Heart to be Natural.")
                @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 100)
                public double naturalCreakingHeartChance = 100.0D;
            }
        }

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.eyeblossomFullbright")
        @net.minecraftforge.common.config.Config.Comment("Renders the Open Eyeblossom at fullbright. This is Clientside, altering this requires the lightmap to be reloaded!")
        public boolean eyeblossomFullbright = true;

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


        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.incenseThorns")
        @net.minecraftforge.common.config.Config.Comment("Incense Thorns config")
        public configBlock.configIncenseThorns incenseThorns = new configBlock.configIncenseThorns();

        public static class configIncenseThorns
        {
            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.incenseThorns.enableIncenseThorns")
            @net.minecraftforge.common.config.Config.Comment("Enables Incense Thorns.")
            public boolean enableIncenseThorns = true;
        }


        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.paleOakHollow")
        @net.minecraftforge.common.config.Config.Comment("Pale Oak Hollow config")
        public configBlock.configPaleOakHollow paleOakHollow = new configBlock.configPaleOakHollow();

        public static class configPaleOakHollow
        {
            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.paleOakHollow.enablePaleOakHollow")
            @net.minecraftforge.common.config.Config.Comment("Enables Pale Oak Hollows.")
            public boolean enablePaleOakHollow = true;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.paleOakHollow.resinToSapQuantity")
            @net.minecraftforge.common.config.Config.Comment("How much resin the Hollow takes to become full of Sap.")
            public int resinToSapQuantity = 32;
        }


        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.pollenhead")
        @net.minecraftforge.common.config.Config.Comment("Pollenhead config")
        public configBlock.configPollenhead pollenhead = new configBlock.configPollenhead();

        public static class configPollenhead
        {
            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.pollenhead.enableResinBulb")
            @net.minecraftforge.common.config.Config.Comment("Enables Pollenheads.")
            public boolean enablePollenhead = true;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.pollenhead.pollenheadHybridizeList")
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

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.resinBulb")
        @net.minecraftforge.common.config.Config.Comment("Resin Bulb config")
        public configBlock.configResinBulb resinBulb = new configBlock.configResinBulb();

        public static class configResinBulb
        {
            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.resinBulb.enableResinBulb")
            @net.minecraftforge.common.config.Config.Comment("Enables the Resin Bulb. Disabling this will break MANY parts of the mod currently.")
            public boolean enableResinBulb = true;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.resinBulb.resinBulbCollectList")
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
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.block.resinBulb.activeCreakingHeartResinAmount")
            @net.minecraftforge.common.config.Config.Comment("How much Resin a Resin Bulb will pull from an active Creaking Heart at night. Setting to 0 disables this.")
            @net.minecraftforge.common.config.Config.RangeInt(min = 0, max = 64)
            public int activeCreakingHeartResinAmount = 8;
        }
    }


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
            @net.minecraftforge.common.config.Config.Comment("The furthest, or 'end' point, of the Fog. Nothing past this is visible.")
            @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 9999)
            public double chaseSpeedMult = 5.0D;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.creaking.attackDamage")
            @net.minecraftforge.common.config.Config.Comment("The furthest, or 'end' point, of the Fog. Nothing past this is visible.")
            @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 9999)
            public double attackDamage = 4.0D;
        }

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.mannequin")
        @net.minecraftforge.common.config.Config.Comment("Config related to Entities")
        public configMannequin mannequin = new configMannequin();

        public static class configMannequin
        {
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.mannequin.enableMannequin")
            @net.minecraftforge.common.config.Config.Comment("Enables the Mannequin.")
            public boolean enableMannequin = true;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.mannequin.chaseSpeedMult")
            @net.minecraftforge.common.config.Config.Comment("The furthest, or 'end' point, of the Fog. Nothing past this is visible.")
            @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 9999)
            public double chaseSpeedMult = 5.0D;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.mannequin.attackDamage")
            @net.minecraftforge.common.config.Config.Comment("The furthest, or 'end' point, of the Fog. Nothing past this is visible.")
            @net.minecraftforge.common.config.Config.RangeDouble(min = 0, max = 9999)
            public double attackDamage = 4.0D;
        }

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.pale_creeper")
        @net.minecraftforge.common.config.Config.Comment("Pale Creeper Config")
        public configPaleCreeper paleCreeper = new configPaleCreeper();

        public static class configPaleCreeper
        {
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
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.entity.reapingWillow.enableReapingWillow")
            @net.minecraftforge.common.config.Config.Comment("Enables the Reaping Willow.")
            public boolean enableReapingWillow = true;
        }
    }

    @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item")
    @net.minecraftforge.common.config.Config.Comment("Config related to Items")
    public static configItem item = new configItem();

    public static class configItem
    {
        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.enableAmberValve")
        @net.minecraftforge.common.config.Config.Comment("Enables the Amber Valve. Note this is used for a lot of recipes. ")
        public boolean enableAmberValve = true;

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.enablePaleCreeperHusk")
        @net.minecraftforge.common.config.Config.Comment("Enables the Pale Creeper Husk.")
        public boolean enablePaleCreeperHusk = true;

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

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.gardengraftedTools")
        @net.minecraftforge.common.config.Config.Comment("Config for all Gardengrafted Tools and Weapons")
        public configItem.configGardengraftedTools gardengraftedTools = new configItem.configGardengraftedTools();

        public static class configGardengraftedTools
        {

        }

        @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.paleMossCloak")
        @net.minecraftforge.common.config.Config.Comment("Config for the Pale Moss Cloak")
        public configItem.configPaleMossCloak paleMossCloak = new configItem.configPaleMossCloak();

        public static class configPaleMossCloak
        {
            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.paleMossCloak.enablePaleMossCloak")
            @net.minecraftforge.common.config.Config.Comment("Enables the Pale Moss Cloak.")
            public boolean enablePaleMossCloak = true;

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.paleMossCloak.creepersIgnoreCloakWearers")
            @net.minecraftforge.common.config.Config.Comment("Creepers do not auto target wearers of a Pale Moss Cloak. This doesn't stop Creepers from defending themselves.")
            public boolean creepersIgnoreCloakWearers = true;

            @net.minecraftforge.common.config.Config.LangKey("config.palebloom.item.paleMossCloak.reapingWillowsDefendCloakWearers")
            @net.minecraftforge.common.config.Config.Comment("Reaping Willows will defend Pale Moss Cloak wearers.")
            public boolean reapingWillowsDefendCloakWearers = true;
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