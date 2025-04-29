package com.sirsquidly.creaturesfromdarkness.init;

import com.sirsquidly.creaturesfromdarkness.creaturesfromdarkness;
import com.sirsquidly.creaturesfromdarkness.item.ItemNightmareLeg;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = creaturesfromdarkness.MOD_ID)
public class CFDItems
{
    public static final List<Item> itemList = new ArrayList<Item>();
    public static Item NIGHTMARE_LEG = new ItemNightmareLeg();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        itemReadyForRegister(NIGHTMARE_LEG, "dream_spine");

        for (Item items : itemList) event.getRegistry().register(items);
    }

    public static Item itemReadyForRegister(Item item, String name)
    {
        if (name != null)
        {
            item.setTranslationKey(creaturesfromdarkness.MOD_ID + "." + name);
            item.setRegistryName(name);
        }

        itemList.add(item);

        return item;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event)
    { for (Item items : itemList) creaturesfromdarkness.proxy.registerItemRenderer(items, 0, "inventory"); }
}