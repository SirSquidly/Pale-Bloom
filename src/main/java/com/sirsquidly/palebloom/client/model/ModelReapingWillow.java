package com.sirsquidly.palebloom.client.model;

import com.sirsquidly.palebloom.entity.EntityReapingWillow;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelReapingWillow extends AnimatedTickingGeoModel<EntityReapingWillow>
{
    static final ResourceLocation ANIMATIONS = new ResourceLocation(paleBloom.MOD_ID, "animations/reaping_willow.animation.json");
    static final ResourceLocation MODEL = new ResourceLocation(paleBloom.MOD_ID, "geo/entity/reaping_willow.geo.json");
    static final ResourceLocation REAP_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/entities/reaping_willow/reaping_willow.png");
    static final ResourceLocation REAP_TEXTURE_AWAKE = new ResourceLocation(paleBloom.MOD_ID, "textures/entities/reaping_willow/reaping_willow_awake.png");

    public ModelReapingWillow()
    { super(); }

    @Override
    public ResourceLocation getModelLocation(EntityReapingWillow object)
    { return MODEL; }

    @Override
    public ResourceLocation getTextureLocation(EntityReapingWillow object)
    { return object.getGlowingEyes() ? REAP_TEXTURE_AWAKE : REAP_TEXTURE; }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityReapingWillow animatable)
    { return ANIMATIONS; }

    @Override
    public void setLivingAnimations(EntityReapingWillow entity, Integer uniqueID, AnimationEvent customPredicate)
    {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("upper_body");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        float clampedPitch = MathHelper.clamp(extraData.headPitch, -20F, 20F);
        float clampedYaw = MathHelper.clamp(extraData.netHeadYaw, -20F, 20F);

        head.setRotationX(head.getRotationX() + clampedPitch * ((float) Math.PI / 180F));
        head.setRotationY(head.getRotationY() + clampedYaw * ((float) Math.PI / 180F));
    }
}