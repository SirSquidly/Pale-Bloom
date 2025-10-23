package com.sirsquidly.palebloom.client.model;

import com.sirsquidly.palebloom.common.entity.EntityMannequin;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelMannequin extends ModelBiped
{
    public ModelMannequin()
    { this(0.0F); }

    public ModelMannequin(float modelSize)
    {
        super(modelSize, -14.0F, 64, 64);

        this.bipedHead = new ModelRenderer(this);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHead.cubeList.add(new ModelBox(this.bipedHead, 0, 0, -3.5F, -8.0F, -3.0F, 7, 8, 7, 0.0F, false));

        this.bipedHeadwear = new ModelRenderer(this);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHeadwear.cubeList.add(new ModelBox(this.bipedHeadwear, 28, 0, -3.5F, -8.0F, -3.0F, 7, 8, 7, 0.5F, false));

        this.bipedBody = new ModelRenderer(this);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody.cubeList.add(new ModelBox(this.bipedBody, 0, 15, -4.0F, 0.0F, -1.5F, 8, 11, 3, 0.0F, false));

        this.bipedLeftArm = new ModelRenderer(this);
        this.bipedLeftArm.setRotationPoint(5.0F, 1.5F, 0.5F);
        this.bipedLeftArm.cubeList.add(new ModelBox(this.bipedLeftArm, 34, 15, -1.0F, -1.5F, -1.5F, 3, 13, 3, 0.0F, true));

        this.bipedRightArm = new ModelRenderer(this);
        this.bipedRightArm.setRotationPoint(-5.0F, 1.5F, 0.5F);
        this.bipedRightArm.cubeList.add(new ModelBox(this.bipedRightArm, 22, 15, -2.0F, -1.5F, -1.5F, 3, 13, 3, 0.0F, false));

        this.bipedLeftLeg = new ModelRenderer(this);
        this.bipedLeftLeg.setRotationPoint(2.0F, 11.0F, 0.6F);
        this.bipedLeftLeg.cubeList.add(new ModelBox(this.bipedLeftLeg, 12, 29, -1.5F, 0.0F, -1.6F, 3, 13, 3, 0.0F, true));

        this.bipedRightLeg = new ModelRenderer(this);
        this.bipedRightLeg.setRotationPoint(-2.0F, 11.0F, 0.6F);
        this.bipedRightLeg.cubeList.add(new ModelBox(this.bipedRightLeg, 0, 29, -1.5F, 0.0F, -1.6F, 3, 13, 3, 0.0F, false));
    }



    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        EntityMannequin mannequin = (EntityMannequin) entityIn;

        if (mannequin.frozenPose || mannequin.getInactive())
        {
            super.setRotationAngles( mannequin.cachedLimbSwing, mannequin.cachedLimbSwingAmount, mannequin.cachedAgeInTicks, netHeadYaw, headPitch, scaleFactor, entityIn );
            this.swingProgress = mannequin.cachedSwingProgress;
            //this.swingProgress = mannequin.cachedLimbSwingAmount;

            limbSwing = mannequin.cachedLimbSwing;
            limbSwingAmount = mannequin.cachedLimbSwingAmount;
            ageInTicks = mannequin.cachedAgeInTicks;
        }
        else { super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn); }

        /* Stops the default Biped 'breathing' arm animations. */
        if (mannequin.getInactive())
        {
            this.bipedRightArm.rotateAngleX = 0;
            this.bipedLeftArm.rotateAngleX = 0;
            this.bipedRightArm.rotateAngleZ = 0;
            this.bipedLeftArm.rotateAngleZ = 0;
        }

        this.bipedBody.rotationPointZ = 0;
        this.bipedLeftLeg.rotationPointY = 11.0F;
        this.bipedRightLeg.rotationPointY = 11.0F;

        this.bipedBody.rotationPointX = 0;
        this.bipedBody.rotateAngleZ = 0;
        this.bipedHead.rotationPointX = 0;
        this.bipedHead.rotationPointZ = 0;
        this.bipedHead.rotateAngleZ = 0;

        float swing = this.swingProgress;
        if (swing > 0)
        {
            float swingCentered = -(float)Math.sin(swingProgress * Math.PI * 2) + 0.2f * (float)Math.sin(swingProgress * Math.PI * 3);


            float f = MathHelper.sin(this.swingProgress * (float)Math.PI);
            this.bipedBody.rotateAngleX = swingCentered / 2F;
            this.bipedBody.rotateAngleY = -swingCentered / 2F;
            this.bipedBody.rotateAngleZ = -swingCentered / 4F;

            this.bipedHead.rotateAngleZ = -swingCentered / 1.2F;
            this.bipedHead.rotateAngleY = 0;

            this.bipedBody.rotationPointX = -swingCentered * 2;
            this.bipedHead.rotationPointX = -swingCentered * 3;

            this.bipedLeftArm.rotationPointX = -swingCentered + 4;
            this.bipedLeftArm.rotateAngleX = swingCentered * 1.25F;

            this.bipedHead.rotationPointZ = -swingCentered * 6;
            this.bipedHead.rotateAngleX = swingCentered;
            this.bipedBody.rotationPointZ = -swingCentered * 6;
            this.bipedLeftArm.rotationPointZ = Math.max(-swingCentered * 6F, -1);
            this.bipedRightArm.rotationPointZ = -swingCentered * 6;


            float x = (float)( -4 * Math.sin(swing * Math.PI) + 5 * Math.sin(swing * Math.PI * 3) * Math.exp(-7 * swing));

            this.bipedRightArm.rotateAngleX = x;
            this.bipedRightArm.rotateAngleZ = x * 0.25F;
        }
        else if (hasNoArmor(mannequin))
        {
            /* Wait what is this? An ENTIRE GREAT WALK ANIMATION??? AAAAND IT WAS CUT?! MORE LIKELY THAN YOU FUCKING THINK!
            *
            * Blame how Minecraft's Armor Rendering code operates, where it is a separate model inheriting from
            * ModelBiped, so it DOESN'T OBEY the Entity's own model values.
            *
            * s
            * */

            float twitchStrength = 0F;

            float landSpeed = 2.5F;
            /* This value is used for most all the math. limbSwingAmount is capped, to prevent it from looking bad at high speeds (such as when hit). */
            float swingCap = Math.min(limbSwingAmount * 3, 0.15F);
            float legSwingR = MathHelper.cos(limbSwing * landSpeed) * 2F * swingCap;
            float legSwingL = MathHelper.cos(limbSwing * landSpeed + (float)Math.PI + 0.1F) * 2.2F * swingCap;
            float bodySwing = MathHelper.cos(limbSwing * landSpeed + 0.6F) * 3F * swingCap;
            float HeadSwing = MathHelper.cos(limbSwing * landSpeed + 0.8F) * 3F * swingCap;

            /* The cap is so the legs are only rotating when the legSwing motion brings them up and off the ground. */
            this.bipedRightLeg.rotateAngleX = legSwingR * 1.5F;
            this.bipedLeftLeg.rotateAngleX = legSwingL * 1.5F;

            this.bipedBody.rotateAngleY = legSwingL * 0.5F;

            float bodylean = Math.abs(bodySwing * 8);
            float maxLean = 8F * 0.15F * 2F;
            if (((int)(limbSwing / Math.PI) % 4) == 0 && bodylean > maxLean * 0.97F) twitchStrength = MathHelper.cos(ageInTicks) * 0.8F;

            bodylean += (twitchStrength * 2);
            this.bipedBody.rotateAngleX = Math.abs(bodySwing * 0.7F);
            this.bipedHead.rotateAngleX = Math.abs(HeadSwing * 0.7F);
            this.bipedBody.rotationPointZ = -bodylean;
            this.bipedHead.rotationPointZ = -bodylean;
            this.bipedRightArm.rotationPointZ = -bodylean;
            this.bipedLeftArm.rotationPointZ = -bodylean;

            this.bipedHead.rotateAngleY += twitchStrength * 2;
            this.bipedHead.rotateAngleZ += twitchStrength;
            this.bipedRightArm.rotateAngleY += twitchStrength;
            this.bipedLeftArm.rotateAngleY += twitchStrength;
            this.bipedRightArm.rotateAngleX += twitchStrength * 0.5F;
            this.bipedLeftArm.rotateAngleX += twitchStrength * 0.5F;


            float legLift = legSwingR * 0.4F;
            this.bipedRightLeg.offsetY = (legSwingR < 0.1F) ? legLift : 0.0F;
            this.bipedLeftLeg.offsetY = (legSwingL < 0.1F) ? -legLift : 0.0F;

            float legZOffset = MathHelper.sin(limbSwing * landSpeed) * 0.4F * Math.min(limbSwingAmount * 3, 0.5F);
            /* Moves the leg back and forth. */
            this.bipedRightLeg.offsetZ = legZOffset * 0.5F;
            this.bipedLeftLeg.offsetZ = -legZOffset * 0.5F;
        }
    }

    public boolean hasNoArmor(EntityLivingBase entity)
    {
        for (ItemStack stack : entity.getArmorInventoryList())
        {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }
}