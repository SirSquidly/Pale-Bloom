package com.sirsquidly.creaturesfromdarkness.client;

import com.sirsquidly.creaturesfromdarkness.creaturesfromdarkness;
import com.sirsquidly.creaturesfromdarkness.entity.EntityNightmare;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderNightmare extends RenderLiving<EntityNightmare>
{
    public static final ResourceLocation NIGHTMARE_TEXTURE = new ResourceLocation(creaturesfromdarkness.MOD_ID + ":textures/entities/nightmare/nightmare.png");

    public RenderNightmare(RenderManager managerIn)
    {
        super(managerIn, new ModelNightmare(), 0.0F);
        this.addLayer(new LayerNightmareEyes(this));
    }

    /** Scaling done outside of `doRender` as to not scale the hitbox wrong. */
    @Override
    protected void preRenderCallback(EntityNightmare entity, float partialTickTime)
    { GlStateManager.scale(1.2F, 1.2F, 1.2F); }

    @Override
    public void doRender(EntityNightmare entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        int lightLevel = entity.world.getLightFor(EnumSkyBlock.BLOCK, entity.getPosition());
        float renderAlpha = 0;

        if (lightLevel > 5) renderAlpha = (lightLevel - 5) / 10.0F;
        //if (Minecraft.getMinecraft().player.canEntityBeSeen(entity)) lightLevel = 0;
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //renderAlpha = 0.5F;
        GlStateManager.color(1.0F, 1.0F, 1.0F, renderAlpha);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
    }

    protected ResourceLocation getEntityTexture(EntityNightmare entity)
    {
        return NIGHTMARE_TEXTURE;
    }
}