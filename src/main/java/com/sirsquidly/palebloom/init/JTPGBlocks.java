package com.sirsquidly.palebloom.init;

import com.google.common.collect.Maps;
import com.sirsquidly.palebloom.common.blocks.*;
import com.sirsquidly.palebloom.common.blocks.base.*;
import com.sirsquidly.palebloom.config.Config;
import com.sirsquidly.palebloom.common.item.itemblock.ItemBlockMetaAware;
import com.sirsquidly.palebloom.common.item.itemblock.ItemBlockStacking;
import com.sirsquidly.palebloom.common.item.itemblock.ItemIncenseBush;
import com.sirsquidly.palebloom.common.item.itemblock.ItemPalePumpkin;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.block.*;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = paleBloom.MOD_ID)
public class JTPGBlocks
{
    /** Contains every block ran through the 'blockReadyForRegister' function. So they all SHOULD be registered. */
    private static List<Block> blockList = new ArrayList<Block>();
    /** Records any blocks that have a unique itemBlock assigned, as the rest are automatically slapped with the default itemBlock.*/
    private static List<Block> itemBlockBlacklist = new ArrayList<Block>();
    /** Any blocks that should skip registering a normal Item Model. */
    private static List<Block> itemModelBlacklist = new ArrayList<Block>();
    /** Used for items settup here, and need to have models registered as such. */
    private static List<Item> blockDirectItemList = new ArrayList<Item>();

    public static Block CREAKING_HEART = new BlockCreakingHeart(Material.WOOD, MapColor.QUARTZ).setHardness(10.0F).setResistance(10.0F);

    public static BlockBush EYEBLOSSOM_CLOSED = new BlockEyeblossom(false);
    public static BlockBush EYEBLOSSOM_OPEN = new BlockEyeblossom(true);

    public static Block PALE_OAK_LEAVES = new BlockPaleOakLeaves().setHardness(0.2F).setResistance(0.2F);
    public static Block PALE_OAK_LOG = new BlockJTPGLog();
    public static Block PALE_SAPLING = new BlockJTPGSapling();

    public static Block PALE_OAK_DOOR = new BlockJTPGDoor().setHardness(2.0F).setResistance(5.0F);
    public static Block PALE_OAK_PLANKS = new BlockJTPGBlock(Material.WOOD, MapColor.QUARTZ, SoundType.WOOD).setHardness(2.0F).setResistance(5.0F);
    public static BlockSlab PALE_OAK_SLAB = new BlockJTPGSlab(Material.WOOD, SoundType.WOOD, 2.0F, 5.0F);
    public static BlockSlab PALE_OAK_SLAB_D = new BlockJTPGSlabDouble(PALE_OAK_SLAB, Material.WOOD, SoundType.WOOD, 2.0F, 5.0F);
    public static Block PALE_OAK_STAIRS = new BlockJTPGStairs(JTPGBlocks.PALE_OAK_PLANKS.getDefaultState(), SoundType.WOOD).setHardness(2.0F).setResistance(5.0F);
    public static Block PALE_OAK_FENCE = new BlockJTPGFence(Material.WOOD, MapColor.QUARTZ, SoundType.WOOD).setHardness(2.0F).setResistance(5.0F);
    public static Block PALE_OAK_FENCE_GATE = new BlockJTPGFenceGate(BlockPlanks.EnumType.DARK_OAK, SoundType.WOOD).setHardness(2.0F).setResistance(5.0F);
    public static Block PALE_OAK_TRAPDOOR = new BlockJTPGTrapDoor().setHardness(2.0F).setResistance(5.0F);

    public static Block PALE_HANGING_MOSS = new BlockPaleHangingMoss();
    public static Block PALE_MOSS = new BlockPaleMossBlock().setHardness(0.1F);
    public static Block PALE_MOSS_CARPET = new BlockPaleMossCarpet().setHardness(0.1F);

    public static final Block[] RESIN_CLUMPS = {new BlockResinClump(0), new BlockResinClump(1), new BlockResinClump(2), new BlockResinClump(3)};

