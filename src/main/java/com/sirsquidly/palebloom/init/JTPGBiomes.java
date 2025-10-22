package com.sirsquidly.palebloom.init;

import com.sirsquidly.palebloom.Config;
import com.sirsquidly.palebloom.world.biome.BiomePaleGarden;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import static net.minecraft.world.biome.Biome.getIdForBiome;

@Mod.EventBusSubscriber
public class JTPGBiomes
{
    public static Biome PALE_GARDEN = new BiomePaleGarden();

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event)
    {
        registerBiome(PALE_GARDEN, "pale_garden", 1000, BiomeManager.BiomeType.WARM, BiomeDictionary.Type.SPOOKY, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.FOREST);
    }

    private static void registerBiome(Biome biome, String name, int biomeWeight, BiomeManager.BiomeType biomeType, BiomeDictionary.Type... types)
    {
        biome.setRegistryName(name);
        ForgeRegistries.BIOMES.register(biome);
        BiomeDictionary.addTypes(biome, types);
        BiomeManager.addBiome(biomeType, new BiomeManager.BiomeEntry(biome, biomeWeight));

        if (Config.paleGarden.subbiomeOfDarkOak) Biome.MUTATION_TO_BASE_ID_MAP.put(biome, getIdForBiome(ForgeRegistries.BIOMES.getValue(new ResourceLocation("minecraft:roofed_forest"))));

    }
}
