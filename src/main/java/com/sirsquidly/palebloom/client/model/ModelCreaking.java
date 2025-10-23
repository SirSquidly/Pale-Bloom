package com.sirsquidly.palebloom.client.model;

import com.sirsquidly.palebloom.common.entity.EntityCreaking;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelCreaking extends AnimatedTickingGeoModel<EntityCreaking>
{
    static final ResourceLocation ANIMATIONS = new ResourceLocation(paleBloom.MOD_ID, "animations/creaking.animation.json");
    static final ResourceLocation MODEL = new ResourceLocation(paleBloom.MOD_ID, "geo/entity/creaking.geo.json");
    static final ResourceLocation TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/entities/creaking/creaking.png");

    public ModelCreaking()
    { super(); }

    @Override
    public ResourceLocation getModelLocation(EntityCreaking object)
    { return MODEL; }

    @Override
    public ResourceLocation getTextureLocation(EntityCreaking object)
    { return TEXTURE; }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityCreaking animatable)
    { return ANIMATIONS; }

    @Override
    public void setLivingAnimations(EntityCreaking entity, Integer uniqueID, AnimationEvent customPredicate)
    {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(head.getRotationX() + extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(head.getRotationY() + extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}