    public static Block RESIN_BLOCK = new BlockJTPGBlock(Material.GROUND, MapColor.ORANGE_STAINED_HARDENED_CLAY, JTPGSounds.RESIN_BRICK);
    public static Block RESIN_BRICKS = new BlockJTPGBlock(Material.GROUND, MapColor.ORANGE_STAINED_HARDENED_CLAY, JTPGSounds.RESIN_BRICK).setHardness(1.5F).setResistance(6.0F);
    public static BlockSlab RESIN_BRICKS_SLAB = new BlockJTPGSlab(Material.GROUND, JTPGSounds.RESIN_BRICK, 1.5F, 6.0F);
    public static BlockSlab RESIN_BRICKS_SLAB_D = new BlockJTPGSlabDouble(PALE_OAK_SLAB, Material.GROUND, JTPGSounds.RESIN_BRICK, 1.5F, 6.0F);
    public static Block RESIN_BRICKS_STAIRS = new BlockJTPGStairs(JTPGBlocks.RESIN_BRICKS.getDefaultState(), JTPGSounds.RESIN_BRICK).setHardness(1.5F).setResistance(6.0F);
    public static Block RESIN_BRICKS_WALL = new BlockJTPGWall(JTPGBlocks.RESIN_BRICKS).setHardness(1.5F).setResistance(6.0F);
    public static Block RESIN_BRICKS_CHISELED = new BlockJTPGBlock(Material.GROUND, MapColor.ORANGE_STAINED_HARDENED_CLAY, JTPGSounds.RESIN_BRICK).setHardness(1.5F).setResistance(6.0F);


    public static BlockBush BRAMBLE = (BlockBush) new BlockBramble().setHardness(0.1F);
    public static Block BLOOMING_PALE_OAK_LEAVES = new BlockPaleOakLeaves().setHardness(0.2F).setResistance(0.2F);
    public static Block CREAKING_LANTERN = new BlockCreakingLantern().setHardness(1.0F).setResistance(1.0F);
    public static Block HYDRAWEED_BODY = new BlockHydraweedBody();
    public static Block INCENSE_THORNS = new BlockIncenseThorn().setHardness(0.4F);
    public static Block SEED_BOMB = new BlockSeedBomb().setHardness(0.4F);
    public static Block SUCKER_ROOTS = new BlockSuckerRoots().setHardness(0.4F);
    public static Block SUCKER_ROOT_NODULE = new BlockSuckerRootNodule().setHardness(0.4F);
    public static Block RESIN_BULB = new BlockResinBulb(Material.GROUND, MapColor.ORANGE_STAINED_HARDENED_CLAY, JTPGSounds.RESIN);
    public static Block REAPING_WILLOW_SAPLING = new BlockReapingWillowSapling().setHardness(0.4F);
    public static BlockBush PALE_DOUBLE_PLANT = new BlockDoublePalePlant();
    public static BlockBush PALE_PETALS = new BlockPalePetals();
    public static Block PALE_OAK_HOLLOW = new BlockPaleOakHollow();
    public static Block PEEPING_BIRCH_LOG = new BlockJTPGLog();
    public static Block POLLENHEAD = new BlockPollenhead();
    public static Block NIGHTLIGHT = new BlockNightlight().setHardness(0.4F);

    public static Block PALE_PUMPKIN = new BlockJTPGBlock(Material.GOURD, MapColor.WHITE_STAINED_HARDENED_CLAY, SoundType.WOOD).setHardness(1.0F).setResistance(1.0F);
    public static Block PALE_CARVED_PUMPKIN = new BlockJTPGPumpkin().setHardness(1.0F).setResistance(1.0F);
    public static Block PALE_JACK_O_LANTERN = new BlockJTPGPumpkin().setLightLevel(1.0F).setHardness(1.0F).setResistance(1.0F);
    public static Block PALE_SOUL_JACK_O_LANTERN = new BlockJTPGPumpkin().setLightLevel(0.7F).setHardness(1.0F).setResistance(1.0F);


    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        if (Config.block.awakenedFloraBlocks.creakingHeart.enableCreakingHeart) blockReadyForRegister(CREAKING_HEART, "creaking_heart");

        blockReadyForRegister(EYEBLOSSOM_CLOSED, "eyeblossom_closed");
        blockReadyForRegister(EYEBLOSSOM_OPEN, "eyeblossom_open");

        blockReadyForRegister(PALE_OAK_LEAVES, "pale_oak_leaves");
        blockReadyForRegister(PALE_OAK_LOG, "pale_oak_log");
        blockReadyForRegister(PALE_SAPLING, "pale_sapling");

