package com.sirsquidly.palebloom.init;

import com.sirsquidly.palebloom.blocks.tileentity.*;
import com.sirsquidly.palebloom.client.render.*;
import com.sirsquidly.palebloom.entity.*;
import com.sirsquidly.palebloom.entity.item.*;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class JTPGEntities
{
    public static int id;

    public static void registerEntities()
    {
        registerEntity("creaking", EntityCreaking.class, ++id, 60, 6250335, 16545810);
        registerEntity("reaping_willow", EntityReapingWillow.class, ++id, 60, 14210259, 16545810);
        registerEntity("pale_creeper", EntityPaleCreeper.class, ++id, 81, 9212553, 1776152);
        registerEntity("pale_painting", EntityPalePainting.class, ++id, 81);
        registerEntity("mannequin", EntityMannequin.class, ++id, 60);
        registerEntity("hydraweed_jaw", EntityHydraweedJaw.class, ++id, 81, 6250335, 1776152);

        registerEntity("pale_oak_boat", EntityPaleBoat.class, ++id, 60);
        registerEntity("thorn", EntityThorn.class, ++id, 60);
        registerEntity("seed_bomb", EntitySeedBomb.class, ++id, 80);
    }

    public static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileCreakingHeart.class, new ResourceLocation(paleBloom.MOD_ID, "creaking_heart"));
        GameRegistry.registerTileEntity(TileHydraweedBody.class, new ResourceLocation(paleBloom.MOD_ID, "hydraweed"));
        GameRegistry.registerTileEntity(TileIncenseThorn.class, new ResourceLocation(paleBloom.MOD_ID, "incense_thorns"));
        GameRegistry.registerTileEntity(TilePaleOakHollow.class, new ResourceLocation(paleBloom.MOD_ID, "pale_oak_hollow"));
        GameRegistry.registerTileEntity(TilePollenhead.class, new ResourceLocation(paleBloom.MOD_ID, "pollenhead"));
        GameRegistry.registerTileEntity(TileReapingWillowSapling.class, new ResourceLocation(paleBloom.MOD_ID, "reaping_willow_sapling"));
        GameRegistry.registerTileEntity(TileResinBulb.class, new ResourceLocation(paleBloom.MOD_ID, "resin_bulb"));
        //GameRegistry.registerTileEntity(TileMoonbrewbloom.class, new ResourceLocation(palebloom.MOD_ID, "moonbrewbloom"));
    }

    /** NOTE: Pale Creeper Spawning is handled by an event to replace vanilla Creeper spawns in the Garden. */
    public static void registerEntitySpawns()
    {
        //EntityRegistry.addSpawn(EntityPaleCreeper.class, 80, 1, 2, EnumCreatureType.MONSTER, JTPGBiomes.PALE_GARDEN);
    }

    @SideOnly(Side.CLIENT)
    public static void RegisterRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityCreaking.class, RenderCreaking::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityMannequin.class, RenderMannequin::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPaleCreeper.class, RenderPaleCreeper::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPalePainting.class, RenderPalePainting::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReapingWillow.class, RenderReapingWillow::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHydraweedJaw.class, RenderSnapweedJaw::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPaleBoat.class, RenderPaleOakBoat::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityThorn.class, RenderThorn::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySeedBomb.class, RenderSeedBomb::new);
    }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, int color1, int color2)
    { net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(new ResourceLocation(paleBloom.MOD_ID, name), entity, paleBloom.MOD_ID + "." + name, id, paleBloom.instance, range, 1, true, color1, color2); }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range)
    { net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(new ResourceLocation(paleBloom.MOD_ID, name), entity, paleBloom.MOD_ID + "." + name, id, paleBloom.instance, range, 1, true); }
}