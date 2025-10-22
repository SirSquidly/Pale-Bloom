package com.sirsquidly.palebloom;

import com.sirsquidly.palebloom.capabilities.CapabilityPaleGardenFog;
import com.sirsquidly.palebloom.init.JTPGBiomes;
import com.sirsquidly.palebloom.network.JTPGPacketFogCapability;
import com.sirsquidly.palebloom.network.JTPGPacketHandler;
import com.sirsquidly.palebloom.util.JTPGCapabilityUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class FogEvents
{
    /* Make sure these values are scaled based on `updateRate`! */
    private static final float fadeInSpeed = 0.01f;
    private static final float fadeOutSpeed = 0.04f;
    /* Tick rate to update by. */
    private static final int updateRate = 20;

    private static final boolean enableFog = Config.paleGarden.fog.enableFog;
    /* The furthest distance of the fog. */
    private static final float configFogFarDistance = (float) Config.paleGarden.fog.fogFarDistance;
    private static final boolean enableCreativeImmunity = Config.paleGarden.fog.creativeIgnoresFog;

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof EntityPlayer)
        { event.addCapability(CapabilityPaleGardenFog.ID, new CapabilityPaleGardenFog.Provider(new CapabilityPaleGardenFog.PaleGardenFogMethods(), CapabilityPaleGardenFog.PALE_GARDEN_FOG_CAP, null)); }
    }

    /** Immediately updates the client of the capability when logging in. */
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        CapabilityPaleGardenFog.ICapabilityPaleGardenFog capFog = JTPGCapabilityUtils.getCapability(event.player);
        if (capFog == null) return;

        JTPGPacketHandler.CHANNEL.sendTo(new JTPGPacketFogCapability(event.player.getEntityId(), capFog.isPlayerFogEnabled()), (EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END || !enableFog) return;

        EntityPlayer player = event.player;

        /* Only update every second. Interpolation of the fog makes this doable. */
        if (player.ticksExisted % updateRate != 0) return;

        if (!player.world.isRemote)
        {
            boolean inFogBiome = player.world.getBiome(player.getPosition()) == JTPGBiomes.PALE_GARDEN;
            JTPGCapabilityUtils.setPlayerFog(player, inFogBiome);
        }
        else
        {
            JTPGCapabilityUtils.updatePrevFogProgress(player);

            if (JTPGCapabilityUtils.getPlayerFog(player))
            { JTPGCapabilityUtils.shiftPlayerFogProgress(player, fadeInSpeed); }
            else
            { JTPGCapabilityUtils.shiftPlayerFogProgress(player, -fadeOutSpeed); }
        }
    }


    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderFog(EntityViewRenderEvent.RenderFogEvent event)
    {
        if (!enableFog) return;

        Entity entity = event.getEntity();
        if (!(entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entity;

        if (enableCreativeImmunity && player.isCreative()) return;

        float prev = JTPGCapabilityUtils.getPrevPlayerFogProgress(player);
        float current = JTPGCapabilityUtils.getPlayerFogProgress(player);

        float progress = (float) (prev + (current - prev) * (player.ticksExisted % updateRate + event.getRenderPartialTicks()) / updateRate);
        if (progress <= 0) return;

        float vanillaFogEnd = event.getFarPlaneDistance();
        float targetFogStart = 0.0F;

        float fogMaxDistance = (float) (vanillaFogEnd + (Config.paleGarden.fog.fogFarDistance - vanillaFogEnd) * progress);
        float fogMinDistance = vanillaFogEnd * 0.75F + (targetFogStart - vanillaFogEnd * 0.75F) * progress;

        GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
        GlStateManager.setFogStart(fogMinDistance);
        GlStateManager.setFogEnd(fogMaxDistance);
    }
}