        blockReadyForRegister(PALE_OAK_DOOR, "pale_oak_door");
        blockReadyForRegister(PALE_OAK_PLANKS, "pale_oak_planks");
        blockReadyForRegister(PALE_OAK_SLAB, "pale_oak_slab");
        blockReadyForRegister(PALE_OAK_SLAB_D, "pale_oak_slab_double");
        blockReadyForRegister(PALE_OAK_STAIRS, "pale_oak_stairs");
        blockReadyForRegister(PALE_OAK_FENCE, "pale_oak_fence");
        blockReadyForRegister(PALE_OAK_FENCE_GATE, "pale_oak_fence_gate");
        blockReadyForRegister(PALE_OAK_TRAPDOOR, "pale_oak_trapdoor");

        blockReadyForRegister(PALE_HANGING_MOSS, "pale_hanging_moss");
        blockReadyForRegister(PALE_MOSS, "pale_moss_block");
        blockReadyForRegister(PALE_MOSS_CARPET, "pale_moss_carpet");

        for (Block block : RESIN_CLUMPS)  blockReadyForRegister(block, "resin_clump_" + ((BlockResinClump)block).getOrdinal());

        blockReadyForRegister(RESIN_BLOCK, "resin_block");
        blockReadyForRegister(RESIN_BRICKS, "resin_bricks");
        blockReadyForRegister(RESIN_BRICKS_SLAB, "resin_brick_slab");
        blockReadyForRegister(RESIN_BRICKS_SLAB_D, "resin_brick_slab_double");
        blockReadyForRegister(RESIN_BRICKS_STAIRS, "resin_brick_stairs");
        blockReadyForRegister(RESIN_BRICKS_WALL, "resin_brick_wall");
        blockReadyForRegister(RESIN_BRICKS_CHISELED, "resin_brick_chiseled");

        blockReadyForRegister(BRAMBLE, "bramble");
        blockReadyForRegister(BLOOMING_PALE_OAK_LEAVES, "blooming_pale_oak_leaves");
        blockReadyForRegister(CREAKING_LANTERN, "creaking_lantern");
        blockReadyForRegister(HYDRAWEED_BODY, "hydraweed_body");
        if (Config.block.awakenedFloraBlocks.incenseThorns.enableIncenseThorns) blockReadyForRegister(INCENSE_THORNS, "incense_thorns");
        blockReadyForRegister(PALE_DOUBLE_PLANT, "pale_plant_double");
        blockReadyForRegister(NIGHTLIGHT, "nightlight");
        blockReadyForRegister(PALE_PETALS, "pale_petals");
        if (Config.block.awakenedFloraBlocks.paleOakHollow.enablePaleOakHollow) blockReadyForRegister(PALE_OAK_HOLLOW, "pale_oak_hollow");
        blockReadyForRegister(PALE_PUMPKIN, "pale_pumpkin");
        blockReadyForRegister(PALE_CARVED_PUMPKIN, "pale_carved_pumpkin");
        blockReadyForRegister(PALE_JACK_O_LANTERN, "pale_jack_o_lantern");
        blockReadyForRegister(PALE_SOUL_JACK_O_LANTERN, "pale_soul_jack_o_lantern");
        blockReadyForRegister(PEEPING_BIRCH_LOG, "peeping_birch_log");
        if (Config.block.awakenedFloraBlocks.pollenhead.enablePollenhead) blockReadyForRegister(POLLENHEAD, "pollenhead");
        blockReadyForRegister(REAPING_WILLOW_SAPLING, "reaping_willow_sapling");
        if (Config.block.awakenedFloraBlocks.resinBulb.enableResinBulb) blockReadyForRegister(RESIN_BULB, "resin_bulb");
        blockReadyForRegister(SEED_BOMB, "seed_bomb");
        blockReadyForRegister(SUCKER_ROOTS, "sucker_roots");
        blockReadyForRegister(SUCKER_ROOT_NODULE, "sucker_root_nodule");

        setupFireInfo();

