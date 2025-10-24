package com.sirsquidly.palebloom.common.world.biome;

import com.sirsquidly.palebloom.common.world.feature.*;
import com.sirsquidly.palebloom.config.Config;
import com.sirsquidly.palebloom.config.ConfigCache;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeForest;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BiomePaleGarden extends BiomeForest
{
    public final int mossPerChunk = 10;
    public final WorldGenerator mossGen = new WorldGenMoss(7, 1.0F);
    public final int eyeblossomPerChunk = 1;
    public final WorldGenerator eyeblossomGen = new WorldGenEyeblossom();
    public final int paleShrubPerChunk = 1;
    public final WorldGenerator paleShrubGen = new WorldGenPaleBush();
    public final int doublePalePerChunk = 1;
    public final WorldGenDoublePalePlant doublePaleGen = new WorldGenDoublePalePlant();
    public final int bramblePerChunk = 1;
    public final WorldGenBush brambleGen = new WorldGenBush(JTPGBlocks.BRAMBLE);

    private static final int configDarkOakChance = Config.paleGarden.treeGen.darkOakChance;
    private static final double configDarkOakHeartChance = Config.paleGarden.treeGen.paleOakTree.creakingHeartChance;
    private static final double configPaleOakDyingChance = Config.paleGarden.treeGen.paleOakTree.dyingTreeChance;
    private static final double configDarkOakNautralHeartChance = Config.paleGarden.treeGen.paleOakTree.naturalCreakingHeartChance;
    private static final int configBrambleChance = Config.paleGarden.understoryGen.brambleChance;
    private static final int configDoublePalePlantChance = Config.paleGarden.understoryGen.doublePalePlantChance;
    private static final int configEyeblossomPatchChance = Config.paleGarden.understoryGen.eyeblossomChance;
    private static final int configShrubChance = Config.paleGarden.understoryGen.shrubChance;

    protected static final GeneratorPaleOakTree PALE_OAK_TREE = new GeneratorPaleOakTree((float) (configDarkOakHeartChance * 0.01F), (float) (configDarkOakNautralHeartChance * 0.01F), (float) (configPaleOakDyingChance * 0.01F));
    protected static final WorldGenCanopyTree DARK_OAK_TREE = new WorldGenCanopyTree(false);

    /* Wazzup yo */
    public BiomePaleGarden()
    {
        super(BiomeForest.Type.ROOFED, (new Biome.BiomeProperties("Pale Garden")).setBaseBiome("roofed_forest").setBaseHeight(0.2F).setHeightVariation(0.4F).setTemperature(0.7F).setRainfall(0.8F).setWaterColor(7768221));
        this.decorator.treesPerChunk = -999;
        this.decorator.flowersPerChunk = -999;

        /* Removes the spawning of passive animals. */
        this.spawnableCreatureList.clear();
        this.flowers.clear();
        this.addFlower(JTPGBlocks.EYEBLOSSOM_CLOSED.getDefaultState(), 10);
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand)
    { return configDarkOakChance != 0 && rand.nextInt(configDarkOakChance) == 0 || configDarkOakChance == 1 ? DARK_OAK_TREE : PALE_OAK_TREE; }

    @Override
    /** This override helps by disabling Mushroom and Tall Plant generation that gets inherited. */
    public void decorate(World worldIn, Random rand, BlockPos pos)
    {
        this.placeTrees(worldIn, rand, pos);

        if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, rand, new net.minecraft.util.math.ChunkPos(pos), net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.FLOWERS))
        {
            if (ConfigCache.eyeblm_enabled && configEyeblossomPatchChance != 0)
            {
                for (int j1 = 0; j1 < this.eyeblossomPerChunk; ++j1)
                {
                    if (rand.nextInt(configEyeblossomPatchChance) != 0) continue;
                    int j = rand.nextInt(16) + 8;
                    int k = rand.nextInt(16) + 8;
                    this.eyeblossomGen.generate(worldIn, rand, worldIn.getTopSolidOrLiquidBlock(pos.add(j, 0, k)));
                }
            }

            if (ConfigCache.brmbl_enabled && configBrambleChance != 0)
            {
                for (int l1 = 0; l1 < this.bramblePerChunk; ++l1)
                {
                    if (rand.nextInt(configBrambleChance) != 0) continue;
                    int j = rand.nextInt(16) + 8;
                    int k = rand.nextInt(16) + 8;
                    this.brambleGen.generate(worldIn, rand, worldIn.getTopSolidOrLiquidBlock(pos.add(j, 0, k)));
                }
            }

            if (ConfigCache.dblPalPnt_enabled && configDoublePalePlantChance != 0)
            {
                for (int l1 = 0; l1 < this.doublePalePerChunk; ++l1)
                {
                    if (rand.nextInt(configDoublePalePlantChance) != 0) continue;
                    this.doublePaleGen.setPalePlantType(worldIn.rand.nextInt(3));
                    int j = rand.nextInt(16) + 8;
                    int k = rand.nextInt(16) + 8;
                    this.doublePaleGen.generate(worldIn, rand, worldIn.getTopSolidOrLiquidBlock(pos.add(j, 0, k)));
                }
            }

            if (configShrubChance != 0)
            {
                for (int k1 = 0; k1 < this.paleShrubPerChunk; ++k1)
                {
                    if (rand.nextInt(configShrubChance) != 0) continue;
                    int j = rand.nextInt(16) + 8;
                    int k = rand.nextInt(16) + 8;
                    this.paleShrubGen.generate(worldIn, rand, worldIn.getTopSolidOrLiquidBlock(pos.add(j, 0, k)));
                }
            }
        }


        /* Adds Pale Moss Patches. These include the additional Tall Grass, Double Grass, and Pale Moss Carpets. */
        for (int i = 0; i < mossPerChunk; ++i)
        {
            int j = rand.nextInt(16) + 8;
            int k = rand.nextInt(16) + 8;

            this.mossGen.generate(worldIn, rand, worldIn.getTopSolidOrLiquidBlock(pos.add(j, 0, k)).down());
        }

        this.decorator.decorate(worldIn, rand, this, pos);
    }

    /** Roofed Forests use a custom tree placer, like this. */
    public void placeTrees(World worldIn, Random rand, BlockPos pos)
    {
        for (int i = 0; i < 4; ++i)
        {
            for (int j = 0; j < 4; ++j)
            {
                int k = i * 4 + 1 + 8 + rand.nextInt(3);
                int l = j * 4 + 1 + 8 + rand.nextInt(3);
                BlockPos blockpos = worldIn.getHeight(pos.add(k, 0, l));

                if (net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, rand, new net.minecraft.util.math.ChunkPos(pos), blockpos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.TREE))
                {
                    WorldGenAbstractTree worldgenabstracttree = this.getRandomTreeFeature(rand);
                    worldgenabstracttree.setDecorationDefaults();

                    if (worldgenabstracttree.generate(worldIn, rand, blockpos))
                    { worldgenabstracttree.generateSaplings(worldIn, rand, blockpos); }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) { return 12171705; }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) { return getModdedBiomeGrassColor(7832178); }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos)
    {
        return getModdedBiomeFoliageColor(8883574);
    }
}
