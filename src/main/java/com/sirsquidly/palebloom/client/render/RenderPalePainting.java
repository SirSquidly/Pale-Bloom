package com.sirsquidly.palebloom.client.render;

import com.sirsquidly.palebloom.entity.item.EntityPalePainting;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderPalePainting extends Render<EntityPalePainting>
{
    private static final ResourceLocation PAINTING_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/entities/painting/painting_1.png");

    public RenderPalePainting(RenderManager renderManager) {
        super(renderManager);
    }

    protected ResourceLocation getEntityTexture(EntityPalePainting entity) { return PAINTING_TEXTURE; }


    @Override
    public void doRender(EntityPalePainting entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180 - entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.enableRescaleNormal();

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);



        this.bindEntityTexture(entity);

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        float relWidth = 48;
        float relHeight = 48;

        float width = relWidth / 16 / 2;
        float height = relHeight / 16 / 2;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);

        buffer.pos(width, -height, 0).tex(0, 1).normal(0, 0, 1).endVertex();
        buffer.pos(-width, -height, 0).tex(1, 1).normal(0, 0, 1).endVertex();
        buffer.pos(-width, height, 0).tex(1, 0).normal(0, 0, 1).endVertex();
        buffer.pos(width, height, 0).tex(0, 0).normal(0, 0, 1).endVertex();

        tessellator.draw();



        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.disableBlend();

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
