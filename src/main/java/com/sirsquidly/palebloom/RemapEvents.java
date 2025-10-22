package com.sirsquidly.palebloom;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
* During development, the mod ID used to be `justthepalegarden`, this just
 * converts all the mappings properly for worlds made in earlier dev versions.
* */
@Mod.EventBusSubscriber
public class RemapEvents
{
    private static final String OLD_MOD = "justthepalegarden";
    private static final String NEW_MOD = "palebloom";


    @SubscribeEvent
    public static void onMissingBiome(RegistryEvent.MissingMappings<Biome> event) {
        for (RegistryEvent.MissingMappings.Mapping<Biome> mapping : event.getAllMappings()) {
            if (mapping.key.getNamespace().equals(OLD_MOD)) {
                mapping.remap(ForgeRegistries.BIOMES.getValue(new ResourceLocation("palebloom", mapping.key.getPath())));
            }
        }
    }

    @SubscribeEvent
    public static void onMissingBlock(RegistryEvent.MissingMappings<Block> event) {
        for (RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getAllMappings()) {
            if (mapping.key.getNamespace().equals(OLD_MOD)) {
                mapping.remap(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("palebloom", mapping.key.getPath())));
            }
        }
    }

    @SubscribeEvent
    public static void onMissingEntity(RegistryEvent.MissingMappings<EntityEntry> event) {
        for (RegistryEvent.MissingMappings.Mapping<EntityEntry> mapping : event.getAllMappings()) {
            if (mapping.key.getNamespace().equals(OLD_MOD)) {
                mapping.remap(ForgeRegistries.ENTITIES.getValue(new ResourceLocation("palebloom", mapping.key.getPath())));
            }
        }
    }

    @SubscribeEvent
    public static void onMissingItem(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getAllMappings()) {
            if (mapping.key.getNamespace().equals(OLD_MOD)) {
                mapping.remap(ForgeRegistries.ITEMS.getValue(new ResourceLocation("palebloom", mapping.key.getPath())));
            }
        }
    }

    @SubscribeEvent
    public static void onMissingSound(RegistryEvent.MissingMappings<SoundEvent> event) {
        for (RegistryEvent.MissingMappings.Mapping<SoundEvent> mapping : event.getAllMappings()) {
            if (mapping.key.getNamespace().equals(OLD_MOD)) {
                mapping.remap(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("palebloom", mapping.key.getPath())));
            }
        }
    }

    @SubscribeEvent
    public static void onMissingPotion(RegistryEvent.MissingMappings<Potion> event) {
        for (RegistryEvent.MissingMappings.Mapping<Potion> mapping : event.getAllMappings()) {
            if (mapping.key.getNamespace().equals(OLD_MOD)) {
                mapping.remap(ForgeRegistries.POTIONS.getValue(new ResourceLocation("palebloom", mapping.key.getPath())));
            }
        }
    }
}