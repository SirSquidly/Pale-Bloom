package com.sirsquidly.palebloom.capabilities;

import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public class CapabilityPaleGardenFog
{
    @CapabilityInject(ICapabilityPaleGardenFog.class)
    public static Capability<ICapabilityPaleGardenFog> PALE_GARDEN_FOG_CAP;
    public static final ResourceLocation ID = new ResourceLocation(paleBloom.MOD_ID, "paleGardenPlayerFog");

    /** If this player has a Nightmare haunting them. */
    private static final String FOG_ENABLED = "paleGardenPlayerFogEnabled";
    private static final String FOG_DISTANCE = "paleGardenPlayerFogProgress";
    private static final String PREV_FOG_DISTANCE = "paleGardenPrevPlayerFogProgress";

    public interface ICapabilityPaleGardenFog
    {
        boolean isPlayerFogEnabled();
        void setPlayerFogEnabled(boolean value);

        float getPlayerFogProgress();
        void setPlayerFogProgress(float value);

        float getPrevPlayerFogProgress();
        void setPrevPlayerFogProgress(float value);
    }

    public static class PaleGardenFogMethods implements ICapabilityPaleGardenFog
    {
        private boolean paleGardenPlayerFogEnabled = false;
        private float paleGardenPlayerFogProgress = 0;
        private float paleGardenPrevPlayerFogProgress = 0;

        @Override
        public boolean isPlayerFogEnabled()
        { return paleGardenPlayerFogEnabled; }
        @Override
        public void setPlayerFogEnabled(boolean value)
        { paleGardenPlayerFogEnabled = value; }

        @Override
        public float getPlayerFogProgress()
        { return paleGardenPlayerFogProgress; }
        @Override
        public void setPlayerFogProgress(float value)
        { paleGardenPlayerFogProgress = value; }

        @Override
        public float getPrevPlayerFogProgress()
        { return paleGardenPrevPlayerFogProgress; }
        @Override
        public void setPrevPlayerFogProgress(float value)
        { paleGardenPrevPlayerFogProgress = value; }
    }

    public static class Storage implements Capability.IStorage<ICapabilityPaleGardenFog>
    {
        @Override
        public NBTBase writeNBT(Capability<ICapabilityPaleGardenFog> capability, ICapabilityPaleGardenFog instance, EnumFacing side)
        {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setBoolean(FOG_ENABLED, instance.isPlayerFogEnabled());
            compound.setFloat(FOG_DISTANCE, instance.getPlayerFogProgress());
            compound.setFloat(PREV_FOG_DISTANCE, instance.getPrevPlayerFogProgress());
            return compound;
        }

        @Override
        public void readNBT(Capability<ICapabilityPaleGardenFog> capability, ICapabilityPaleGardenFog instance, EnumFacing side, NBTBase nbt)
        {
            NBTTagCompound compound = (NBTTagCompound) nbt;
            instance.setPlayerFogEnabled(compound.getBoolean(FOG_ENABLED));
            instance.setPlayerFogProgress(compound.getFloat(FOG_DISTANCE));
            instance.setPrevPlayerFogProgress(compound.getFloat(PREV_FOG_DISTANCE));
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTBase>
    {
        final Capability<ICapabilityPaleGardenFog> capability;
        final EnumFacing facing;
        final ICapabilityPaleGardenFog instance;

        public Provider(final ICapabilityPaleGardenFog instance, final Capability<ICapabilityPaleGardenFog> capability, @Nullable final EnumFacing facing)
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

        final Capability<ICapabilityPaleGardenFog> getCapability()
        { return capability; }

        EnumFacing getFacing()
        { return facing; }

        final ICapabilityPaleGardenFog getInstance()
        { return instance; }

        @Override
        public NBTBase serializeNBT()
        { return getCapability().writeNBT(getInstance(), getFacing()); }

        @Override
        public void deserializeNBT(NBTBase nbt)
        { getCapability().readNBT(getInstance(), getFacing(), nbt); }
    }
}