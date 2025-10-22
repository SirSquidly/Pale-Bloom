package com.sirsquidly.palebloom.client.particle;

import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleAmberSightPulse extends ParticleBase
{
    private static final ResourceLocation ROOTS_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/particles/roots.png");

    public ParticleAmberSightPulse(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, int size)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, ROOTS_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.canCollide = false;
        this.particleMaxAge = 10 + this.rand.nextInt(10);
        this.texSheetSeg = 2;
        this.renderYOffset = 0.001F;
        this.particleScale = size;

        this.particleAlpha = 0.2F;
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        GlStateManager.disableDepth();
        super.renderParticle(buffer, entity, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
        GlStateManager.enableDepth();
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        this.texSpot = Math.min((int)((this.particleAge / (float)this.particleMaxAge) * 6), 3);

        float progress = (this.particleAge - (this.particleMaxAge * 0.5F)) / (this.particleMaxAge * 0.5F);
        if (this.particleAge > this.particleMaxAge * 0.5F) this.setAlphaF(0.2F * (1.0F - progress));
    }

    public Vec3d[] particleVertexRendering(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, float particleSize)
    {
        /* 90 degrees tilt, since this current math makes 0 completely vertical, when we want a more horizontal tilt. */
        double tilt = Math.toRadians(90);
        double cos = Math.cos(tilt);
        double sin = Math.sin(tilt);
        return new Vec3d[]
                {
                        new Vec3d(-particleSize, particleSize * cos, -particleSize * sin),
                        new Vec3d(-particleSize, -particleSize * cos, particleSize * sin),
                        new Vec3d(particleSize, -particleSize * cos, particleSize * sin),
                        new Vec3d(particleSize, particleSize * cos, -particleSize * sin)
                };
    }

    @Override
    public int getBrightnessForRender(float partialTicks)
    { return 15728880; }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
            if (parameters.length == 1)
            {
                return new ParticleAmberSightPulse(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0]);
            }
            return new ParticleAmberSightPulse(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, 10);
        }
    }
}