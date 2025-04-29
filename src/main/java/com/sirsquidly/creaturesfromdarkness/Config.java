package com.sirsquidly.creaturesfromdarkness;

import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@net.minecraftforge.common.config.Config(modid = creaturesfromdarkness.MOD_ID, name = creaturesfromdarkness.CONFIG_NAME)
@net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.title")
@Mod.EventBusSubscriber(modid = creaturesfromdarkness.MOD_ID)
public class Config
{

    @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity")
    @net.minecraftforge.common.config.Config.Comment("Config related to Entities")
    public static configEntity entity = new configEntity();

    public static class configEntity
    {
        @net.minecraftforge.common.config.Config.RequiresMcRestart
        @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.nightmare")
        public configNightmare nightmare = new configNightmare();

        public static class configNightmare
        {
            @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.nightmare.enableNightmare")
            @net.minecraftforge.common.config.Config.Comment("If Nightmares should be enabled.")
            public boolean enableNightmare = true;

            @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.nightmare.enableSpawnEgg")
            @net.minecraftforge.common.config.Config.Comment("Enables a Spawn Egg for the Nightmare.")
            public boolean enableSpawnEgg = true;

            @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.nightmare.nightmareScaresAnimals")
            @net.minecraftforge.common.config.Config.Comment("Causes animals near Nightmares to panic.")
            public boolean nightmareScaresAnimals = true;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.nightmare.nightmareSpawning")
            public configNightmareSpawning nightmareSpawning = new configNightmareSpawning();

            public static class configNightmareSpawning
            {
                @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.nightmare.nightmareSpawning.enableNightmareSpawning")
                @net.minecraftforge.common.config.Config.Comment("If Nightmares should spawn naturally.")
                public boolean enableNightmareSpawning = true;

                @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.nightmare.nightmareSpawning.maxYLevel")
                @net.minecraftforge.common.config.Config.Comment("The maximum y-level Nightmares will spawn at. Setting to 0 disables this check.")
                public int maxYLevel = 40;

                @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.nightmare.nightmareSpawning.maxLightLevel")
                @net.minecraftforge.common.config.Config.Comment("The maximum light Nightmares will spawn within. Setting to 15 disables this check.")
                public int maxLightLevel = 7;

                @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.nightmare.nightmareSpawning.spawnWeight")
                @net.minecraftforge.common.config.Config.Comment("The spawn weight for Nightmares. Scaled for Vanilla spawning.")
                public int spawnWeight = 3;

                @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.nightmare.nightmareSpawning.biomesAsBlacklist")
                @net.minecraftforge.common.config.Config.Comment("Flips the Biomes List to operate as a Blacklist.")
                public boolean biomesAsBlacklist = true;

                @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.nightmare.nightmareSpawning.biomes")
                @net.minecraftforge.common.config.Config.Comment("A list of biomes this spawn uses, as a Whitelist (or Blacklist if enabled). Entire Biome Types are accepted.")
                public String[] biomes = {
                        "minecraft:mushroom_island",
                        "minecraft:mushroom_island_shore",
                        "Type.END",
                        "Type.NETHER"
                };
            }
        }

        @net.minecraftforge.common.config.Config.RequiresMcRestart
        @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.shadow")
        public configShadow shadow = new configShadow();

        public static class configShadow
        {
            @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.shadow.enableShadow")
            @net.minecraftforge.common.config.Config.Comment("If Shadows should be enabled.")
            public boolean enableShadow = true;

            @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.shadow.enableSpawnEgg")
            @net.minecraftforge.common.config.Config.Comment("Enables a Spawn Egg for the Shadow.")
            public boolean enableSpawnEgg = true;

            @net.minecraftforge.common.config.Config.RequiresMcRestart
            @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.shadow.shadowSpawning")
            public configShadowSpawning shadowSpawning = new configShadowSpawning();

            public static class configShadowSpawning
            {
                @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.shadow.shadowSpawning.enableShadowSpawning")
                @net.minecraftforge.common.config.Config.Comment("If Nightmares should spawn naturally.")
                public boolean enableShadowSpawning = true;

                @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.shadow.shadowSpawning.maxYLevel")
                @net.minecraftforge.common.config.Config.Comment("The maximum y-level Nightmares will spawn at. Setting to 0 disables this check.")
                public int maxYLevel = 40;

                @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.shadow.shadowSpawning.maxLightLevel")
                @net.minecraftforge.common.config.Config.Comment("The maximum light Nightmares will spawn within. Setting to 15 disables this check.")
                public int maxLightLevel = 2;

                @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.shadow.shadowSpawning.spawnWeight")
                @net.minecraftforge.common.config.Config.Comment("The spawn weight for Nightmares. Scaled for Vanilla spawning.")
                public int spawnWeight = 3;

                @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.shadow.shadowSpawning.biomesAsBlacklist")
                @net.minecraftforge.common.config.Config.Comment("Flips the Biomes List to operate as a Blacklist.")
                public boolean biomesAsBlacklist = true;

                @net.minecraftforge.common.config.Config.LangKey("creaturesfromdarkness.config.entity.shadow.shadowSpawning.biomes")
                @net.minecraftforge.common.config.Config.Comment("A list of biomes this spawn uses, as a Whitelist (or Blacklist if enabled). Entire Biome Types are accepted.")
                public String[] biomes = {
                        "minecraft:mushroom_island",
                        "minecraft:mushroom_island_shore",
                        "Type.END",
                        "Type.NETHER"
                };
            }
        }
    }

    @Mod.EventBusSubscriber(modid = creaturesfromdarkness.MOD_ID)
    public static class ConfigSyncHandler
    {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if(event.getModID().equals(creaturesfromdarkness.MOD_ID))
            { ConfigManager.sync(creaturesfromdarkness.MOD_ID, net.minecraftforge.common.config.Config.Type.INSTANCE); }
        }
    }
}