        //Main.logger.info("Oceanic Expanse Blocks are Registering!");
        for (Block blocks : blockList) event.getRegistry().register(blocks);
        //Main.logger.info("Oceanic Expanse Blocks are Registered!");
    }

    /** Sets the Fire Info for all the blocks. */
    public static void setupFireInfo()
    {
        Blocks.FIRE.setFireInfo(BLOOMING_PALE_OAK_LEAVES, 5, 100);
        Blocks.FIRE.setFireInfo(PALE_HANGING_MOSS, 5, 100);
        Blocks.FIRE.setFireInfo(PALE_MOSS, 5, 100);
        Blocks.FIRE.setFireInfo(PALE_MOSS_CARPET, 5, 100);
        Blocks.FIRE.setFireInfo(PALE_OAK_FENCE, 5, 20);
        Blocks.FIRE.setFireInfo(PALE_OAK_FENCE_GATE, 5, 20);
        Blocks.FIRE.setFireInfo(PALE_OAK_LEAVES, 30, 60);
        Blocks.FIRE.setFireInfo(PALE_OAK_LOG, 5, 5);
        Blocks.FIRE.setFireInfo(PALE_OAK_PLANKS, 5, 20);
        Blocks.FIRE.setFireInfo(PALE_OAK_SLAB, 5, 20);
        Blocks.FIRE.setFireInfo(PALE_OAK_SLAB_D, 5, 20);
        Blocks.FIRE.setFireInfo(PALE_OAK_STAIRS, 5, 20);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> r = event.getRegistry();

        registerDoorItem(r, new ItemDoor(PALE_OAK_DOOR), PALE_OAK_DOOR); itemBlockBlacklist.add(PALE_OAK_DOOR);
        event.getRegistry().register(new ItemSlab(PALE_OAK_SLAB, PALE_OAK_SLAB, PALE_OAK_SLAB_D).setRegistryName(PALE_OAK_SLAB.getRegistryName())); itemBlockBlacklist.add(PALE_OAK_SLAB);
        event.getRegistry().register(new ItemSlab(RESIN_BRICKS_SLAB, RESIN_BRICKS_SLAB, RESIN_BRICKS_SLAB_D).setRegistryName(RESIN_BRICKS_SLAB.getRegistryName())); itemBlockBlacklist.add(RESIN_BRICKS_SLAB);

        /* Attaches 'hasSubtypes' to the ItemBlock, for proper rendering. */
        event.getRegistry().register(new ItemBlockMetaAware(RESIN_BULB)); itemBlockBlacklist.add(RESIN_BULB);
        event.getRegistry().register(new ItemBlockMetaAware(PALE_SAPLING)); itemBlockBlacklist.add(PALE_SAPLING); itemModelBlacklist.add(PALE_SAPLING);
        event.getRegistry().register(new ItemBlockMetaAware(PALE_DOUBLE_PLANT)); itemBlockBlacklist.add(PALE_DOUBLE_PLANT); itemModelBlacklist.add(PALE_DOUBLE_PLANT);

        event.getRegistry().register(new ItemBlockStacking(PALE_PETALS)); itemBlockBlacklist.add(PALE_PETALS);
        event.getRegistry().register(new ItemBlockStacking(SUCKER_ROOTS)); itemBlockBlacklist.add(SUCKER_ROOTS);

        event.getRegistry().register(new ItemIncenseBush(INCENSE_THORNS)); itemBlockBlacklist.add(INCENSE_THORNS);

        event.getRegistry().register(new ItemPalePumpkin(PALE_CARVED_PUMPKIN)); itemBlockBlacklist.add(PALE_CARVED_PUMPKIN);
        event.getRegistry().register(new ItemPalePumpkin(CREAKING_LANTERN)); itemBlockBlacklist.add(CREAKING_LANTERN);


        /** As stated on itemBlockBlacklist, this registers anything NOT from the blacklist with a generic itemBlock.*/
        for (Block blocks : blockList) if (!(itemBlockBlacklist.contains(blocks))) { registerItemBlock(r, blocks);}
    }


    /** If blocks don't specify the 'addToTab' boolean, assume true.*/
    public static Block blockReadyForRegister(Block block, String name)
    { return blockReadyForRegister(block, name, true);}

    /** Slaps the names to Blocks, and adds them to the blockList to be registered in 'registerBlocks'.*/
    public static Block blockReadyForRegister(Block block, String name, Boolean addNormalModel)
    {
        block.setTranslationKey(paleBloom.MOD_ID + "." + name);
        block.setRegistryName(name);

        // if (addToTab) block.setCreativeTab(Main.OCEANEXPTAB);
        // else block.setCreativeTab(null);

        blockList.add(block);

        return block;
    }

    public static ItemDoor registerDoorItem(IForgeRegistry<Item> r, ItemDoor itemDoor, Block block)
    {
        itemDoor.setTranslationKey(block.getTranslationKey());
        itemDoor.setRegistryName(block.getRegistryName());
        //itemDoor.setCreativeTab(Main.OCEANEXPTAB);

        r.register(itemDoor);
        ((BlockJTPGDoor) block).setItem(itemDoor);

        blockDirectItemList.add(itemDoor);

        return itemDoor;
    }

    public static ItemBlock registerItemBlock(IForgeRegistry<Item> registry, Block block)
    { return registerItemBlock(registry, new ItemBlock(block)); }

    public static <T extends ItemBlock> T registerItemBlock(IForgeRegistry<Item> registry, T item) {
        Block block = item.getBlock();
        item.setTranslationKey(block.getTranslationKey());
        item.setRegistryName(block.getRegistryName());

        registry.register(item);

        return item;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event)
    {
        for(Item i : blockDirectItemList)
        { ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory")); }

        for(Block b : blockList)
        { if (!(itemModelBlacklist.contains(b))) ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, new ModelResourceLocation(b.getRegistryName(), "inventory")); }

        for (int i = 0; i <= 3; i++)
        { ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(JTPGBlocks.RESIN_BULB), i, new ModelResourceLocation(new ResourceLocation(paleBloom.MOD_ID, "resin_bulb_" + i), "inventory")); }

        for (int i = 0; i <= BlockJTPGSapling.EnumType.values().length; i++)
        { ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(JTPGBlocks.PALE_SAPLING), i, new ModelResourceLocation(new ResourceLocation(paleBloom.MOD_ID, BlockJTPGSapling.EnumType.byMetadata(i).getName() + "_sapling"), "inventory")); }

        for (int i = 0; i <= BlockDoublePalePlant.EnumType.values().length; i++)
        { ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(JTPGBlocks.PALE_DOUBLE_PLANT), i, new ModelResourceLocation(new ResourceLocation(paleBloom.MOD_ID, BlockDoublePalePlant.EnumType.byMetadata(i).getName()), "inventory")); }

        /* Specialized Model loading! */
        ModelLoader.setCustomStateMapper(JTPGBlocks.PALE_OAK_DOOR, new StateMap.Builder().ignore(BlockDoor.POWERED).build());
        ModelLoader.setCustomStateMapper(JTPGBlocks.PALE_OAK_LEAVES, new StateMap.Builder().ignore(BlockLeaves.CHECK_DECAY).ignore(BlockLeaves.DECAYABLE).build());
        ModelLoader.setCustomStateMapper(JTPGBlocks.PALE_OAK_SLAB, new StateMap.Builder().ignore(BlockJTPGSlab.VARIANT).build());
        ModelLoader.setCustomStateMapper(JTPGBlocks.PALE_OAK_SLAB_D, new StateMap.Builder().ignore(BlockJTPGSlab.VARIANT).build());
        ModelLoader.setCustomStateMapper(JTPGBlocks.PALE_OAK_FENCE_GATE, new StateMap.Builder().ignore(BlockFenceGate.POWERED).build());
        ModelLoader.setCustomStateMapper(JTPGBlocks.BLOOMING_PALE_OAK_LEAVES, new StateMap.Builder().ignore(BlockLeaves.CHECK_DECAY).ignore(BlockLeaves.DECAYABLE).build());

        for (Block block : RESIN_CLUMPS) ModelLoader.setCustomStateMapper(block, sharedResinClumpMapper());
        ModelLoader.setCustomStateMapper(JTPGBlocks.RESIN_BRICKS_SLAB, new StateMap.Builder().ignore(BlockJTPGSlab.VARIANT).build());
        ModelLoader.setCustomStateMapper(JTPGBlocks.RESIN_BRICKS_SLAB_D, new StateMap.Builder().ignore(BlockJTPGSlab.VARIANT).build());

        //ModelLoader.setCustomStateMapper(LSBlocks.PALE_OAK_DOOR, new StateMap.Builder().ignore(BlockDoor.POWERED).build());
    }


    /** A custom StateMap used by Resin Clumps, adapted from the one in Deeper Depths. */
    @SideOnly(Side.CLIENT)
    private static StateMapperBase sharedResinClumpMapper()
    {
        return new StateMapperBase()
        {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state)
            {
                Map<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap(state.getProperties());
                map.remove(BlockResinClump.META);
                return new ModelResourceLocation( paleBloom.MOD_ID + ":resin_clump", getPropertyString(map));
            }
        };
    }
}