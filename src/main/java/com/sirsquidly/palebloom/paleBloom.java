package com.sirsquidly.palebloom;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

@Mod.EventBusSubscriber
@Mod(modid = paleBloom.MOD_ID, name = paleBloom.NAME, version = paleBloom.VERSION, dependencies = paleBloom.DEPENDENCIES)
public class paleBloom
{
    public static final String MOD_ID = "palebloom";
    public static final String NAME = "Pale Bloom";
    public static final String CONFIG_NAME = "pale_bloom";
    public static final String VERSION = "0.9.1";
    public static final String DEPENDENCIES = "";
    public static final String CLIENT_PROXY_CLASS = "com.sirsquidly.palebloom.client.ClientProxy";
    public static final String COMMON_PROXY_CLASS = "com.sirsquidly.palebloom.CommonProxy";
    public static Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Mod.Instance
    public static paleBloom instance;

    @SidedProxy(clientSide = paleBloom.CLIENT_PROXY_CLASS, serverSide = paleBloom.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        GeckoLib.initialize();
        proxy.preInitRegisteries(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.initRegisteries(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {  proxy.postInitRegisteries(event);  }
}
