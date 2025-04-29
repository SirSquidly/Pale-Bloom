package com.sirsquidly.creaturesfromdarkness.init;

import com.sirsquidly.creaturesfromdarkness.Config;
import com.sirsquidly.creaturesfromdarkness.ConfigParser;
import com.sirsquidly.creaturesfromdarkness.client.*;
import com.sirsquidly.creaturesfromdarkness.creaturesfromdarkness;
import com.sirsquidly.creaturesfromdarkness.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CFDEntities
{
    public static int id;

    public static void registerEntities()
    {
        if (Config.entity.shadow.enableShadow)
        {
            if (Config.entity.shadow.enableSpawnEgg) registerEntity("shadow", EntityShadow.class, ++id, 60, 460551, 4737096);
            else registerEntity("shadow", EntityShadow.class, ++id, 60);
        }

        if (Config.entity.nightmare.enableNightmare)
        {
            if (Config.entity.nightmare.enableSpawnEgg) registerEntity("nightmare", EntityNightmare.class, ++id, 100, 460551, 16777215);
            else registerEntity("nightmare", EntityNightmare.class, ++id, 100);
        }
    }

    public static void registerEntitySpawns()
    {
        if (Config.entity.nightmare.enableNightmare && Config.entity.nightmare.nightmareSpawning.enableNightmareSpawning)
        { EntityRegistry.addSpawn(EntityNightmare.class, Config.entity.nightmare.nightmareSpawning.spawnWeight, 1, 1, EnumCreatureType.MONSTER, ConfigParser.nightmareSpawnBiomes.toArray(new Biome[0])); }

        if (Config.entity.shadow.enableShadow && Config.entity.shadow.shadowSpawning.enableShadowSpawning)
        { EntityRegistry.addSpawn(EntityNightmare.class, Config.entity.shadow.shadowSpawning.spawnWeight, 1, 1, EnumCreatureType.MONSTER, ConfigParser.shadowSpawnBiomes.toArray(new Biome[0])); }
    }

    @SideOnly(Side.CLIENT)
    public static void RegisterRenderers()
    {
        if (Config.entity.shadow.enableShadow) RenderingRegistry.registerEntityRenderingHandler(EntityShadow.class, RenderShadow::new);
        if (Config.entity.nightmare.enableNightmare) RenderingRegistry.registerEntityRenderingHandler(EntityNightmare.class, RenderNightmare::new);
    }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, int color1, int color2)
    { net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(new ResourceLocation(creaturesfromdarkness.MOD_ID, name), entity, creaturesfromdarkness.MOD_ID + "." + name, id, creaturesfromdarkness.instance, range, 1, true, color1, color2); }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range)
    { net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(new ResourceLocation(creaturesfromdarkness.MOD_ID, name), entity, creaturesfromdarkness.MOD_ID + "." + name, id, creaturesfromdarkness.instance, range, 1, true); }
}