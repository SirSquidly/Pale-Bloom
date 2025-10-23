package com.sirsquidly.palebloom.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This is used so the Armor rendered on a Mannequin obeys the Freezing behavior.
 * */
@SideOnly(Side.CLIENT)
public class ModelMannequinArmor extends ModelBiped
{
    ModelBiped modelBase;

    public ModelMannequinArmor(float modelSize, ModelBiped modelBaseIn)
    { this(modelSize, 64, 32, modelBaseIn); }

    protected ModelMannequinArmor(float modelSize, int textureWidthIn, int textureHeightIn, ModelBiped modelBaseIn)
    {
        super(modelSize, 0.0F, textureWidthIn, textureHeightIn);
        modelBase = modelBaseIn;
    }

    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        copyModelAngles(modelBase.bipedHead, this.bipedHead);
        copyModelAngles(modelBase.bipedHeadwear, this.bipedHeadwear);
        copyModelAngles(modelBase.bipedBody, this.bipedBody);
        copyModelAngles(modelBase.bipedRightArm, this.bipedRightArm);
        copyModelAngles(modelBase.bipedLeftArm, this.bipedLeftArm);
        copyModelAngles(modelBase.bipedRightLeg, this.bipedRightLeg);
        copyModelAngles(modelBase.bipedLeftLeg, this.bipedLeftLeg);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

        copyModelAngles(modelBase.bipedHead, this.bipedHead);
        copyModelAngles(modelBase.bipedHeadwear, this.bipedHeadwear);
        copyModelAngles(modelBase.bipedBody, this.bipedBody);
        copyModelAngles(modelBase.bipedRightArm, this.bipedRightArm);
        copyModelAngles(modelBase.bipedLeftArm, this.bipedLeftArm);
        copyModelAngles(modelBase.bipedRightLeg, this.bipedRightLeg);
        copyModelAngles(modelBase.bipedLeftLeg, this.bipedLeftLeg);
    }

    public void setModelAttributes(ModelBase model)
    {
        this.swingProgress = model.swingProgress;
        this.isRiding = model.isRiding;
        this.isChild = model.isChild;
        copyModelAngles(modelBase.bipedHead, this.bipedHead);
        copyModelAngles(modelBase.bipedHeadwear, this.bipedHeadwear);
        copyModelAngles(modelBase.bipedBody, this.bipedBody);
        copyModelAngles(modelBase.bipedRightArm, this.bipedRightArm);
        copyModelAngles(modelBase.bipedLeftArm, this.bipedLeftArm);
        copyModelAngles(modelBase.bipedRightLeg, this.bipedRightLeg);
        copyModelAngles(modelBase.bipedLeftLeg, this.bipedLeftLeg);
    }
}