package com.sirsquidly.palebloom.init;

import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class JTPGSounds
{
    private static List<SoundEvent> soundList = new ArrayList<SoundEvent>();

    public static SoundEvent BLOCK_CREAKING_HEART_BREAK = soundReadyForRegister("block.creaking_heart.break");
    public static SoundEvent BLOCK_CREAKING_HEART_FALL = soundReadyForRegister("block.creaking_heart.fall");
    public static SoundEvent BLOCK_CREAKING_HEART_HIT = soundReadyForRegister("block.creaking_heart.hit");
    public static SoundEvent BLOCK_CREAKING_HEART_PLACE = soundReadyForRegister("block.creaking_heart.place");
    public static SoundEvent BLOCK_CREAKING_HEART_STEP = soundReadyForRegister("block.creaking_heart.step");

    public static SoundEvent BLOCK_CREAKING_HEART_AMBIENT = soundReadyForRegister("block.creaking_heart.ambient");
    public static SoundEvent BLOCK_CREAKING_HEART_SPAWN_MOB = soundReadyForRegister("block.creaking_heart.spawn");
    public static SoundEvent BLOCK_CREAKING_HEART_TRAIL = soundReadyForRegister("block.creaking_heart.trail");

    public static SoundType CREAKING_HEART = new SoundType(1.0f, 1.0f, BLOCK_CREAKING_HEART_BREAK, BLOCK_CREAKING_HEART_STEP, BLOCK_CREAKING_HEART_PLACE, BLOCK_CREAKING_HEART_HIT, BLOCK_CREAKING_HEART_FALL);

    public static SoundEvent BLOCK_MOSS_BREAK = soundReadyForRegister("block.moss.break");
    public static SoundEvent BLOCK_MOSS_FALL = soundReadyForRegister("block.moss.fall");
    public static SoundEvent BLOCK_MOSS_HIT = soundReadyForRegister("block.moss.hit");
    public static SoundEvent BLOCK_MOSS_PLACE = soundReadyForRegister("block.moss.place");
    public static SoundEvent BLOCK_MOSS_STEP = soundReadyForRegister("block.moss.step");

    public static SoundType MOSS = new SoundType(1.0f, 1.0f, BLOCK_MOSS_BREAK, BLOCK_MOSS_STEP, BLOCK_MOSS_PLACE, BLOCK_MOSS_HIT, BLOCK_MOSS_FALL);

    public static SoundEvent BLOCK_NIGHTLIGHT_CLOSE = soundReadyForRegister("block.nightlight.close");
    public static SoundEvent BLOCK_NIGHTLIGHT_OPEN = soundReadyForRegister("block.nightlight.open");

    public static SoundEvent BLOCK_PALE_HANGING_MOSS_AMBIENT = soundReadyForRegister("block.pale_hanging_moss.ambient");

    public static SoundEvent BLOCK_POLLENHEAD_AMBIENT = soundReadyForRegister("block.pollenhead.ambient");
    public static SoundEvent BLOCK_POLLENHEAD_AWAKEN = soundReadyForRegister("block.pollenhead.awake");
    public static SoundEvent BLOCK_POLLENHEAD_CLOSE = soundReadyForRegister("block.pollenhead.close");
    public static SoundEvent BLOCK_POLLENHEAD_CLOSE_LUCID = soundReadyForRegister("block.pollenhead.close_lucid");
    public static SoundEvent BLOCK_POLLENHEAD_OPEN = soundReadyForRegister("block.pollenhead.open");

    public static SoundEvent BLOCK_PLANT_RESIN_DRAW = soundReadyForRegister("block.plant.resin_draw");

    public static SoundEvent BLOCK_RESIN_BREAK = soundReadyForRegister("block.resin.break");
    public static SoundEvent BLOCK_RESIN_FALL = soundReadyForRegister("block.resin.fall");
    public static SoundEvent BLOCK_RESIN_HIT = soundReadyForRegister("block.resin.hit");
    public static SoundEvent BLOCK_RESIN_PLACE = soundReadyForRegister("block.resin.place");
    public static SoundEvent BLOCK_RESIN_STEP = soundReadyForRegister("block.resin.step");

    public static SoundType RESIN = new SoundType(1.0f, 1.0f, BLOCK_RESIN_BREAK, BLOCK_RESIN_STEP, BLOCK_RESIN_PLACE, BLOCK_RESIN_HIT, BLOCK_RESIN_FALL);

    public static SoundEvent BLOCK_RESIN_BRICK_BREAK = soundReadyForRegister("block.resin_brick.break");
    public static SoundEvent BLOCK_RESIN_BRICK_FALL = soundReadyForRegister("block.resin_brick.fall");
    public static SoundEvent BLOCK_RESIN_BRICK_HIT = soundReadyForRegister("block.resin_brick.hit");
    public static SoundEvent BLOCK_RESIN_BRICK_PLACE = soundReadyForRegister("block.resin_brick.place");
    public static SoundEvent BLOCK_RESIN_BRICK_STEP = soundReadyForRegister("block.resin_brick.step");

    public static SoundType RESIN_BRICK = new SoundType(1.0f, 1.0f, BLOCK_RESIN_BRICK_BREAK, BLOCK_RESIN_BRICK_STEP, BLOCK_RESIN_BRICK_PLACE, BLOCK_RESIN_BRICK_HIT, BLOCK_RESIN_BRICK_FALL);

    public static SoundEvent BLOCK_RESIN_BULB_DEFLATE = soundReadyForRegister("block.resin_bulb.deflate");
    public static SoundEvent BLOCK_RESIN_BULB_INFLATE = soundReadyForRegister("block.resin_bulb.inflate");

    public static SoundEvent BLOCK_EYEBLOSSOM_AMBIENT = soundReadyForRegister("block.eyeblossom.ambient");
    public static SoundEvent BLOCK_EYEBLOSSOM_CLOSE = soundReadyForRegister("block.eyeblossom.close");
    public static SoundEvent BLOCK_EYEBLOSSOM_CLOSE_LONG = soundReadyForRegister("block.eyeblossom.close_long");
    public static SoundEvent BLOCK_EYEBLOSSOM_OPEN = soundReadyForRegister("block.eyeblossom.open");
    public static SoundEvent BLOCK_EYEBLOSSOM_OPEN_LONG = soundReadyForRegister("block.eyeblossom.open_long");

    public static SoundEvent ENTITY_CREAKING_AMBIENT = soundReadyForRegister("entity.creaking.ambient");
    public static SoundEvent ENTITY_CREAKING_ATTACK = soundReadyForRegister("entity.creaking.attack");
    public static SoundEvent ENTITY_CREAKING_DEATH = soundReadyForRegister("entity.creaking.death");
    public static SoundEvent ENTITY_CREAKING_FREEZE = soundReadyForRegister("entity.creaking.freeze");
    public static SoundEvent ENTITY_CREAKING_NOTARGET = soundReadyForRegister("entity.creaking.notarget");
    public static SoundEvent ENTITY_CREAKING_SPAWN = soundReadyForRegister("entity.creaking.spawn");
    public static SoundEvent ENTITY_CREAKING_STEP = soundReadyForRegister("entity.creaking.step");
    public static SoundEvent ENTITY_CREAKING_SWAY = soundReadyForRegister("entity.creaking.sway");
    public static SoundEvent ENTITY_CREAKING_TARGET = soundReadyForRegister("entity.creaking.target");
    public static SoundEvent ENTITY_CREAKING_TWITCH = soundReadyForRegister("entity.creaking.twitch");
    public static SoundEvent ENTITY_CREAKING_UNFREEZE = soundReadyForRegister("entity.creaking.unfreeze");

    public static SoundEvent ENTITY_HYDRAWEED_JAW_DEATH = soundReadyForRegister("entity.hydraweed_jaw.death");
    public static SoundEvent ENTITY_HYDRAWEED_JAW_HURT = soundReadyForRegister("entity.hydraweed_jaw.hurt");

    public static SoundEvent ENTITY_MANNEQUIN_BREAK = soundReadyForRegister("entity.mannequin.break");
    public static SoundEvent ENTITY_MANNEQUIN_PLACE = soundReadyForRegister("entity.mannequin.place");
    public static SoundEvent ENTITY_MANNEQUIN_SWAY = soundReadyForRegister("entity.mannequin.sway");

    public static SoundEvent ENTITY_REAPING_WILLOW_ANGER = soundReadyForRegister("entity.reaping_willow.angry");
    public static SoundEvent ENTITY_REAPING_WILLOW_HURT = soundReadyForRegister("entity.reaping_willow.hurt");
    public static SoundEvent ENTITY_REAPING_WILLOW_DEATH = soundReadyForRegister("entity.reaping_willow.death");
    public static SoundEvent ENTITY_REAPING_WILLOW_STEP = soundReadyForRegister("entity.reaping_willow.step");

    public static SoundEvent EVENT_GARDEN_CALL = soundReadyForRegister("event.garden.call");

    public static void registerSounds()
    { for (SoundEvent sounds : soundList) ForgeRegistries.SOUND_EVENTS.register(sounds); }

    private static SoundEvent soundReadyForRegister(String name)
    {
        ResourceLocation location = new ResourceLocation(paleBloom.MOD_ID, name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(name);
        soundList.add(event);

        return event;
    }
}