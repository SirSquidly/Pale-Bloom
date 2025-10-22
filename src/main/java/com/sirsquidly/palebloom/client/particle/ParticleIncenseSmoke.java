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
public class ParticleIncenseSmoke extends ParticleBase
{
    private static final ResourceLocation INCENSE_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/particles/incense_smoke.png");

    private float texProgress = 0.0F;
    private static final float FRAME_SPEED = 0.25F;

    public ParticleIncenseSmoke(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, int color)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, INCENSE_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.canCollide = false;
        this.particleMaxAge = 60 + this.rand.nextInt(20);
        this.texSheetSeg = 3;
        this.renderYOffset = this.height / 2;
        this.particleScale =  1.5F;
        this.texProgress = this.rand.nextInt(8);

        float[] colors = decimalIntToRGB(color);
        /* This reintroduces some white from the original smoke. */
        float shade = 0.5F;
        setRBGColorF( 1.0F - (1.0F - colors[0]) * shade, 1.0F - (1.0F - colors[1]) * shade, 1.0F - (1.0F - colors[2]) * shade );
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        texProgress += 0.4F;
        if (texProgress >= 8) texProgress -= 8;
        this.texSpot = (int) texProgress;

        //this.texSpot = Math.min(this.particleAge * 8 / (this.particleMaxAge), 8);
        if (this.particleAge > this.particleMaxAge / 2) this.setAlphaF(1.2F - (float)particleAge / (float)this.particleMaxAge - ((float)(this.particleMaxAge / 2)));
    }


    @Override
    public int getBrightnessForRender(float partialTicks)
    { return super.getBrightnessForRender(partialTicks); }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
            if (parameters.length == 1)
            {
                return new ParticleIncenseSmoke(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0]);
            }
            return null;
        }
    }
}