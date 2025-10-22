package com.sirsquidly.palebloom.client.render;

import com.sirsquidly.palebloom.entity.IEyeGlow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.EntityLivingBase;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;

public class LayerGeoEyeGlowEmissive extends GeoLayerRenderer
{
    private final RenderGeoExtended FUCK;

    public LayerGeoEyeGlowEmissive(RenderGeoExtended entityRendererIn)
    {
        super(entityRendererIn);
        this.FUCK = entityRendererIn;
    }

    @Override
    public void render(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, Color renderColor)
    {
        if (entity instanceof IEyeGlow && !((IEyeGlow)entity).getGlowingEyes()) return;

        Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(((IEyeGlow) entity).getEyeGlowTexture());

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(!entity.isInvisible());
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);

        entityRenderer.render(entityRenderer.getGeoModelProvider().getModel(this.FUCK.getEntityEmissiveModel(entity)), entity, partialTicks, (float) renderColor.getRed() / 255f, (float) renderColor.getBlue() / 255f, (float) renderColor.getGreen() / 255f, (float) renderColor.getAlpha() / 255);

        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        //this.entityRenderer.setLightmap(entitylivingbaseIn);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
    }

    @Override
    public boolean shouldCombineTextures() { return false; }
}