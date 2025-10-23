package com.sirsquidly.palebloom.client.model;

import com.sirsquidly.palebloom.common.entity.EntityHydraweedJaw;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelSnapweedJaw extends ModelBase
{
    private final ModelRenderer main;
    private final ModelRenderer head;
    private final ModelRenderer jawL;
    private final ModelRenderer jawR;

    public ModelSnapweedJaw()
    {
        textureWidth = 64;
        textureHeight = 64;

        main = new ModelRenderer(this);
        main.setRotationPoint(0.0F, 18.0F, 0.0F);
        main.cubeList.add(new ModelBox(main, 0, 26, -1.5F, -0.9F, -1.5F, 3, 7, 3, 0.0F, false));
        main.cubeList.add(new ModelBox(main, 22, 21, -1.5F, -0.9F, -2.5F, 0, 7, 5, 0.0F, false));
        main.cubeList.add(new ModelBox(main, 22, 28, 1.5F, -0.9F, -2.5F, 0, 7, 5, 0.0F, false));
        main.cubeList.add(new ModelBox(main, 12, 33, -2.5F, -0.9F, -1.5F, 5, 7, 0, 0.0F, false));
        main.cubeList.add(new ModelBox(main, 22, 33, -2.5F, -0.9F, 1.5F, 5, 7, 0, 0.0F, false));

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 3.0F, 0.0F);
        main.addChild(head);

        jawL = new ModelRenderer(this);
        jawL.setRotationPoint(0.0F, -4.0F, 0.0F);
        head.addChild(jawL);
        jawL.cubeList.add(new ModelBox(jawL, 0, 13, -8.0F, -5.0F, -8.0F, 16, 5, 8, 0.0F, false));

        jawR = new ModelRenderer(this);
        jawR.setRotationPoint(0.0F, -4.0F, 0.0F);
        head.addChild(jawR);
        jawR.cubeList.add(new ModelBox(jawR, 0, 0, -8.0F, -5.0F, 0.0F, 16, 5, 8, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    { main.render(f5); }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        EntityHydraweedJaw snapweedJaw = (EntityHydraweedJaw) entityIn;

        if (snapweedJaw.getPassengers().isEmpty())
        {
            main.offsetY = 0.575F;
            main.rotateAngleY = 0F;
            head.rotateAngleY = 0F;
            jawL.rotateAngleX = 0F;
            jawR.rotateAngleX = 0F;
        }
        else
        {
            float f = MathHelper.sin(this.swingProgress * (float)Math.PI);

            main.offsetY = 0;
            main.rotateAngleY = 0 - (f * 0.5F);
            head.rotateAngleY = 0 - f;
            jawL.rotateAngleX = -0.5F - f;
            jawR.rotateAngleX = 0.5F + f;
        }

    }
}