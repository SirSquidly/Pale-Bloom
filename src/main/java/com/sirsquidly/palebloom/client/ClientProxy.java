package com.sirsquidly.palebloom.client;

import com.sirsquidly.palebloom.CommonProxy;
import com.sirsquidly.palebloom.client.particle.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy
{
    public void registerItemRenderer(Item item, int meta, String id)
    { ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id)); }

    @Override
    public void spawnParticle(int particleId, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        World world = minecraft.world;
        minecraft.effectRenderer.addEffect(getFactory(particleId).createParticle(0, world, posX, posY, posZ, speedX, speedY, speedZ, parameters));
    }

    /**
     * This is used by the Particle Spawning as an ID system for out Particles.
     * We do not require Ids for Particles, it's just more convenient for sending over packets!
     * */
    @SideOnly(Side.CLIENT)
    public static IParticleFactory getFactory(int particleId)
    {
        switch(particleId)
        {
            default:
            case 0:
                return new ParticleTrail.Factory();
            case 1:
                return new ParticlePaleLeaf.Factory();
            case 2:
                return new ParticleResinPollen.Factory();
            case 3:
                return new ParticleRootsCommune.Factory();
            case 4:
                return new ParticleAmberSightPulse.Factory();
            case 5:
                return new ParticleIncenseSmoke.Factory();
            case 6:
                return new ParticleFloorMist.Factory();
        }
    }
}