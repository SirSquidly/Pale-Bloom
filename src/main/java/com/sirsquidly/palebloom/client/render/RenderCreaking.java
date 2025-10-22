package com.sirsquidly.palebloom.client.render;

import com.sirsquidly.palebloom.client.model.ModelCreaking;
import com.sirsquidly.palebloom.entity.EntityCreaking;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCreaking extends RenderGeoExtended<EntityCreaking>
{
    static final ResourceLocation CREAK_MODEL = new ResourceLocation(paleBloom.MOD_ID, "geo/entity/creaking.geo.json");

    public RenderCreaking(RenderManager renderManager)
    {
        super(renderManager, new ModelCreaking());
        this.shadowSize = 0.6F;

        this.addLayer(new LayerGeoEyeGlowEmissive(this));
    }

    public ResourceLocation getEntityEmissiveModel(EntityCreaking entity) { return CREAK_MODEL; }
}