package com.sirsquidly.palebloom.init;

import com.sirsquidly.palebloom.paleBloom;
import com.sirsquidly.palebloom.potion.PotionAmberEyes;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = paleBloom.MOD_ID)
public class JTPGPotions
{
    public static final Potion AMBEREYES = new PotionAmberEyes("ambereyes", false, 16234054, 0);

    public static final PotionType AMBER_EYES_POTION = new PotionType(paleBloom.MOD_ID + "." + "ambereyes", new PotionEffect[] { new PotionEffect(AMBEREYES, 3600)} ).setRegistryName("ambereyes");

    @SubscribeEvent
    public static void onPotionTypeRegister(RegistryEvent.Register<PotionType> event)
    {
        event.getRegistry().register(AMBER_EYES_POTION);

        registerBrewingRecipes();
    }

    @SubscribeEvent
    public static void onPotionEffectRegister(RegistryEvent.Register<Potion> event)
    {
        event.getRegistry().register(AMBEREYES);
    }

    public static void registerBrewingRecipes()
    {
        PotionHelper.addMix(PotionTypes.AWKWARD, JTPGItems.LIVE_ROOT, AMBER_EYES_POTION);
    }
}