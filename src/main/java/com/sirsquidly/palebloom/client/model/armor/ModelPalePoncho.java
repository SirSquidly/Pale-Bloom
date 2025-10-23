package com.sirsquidly.palebloom.client.model.armor;

import com.sirsquidly.palebloom.common.item.ItemPaleMossCloak;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class ModelPalePoncho extends ModelBiped
{
    private final ModelRenderer pale_poncho;
    private final ModelRenderer resin_bulb;
    private final ModelRenderer blooming_pale_oak;
    private final ModelRenderer stem;
    private final ModelRenderer bramble;
    private final ModelRenderer brambleR1;
    private final ModelRenderer brambleR2;
    private final ModelRenderer brambleL;
    private final ModelRenderer pale_creeper;
    private final ModelRenderer creeperHead1;
    private final ModelRenderer live_root;
    private final ModelRenderer creaking_heart;
    private final ModelRenderer sucker_roots;
    private final ModelRenderer sucker_roots_3d;

    public ModelPalePoncho(float size)
    {
        /* Inheriting from ModelBiped means `textureWidthIn` and `textureHeightIn` are used for rendering over a default ModelBiped, passing 0 lets us ignore that. */
        super(size, 0, 0,0);

        textureWidth = 128;
        textureHeight = 128;

        pale_poncho = new ModelRenderer(this);
        pale_poncho.setRotationPoint(-5.0F, 3.0F, -5.0F);
        pale_poncho.cubeList.add(new ModelBox(pale_poncho, 64, 32, -3.0F, -2.95F, 3.0F, 16, 16, 4, 0.6F, false));

        resin_bulb = new ModelRenderer(this);
        resin_bulb.setRotationPoint(5.0F, 10.05F, 5.0F);
        pale_poncho.addChild(resin_bulb);
        resin_bulb.cubeList.add(new ModelBox(resin_bulb, 64, 16, -5.0F, -12.9F, 2.0F, 6, 6, 4, 0.0F, false));

        blooming_pale_oak = new ModelRenderer(this);
        blooming_pale_oak.setRotationPoint(5.0F, 10.05F, 5.0F);
        pale_poncho.addChild(blooming_pale_oak);
        blooming_pale_oak.cubeList.add(new ModelBox(blooming_pale_oak, 108, 0, 5.0F, -20.0F, -1.0F, 5, 9, 5, 0.0F, false));

        stem = new ModelRenderer(this);
        stem.setRotationPoint(7.5F, -13.75F, 1.0F);
        blooming_pale_oak.addChild(stem);
        stem.rotateAngleY = -0.7854F;
        stem.cubeList.add(new ModelBox(stem, 108, 14, -2.5F, -5.85F, 0.0F, 5, 6, 0, 0.0F, false));

        bramble = new ModelRenderer(this);
        bramble.setRotationPoint(5.0F, 10.05F, 5.0F);
        pale_poncho.addChild(bramble);


        brambleR1 = new ModelRenderer(this);
        brambleR1.setRotationPoint(7.0F, -13.0F, -1.0F);
        bramble.addChild(brambleR1);
        brambleR1.rotateAngleY = -0.3927F;
        brambleR1.cubeList.add(new ModelBox(brambleR1, 84, 11, -3.0F, -6.0F, 0.0F, 9, 11, 0, 0.0F, false));

        brambleR2 = new ModelRenderer(this);
        brambleR2.setRotationPoint(7.0F, -13.0F, -1.0F);
        bramble.addChild(brambleR2);
        brambleR2.rotateAngleY = 0.3927F;
        brambleR2.cubeList.add(new ModelBox(brambleR2, 84, 0, -3.0F, -6.0F, 0.0F, 9, 11, 0, 0.0F, false));

        brambleL = new ModelRenderer(this);
        brambleL.setRotationPoint(-11.0F, -13.0F, -1.0F);
        bramble.addChild(brambleL);
        brambleL.cubeList.add(new ModelBox(brambleL, 67, 0, -2.0F, -6.0F, 2.0F, 9, 11, 0, 0.0F, false));

        pale_creeper = new ModelRenderer(this);
        pale_creeper.setRotationPoint(4.4F, -3.75F, 5.3F);
        pale_poncho.addChild(pale_creeper);
        pale_creeper.cubeList.add(new ModelBox(pale_creeper, 64, 39, -8.0F, -0.75F, -2.0F, 3, 1, 2, 0.0F, false));

        creeperHead1 = new ModelRenderer(this);
        creeperHead1.setRotationPoint(-6.5F, -0.75F, -1.0F);
        pale_creeper.addChild(creeperHead1);
        creeperHead1.cubeList.add(new ModelBox(creeperHead1, 64, 26, -1.5F, -3.0F, -1.5F, 3, 3, 3, 0.0F, false));

        live_root = new ModelRenderer(this);
        live_root.setRotationPoint(4.4F, 1.25F, 7.3F);
        pale_poncho.addChild(live_root);
        live_root.cubeList.add(new ModelBox(live_root, 104, 32, -3.4F, -4.0F, -4.3F, 8, 17, 4, 0.54F, false));

        creaking_heart = new ModelRenderer(this);
        creaking_heart.setRotationPoint(4.4F, 1.25F, 7.3F);
        pale_poncho.addChild(creaking_heart);
        creaking_heart.cubeList.add(new ModelBox(creaking_heart, 104, 22, -3.4F, -3.3F, -4.3F, 8, 6, 4, 0.62F, false));

        sucker_roots = new ModelRenderer(this);
        sucker_roots.setRotationPoint(-5.0F, 11.0F, 0.0F);
        sucker_roots.cubeList.add(new ModelBox(sucker_roots, 112, 53, 4.0F, -11.0F, -2.0F, 4, 11, 4, 0.26F, false));

        sucker_roots_3d = new ModelRenderer(this);
        sucker_roots_3d.setRotationPoint(8.75F, -4.0F, 1.15F);
        sucker_roots.addChild(sucker_roots_3d);
        sucker_roots_3d.rotateAngleZ = -0.3927F;
        sucker_roots_3d.cubeList.add(new ModelBox(sucker_roots_3d, 106, 57, -1.0F, -5.0F, -1.0F, 2, 5, 1, 0.11F, false));

        bipedBody.addChild(pale_poncho);
        bipedLeftArm.addChild(sucker_roots);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

        EntityLivingBase living = (EntityLivingBase) entity;
        ItemStack stack = living.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        Map<String, String> scionAbilities = ((ItemPaleMossCloak)stack.getItem()).getCachedScionList(stack);

        this.toggleScionLayers(scionAbilities);

        if (!pale_creeper.isHidden)
        {
            creeperHead1.rotateAngleX = bipedHead.rotateAngleX - (entity.isSneaking() ? 0.7F : 0);
            creeperHead1.rotateAngleY = bipedHead.rotateAngleY;
        }

        /** An alright workaround for the rendering w/ Mannequins */
        if (entity instanceof EntityArmorStand)
        { super.render(entity, 0, 0, 0, 0, 0, scale); }
        else
        { super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale); }
    }

    public void toggleScionLayers(Map<String, String> scionAbilities)
    {
        pale_creeper.isHidden = !scionAbilities.containsValue("pale_creeper");
        creaking_heart.isHidden = !scionAbilities.containsValue("creaking_heart");
        sucker_roots.isHidden = !scionAbilities.containsValue("sucker_roots");
        bramble.isHidden = !scionAbilities.containsValue("bramble");
        blooming_pale_oak.isHidden = !scionAbilities.containsValue("blooming_pale_oak");
        resin_bulb.isHidden = !scionAbilities.containsValue("resin_bulb");
        live_root.isHidden = !scionAbilities.containsValue("live_root.preformed");
    }
}