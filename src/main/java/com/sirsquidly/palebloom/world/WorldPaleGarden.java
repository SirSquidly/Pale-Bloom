package com.sirsquidly.palebloom.world;

import com.sirsquidly.palebloom.Config;
import com.sirsquidly.palebloom.blocks.tileentity.TileResinBulb;
import com.sirsquidly.palebloom.entity.EntityCreaking;
import com.sirsquidly.palebloom.entity.EntityPaleCreeper;
import com.sirsquidly.palebloom.entity.EntityReapingWillow;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WorldPaleGarden
{
    private static final boolean enableResinBulb = Config.block.resinBulb.enableResinBulb;

    /** Call nearby Reapings to defend. */
    public static void alertReapingWillow(World world, BlockPos pos, EntityLivingBase target, int distance)
    {
        if (!target.isEntityAlive() || world.isRemote) return;
        boolean flag = true;

        for (EntityReapingWillow entityReapingWillow : world.getEntitiesWithinAABB(EntityReapingWillow.class, (new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0D, pos.getY() + 1.0D, pos.getZ() + 1.0D)).grow(distance, 10.0D, distance)))
        {
            if (entityReapingWillow.getAttackTarget() == null && !entityReapingWillow.isOnSameTeam(target))
            {
                entityReapingWillow.setRevengeTarget(target);
                preformGardenCallEffects(world, entityReapingWillow, target, flag);
                flag = false;
                return;
            }
        }
    }

    /** If it is Night. */
    public static boolean isNight(World worldIn)
    {
        int worldTime = (int)(worldIn.getWorldTime() % 24000L);
        return worldTime >= 12600 && worldTime <= 23400;
    }

    public static boolean isPaleEntity(Entity entityIn)
    { return entityIn instanceof EntityCreaking || entityIn instanceof EntityReapingWillow || entityIn instanceof EntityPaleCreeper; }

    /** Replaces a Creeper with a Pale Creeper. */
    public static void convertCreeperToPale(World world, Entity entity)
    {
        EntityPaleCreeper paleCreeper = new EntityPaleCreeper(world);

        paleCreeper.setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);

        if (entity.hasCustomName())
        {
            paleCreeper.setCustomNameTag(entity.getCustomNameTag());
            paleCreeper.setAlwaysRenderNameTag(entity.getAlwaysRenderNameTag());
        }

        world.spawnEntity(paleCreeper);
        entity.setDead();
    }

    public static void preformGardenCallEffects(World worldIn, EntityLivingBase attacker, EntityLivingBase target)
    { preformGardenCallEffects(worldIn, attacker, target, true); }

    /**
     * Preforms all the effects for the Garden Call (Particles, Sound)
     * boolean `firstOne` - determines if the Sound and particles for the target are needed
     * */
    public static void preformGardenCallEffects(World worldIn, EntityLivingBase attacker, EntityLivingBase target, boolean firstOne)
    {
        if (firstOne) worldIn.playSound(null, target.getPosition(), JTPGSounds.EVENT_GARDEN_CALL, SoundCategory.BLOCKS, 2.0F, worldIn.rand.nextFloat() * 0.4F + 0.8F);

        RayTraceResult resultA = worldIn.rayTraceBlocks( new Vec3d(attacker.posX, attacker.posY, attacker.posZ), new Vec3d(attacker.posX, attacker.posY - 4.0, attacker.posZ), false, true, false);
        if (resultA != null && resultA.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            paleBloom.proxy.spawnParticle(3, worldIn, attacker.posX, resultA.hitVec.y, attacker.posZ, 0, 0, 0);
        }

        RayTraceResult resultB = worldIn.rayTraceBlocks( new Vec3d(target.posX, target.posY, target.posZ), new Vec3d(target.posX, target.posY - 4.0, target.posZ), false, true, false);
        if (resultB != null && resultB.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            if (firstOne) paleBloom.proxy.spawnParticle(3, worldIn, target.posX, resultB.hitVec.y, target.posZ, 0, 0, 0);

            for (int i = 0; i < 10; i ++)
            {
                double sX = target.posX + (worldIn.rand.nextDouble() * 2 - 1);
                double sY = resultB.hitVec.y + (worldIn.rand.nextDouble() * 0.75 - 0.5);
                double sZ = target.posZ +(worldIn.rand.nextDouble() * 2 - 1);

                if (firstOne)
                {
                    double hx = target.posX + (worldIn.rand.nextDouble() * 2 - 1);
                    double hy = target.posY + 2 + (worldIn.rand.nextDouble() * 0.5 - 0.25);
                    double hz = target.posZ + (worldIn.rand.nextDouble() * 2 - 1);
                    paleBloom.proxy.spawnParticle(0, worldIn, sX, sY, sZ, hx, hy, hz, 1);
                }

                double aX = attacker.posX + (worldIn.rand.nextDouble() * 0.5 - 0.25);
                double aY = attacker.posY + 2 + (worldIn.rand.nextDouble() * 0.5 - 0.25);
                double aZ = attacker.posZ + (worldIn.rand.nextDouble() * 0.5 - 0.25);
                paleBloom.proxy.spawnParticle(0, worldIn, sX, sY, sZ, aX, aY, aZ, 0);
            }
        }
    }

    public static BulbPullResults requestBulbResin(World world, BlockPos pos, int requiredResin)
    { return requestBulbResin(world, pos, requiredResin, false); }

    /**
     * This seeks out nearby Resin Bulbs, and tries pulling the requested amount of Resin from the bulb.
     *
     * If the Bulb doesn't have enough, it will pull all it can at least.
     *
     * boolean singlePullRequest = Only takes Resin if the found Bulb can meet the entire requested quantity, otherwise skips.
     * */
    public static BulbPullResults requestBulbResin(World world, BlockPos pos, int requiredResin, boolean singlePullRequest)
    {
        BulbPullResults result = new BulbPullResults();
        /* Skip any checks if Bulbs are already disabled. */
        if (!enableResinBulb) return result;

        List<TileResinBulb> bulbs = getNearestBulbs(world, pos, 10);
        if (bulbs.isEmpty()) return result;

        /* Sort bulbs based on distance from the position. */
        bulbs.sort(Comparator.comparingDouble(b -> b.getPos().distanceSq(pos)));

        for (TileResinBulb bulb : bulbs)
        {
            int storedResin = bulb.getStoredResin();

            /* Skip this bulb if the method requires an exact match. */
            if (singlePullRequest && storedResin < requiredResin) continue;

            int take = singlePullRequest ? requiredResin : Math.min(requiredResin - result.resinPulled, storedResin);

            bulb.setStoredResin(Math.max(storedResin - take, 0));
            bulb.markDirty();
            result.resinPulled += take;
            result.bulbsContributing.add(bulb.getPos());

            world.playSound(null, pos, JTPGSounds.BLOCK_PLANT_RESIN_DRAW, SoundCategory.BLOCKS, 1.0F, (world.rand.nextFloat() * 0.4F) + 0.8F);
            spawnParticles(world, bulb.getPos(), pos, take, 2);

            if (result.resinPulled >= requiredResin || singlePullRequest) break;
        }

        return result;
    }

    /** Gets all bulbs nearby that are above 0 Resin. */
    public static List<TileResinBulb> getNearestBulbs(World world, BlockPos pos, int radius)
    {
        List<TileResinBulb> bulbs = new ArrayList<>();

        for (BlockPos checkPos : BlockPos.getAllInBoxMutable( pos.add(-radius, -radius, -radius), pos.add(radius, radius, radius)))
        {
            if (!world.isBlockLoaded(checkPos)) continue;

            TileEntity te = world.getTileEntity(checkPos);
            if (te instanceof TileResinBulb)
            {
                TileResinBulb bulb = (TileResinBulb) te;
                if (bulb.getStoredResin() > 0) bulbs.add(bulb);
            }
        }
        return bulbs;
    }


    public static void spawnParticles(World world, BlockPos posA, BlockPos posB, int particleQuantity, int bublPosIn)
    { spawnParticles(world, posA, posB, particleQuantity, bublPosIn, true); }

    /**
     * A simple method for spawning Resin Particles from one position to another.
     *
     * BlockPos posA = The starting position, where they are spawned.
     * BlockPos posB = The ending position, where they move to.
     * int bublPosIn = Determines which given position is the Bulb. Just adjusts the Y-offset. (1 = posA, 2 = posB, 3 = neither)
     * boolean liveResin = If the Resin is 'live', as in Orange and Glowing. Else it is grey and dead.
     * */
    public static void spawnParticles(World world, BlockPos posA, BlockPos posB, int particleQuantity, int bublPosIn, boolean liveResin)
    {
        for (int i = 0; i < particleQuantity; ++i)
        {
            double startX = posA.getX() + 0.75 + (world.rand.nextDouble() * 0.5 - 0.5);
            double startY = posA.getY() + 0.5 + (world.rand.nextDouble() * 0.5 - (bublPosIn == 1 ? 0.75 : 0.5));
            double startZ = posA.getZ() + 0.75 + (world.rand.nextDouble() * 0.5 - 0.5);

            double endX = posB.getX() + 0.5 + (world.rand.nextDouble() * 0.5 - 0.25);
            double endY = posB.getY() + 0.5 + (world.rand.nextDouble() * 0.5 - (bublPosIn == 2 ? 0.75 : 0.5));
            double endZ = posB.getZ() + 0.5 + (world.rand.nextDouble() * 0.5 - 0.25);

            paleBloom.proxy.spawnParticle(0, world, startX, startY, startZ, endX, endY, endZ, liveResin ? 1 : 0);
        }
    }

    public static void spawnCreakingTrailParticles(Entity entity, BlockPos heartPos, int quantityIn)
    { spawnCreakingTrailParticles(entity, heartPos, quantityIn, 1); }

    /** Spawns Trail Particles between a block and entity. */
    public static void spawnCreakingTrailParticles(Entity entity, BlockPos heartPos, int quantityIn, int speedIn)
    {
        if (entity == null || entity.isDead) return;

        for (int i = 0; i < quantityIn; ++i)
        {
            double hx = heartPos.getX() + 0.5 + (entity.world.rand.nextDouble() * 0.5 - 0.25);
            double hy = heartPos.getY() + 0.5 + (entity.world.rand.nextDouble() * 0.5 - 0.25);
            double hz = heartPos.getZ() + 0.5 + (entity.world.rand.nextDouble() * 0.5 - 0.25);

            double cX = entity.posX + (entity.world.rand.nextDouble() * 0.5 - 0.25);
            double cY = entity.posY + (entity.world.rand.nextDouble() * entity.height);
            double cZ = entity.posZ + (entity.world.rand.nextDouble() * 0.5 - 0.25);

            paleBloom.proxy.spawnParticle(0, entity.world, cX, cY, cZ, hx, hy, hz, 0, speedIn);
            paleBloom.proxy.spawnParticle(0, entity.world, hx, hy, hz, cX, cY, cZ, 1, speedIn);
        }
    }



    public static class BulbPullResults
    {
        public int resinPulled;
        public final List<BlockPos> bulbsContributing = new ArrayList<>();
    }
}