package com.sirsquidly.creaturesfromdarkness.client;

import com.sirsquidly.creaturesfromdarkness.creaturesfromdarkness;
import com.sirsquidly.creaturesfromdarkness.entity.EntityNightmare;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerNightmareEyes implements LayerRenderer<EntityNightmare>
{
    public static final ResourceLocation EYES_TEXTURE = new ResourceLocation(creaturesfromdarkness.MOD_ID + ":textures/entities/nightmare/nightmare_eyes.png");
    private final RenderNightmare nightmareRender;

    public LayerNightmareEyes(RenderNightmare nightmareRenderIn)
    {
        this.nightmareRender = nightmareRenderIn;
    }

    @Override
    public void doRenderLayer(EntityNightmare entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.nightmareRender.bindTexture(EYES_TEXTURE);

        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
        int i = 15728880;
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.nightmareRender.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        i = entitylivingbaseIn.getBrightnessForRender();
        j = i % 65536;
        k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        this.nightmareRender.setLightmap(entitylivingbaseIn);

        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    @Override
    public boolean shouldCombineTextures()
    { return true; }
}
