package com.sirsquidly.palebloom.common.potion;

import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.init.JTPGPotions;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class PotionAmberEyes extends Potion
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/gui/potion_effects.png");

    public PotionAmberEyes(String name, boolean isBad, int color, int icon)
    {
        super(isBad, color);
        this.setPotionName("effect." + paleBloom.MOD_ID + "." + name + ".name");
        this.setRegistryName(name);
        this.setIconIndex(icon % 8, icon / 8);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        World world = mc.world;

        // If no player/world or player doesn't have Speed effect, stop
        if (player == null || world == null) return;
        if (!player.isPotionActive(JTPGPotions.AMBEREYES)) return;
        if (player.ticksExisted % 20 != 0) return;

        //System.out.print("Speedt");

        // Search radius for entities near dirt
        double radius = 32.0D;
        AxisAlignedBB box = new AxisAlignedBB(player.posX - radius, player.posY - radius, player.posZ - radius,player.posX + radius, player.posY + radius, player.posZ + radius);

        // Loop all entities in range
        for (EntityLivingBase entity : world.getEntitiesWithinAABB(EntityLivingBase.class, box, e -> e != player))
        {
            if (nearEyeblossom(world, entity.getPosition(), 3))
            { paleBloom.proxy.spawnParticle(4, world, entity.posX, entity.posY, entity.posZ, 0, 0, 0, 1 + (int)(entity.width * 10)); }
        }
    }

    /**
     * Checks if there's Dirt within 1 block horizontally & vertically around position.
     */
    private static boolean nearEyeblossom(World world, BlockPos entityPos, int distance)
    {
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
        for (int dx = -distance; dx <= distance; dx++)
        {
            for (int dy = -distance; dy <= distance; dy++)
            {
                for (int dz = -distance; dz <= distance; dz++)
                {
                    checkPos.setPos(entityPos.getX() + dx, entityPos.getY() + dy, entityPos.getZ() + dz);

                    if (world.getBlockState(checkPos).getBlock() == JTPGBlocks.EYEBLOSSOM_OPEN)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /* Required so the effect actually runs. */
    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public int getStatusIconIndex()
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        return super.getStatusIconIndex();
    }
}