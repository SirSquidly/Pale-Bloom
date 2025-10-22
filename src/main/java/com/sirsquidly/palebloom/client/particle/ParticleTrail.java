package com.sirsquidly.palebloom.client.particle;

import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleTrail extends ParticleBase
{
    private final Vec3d targetPos;

    private static final int[] COLOR_LIVE = { 15833650, 15627811, 14440210, 12342557 };
    private static final int[] COLOR_DEAD = { 6186077, 5918026, 6178877, 5324090 };

    private static final ResourceLocation TRAIL_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/particles/pixel.png");

    public ParticleTrail(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, int glowing)
    { this(textureManager, world, x, y, z, movementX,movementY, movementZ, glowing, 1); }

    public ParticleTrail(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, int glowing, int speedIn)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, TRAIL_TEXTURE, 0);
        this.textureManager = textureManager;

        this.targetPos = new Vec3d(movementX, movementY, movementZ);

        Vec3d current = new Vec3d(posX, posY, posZ);
        Vec3d motionVec = this.targetPos.subtract(current).scale(1.0 / (20 + (20 * this.rand.nextFloat())));

        motionX = motionVec.x * speedIn;
        motionY = motionVec.y * speedIn;
        motionZ = motionVec.z * speedIn;

        this.canCollide = false;
        this.particleMaxAge = (int)(64.0 / (Math.random() * 0.8 + 0.2));
        this.setupColors(glowing);
        this.texSheetSeg = 1;
        this.renderYOffset = this.height / 2;
        this.particleScale =  0.7F + (float) (Math.random() * 0.3F);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        Vec3d currentPos = new Vec3d(posX, posY, posZ);

        if (currentPos.distanceTo(this.targetPos) < 0.5)
        { setExpired(); }
    }

    public void setupColors(int colorType)
    {
        int color = colorType == 1 ? COLOR_LIVE[this.rand.nextInt(COLOR_LIVE.length)] : COLOR_DEAD[this.rand.nextInt(COLOR_DEAD.length)];
        int r = (color & 16711680) >> 16;
        int g = (color & 65280) >> 8;
        int b = (color & 255);

        setRBGColorF(r * 0.00392156862F, g * 0.00392156862F,b * 0.00392156862F);
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
            switch (parameters.length)
            {
                case 1:
                    return new ParticleTrail(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0]);
                case 2:
                    return new ParticleTrail(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0], parameters[1]);
            }
            return null;
        }
    }
}