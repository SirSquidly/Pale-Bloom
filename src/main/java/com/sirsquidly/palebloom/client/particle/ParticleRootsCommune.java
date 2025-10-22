package com.sirsquidly.palebloom.client.particle;

import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleRootsCommune extends ParticleBase
{
    private static final ResourceLocation ROOTS_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/particles/roots.png");

    public ParticleRootsCommune(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ)
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
        this.particleScale =  10.0F;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        this.texSpot = Math.min(this.particleAge * 5 / (this.particleMaxAge), 3);

        //this.particleAlpha = (1.0F - (float)particleAge / (float)this.particleMaxAge);
        if (this.particleAge > this.particleMaxAge / 2) this.setAlphaF(1.2F - (float)particleAge / (float)this.particleMaxAge - ((float)(this.particleMaxAge / 2)));
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
        { return new ParticleRootsCommune(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ); }
    }
}