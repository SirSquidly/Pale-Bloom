package com.sirsquidly.palebloom.client.render;

import com.sirsquidly.palebloom.client.model.ModelMannequin;
import com.sirsquidly.palebloom.client.model.ModelMannequinArmor;
import com.sirsquidly.palebloom.common.entity.EntityMannequin;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMannequin extends RenderBiped<EntityMannequin>
{
    private static final ResourceLocation MANNEQUIN_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/entities/mannequin/mannequin.png");
    private static final ResourceLocation MANNEQUIN_INACTIVE_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/entities/mannequin/mannequin_dead.png");

    public RenderMannequin(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelMannequin(), 0.5F);

        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
        {
            /** Override the values passed to the armor rendering, in order to match the Mannequin's locked values. */
            public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
            {
                if (entitylivingbaseIn instanceof EntityMannequin)
                {
                    EntityMannequin mannequin = (EntityMannequin) entitylivingbaseIn;
                    if (mannequin.frozenPose || mannequin.getInactive())
                    {
                        limbSwing = mannequin.cachedLimbSwing;
                        limbSwingAmount = mannequin.cachedLimbSwingAmount;
                        ageInTicks = mannequin.cachedAgeInTicks;
                    }
                }

                super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            }

            protected void initArmor()
            {
                this.modelLeggings = new ModelMannequinArmor(0.5F, (ModelBiped) getMainModel());
                this.modelArmor = new ModelMannequinArmor(1.001F, (ModelBiped) getMainModel());
            }
        };
        this.addLayer(layerbipedarmor);

        this.addLayer(new LayerEyeGlowEmissive<>(this));
    }

    protected void preRenderCallback(EntityMannequin entity, float partialTickTime)
    {
        float f = 0.9375F;
        GlStateManager.scale(f, f, f);
    }

    protected ResourceLocation getEntityTexture(EntityMannequin entity)
    { return entity.getInactive() ? MANNEQUIN_INACTIVE_TEXTURE : MANNEQUIN_TEXTURE; }

    protected void applyRotations(EntityMannequin entity, float ageInTicks, float rotationYaw, float partialTicks)
    {
        float f = (float)entity.getHurtTime() - partialTicks;
        float f1 = entity.getHurtTime() - partialTicks;

        if (f > 0.0F) rotationYaw +=  MathHelper.sin(f) * f * f1 / 5.0F;

        super.applyRotations(entity, ageInTicks, rotationYaw, partialTicks);
    }
}