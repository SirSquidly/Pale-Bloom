package com.sirsquidly.palebloom.common.entity;

import net.minecraft.util.ResourceLocation;

/**
 * A universal method for all the entities that are utilizing selective eye-glow in this mod.
 */
public interface IEyeGlow
{
    ResourceLocation getEyeGlowTexture();

    boolean getGlowingEyes();
    void setGlowingEyes(boolean bool);
}