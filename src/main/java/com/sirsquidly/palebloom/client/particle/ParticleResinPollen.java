package com.sirsquidly.palebloom.client.particle;

import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleResinPollen extends ParticleBase
{
    private static final ResourceLocation POLLEN_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/particles/resin_pollen.png");

    public ParticleResinPollen(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, POLLEN_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.canCollide = false;
        this.particleMaxAge = 40 + this.rand.nextInt(200);
        this.texSheetSeg = 2;
        this.texSpot = world.rand.nextInt(4);
        this.renderYOffset = this.height / 2;
        this.particleScale =  0.7F + (float) (Math.random() * 0.3F);
    }

    @Override
    public int getBrightnessForRender(float partialTicks)
    { return 15728880; }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        { return new ParticleResinPollen(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ); }
    }
}