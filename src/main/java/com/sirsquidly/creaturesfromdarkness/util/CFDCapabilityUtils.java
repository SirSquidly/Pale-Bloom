package com.sirsquidly.creaturesfromdarkness.util;

import com.sirsquidly.creaturesfromdarkness.capabilities.CapabilityNightmare;
import net.minecraft.entity.player.EntityPlayer;

/** This class includes multiple methods used for Capability Logic. */
public class CFDCapabilityUtils
{
    /** Used to add the Nightmare to a Player */
    public static void addNightmarePackage(EntityPlayer playerIn)
    {
        if (playerIn.hasCapability(CapabilityNightmare.RIPTIDE_CAP, null))
        {
            CapabilityNightmare.ICapabilityRiptide capNightmare = playerIn.getCapability(CapabilityNightmare.RIPTIDE_CAP, null);

            capNightmare.setNightmareOnPlayer(true);
            capNightmare.setNightmareNearby(3);
            capNightmare.setNightmareHealth(60);
            /* Entire Hauntings automatically end after 10-20 minutes*/
            capNightmare.setNightmareOverallTimer(600 + playerIn.world.rand.nextInt(600));
            capNightmare.setNightmareEventTimer(10 + playerIn.world.rand.nextInt(10));
            /* Cooldown is between 1-5 minutes default */
            capNightmare.setNightmareSpawnTimer(60 + playerIn.world.rand.nextInt(240));
        }
    }

    /** Used to remove the Nightmare from a Player */
    public static void removeNightmarePackage(EntityPlayer playerIn)
    {
        if (playerIn.hasCapability(CapabilityNightmare.RIPTIDE_CAP, null))
        {
            CapabilityNightmare.ICapabilityRiptide capNightmare = playerIn.getCapability(CapabilityNightmare.RIPTIDE_CAP, null);

            capNightmare.setNightmareOnPlayer(false);
            capNightmare.setNightmareNearby(0);
            capNightmare.setNightmareOverallTimer(0);
        }
    }
}