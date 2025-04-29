package com.sirsquidly.creaturesfromdarkness.capabilities;

import com.sirsquidly.creaturesfromdarkness.creaturesfromdarkness;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public class CapabilityNightmare
{
    @CapabilityInject(ICapabilityRiptide.class)
    public static Capability<ICapabilityRiptide> RIPTIDE_CAP;
    public static final ResourceLocation ID = new ResourceLocation(creaturesfromdarkness.MOD_ID, "riptideTime");

    /** If this player has a Nightmare haunting them. */
    private static final String NIGHTMARE_ACTIVE_BOOL = "nightmareOnPlayerBool";
    /** The player's distance from a Nightmare. Used for applying effects, 1 = VERY close, 2 = Near enough, 3 = Fresh Infection */
    private static final String NIGHTMARE_DISTANCE_INT = "nightmareDistance";
    /** The Health of the Nightmare haunting the player. */
    private static final String NIGHTMARE_HEALTH_INT = "nightmareHealth";
    /** How long a Nightmare's haunt lasts. If this runs out, the Nightmare automatically leaves the player. */
    private static final String NIGHTMARE_OVERALL_TIMER_INT = "nightmareOverallTimer";
    /** Cooldown between attempts for the Nightmare to spawn. Goes down overtime, but much faster if the player is in Darkness. */
    private static final String NIGHTMARE_COOLDOWN_TIMER_INT = "nightmareSpawnTimer";

    /** A timer used between events occurring to the haunted player. */
    private static final String NIGHTMARE_EVENT_TIMER_INT = "nightmareEventTimer";
    /** If the player currently has the 'fog' event. */
    private static final String NIGHTMARE_EVENT_FOG_BOOL = "nightmareEventFogBool";

    public interface ICapabilityRiptide
    {
        boolean getNightmareOnPlayer();
        void setNightmareOnPlayer(boolean value);

        int getNightmareNearby();
        void setNightmareNearby(int value);

        int getNightmareHealth();
        void setNightmareHealth(int value);

        int getNightmareOverallTimer();
        void setNightmareOverallTimer(int value);

        int getNightmareSpawnTimer();
        void setNightmareSpawnTimer(int value);

        int getNightmareEventTimer();
        void setNightmareEventTimer(int value);

        boolean getNightmareEventFog();
        void setNightmareEventFog(boolean value);
    }

    public static class RiptideMethods implements ICapabilityRiptide
    {
        private boolean nightmareOnPlayerBool = false;
        private int nightmareNearPlayer = 0;
        private int nightmareHealth = 0;
        private int nightmareOverallTimer = 0;
        private int nightmareSpawnTimer = 0;
        private int nightmareEventTimer = 0;
        private boolean nightmareEventFogBool = false;


        @Override
        public boolean getNightmareOnPlayer()
        { return nightmareOnPlayerBool; }
        @Override
        public void setNightmareOnPlayer(boolean value)
        { nightmareOnPlayerBool = value; }


        @Override
        public int getNightmareNearby()
        { return nightmareNearPlayer; }
        @Override
        public void setNightmareNearby(int value)
        { nightmareNearPlayer = value; }


        @Override
        public int getNightmareHealth()
        { return nightmareHealth; }
        @Override
        public void setNightmareHealth(int value)
        { nightmareHealth = value; }


        @Override
        public int getNightmareSpawnTimer()
        { return nightmareSpawnTimer; }
        @Override
        public void setNightmareSpawnTimer(int value)
        { nightmareSpawnTimer = value; }


        @Override
        public int getNightmareOverallTimer()
        { return nightmareOverallTimer; }
        @Override
        public void setNightmareOverallTimer(int value)
        { nightmareOverallTimer = value; }


        @Override
        public int getNightmareEventTimer()
        { return nightmareEventTimer; }
        @Override
        public void setNightmareEventTimer(int value)
        { nightmareEventTimer = value; }


        @Override
        public boolean getNightmareEventFog()
        { return nightmareEventFogBool; }
        @Override
        public void setNightmareEventFog(boolean value)
        { nightmareEventFogBool = value; }
    }

    public static class Storage implements Capability.IStorage<ICapabilityRiptide>
    {
        @Override
        public NBTBase writeNBT(Capability<ICapabilityRiptide> capability, ICapabilityRiptide instance, EnumFacing side)
        {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setBoolean(NIGHTMARE_ACTIVE_BOOL, instance.getNightmareOnPlayer());
            compound.setInteger(NIGHTMARE_DISTANCE_INT, instance.getNightmareNearby());
            compound.setInteger(NIGHTMARE_HEALTH_INT, instance.getNightmareHealth());
            compound.setInteger(NIGHTMARE_OVERALL_TIMER_INT, instance.getNightmareOverallTimer());
            compound.setInteger(NIGHTMARE_COOLDOWN_TIMER_INT, instance.getNightmareSpawnTimer());
            compound.setInteger(NIGHTMARE_EVENT_TIMER_INT, instance.getNightmareEventTimer());
            compound.setBoolean(NIGHTMARE_EVENT_FOG_BOOL, instance.getNightmareEventFog());
            return compound;
        }

        @Override
        public void readNBT(Capability<ICapabilityRiptide> capability, ICapabilityRiptide instance, EnumFacing side, NBTBase nbt)
        {
            NBTTagCompound compound = (NBTTagCompound) nbt;
            instance.setNightmareOnPlayer(compound.getBoolean(NIGHTMARE_ACTIVE_BOOL));
            instance.setNightmareNearby(compound.getInteger(NIGHTMARE_DISTANCE_INT));
            instance.setNightmareHealth(compound.getInteger(NIGHTMARE_HEALTH_INT));
            instance.setNightmareOverallTimer(compound.getInteger(NIGHTMARE_OVERALL_TIMER_INT));
            instance.setNightmareSpawnTimer(compound.getInteger(NIGHTMARE_COOLDOWN_TIMER_INT));
            instance.setNightmareEventTimer(compound.getInteger(NIGHTMARE_EVENT_TIMER_INT));
            instance.setNightmareEventFog(compound.getBoolean(NIGHTMARE_EVENT_FOG_BOOL));
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTBase>
    {
        final Capability<ICapabilityRiptide> capability;
        final EnumFacing facing;
        final ICapabilityRiptide instance;

        public Provider(final ICapabilityRiptide instance, final Capability<ICapabilityRiptide> capability, @Nullable final EnumFacing facing)
        {
            this.instance = instance;
            this.capability = capability;
            this.facing = facing;
        }

        @Override
        public boolean hasCapability(@Nullable final Capability<?> capability, final EnumFacing facing)
        { return capability == getCapability(); }

        @Override
        public <T> T getCapability(@Nullable Capability<T> capability, EnumFacing facing)
        { return capability == getCapability() ? getCapability().cast(this.instance) : null; }

        final Capability<ICapabilityRiptide> getCapability()
        { return capability; }

        EnumFacing getFacing()
        { return facing; }

        final ICapabilityRiptide getInstance()
        { return instance; }

        @Override
        public NBTBase serializeNBT()
        { return getCapability().writeNBT(getInstance(), getFacing()); }

        @Override
        public void deserializeNBT(NBTBase nbt)
        { getCapability().readNBT(getInstance(), getFacing(), nbt); }
    }
}