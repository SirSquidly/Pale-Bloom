package com.sirsquidly.palebloom.client.render;

import com.sirsquidly.palebloom.entity.IEyeGlow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerEyeGlowEmissive<T extends EntityLiving>  implements LayerRenderer<T>
{
    private final RenderLiving<T> entityRenderer;

	public LayerEyeGlowEmissive(RenderLiving<T> entityRenderIn)
    { this.entityRenderer = entityRenderIn; }

    public void doRenderLayer(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (entity instanceof IEyeGlow && !((IEyeGlow)entity).getGlowingEyes()) return;

        this.entityRenderer.bindTexture(((IEyeGlow) entity).getEyeGlowTexture());
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(!entity.isInvisible());
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.entityRenderer.getMainModel().setModelAttributes(this.entityRenderer.getMainModel());
        this.entityRenderer.getMainModel().setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
        this.entityRenderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        this.entityRenderer.setLightmap(entity);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
    }

    public boolean shouldCombineTextures() { return false; }
}