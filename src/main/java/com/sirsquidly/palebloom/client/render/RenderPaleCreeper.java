package com.sirsquidly.palebloom.client.render;

import com.sirsquidly.palebloom.entity.EntityPaleCreeper;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPaleCreeper extends RenderLiving<EntityPaleCreeper>
{
    private static final ResourceLocation PALE_CREEPER_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/entities/pale_creeper/pale_creeper.png");
    private static final ResourceLocation PALE_CREEPER_AWAKE_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/entities/pale_creeper/pale_creeper_awake.png");

    public RenderPaleCreeper(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelCreeper(), 0.5F);
        this.addLayer(new LayerEyeGlowEmissive<>(this));
        //this.addLayer(new LayerCreeperCharge(this));
    }

    protected void preRenderCallback(EntityPaleCreeper entitylivingbaseIn, float partialTickTime)
    {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        GlStateManager.scale(f2, f3, f2);
    }

    protected int getColorMultiplier(EntityPaleCreeper entitylivingbaseIn, float lightBrightness, float partialTickTime)
    {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);

        if ((int)(f * 10.0F) % 2 == 0)
        {
            return 0;
        }
        else
        {
            int i = (int)(f * 0.2F * 255.0F);
            i = MathHelper.clamp(i, 0, 255);
            return i << 24 | 822083583;
        }
    }

    protected ResourceLocation getEntityTexture(EntityPaleCreeper entity)
    { return entity.getGlowingEyes() ? PALE_CREEPER_AWAKE_TEXTURE : PALE_CREEPER_TEXTURE; }
}