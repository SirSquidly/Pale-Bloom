package com.sirsquidly.palebloom.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

/**
 * Adds Nametag rendering for GeckoLib entities
 * */
public class RenderGeoExtended<T extends EntityLivingBase & IAnimatable> extends GeoEntityRenderer<T>
{
    public static float NAME_TAG_RANGE = 64.0f;
    public static float NAME_TAG_RANGE_SNEAK = 32.0f;

    protected RenderGeoExtended(RenderManager renderManager, AnimatedGeoModel<T> modelProvider)
    {
        super(renderManager, modelProvider);
    }

    /** Some stupid stuff used for Eye Glowing, since every entity in this mod uses Eye glowing. */
    public ResourceLocation getEntityEmissiveModel(T entity) { return null; }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.renderName(entity, x, y, z);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    public void renderName(T entity, double x, double y, double z)
    {
        if (this.canRenderName(entity))
        {
            double entityRenderDistance = entity.getDistanceSq(this.renderManager.renderViewEntity);
            float f = entity.isSneaking() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;

            if (entityRenderDistance < (double)(f * f))
            {
                String s = entity.getDisplayName().getFormattedText();
                GlStateManager.alphaFunc(516, 0.1F);
                this.renderEntityName(entity, x, y, z, s, entityRenderDistance);
            }
        }
    }

    protected boolean canRenderName(T entity)
    {
        EntityPlayerSP entityplayersp = Minecraft.getMinecraft().player;
        boolean flag = !entity.isInvisibleToPlayer(entityplayersp);

        Team team = entity.getTeam();
        Team team1 = entityplayersp.getTeam();

        if (team != null)
        {
            Team.EnumVisible team$enumvisible = team.getNameTagVisibility();

            switch (team$enumvisible)
            {
                case ALWAYS:
                    return flag;
                case NEVER:
                    return false;
                case HIDE_FOR_OTHER_TEAMS:
                    return team1 == null ? flag : team.isSameTeam(team1) && (team.getSeeFriendlyInvisiblesEnabled() || flag);
                case HIDE_FOR_OWN_TEAM:
                    return team1 == null ? flag : !team.isSameTeam(team1) && flag;
                default:
                    return true;
            }
        }

        return (entity.getAlwaysRenderNameTagForRender() || (entity.hasCustomName() && entity == this.renderManager.pointedEntity)) && Minecraft.isGuiEnabled() && entity != this.renderManager.renderViewEntity && flag && !entity.isBeingRidden();
    }
}