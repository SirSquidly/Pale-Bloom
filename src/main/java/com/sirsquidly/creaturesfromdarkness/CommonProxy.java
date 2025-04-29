package com.sirsquidly.creaturesfromdarkness;

import com.sirsquidly.creaturesfromdarkness.capabilities.CapabilityNightmare;
import com.sirsquidly.creaturesfromdarkness.init.CFDEntities;
import com.sirsquidly.creaturesfromdarkness.init.CFDLootTables;
import com.sirsquidly.creaturesfromdarkness.init.CFDSounds;
import com.sirsquidly.creaturesfromdarkness.network.CFDPacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommonProxy
{
    public static DamageSource causeNightmareDamage(Entity source)
    { return (new EntityDamageSource(creaturesfromdarkness.MOD_ID + "." + "nightmare", source)).setDamageBypassesArmor().setDamageIsAbsolute(); }

    public void preInitRegisteries(FMLPreInitializationEvent event)
    {
        CFDEntities.registerEntities();
        CFDLootTables.registerLootTables();
        CFDSounds.registerSounds();
        CFDPacketHandler.registerMessages();
        CapabilityManager.INSTANCE.register(CapabilityNightmare.ICapabilityRiptide.class, new CapabilityNightmare.Storage(), CapabilityNightmare.RiptideMethods::new);

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) CFDEntities.RegisterRenderers();
    }

    public void postInitRegisteries(FMLPostInitializationEvent event)
    {
        ConfigParser.breakupConfigArrays();
        CFDEntities.registerEntitySpawns();
    }

    @SideOnly(Side.CLIENT)
    public void registerItemRenderer(Item item, int meta, String id){}
}