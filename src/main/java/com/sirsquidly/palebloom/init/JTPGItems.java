package com.sirsquidly.palebloom.init;

import com.sirsquidly.palebloom.Config;
import com.sirsquidly.palebloom.item.*;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = paleBloom.MOD_ID)
public class JTPGItems
{
    private static final int configNightlightBulbEffectLength = Config.item.foods.nightlightBulbEffectLength;
    private static final int configPalePumpkinPieEffectLength = Config.item.foods.palePumpkinPieEffectLength;

    public static final List<Item> itemList = new ArrayList<Item>();
    public static final List<Item> itemModelList = new ArrayList<Item>();

    public static final ItemArmor.ArmorMaterial PALE_MOSS_MAT = EnumHelper.addArmorMaterial("pale_moss", paleBloom.MOD_ID + ":pale_moss_mat", 2, new int[]{1, 1, 2, 1}, 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
    public static final Item.ToolMaterial CULTIVAR_MAT = EnumHelper.addToolMaterial("cultivar_mat", 1, 59, 4.0F, 1.0F, 15);

    public static final Item RESIN_BRICK = new Item().setCreativeTab(CreativeTabs.MATERIALS);
    public static final Item RESIN_CLUMP = new ItemResinClump().setCreativeTab(CreativeTabs.MATERIALS);

    public static final Item AMBER_VALVE = new Item().setCreativeTab(CreativeTabs.MATERIALS);
    public static final Item CULTIVAR_AXE = new ItemPaleAxe(CULTIVAR_MAT);
    public static final Item CULTIVAR_HOE = new ItemPaleHoe(CULTIVAR_MAT);
    public static final Item CULTIVAR_SHOVEL = new ItemPaleShovel(CULTIVAR_MAT);
    public static final Item CULTIVAR_PICKAXE = new ItemPalePickaxe(CULTIVAR_MAT);
    public static final Item CULTIVAR_SWORD = new ItemPaleSword(CULTIVAR_MAT);
    public static final Item LIVE_ROOT = new ItemJTPGFood(2, 0.4F, false, 128).setCreativeTab(CreativeTabs.MATERIALS).setMaxStackSize(1);
    public static final Item MANNEQUIN = new ItemMannequin();
    public static final Item NIGHTLIGHT_BULB = new ItemJTPGFood(4, 0.6F, false).setPotionEffect(new PotionEffect(JTPGPotions.AMBEREYES, configNightlightBulbEffectLength), configNightlightBulbEffectLength > 0 ? 1.0F : 0F);
    public static final Item PALE_OAK_BOAT = new ItemJTPGBoat();
    public static final Item PALE_OAK_SAP = new Item().setCreativeTab(CreativeTabs.MATERIALS);
    public static final Item PALE_CREEPER_HUSK = new Item().setCreativeTab(CreativeTabs.MATERIALS);
    public static final Item PALE_MOSS_CLOAK = new ItemPaleMossCloak(PALE_MOSS_MAT, 0, EntityEquipmentSlot.CHEST);
    public static final Item PALE_PAINTING = new ItemCustomPainting();
    public static final Item PALE_PUMPKIN_PIE = new ItemJTPGFood(8, 0.3F, false).setPotionEffect(new PotionEffect(JTPGPotions.AMBEREYES, configPalePumpkinPieEffectLength), configPalePumpkinPieEffectLength > 0 ? 1.0F : 0F);

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        itemReadyForRegister(RESIN_BRICK, "resin_brick");
        itemReadyForRegister(RESIN_CLUMP, "resin_clump");

        if (Config.item.enableAmberValve) itemReadyForRegister(AMBER_VALVE, "amber_valve");
        itemReadyForRegister(LIVE_ROOT, "live_root");
        itemReadyForRegister(CULTIVAR_AXE, "cultivar_axe");
        itemReadyForRegister(CULTIVAR_HOE, "cultivar_hoe");
        itemReadyForRegister(CULTIVAR_SHOVEL, "cultivar_shovel");
        itemReadyForRegister(CULTIVAR_PICKAXE, "cultivar_pickaxe");
        itemReadyForRegister(CULTIVAR_SWORD, "cultivar_sword");
        itemReadyForRegister(MANNEQUIN, "mannequin", false);
        if (Config.item.foods.enableNightligtBulb) itemReadyForRegister(NIGHTLIGHT_BULB, "nightlight_bulb");
        itemReadyForRegister(PALE_OAK_BOAT, "pale_oak_boat");
        itemReadyForRegister(PALE_OAK_SAP, "pale_oak_sap");
        if (Config.item.enablePaleCreeperHusk) itemReadyForRegister(PALE_CREEPER_HUSK, "pale_creeper_husk");
        if (Config.item.paleMossCloak.enablePaleMossCloak) itemReadyForRegister(PALE_MOSS_CLOAK, "pale_moss_cloak");
        itemReadyForRegister(PALE_PAINTING, "pale_painting");
        if (Config.item.foods.enablePalePumpkinPie) itemReadyForRegister(PALE_PUMPKIN_PIE, "pale_pumpkin_pie");

        PALE_MOSS_MAT.repairMaterial = new ItemStack(JTPGItems.PALE_CREEPER_HUSK, 1);

        for (Item items : itemList) event.getRegistry().register(items);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
    {
        GameRegistry.addSmelting(JTPGItems.RESIN_CLUMP, new ItemStack(JTPGItems.RESIN_BRICK, 1), 0.1F);
        GameRegistry.addSmelting(JTPGBlocks.PALE_OAK_LOG, new ItemStack(Items.COAL, 1, 1), 0.15F);
        GameRegistry.addSmelting(JTPGBlocks.PEEPING_BIRCH_LOG, new ItemStack(Items.COAL, 1, 1), 0.15F);

        initOreDict();
    }

    public static void initOreDict()
    {
        OreDictionary.registerOre("logWood", JTPGBlocks.PALE_OAK_LOG);
        OreDictionary.registerOre("plankWood", JTPGBlocks.PALE_OAK_PLANKS);
        OreDictionary.registerOre("slabWood", JTPGBlocks.PALE_OAK_SLAB);
        OreDictionary.registerOre("stairWood", JTPGBlocks.PALE_OAK_STAIRS);
        OreDictionary.registerOre("fenceWood", JTPGBlocks.PALE_OAK_FENCE);
        OreDictionary.registerOre("fenceGateWood", JTPGBlocks.PALE_OAK_FENCE_GATE);
        OreDictionary.registerOre("treeSapling", JTPGBlocks.PALE_SAPLING);
        OreDictionary.registerOre("treeLeaves", JTPGBlocks.PALE_OAK_LEAVES);

        OreDictionary.registerOre("logWood", JTPGBlocks.PEEPING_BIRCH_LOG);
        OreDictionary.registerOre("paper", JTPGItems.PALE_CREEPER_HUSK);
    }

    /** If blocks don't specify the 'addToTab' boolean, assume true.*/
    public static Item itemReadyForRegister(Item item, String name)
    { return itemReadyForRegister(item, name, true);}

    public static Item itemReadyForRegister(Item item, String name, Boolean addNormalModel)
    {
        if (name != null)
        {
            item.setTranslationKey(paleBloom.MOD_ID + "." + name);
            item.setRegistryName(name);
        }
        itemList.add(item);
        if (addNormalModel) itemModelList.add(item);
        return item;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event)
    {
        for (Item items : itemModelList) paleBloom.proxy.registerItemRenderer(items, 0, "inventory");

        for (int i = 0; i <= 1; i++)
        { ModelLoader.setCustomModelResourceLocation(JTPGItems.MANNEQUIN, i, new ModelResourceLocation(new ResourceLocation(paleBloom.MOD_ID, "mannequin_" + i), "inventory")); }
    }
}