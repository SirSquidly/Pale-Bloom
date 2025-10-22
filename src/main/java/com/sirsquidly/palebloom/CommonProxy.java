package com.sirsquidly.palebloom;

import com.sirsquidly.palebloom.capabilities.CapabilityPaleGardenFog;
import com.sirsquidly.palebloom.init.*;
import com.sirsquidly.palebloom.network.JTPGPacketHandler;
import com.sirsquidly.palebloom.network.JTPGPacketSpawnParticles;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommonProxy
{
    public static DamageSource causeNightmareDamage(Entity source)
    { return (new EntityDamageSource(paleBloom.MOD_ID + "." + "hydraweed_jaw", source)).setDamageBypassesArmor().setDamageIsAbsolute(); }

    public void preInitRegisteries(FMLPreInitializationEvent event)
    {
        JTPGEntities.registerEntities();
        JTPGLootTables.registerLootTables();
        JTPGEntities.registerTileEntities();
        JTPGSounds.registerSounds();
        JTPGPacketHandler.registerMessages();
        CapabilityManager.INSTANCE.register(CapabilityPaleGardenFog.ICapabilityPaleGardenFog.class, new CapabilityPaleGardenFog.Storage(), CapabilityPaleGardenFog.PaleGardenFogMethods::new);

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) JTPGEntities.RegisterRenderers();
    }

    public void initRegisteries(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(FogEvents.class);
    }

    public void postInitRegisteries(FMLPostInitializationEvent event)
    {
        ConfigParser.breakupConfigArrays();
        JTPGEntities.registerEntitySpawns();
    }

    @SideOnly(Side.CLIENT)
    public void registerItemRenderer(Item item, int meta, String id){}

    /**
     *  Specialized particle method that sends particles on servers
     * */
    public void spawnParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
    {
        if (world.isRemote)
        { spawnParticle(particleId, posX, posY, posZ, speedX, speedY, speedZ, parameters); }
        else
        { JTPGPacketHandler.CHANNEL.sendToAllTracking( new JTPGPacketSpawnParticles(particleId, posX, posY, posZ, speedX, speedY, speedZ, parameters), new NetworkRegistry.TargetPoint(world.provider.getDimension(), posX, posY, posZ, 0.0D)); }
    }

    public void spawnParticle(int particleId, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
    {}
}