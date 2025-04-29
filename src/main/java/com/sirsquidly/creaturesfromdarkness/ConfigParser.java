package com.sirsquidly.creaturesfromdarkness;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;

/**
 * 	This is to break part arrays in the config for use in other areas of the code.
 *
 *  I break it up in this class so that I don't have to break the config arrays every time I want to use them.
 */
public class ConfigParser
{
	/** Nightmare spawn biomes list. */
	public static List<Biome> nightmareSpawnBiomes = Lists.<Biome>newArrayList();
	/** Shadow spawn biomes list. */
	public static List<Biome> shadowSpawnBiomes = Lists.<Biome>newArrayList();
	/** A Spawn List that is used for temporary functions, such as flipping the current list. */
	public static List<Biome> tempSpawnList = Lists.<Biome>newArrayList();

	/** Goes through the many Arrays in the config, to translate them into lists to be used elsewhere. */
	public static void breakupConfigArrays()
	{
		for(String S : Config.entity.nightmare.nightmareSpawning.biomes)
		{
			boolean isType = S.startsWith("Type.");

			if (isType)
			{ addBiomesFromType(S.substring(5), nightmareSpawnBiomes); }
			else
			{
				Biome biome = getBiomeFromString(S);
				if (biome != null && !nightmareSpawnBiomes.contains(biome))
				{ nightmareSpawnBiomes.add(biome); }
			}
		}
		if (Config.entity.nightmare.nightmareSpawning.biomesAsBlacklist) flipBiomeList(nightmareSpawnBiomes);


		for(String S : Config.entity.shadow.shadowSpawning.biomes)
		{
			boolean isType = S.startsWith("Type.");

			if (isType)
			{ addBiomesFromType(S.substring(5), shadowSpawnBiomes); }
			else
			{
				Biome biome = getBiomeFromString(S);
				if (biome != null && !shadowSpawnBiomes.contains(biome))
				{ shadowSpawnBiomes.add(biome); }
			}
		}
		if (Config.entity.shadow.shadowSpawning.biomesAsBlacklist) flipBiomeList(shadowSpawnBiomes);
	}

	public static Biome getBiomeFromString(String string)
	{
		String[] ripString = string.split(":");

		if (ripString.length < 2)
		{
			creaturesfromdarkness.LOGGER.error("Improperly written biome!");
			return null;
		}

		return ForgeRegistries.BIOMES.getValue(new ResourceLocation(ripString[0], ripString[1]));
	}

	/** Adds every biome from the given Biome Type. */
	public static void addBiomesFromType(String string, List list)
	{
		for(Biome biome : BiomeDictionary.getBiomes(BiomeDictionary.Type.getType(string)))
		{ list.add(biome); }
	}

	/* Simply copies the given biome list to a temp list, clears the original, adds any existing biome NOT in the temp list, before re-clearing the temp list. */
	public static void flipBiomeList(List list)
	{
		tempSpawnList.addAll(list);
		list.clear();

		for (Biome biome : ForgeRegistries.BIOMES)
		{
			if (!tempSpawnList.contains(biome))
			{ list.add(biome); }
		}

		tempSpawnList.clear();
	}
}