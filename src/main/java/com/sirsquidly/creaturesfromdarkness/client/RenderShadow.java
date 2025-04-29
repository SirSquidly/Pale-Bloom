package com.sirsquidly.creaturesfromdarkness.client;

import com.sirsquidly.creaturesfromdarkness.capabilities.CapabilityNightmare;
import com.sirsquidly.creaturesfromdarkness.creaturesfromdarkness;
import com.sirsquidly.creaturesfromdarkness.entity.EntityShadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderShadow extends RenderBiped<EntityShadow>
{
    public static final ResourceLocation SHADOW_TEXTURE = new ResourceLocation(creaturesfromdarkness.MOD_ID + ":textures/entities/shadow/shadow.png");

    public RenderShadow(RenderManager managerIn)
    {
        super(managerIn, new ModelPlayer(0.0F, false), 0.0F);
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerShadowEyes(this));
        this.addLayer(new LayerHeldItem(this));
    }

        @Override
        public void doRender(EntityShadow entity, double x, double y, double z, float entityYaw, float partialTicks)
        {
            int lightLevel = entity.world.getLightFor(EnumSkyBlock.BLOCK, entity.getPosition());
            float renderAlpha = 0;

            if (lightLevel > 5) renderAlpha = (lightLevel - 5) / 10.0F;
            //if (Minecraft.getMinecraft().player.canEntityBeSeen(entity)) lightLevel = 0;

            /* Players experiencing the dreamy shader will fully see Shadows, just for Fun */
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player != null && mc.player.hasCapability(CapabilityNightmare.RIPTIDE_CAP, null))
            {
                CapabilityNightmare.ICapabilityRiptide capNightmare = mc.player.getCapability(CapabilityNightmare.RIPTIDE_CAP, null);

                if (capNightmare.getNightmareNearby() > 0)
                { renderAlpha = renderAlpha + 0.5F; }
            }

            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GlStateManager.color(1.0F, 1.0F, 1.0F, renderAlpha);

            super.doRender(entity, x, y, z, entityYaw, partialTicks);

            GlStateManager.disableBlend();
        }

    protected ResourceLocation getEntityTexture(EntityShadow entity)
    {
        return SHADOW_TEXTURE;
    }
}