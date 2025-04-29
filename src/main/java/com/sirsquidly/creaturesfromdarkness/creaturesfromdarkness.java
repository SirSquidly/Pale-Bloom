package com.sirsquidly.creaturesfromdarkness;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber
@Mod(modid = creaturesfromdarkness.MOD_ID, name = creaturesfromdarkness.NAME, version = creaturesfromdarkness.VERSION, dependencies = creaturesfromdarkness.DEPENDENCIES)
public class creaturesfromdarkness {
    public static final String MOD_ID = "creaturesfromdarkness";
    public static final String NAME = "Creatures From Darkness";
    public static final String CONFIG_NAME = "creatures_from_darkness";
    public static final String VERSION = "0.1.1";
    public static final String DEPENDENCIES = "";
    public static final String CLIENT_PROXY_CLASS = "com.sirsquidly.creaturesfromdarkness.client.ClientProxy";
    public static final String COMMON_PROXY_CLASS = "com.sirsquidly.creaturesfromdarkness.CommonProxy";
    public static Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Mod.Instance
    public static creaturesfromdarkness instance;

    @SidedProxy(clientSide = creaturesfromdarkness.CLIENT_PROXY_CLASS, serverSide = creaturesfromdarkness.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    { proxy.preInitRegisteries(event); }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {}

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {  proxy.postInitRegisteries(event);  }
}
