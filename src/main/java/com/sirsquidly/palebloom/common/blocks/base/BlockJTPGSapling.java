package com.sirsquidly.palebloom.common.blocks.base;

import com.sirsquidly.palebloom.common.world.feature.GeneratorBloomingPaleOakTree;
import com.sirsquidly.palebloom.common.world.feature.GeneratorPaleOakTree;
import com.sirsquidly.palebloom.common.world.feature.GeneratorPeepingBirch;
import com.sirsquidly.palebloom.config.ConfigCache;
import net.minecraft.block.*;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

import java.util.Random;

public class BlockJTPGSapling extends BlockBush implements IGrowable
{
    public static final PropertyEnum<BlockJTPGSapling.EnumType> TYPE = PropertyEnum.create("type", BlockJTPGSapling.EnumType.class);
    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
    protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    public BlockJTPGSapling()
    {
        setSoundType(SoundType.PLANT);
        setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumType.PALE_OAK).withProperty(STAGE, 0));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return SAPLING_AABB;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            super.updateTick(worldIn, pos, state, rand);

            if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
            {
                this.grow(worldIn, pos, state, rand);
            }
        }
    }

    public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (state.getValue(STAGE) == 0)
        { worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4); }
        else
        { this.generateTree(worldIn, pos, state, rand); }
    }

    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;
        WorldGenerator worldgenerator = new GeneratorPaleOakTree((float) (ConfigCache.palOakSpl_creakingHeartChance * 0.01F), (float) (ConfigCache.palOakSpl_naturalCreakingHeartChance * 0.01F));

        int i = 0;
        int j = 0;
        boolean flag = false;

        switch (state.getValue(TYPE))
        {
            case PALE_OAK:
                for (i = 0; i >= -1; --i)
                {
                    for (j = 0; j >= -1; --j)
                    {
                        if (this.isTwoByTwoOfType(worldIn, pos, i, j, EnumType.PALE_OAK))
                        {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) break;
                }
                if (!flag) return;
                break;
            case BLOOMING_PALE_OAK:
                for (i = 0; i >= -1; --i)
                {
                    for (j = 0; j >= -1; --j)
                    {
                        if (this.isTwoByTwoOfType(worldIn, pos, i, j, EnumType.BLOOMING_PALE_OAK))
                        {
                            worldgenerator = new GeneratorBloomingPaleOakTree((float) (ConfigCache.blmPalOakSpl_creakingHeartChance * 0.01F), (float) (ConfigCache.blmPalOakSpl_naturalCreakingHeartChance * 0.01F));
                            flag = true;
                            break;
                        }
                    }
                    if (flag) break;
                }
                if (!flag) return;
                break;
            case PEEPING_BIRCH:
                worldgenerator = new GeneratorPeepingBirch();
                break;
        }
        IBlockState iblockstate2 = Blocks.AIR.getDefaultState();

        if (flag)
        {
            worldIn.setBlockState(pos.add(i, 0, j), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i, 0, j + 1), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j + 1), iblockstate2, 4);
        }
        else
        { worldIn.setBlockState(pos, iblockstate2, 4); }

        /* Runs the tree generator, and if it fails, replaces the removed Saplings. */
        if (!worldgenerator.generate(worldIn, rand, pos.add(i, 0, j)))
        {
            if (flag)
            {
                worldIn.setBlockState(pos.add(i, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i, 0, j + 1), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j + 1), state, 4);
            }
            else
            { worldIn.setBlockState(pos, state, 4); }
        }
    }

    private boolean isTwoByTwoOfType(World worldIn, BlockPos pos, int offX, int offZ, EnumType type)
    {
        return isTypeAt(worldIn, pos.add(offX, 0, offZ), type) && isTypeAt(worldIn, pos.add(offX + 1, 0, offZ), type) && isTypeAt(worldIn, pos.add(offX, 0, offZ + 1), type) && isTypeAt(worldIn, pos.add(offX + 1, 0, offZ + 1), type);
    }

    public boolean isTypeAt(World worldIn, BlockPos pos, BlockJTPGSapling.EnumType type)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock() == this && iblockstate.getValue(TYPE) == type;
    }

    public int damageDropped(IBlockState state)
    { return state.getValue(TYPE).getMetadata(); }

    /** Cut the sub-blocks from the base Sapling class. */
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        for (BlockJTPGSapling.EnumType blockplanks$enumtype : BlockJTPGSapling.EnumType.values())
        { items.add(new ItemStack(this, 1, blockplanks$enumtype.getMetadata())); }
    }

    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return true;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    { return (double)worldIn.rand.nextFloat() < 0.45D; }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    { this.grow(worldIn, pos, state, rand); }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TYPE, BlockJTPGSapling.EnumType.byMetadata(meta & 7)).withProperty(STAGE, Integer.valueOf((meta & 8) >> 3));
    }

    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(TYPE).getMetadata();
        i = i | state.getValue(STAGE).intValue() << 3;
        return i;
    }

    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, TYPE, STAGE); }

    /** Determines the type of Sapling. */
    public static enum EnumType implements IStringSerializable
    {
        PALE_OAK(0, "pale_oak", MapColor.SILVER),
        BLOOMING_PALE_OAK(1, "blooming_pale_oak", MapColor.QUARTZ),
        PEEPING_BIRCH(2, "peeping_birch", MapColor.SAND);

        private static final BlockJTPGSapling.EnumType[] META_LOOKUP = new BlockJTPGSapling.EnumType[values().length];
        private final int meta;
        private final String name;
        private final String translationKey;
        private final MapColor mapColor;

        private EnumType(int metaIn, String nameIn, MapColor mapColorIn)
        { this(metaIn, nameIn, nameIn, mapColorIn); }

        private EnumType(int metaIn, String nameIn, String unlocalizedNameIn, MapColor mapColorIn)
        {
            this.meta = metaIn;
            this.name = nameIn;
            this.translationKey = unlocalizedNameIn;
            this.mapColor = mapColorIn;
        }

        public int getMetadata()
        { return this.meta; }

        public MapColor getMapColor()
        { return this.mapColor; }

        public String toString()
        { return this.name; }

        public static BlockJTPGSapling.EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName()
        { return this.name; }

        public String getTranslationKey()
        { return this.translationKey; }

        static
        {
            for (BlockJTPGSapling.EnumType type : values())
            { META_LOOKUP[type.getMetadata()] = type; }
        }
    }
}