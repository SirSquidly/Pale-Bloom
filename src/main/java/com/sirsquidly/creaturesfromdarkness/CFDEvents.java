package com.sirsquidly.creaturesfromdarkness;

import com.sirsquidly.creaturesfromdarkness.capabilities.CapabilityNightmare;
import com.sirsquidly.creaturesfromdarkness.entity.EntityNightmare;
import com.sirsquidly.creaturesfromdarkness.entity.EntityShadow;
import com.sirsquidly.creaturesfromdarkness.init.CFDItems;
import com.sirsquidly.creaturesfromdarkness.init.CFDSounds;
import com.sirsquidly.creaturesfromdarkness.item.ItemNightmareLeg;
import com.sirsquidly.creaturesfromdarkness.util.CFDCapabilityUtils;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static net.minecraft.block.BlockDoor.HALF;

@Mod.EventBusSubscriber
public class CFDEvents
{
    private static final ResourceLocation DREAM_SHADER = new ResourceLocation(creaturesfromdarkness.MOD_ID, "shaders/post/dream_100.json");
    private static final ResourceLocation DREAM_SHADER_HALF = new ResourceLocation(creaturesfromdarkness.MOD_ID, "shaders/post/dream_50.json");

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.getRenderViewEntity();

        if (event.phase != TickEvent.Phase.END) return;

