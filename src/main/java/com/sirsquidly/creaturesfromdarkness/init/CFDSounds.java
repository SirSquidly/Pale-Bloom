package com.sirsquidly.creaturesfromdarkness.init;

import com.sirsquidly.creaturesfromdarkness.creaturesfromdarkness;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class CFDSounds
{
    private static List<SoundEvent> soundList = new ArrayList<SoundEvent>();

    public static SoundEvent AMBIENT_DREAM = soundReadyForRegister("ambient.dream");

    public static SoundEvent ENTITY_NIGHTMARE_AMBIENT = soundReadyForRegister("entity.nightmare.ambient");
    public static SoundEvent ENTITY_NIGHTMARE_DEATH = soundReadyForRegister("entity.nightmare.death");
    public static SoundEvent ENTITY_NIGHTMARE_HURT = soundReadyForRegister("entity.nightmare.hurt");
    public static SoundEvent ENTITY_NIGHTMARE_WARN = soundReadyForRegister("entity.nightmare.warn");
    public static SoundEvent ENTITY_NIGHTMARE_STEP = soundReadyForRegister("entity.nightmare.step");

    public static SoundEvent ENTITY_SHADOW_DEATH = soundReadyForRegister("entity.shadow.death");
    public static SoundEvent ENTITY_SHADOW_HURT = soundReadyForRegister("entity.shadow.hurt");

    public static void registerSounds()
    { for (SoundEvent sounds : soundList) ForgeRegistries.SOUND_EVENTS.register(sounds); }

    private static SoundEvent soundReadyForRegister(String name)
    {
        ResourceLocation location = new ResourceLocation(creaturesfromdarkness.MOD_ID, name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(name);
        soundList.add(event);

        return event;
    }
}