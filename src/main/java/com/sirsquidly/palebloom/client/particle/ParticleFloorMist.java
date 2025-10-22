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
public class ParticleFloorMist extends ParticleBase
{
    private static final ResourceLocation MIST_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/particles/mist.png");

    public ParticleFloorMist(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, int color)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, MIST_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.canCollide = true;
        this.particleMaxAge = 60 + this.rand.nextInt(20);
        this.texSheetSeg = 1;
        this.renderYOffset = 0.001F;
        this.particleScale =  10.0F * (this.rand.nextFloat() + 0.5F);
        this.particleGravity = 0.011F;
        this.setAlphaF(0.0F);

        float[] colors = decimalIntToRGB(color);
        /* This reintroduces some white from the original smoke. */
        float shade = 0.5F;
        setRBGColorF( 1.0F - (1.0F - colors[0]) * shade, 1.0F - (1.0F - colors[1]) * shade, 1.0F - (1.0F - colors[2]) * shade );
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        float alpha;
        float half = this.particleMaxAge * 0.5F;

        if (this.particleAge <= half)
        {
            float progress = this.particleAge / half;
            alpha = 0.5F * progress;
        }
        else
        {
            float progress = (this.particleAge - half) / half;
            alpha = 0.5F * (1.0F - progress);
        }
        this.setAlphaF(alpha);

    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        GlStateManager.depthMask(false);
        super.renderParticle(buffer, entity, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
        GlStateManager.depthMask(true);
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

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
            if (parameters.length == 1)
            {
                return new ParticleFloorMist(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0]);
            }
            return null;
        }
    }
}