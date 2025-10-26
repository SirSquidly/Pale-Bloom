package com.sirsquidly.palebloom.client.particle;

import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticlePaleLeaf extends ParticleBase
{
    final float rotSpeed;
    final float swirlScrew;
    final float swirlRadius;

    private static final ResourceLocation PALE_LEAF_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/particles/pale_oak_leaf.png");
    private static final ResourceLocation BLOOMING_LEAF_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/particles/blooming_pale_oak_leaf.png");
    private static final ResourceLocation PEEPING_LEAF_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/particles/peeping_birch_leaf.png");
    private static final ResourceLocation[] LEAF_TEXTURES = new ResourceLocation[]{PALE_LEAF_TEXTURE, BLOOMING_LEAF_TEXTURE, PEEPING_LEAF_TEXTURE};

    public ParticlePaleLeaf(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, int leafType)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, LEAF_TEXTURES[leafType], 0);
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;

        this.textureManager = textureManager;
        this.texSpot = this.rand.nextInt(8);
        this.renderYOffset = 0.05F;
        this.particleMaxAge = 300;
        this.particleScale = 2.5F * (this.rand.nextBoolean() ? 0.5F : 0.75F);

        this.rotSpeed = ((float)Math.random() - 0.5F) * 0.025F;
        this.swirlScrew = 10f + this.rand.nextInt(50);
        this.swirlRadius = 40F;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        boolean inWater = this.world.getBlockState(new BlockPos(this.posX, this.posY, this.posZ)).getMaterial() == Material.WATER;

        this.prevParticleAngle = this.particleAngle;

        if (!this.onGround && !inWater)
        {
            this.motionY -= 0.001F;
            this.particleAngle += (float)Math.PI * this.rotSpeed * 2.0F;

            float f1 = Math.min(this.particleAge / 300.0F, 1.0F);

            double d0 = f1 * Math.cos(f1 * this.swirlScrew) * this.swirlRadius;
            double d1 = f1 * Math.sin(f1 * this.swirlScrew) * this.swirlRadius;

            this.motionX = d0 * 0.0025F;
            this.motionZ = d1 * 0.0025F;
        }
        else if (inWater)
        {
            this.motionX *= 0.95;
            this.motionY *= 0.7;
            this.motionZ *= 0.95;
        }
    }

    public Vec3d[] particleVertexRendering(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, float particleSize)
    {
        Vec3d[] wow = new Vec3d[]{
                new Vec3d(-rotationX * particleSize - rotationXY * particleSize, -rotationZ * particleSize, -rotationYZ * particleSize - rotationXZ * particleSize),
                new Vec3d(-rotationX * particleSize + rotationXY * particleSize, rotationZ * particleSize, -rotationYZ * particleSize + rotationXZ * particleSize),
                new Vec3d(rotationX * particleSize + rotationXY * particleSize, rotationZ * particleSize, rotationYZ * particleSize + rotationXZ * particleSize),
                new Vec3d(rotationX * particleSize - rotationXY * particleSize, -rotationZ * particleSize, rotationYZ * particleSize - rotationXZ * particleSize)
        };

        float angle = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
        float f9 = MathHelper.cos(angle * 0.5F);
        float f10 = MathHelper.sin(angle * 0.5F) * (float)cameraViewDir.x;
        float f11 = MathHelper.sin(angle * 0.5F) * (float)cameraViewDir.y;
        float f12 = MathHelper.sin(angle * 0.5F) * (float)cameraViewDir.z;
        Vec3d vec3d = new Vec3d((double)f10, (double)f11, (double)f12);

        for (int l = 0; l < 4; ++l)
        {
            wow[l] = vec3d.scale(2.0D * wow[l].dotProduct(vec3d)).add(wow[l].scale((double)(f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(wow[l]).scale((double)(2.0F * f9)));
        }

        return wow;
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
            if (parameters.length == 1) return new ParticlePaleLeaf(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0]);
            return null;
        }
    }
}