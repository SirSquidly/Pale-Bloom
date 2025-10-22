package com.sirsquidly.palebloom.util;

import com.sirsquidly.palebloom.capabilities.CapabilityPaleGardenFog;
import com.sirsquidly.palebloom.network.JTPGPacketFogCapability;
import com.sirsquidly.palebloom.network.JTPGPacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;

/** This class includes multiple methods used for Capability Logic. */
public class JTPGCapabilityUtils
{
    /** Sets the Fog Boolean w/ Packet */
    public static void setPlayerFog(EntityPlayer playerIn, boolean valueIn)
    {
        CapabilityPaleGardenFog.ICapabilityPaleGardenFog capFog = getCapability(playerIn);
        if (capFog == null) return;

        if (valueIn != capFog.isPlayerFogEnabled())
        {
            capFog.setPlayerFogEnabled(valueIn);
            //System.out.print("updating tracking....    ");
            JTPGPacketHandler.CHANNEL.sendTo(new JTPGPacketFogCapability(playerIn.getEntityId(), capFog.isPlayerFogEnabled()), (EntityPlayerMP) playerIn);
        }
    }

    public static boolean getPlayerFog(EntityPlayer playerIn)
    {
        CapabilityPaleGardenFog.ICapabilityPaleGardenFog capFog = getCapability(playerIn);
        if (capFog == null) return false;

        return capFog.isPlayerFogEnabled();
    }

    public static float getPlayerFogProgress(EntityPlayer playerIn)
    {
        CapabilityPaleGardenFog.ICapabilityPaleGardenFog capFog = getCapability(playerIn);
        if (capFog == null) return 0.0F;

        return capFog.getPlayerFogProgress();
    }

    public static float getPrevPlayerFogProgress(EntityPlayer playerIn)
    {
        CapabilityPaleGardenFog.ICapabilityPaleGardenFog capFog = getCapability(playerIn);
        if (capFog == null) return 0.0F;

        return capFog.getPrevPlayerFogProgress();
    }

    public static void updatePrevFogProgress(EntityPlayer playerIn)
    {
        CapabilityPaleGardenFog.ICapabilityPaleGardenFog capFog = getCapability(playerIn);
        if (capFog == null) return;

        capFog.setPrevPlayerFogProgress(capFog.getPlayerFogProgress());
    }

    public static void shiftPlayerFogProgress(EntityPlayer playerIn, float shiftByIn)
    { shiftPlayerFogProgress(playerIn, shiftByIn, false); }

    /** Alters the Fog Progress, within a clamped range. */
    public static void shiftPlayerFogProgress(EntityPlayer playerIn, float shiftByIn, boolean replaceWholeProgressIn)
    {
        CapabilityPaleGardenFog.ICapabilityPaleGardenFog capFog = getCapability(playerIn);
        if (capFog == null) return;

        float newProgress = replaceWholeProgressIn ? shiftByIn : MathHelper.clamp(capFog.getPlayerFogProgress() + shiftByIn, 0.0F, 1.0F);

        if (newProgress != capFog.getPlayerFogProgress())
        { capFog.setPlayerFogProgress(newProgress); }
    }

    public static CapabilityPaleGardenFog.ICapabilityPaleGardenFog getCapability(EntityPlayer playerIn)
    {
        if (playerIn.hasCapability(CapabilityPaleGardenFog.PALE_GARDEN_FOG_CAP, null))
        { return playerIn.getCapability(CapabilityPaleGardenFog.PALE_GARDEN_FOG_CAP, null); }

        return null;
    }
}