package com.sirsquidly.palebloom.client.render;

import com.sirsquidly.palebloom.client.model.ModelReapingWillow;
import com.sirsquidly.palebloom.entity.EntityReapingWillow;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderReapingWillow extends RenderGeoExtended<EntityReapingWillow>
{
    static final ResourceLocation REAP_MODEL = new ResourceLocation(paleBloom.MOD_ID, "geo/entity/reaping_willow.geo.json");

    public RenderReapingWillow(RenderManager renderManager)
    {
        super(renderManager, new ModelReapingWillow());
        this.shadowSize = 0.5F;

        this.addLayer(new LayerGeoEyeGlowEmissive(this));
    }

    public ResourceLocation getEntityEmissiveModel(EntityReapingWillow entity) { return REAP_MODEL; }
}