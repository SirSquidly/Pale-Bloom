package com.sirsquidly.palebloom.client.render;

import com.sirsquidly.palebloom.client.model.ModelSnapweedJaw;
import com.sirsquidly.palebloom.common.entity.EntityHydraweedJaw;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSnapweedJaw extends RenderLiving<EntityHydraweedJaw>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/entities/snapweed_jaw.png");

    public RenderSnapweedJaw(RenderManager renderManagerIn)
    { super(renderManagerIn, new ModelSnapweedJaw(), 0.0F); }

    protected ResourceLocation getEntityTexture(EntityHydraweedJaw entity)
    { return TEXTURE; }
}