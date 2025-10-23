package com.sirsquidly.palebloom.config;

import com.google.common.collect.Lists;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 	This is to break part arrays in the config for use in other areas of the code.
 *
 *  I break it up in this class so that I don't have to break the config arrays every time I want to use them.
 */
public class ConfigParser
{
	/** Nightmare spawn biomes list. */
	public static List<Biome> nightmareSpawnBiomes = Lists.newArrayList();
	/** Shadow spawn biomes list. */
	public static List<Biome> shadowSpawnBiomes = Lists.newArrayList();
	/** A Spawn List that is used for temporary functions, such as flipping the current list. */
	public static List<Biome> tempSpawnList = Lists.newArrayList();

	/** Block states that Pale Moss can replace. */
	public static List<IBlockState> PaleMossReplacableList = Lists.newArrayList();


	/** Block states that Pale Moss can replace. */
	public static List<IBlockState> blockPollenheadHybridFROM = Lists.newArrayList();
	/** Block states that Pale Moss can replace. */
	public static List<IBlockState> blockPollenheadHybridTO = Lists.newArrayList();

	/** Block states that Pale Moss can replace. */
	public static List<IBlockState> blockResinBulbCollectFROM = Lists.newArrayList();
	/** Block states that Pale Moss can replace. */
	public static List<Integer> blockResinBulbCollectQUANITTY = Lists.newArrayList();



	/** Goes through the many Arrays in the config, to translate them into lists to be used elsewhere. */
	public static void breakupConfigArrays()
	{
		for(String S : Config.block.paleMossReplacable)
		{ PaleMossReplacableList.addAll(getBlockStatesFromString(S)); }

		for(String S : Config.block.pollenhead.pollenheadHybridizeList)
		{
			String[] split = S.split("=");

			if (split.length != 2)
			{
				paleBloom.LOGGER.error(S + " is improperly written! Did you use a '=' properly?");
				continue;
			}

			List<IBlockState> states1 = getBlockStatesFromString(split[0]);
			List<IBlockState> states2 = getBlockStatesFromString(split[1]);

			if (states1.isEmpty() || states2.isEmpty())
			{ paleBloom.LOGGER.error((states1.isEmpty() ? split[0]: split[1]) + " is not a proper block!"); }

			else if (blockPollenheadHybridFROM.contains(new ResourceLocation(split[0])))
			{ paleBloom.LOGGER.error(split[0] + " already has a conversion/hybrid! Only the first listed will be used!"); }

			else
			{
				blockPollenheadHybridFROM.addAll(getBlockStatesFromString(split[0]));
				blockPollenheadHybridTO.addAll(Collections.nCopies(states1.size(), states2.get(0)));
			}
		}

		for(String S : Config.block.resinBulb.resinBulbCollectList)
		{
			String[] split = S.split("=");

			if (split.length != 2)
			{
				paleBloom.LOGGER.error(S + " is improperly written! Did you use a '=' properly?");
				continue;
			}

			List<IBlockState> states = getBlockStatesFromString(split[0]);
			if (states.isEmpty())
			{ paleBloom.LOGGER.error(split[0] + " is not a proper block!"); }
			else if (blockResinBulbCollectFROM.contains(new ResourceLocation(split[0].split(":")[0], split[0].split(":")[1])))
			{ paleBloom.LOGGER.error(split[0] + " already has a conversion/hybrid! Only the first listed will be used!"); }
			else
			{
				try
				{
					blockResinBulbCollectFROM.addAll(states);
					blockResinBulbCollectQUANITTY.addAll(Collections.nCopies(states.size(), Integer.valueOf(split[1])));
				}
				catch (NumberFormatException e)
				{
					paleBloom.LOGGER.error("{} has an invalid quantity '{}'", split[0], split[1]);
				}
			}
		}
	}

	/**
	 * Rips up a String to return an IBlockState.
	 *
	 * Returns null if the string cannot be processed!
	 */
	public static List<IBlockState> getBlockStatesFromString(String string)
	{
		List<IBlockState> states = new ArrayList<>();
		String[] ripString = string.split(":");

		if (ripString.length < 2)
		{ return states; }

		Block block = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(ripString[0], ripString[1]));
		Integer meta;

		if(block == null || block == Blocks.AIR)
		{ return states; }
		if(ripString.length > 2)
		{
			if (ripString[2].equals("*")) meta = -1;
			else meta = Integer.parseInt(ripString[2]);

			if(meta == -1) states.addAll(block.getBlockState().getValidStates());
			else states.add(block.getStateFromMeta(meta));
		}
		else states.add(block.getDefaultState());

		return states;
	}


	public static Biome getBiomeFromString(String string)
	{
		String[] ripString = string.split(":");

		if (ripString.length < 2)
		{
			paleBloom.LOGGER.error("Improperly written biome!");
			return null;
		}

		return ForgeRegistries.BIOMES.getValue(new ResourceLocation(ripString[0], ripString[1]));
	}

	/** Adds every biome from the given Biome Type. */
	public static void addBiomesFromType(String string, List list)
	{ list.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.getType(string))); }

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