        if (entity instanceof EntityPlayer && ((EntityPlayer)entity).hasCapability(CapabilityNightmare.RIPTIDE_CAP, null))
        {
            CapabilityNightmare.ICapabilityRiptide capNightmare = ((EntityPlayer)entity).getCapability(CapabilityNightmare.RIPTIDE_CAP, null);

            if (capNightmare.getNightmareNearby() > 0)
            {
                if (!isShaderLoaded()) enableShader();
            }
            else if (isShaderLoaded()) disableShader();
        }
        else if (isShaderLoaded()) disableShader();
    }

    private static void enableShader()
    {
        Minecraft.getMinecraft().entityRenderer.loadShader(DREAM_SHADER);
    }

    private static boolean isShaderLoaded() {
        ShaderGroup shader = Minecraft.getMinecraft().entityRenderer.getShaderGroup();
        return shader != null && DREAM_SHADER.toString().equals(shader.getShaderGroupName());
    }

    private static void disableShader()
    {
        Minecraft mc = Minecraft.getMinecraft();
        mc.entityRenderer.loadEntityShader(mc.getRenderViewEntity());
    }

    /** An Event to allow Nightmare Spines to be used by other entities. */
    @SubscribeEvent
    public static void onAttackWithSpine(LivingHurtEvent event)
    {
        if (event.getSource().getTrueSource() instanceof EntityLivingBase)
        {
            EntityLivingBase entity = (EntityLivingBase)event.getSource().getTrueSource();

            if (entity.getHeldItemMainhand().getItem() == CFDItems.NIGHTMARE_LEG)
            { ItemNightmareLeg.doHitEffect(entity.getHeldItemMainhand(), event.getEntityLiving(), entity); }
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.PlaceEvent event)
    {
        EntityPlayer player = event.getPlayer();
        IBlockState placedState = event.getPlacedBlock();

        if (player == null || player.world.isRemote) return;

        if (player.hasCapability(CapabilityNightmare.RIPTIDE_CAP, null))
        {
            CapabilityNightmare.ICapabilityRiptide capNightmare = player.getCapability(CapabilityNightmare.RIPTIDE_CAP, null);

            if (capNightmare.getNightmareOnPlayer() && capNightmare.getNightmareNearby() > 0)
            {
                ItemStack heldItem = event.getItemInHand();

                if (placedState.getLightValue(player.world, event.getPos()) > 0 && !heldItem.isEmpty())
                {
                    int cooldownTime = 200;
                    player.getCooldownTracker().setCooldown(heldItem.getItem(), cooldownTime);
                }
            }
        }
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof EntityPlayer)
        {
            event.addCapability(CapabilityNightmare.ID, new CapabilityNightmare.Provider(new CapabilityNightmare.RiptideMethods(), CapabilityNightmare.RIPTIDE_CAP, null));
        }
    }

    /** Bonus security check */
    @SubscribeEvent
    public static void PlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            EntityPlayer player = event.player;
            World world = player.world;

            if (player.hasCapability(CapabilityNightmare.RIPTIDE_CAP, null))
            {
                CapabilityNightmare.ICapabilityRiptide capNightmare = player.getCapability(CapabilityNightmare.RIPTIDE_CAP, null);

                if (capNightmare.getNightmareOnPlayer())
                {
                    /* We don't need to do these checks CONSTANTLY, break it up, time it! */
                    if (world.getWorldTime() % 20 == 0)
                    {
                        /* Ticks down the overall timer for a Haunting, so it can end automatically after enough time. */
                        capNightmare.setNightmareOverallTimer(capNightmare.getNightmareOverallTimer() - 1);
                        if (capNightmare.getNightmareOverallTimer() <= 0)
                        {
                            if (player.world.isRemote) world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.AMBIENT, 0.4F, 0.4F);
                            if (!player.world.isRemote) player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 20, 0, true, false));
                            CFDCapabilityUtils.removeNightmarePackage(player);

                            return;
                        }

                        boolean playerInDarkness = world.getLightFor(EnumSkyBlock.BLOCK, new BlockPos(player)) <= 7;
                        boolean playerHoldingSpine = player.getHeldItemMainhand().getItem() == CFDItems.NIGHTMARE_LEG;

                        List<Entity> checkForNightmare = player.world.getEntitiesWithinAABB(EntityNightmare.class, player.getEntityBoundingBox().grow(32, 32, 32));

                        if (!checkForNightmare.isEmpty())
                        {
                            for (Entity nightmare: checkForNightmare)
                            {
                                if (world.getWorldTime() % 140 == 0 && world.isRemote) world.playSound(player, player.posX, player.posY, player.posZ, CFDSounds.AMBIENT_DREAM, SoundCategory.AMBIENT, 1.0F, 1.0F);

                                if (player.getDistance(nightmare) < 10)
                                {
                                    capNightmare.setNightmareNearby(1);

                                    if (!player.world.isRemote) player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 40, 0, true, false));
                                }
                                else
                                { capNightmare.setNightmareNearby(2); }
                            }
                        }
                        else
                        {
                            if (capNightmare.getNightmareNearby() > 0) capNightmare.setNightmareNearby(0);

                            capNightmare.setNightmareSpawnTimer(capNightmare.getNightmareSpawnTimer() - (1 + (playerInDarkness ? 1 : 0) * (playerHoldingSpine ? 2 : 1)));

                            if (capNightmare.getNightmareSpawnTimer() <= 0)
                            {
                                /* Only a 90% chance to warn a player of a Nightmare Spawn */
                                if (world.rand.nextFloat() > 0.1F)
                                {
                                    //if (!player.isPotionActive(MobEffects.BLINDNESS)) player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 21, 0, true, false));
                                    if (world.isRemote) world.playSound(player, player.posX, player.posY, player.posZ, CFDSounds.ENTITY_NIGHTMARE_WARN, SoundCategory.HOSTILE, 1.0F, 1.0F);
                                }

                                spawnEntityNearPlayer(player, new EntityNightmare(world), 30, 16);

                                /* Cooldown is between 1-5 minutes default */
                                capNightmare.setNightmareSpawnTimer(60 + world.rand.nextInt(240));
                            }
                        }


                        capNightmare.setNightmareEventTimer(capNightmare.getNightmareEventTimer() - 1);

                        if (capNightmare.getNightmareEventTimer() <= 0)
                        {
                            /* Removes the nearby effect given from a fresh infection. */
                            if (capNightmare.getNightmareNearby() == 3) capNightmare.setNightmareNearby(0);

                            float getEvent = world.rand.nextFloat();

                            if (getEvent > 0.75F)
                            {
                                spawnEntityNearPlayer(player, new EntityShadow(world), 20, 4);
                            }
                            else if (getEvent > 0.5F)
                            {
                                openNearbyDoor(world, new BlockPos(player));
                            }
                            else
                            {
                                if (world.isRemote) playNearbySound(player, world);
                            }

                            /*10 to 20 seconds cooldown */
                            capNightmare.setNightmareEventTimer(10 + world.rand.nextInt(10));
                        }
                    }
                }
            }
        }
    }

    /** A Universal method for spawning stuff near a player.
     * `spawnDistance` is blocks nearby to attempt to spawn
     * */
    public static void spawnEntityNearPlayer(EntityPlayer player, EntityLiving entity, int spawnMaxDistance, int spawnMinDistance)
    {
        World world = player.world;
        if (world.isRemote) return;
        BlockPos pos = new BlockPos(player);
        int maxAttempts = 80;

        for (int i = 0; i < maxAttempts; i++)
        {
            int offsetX = world.rand.nextInt(spawnMaxDistance * 2) - spawnMaxDistance;
            int offsetZ = world.rand.nextInt(spawnMaxDistance * 2) - spawnMaxDistance;
            int offsetY = world.rand.nextInt(5) - 2;
            BlockPos spawnPos = pos.add(offsetX, offsetY, offsetZ);

            if (spawnPos.distanceSq(pos) < spawnMinDistance * spawnMinDistance) continue;
            entity.setLocationAndAngles(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, world.rand.nextFloat() * 360.0F, 0.0F);

            if (world.checkNoEntityCollision(entity.getEntityBoundingBox(), entity) && world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty() && world.getLightFor(EnumSkyBlock.BLOCK, spawnPos) <= 7)
            {
                BlockPos groundPos = spawnPos.down();
                if (world.getBlockState(groundPos).isSideSolid(world, groundPos, EnumFacing.UP))
                {
                    entity.setAttackTarget(player);

                    if (entity instanceof EntityNightmare)
                    {
                        ((EntityNightmare)entity).setIsAttacking(true);
                        /* Exists for about 10-30 seconds */
                        ((EntityNightmare)entity).setLimitedLife((10 + world.rand.nextInt(20)) * 20);
                        ((EntityNightmare)entity).setPreyId(player.getUniqueID());

                        if (player.hasCapability(CapabilityNightmare.RIPTIDE_CAP, null))
                        {
                            CapabilityNightmare.ICapabilityRiptide capNightmare = player.getCapability(CapabilityNightmare.RIPTIDE_CAP, null);

                            ((EntityNightmare)entity).setHealth(capNightmare.getNightmareHealth());
                        }
                    }

                    world.spawnEntity(entity);
                    break;
                }
            }
        }
    }

    public static void openNearbyDoor(World world, BlockPos pos)
    {
        /* Skip client-side updates*/
        if (world.isRemote) return;

        double radius = 16;

        for (double h1 = -radius; h1 <= radius; h1++)
        {
            for (double i1 = -radius; i1 <= radius; i1++)
            {
                for (double j1 = -radius; j1 <= radius; j1++)
                {
                    BlockPos tPos = pos.add(h1, i1, j1);
                    /* Curves the interaction radius. */
                    if (pos.distanceSq(tPos) > radius * radius) continue;
                    Block block = world.getBlockState(tPos).getBlock();

                    if (block instanceof BlockTrapDoor || block instanceof BlockDoor && world.getBlockState(tPos).getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER)
                    {
                        block.onBlockActivated(world, tPos, world.getBlockState(tPos), null, EnumHand.MAIN_HAND, EnumFacing.UP, 0.5F, 0.5F, 0.5F);
                        return;
                    }
                }
            }
        }
    }

    public static void playNearbySound(EntityPlayer player, World worldIn)
    {
        double soundX = player.posX + worldIn.rand.nextDouble() * 10;
        double soundY = player.posY + worldIn.rand.nextDouble() * 10;
        double soundZ = player.posZ + worldIn.rand.nextDouble() * 10;

        worldIn.playSound(player, soundX, soundY, soundZ, getRandomHostileSound(worldIn), SoundCategory.AMBIENT, 1.0F, worldIn.rand.nextFloat());
    }

    private static SoundEvent getRandomHostileSound(World world)
    {
        SoundEvent[] overworldMobSounds = new SoundEvent[]
        {
                CFDSounds.ENTITY_SHADOW_HURT,
                CFDSounds.ENTITY_SHADOW_HURT,
                CFDSounds.ENTITY_SHADOW_HURT,
                CFDSounds.ENTITY_NIGHTMARE_AMBIENT,
                CFDSounds.ENTITY_NIGHTMARE_AMBIENT,
                SoundEvents.AMBIENT_CAVE,
                SoundEvents.AMBIENT_CAVE,
                SoundEvents.ENTITY_ZOMBIE_AMBIENT,
                SoundEvents.ENTITY_ZOMBIE_AMBIENT,
                SoundEvents.ENTITY_ZOMBIE_AMBIENT,
                SoundEvents.ENTITY_SKELETON_AMBIENT,
                SoundEvents.ENTITY_SKELETON_AMBIENT,
                SoundEvents.ENTITY_SKELETON_AMBIENT,
                SoundEvents.ENTITY_ENDERMEN_SCREAM,
                SoundEvents.ENTITY_CREEPER_HURT,
                SoundEvents.ENTITY_CREEPER_PRIMED
        };

        SoundEvent[] netherMobSounds = new SoundEvent[]
        {
                CFDSounds.ENTITY_SHADOW_HURT,
                CFDSounds.ENTITY_SHADOW_HURT,
                CFDSounds.ENTITY_NIGHTMARE_AMBIENT,
                SoundEvents.AMBIENT_CAVE,
                SoundEvents.ENTITY_ZOMBIE_PIG_AMBIENT,
                SoundEvents.ENTITY_ZOMBIE_PIG_AMBIENT,
                SoundEvents.ENTITY_ZOMBIE_PIG_ANGRY,
                SoundEvents.ENTITY_ZOMBIE_PIG_ANGRY,
                SoundEvents.ENTITY_GHAST_AMBIENT,
                SoundEvents.ENTITY_GHAST_WARN
        };

        SoundEvent[] endMobSounds = new SoundEvent[]
        {
                CFDSounds.ENTITY_SHADOW_HURT,
                CFDSounds.ENTITY_SHADOW_HURT,
                CFDSounds.ENTITY_NIGHTMARE_AMBIENT,
                SoundEvents.AMBIENT_CAVE,
                SoundEvents.AMBIENT_CAVE,
                SoundEvents.ENTITY_ENDERMEN_SCREAM,
                SoundEvents.ENTITY_ENDERMEN_STARE
        };

        switch (world.provider.getDimension())
        {
            case -1:
                return netherMobSounds[world.rand.nextInt(netherMobSounds.length)];
            case 1:
                return endMobSounds[world.rand.nextInt(endMobSounds.length)];
            default:
                return overworldMobSounds[world.rand.nextInt(overworldMobSounds.length)];
        }
    }
}