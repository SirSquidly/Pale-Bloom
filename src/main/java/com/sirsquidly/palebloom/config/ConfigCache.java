package com.sirsquidly.palebloom.config;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;

import java.util.List;

/**
 * 	This is simply a static form of the config, for reference throughout the mod.
 */
public class ConfigCache
{
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
}