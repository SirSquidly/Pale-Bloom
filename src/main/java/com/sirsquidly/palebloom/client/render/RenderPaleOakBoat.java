package com.sirsquidly.palebloom.client.render;

import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPaleOakBoat extends RenderBoat
{
    public static final ResourceLocation PALE_OAK_BOAT_TEXTURE = new ResourceLocation(paleBloom.MOD_ID + ":textures/entities/pale_oak_boat.png");

    public RenderPaleOakBoat(RenderManager renderManagerIn)
    { super(renderManagerIn); }

    protected ResourceLocation getEntityTexture(EntityBoat entity)
    { return PALE_OAK_BOAT_TEXTURE; }
}