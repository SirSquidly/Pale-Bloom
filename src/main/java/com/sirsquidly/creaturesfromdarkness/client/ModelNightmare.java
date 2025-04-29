package com.sirsquidly.creaturesfromdarkness.client;

import com.sirsquidly.creaturesfromdarkness.entity.EntityNightmare;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelNightmare extends ModelBase
{
    private final ModelRenderer main;
    private final ModelRenderer legR4_r1;
    private final ModelRenderer legR3_r1;
    private final ModelRenderer legR2_r1;
    private final ModelRenderer legR1_r1;
    private final ModelRenderer legL4_r1;
    private final ModelRenderer legL3_r1;
    private final ModelRenderer legL2_r1;
    private final ModelRenderer legL1_r1;
    private final ModelRenderer upperBody;
    private final ModelRenderer head;
    private final ModelRenderer thorax;

    public ModelNightmare() {
        textureWidth = 64;
        textureHeight = 64;

        main = new ModelRenderer(this);
        main.setRotationPoint(0.0F, 24.0F, 0.0F);
        main.cubeList.add(new ModelBox(main, 0, 44, -3.0F, -12.0F, -3.0F, 6, 6, 6, 0.0F, false));

        legR4_r1 = new ModelRenderer(this);
        legR4_r1.setRotationPoint(-2.0F, -9.0F, 7.0F);
        main.addChild(legR4_r1);
        setRotationAngle(legR4_r1, 0.0F, 0.4363F, 0.0F);
        legR4_r1.cubeList.add(new ModelBox(legR4_r1, 32, 0, -16.0F, -9.0F, 0.0F, 16, 18, 0, 0.0F, true));

        legR3_r1 = new ModelRenderer(this);
        legR3_r1.setRotationPoint(-2.0F, -9.0F, 5.0F);
        main.addChild(legR3_r1);
        setRotationAngle(legR3_r1, 0.0F, 0.1745F, 0.0F);
        legR3_r1.cubeList.add(new ModelBox(legR3_r1, 32, 0, -16.0F, -9.0F, 0.0F, 16, 18, 0, 0.0F, true));

        legR2_r1 = new ModelRenderer(this);
        legR2_r1.setRotationPoint(-2.0F, -9.0F, 2.0F);
        main.addChild(legR2_r1);
        setRotationAngle(legR2_r1, 0.0F, -0.1745F, 0.0F);
        legR2_r1.cubeList.add(new ModelBox(legR2_r1, 32, 0, -16.0F, -9.0F, 0.0F, 16, 18, 0, 0.0F, true));

        legR1_r1 = new ModelRenderer(this);
        legR1_r1.setRotationPoint(-2.0F, -9.0F, -1.0F);
        main.addChild(legR1_r1);
        setRotationAngle(legR1_r1, 0.0F, -0.4363F, 0.0F);
        legR1_r1.cubeList.add(new ModelBox(legR1_r1, 32, 0, -16.0F, -9.0F, 0.0F, 16, 18, 0, 0.0F, true));

        legL4_r1 = new ModelRenderer(this);
        legL4_r1.setRotationPoint(3.0F, -9.0F, 7.0F);
        main.addChild(legL4_r1);
        setRotationAngle(legL4_r1, 0.0F, -0.4363F, 0.0F);
        legL4_r1.cubeList.add(new ModelBox(legL4_r1, 32, 0, 0.0F, -9.0F, 0.0F, 16, 18, 0, 0.0F, false));

        legL3_r1 = new ModelRenderer(this);
        legL3_r1.setRotationPoint(3.0F, -9.0F, 5.0F);
        main.addChild(legL3_r1);
        setRotationAngle(legL3_r1, 0.0F, -0.1745F, 0.0F);
        legL3_r1.cubeList.add(new ModelBox(legL3_r1, 32, 0, 0.0F, -9.0F, 0.0F, 16, 18, 0, 0.0F, false));

        legL2_r1 = new ModelRenderer(this);
        legL2_r1.setRotationPoint(3.0F, -9.0F, 2.0F);
        main.addChild(legL2_r1);
        setRotationAngle(legL2_r1, 0.0F, 0.1745F, 0.0F);
        legL2_r1.cubeList.add(new ModelBox(legL2_r1, 32, 0, 0.0F, -9.0F, 0.0F, 16, 18, 0, 0.0F, false));

        legL1_r1 = new ModelRenderer(this);
        legL1_r1.setRotationPoint(3.0F, -9.0F, -1.0F);
        main.addChild(legL1_r1);
        setRotationAngle(legL1_r1, 0.0F, 0.4363F, 0.0F);
        legL1_r1.cubeList.add(new ModelBox(legL1_r1, 32, 0, 0.0F, -9.0F, 0.0F, 16, 18, 0, 0.0F, false));

        upperBody = new ModelRenderer(this);
        upperBody.setRotationPoint(0.0F, -12.0F, -3.0F);
        main.addChild(upperBody);
        setRotationAngle(upperBody, 0.1745F, 0.0F, 0.0F);
        upperBody.cubeList.add(new ModelBox(upperBody, 0, 16, -4.0F, -12.0F, -3.0F, 8, 12, 4, 0.0F, false));

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, -12.0F, -1.0F);
        upperBody.addChild(head);
        setRotationAngle(head, -0.1745F, 0.0F, 0.0F);
        head.cubeList.add(new ModelBox(head, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));

        thorax = new ModelRenderer(this);
        thorax.setRotationPoint(0.0F, 0.0F, 0.0F);
        main.addChild(thorax);
        thorax.cubeList.add(new ModelBox(thorax, 20, 44, -5.0F, -13.0F, 3.0F, 10, 8, 12, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    { main.render(f5); }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        EntityNightmare nightmare = (EntityNightmare) entityIn;

        float[] yAngles = {0.5F, 0.25F, -0.25F, -0.5F};

        ModelRenderer[] leftLegs = {legL1_r1, legL2_r1, legL3_r1, legL4_r1};
        ModelRenderer[] rightLegs = {legR1_r1, legR2_r1, legR3_r1, legR4_r1};
        float[] limbSwingsY = new float[4];
        float[] limbSwingsZ = new float[4];

        for (int i = 0; i < 4; i++)
        {
            leftLegs[i].rotateAngleY = yAngles[i];
            rightLegs[i].rotateAngleY = -yAngles[i];
            leftLegs[i].rotateAngleZ = 0;
            rightLegs[i].rotateAngleZ = 0;

            float timingOffset = i * 0.7F;
            limbSwingsY[i] = Math.abs(MathHelper.sin(limbSwing + timingOffset) * 0.6F * limbSwingAmount);
            limbSwingsZ[i] = Math.abs(MathHelper.sin(limbSwing + (float) Math.PI * i / 2F) * 0.6F * limbSwingAmount);

            leftLegs[i].rotateAngleY += limbSwingsY[i];
            rightLegs[i].rotateAngleY -= limbSwingsY[i];
            leftLegs[i].rotateAngleZ -= limbSwingsZ[i];
            rightLegs[i].rotateAngleZ += limbSwingsZ[i];
        }

        this.head.rotateAngleX = (headPitch * 0.017453292F) - 0.2F;
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}