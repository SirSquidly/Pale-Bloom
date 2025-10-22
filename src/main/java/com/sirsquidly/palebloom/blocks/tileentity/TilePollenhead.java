package com.sirsquidly.palebloom.blocks.tileentity;

import com.sirsquidly.palebloom.ConfigParser;
import com.sirsquidly.palebloom.blocks.BlockDoublePalePlant;
import com.sirsquidly.palebloom.blocks.BlockPalePetals;
import com.sirsquidly.palebloom.blocks.BlockPollenhead;
import com.sirsquidly.palebloom.blocks.IGardenState;
import com.sirsquidly.palebloom.entity.EntityPaleCreeper;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.paleBloom;
import com.sirsquidly.palebloom.world.WorldPaleGarden;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TilePollenhead extends TileEntity implements ITickable
{
    public int storedResin;
    /** The absolute minium stored Resin, before this block attempts to take more. */
    public int resinMinPull = 32;
    /** How much resin is pulled when . */
    public int resinPullQuantity = 8;

    public boolean isAwake;

    /** The minimum blocks required for a Conduit to be able to Hunt/Defend. */
    public int pollenDistanceXZ = 3;
    public int pollenDistanceY = 2;
    public int poisonDistance = 4;
    public int hybridizeDistance = 4;

    @Override
    public void update()
    {
        if(!world.isRemote && world.getTotalWorldTime() % 20L == 0L)
        {
            int currentResin = this.getStoredResin();
            IBlockState state = world.getBlockState(pos);
            IGardenState.EnumLucidityState lucidity = state.getValue(BlockPollenhead.LUCIDITY_STATE);
            /* Wait for the plant to become Lucid. */
            if (lucidity == IGardenState.EnumLucidityState.DORMANT) return;

            IGardenState.EnumLucidityState newLucidity = this.isAwake ? IGardenState.EnumLucidityState.AWAKE : IGardenState.EnumLucidityState.LUCID;
            if (lucidity != newLucidity)
            {
                SoundEvent sound = this.isAwake ? JTPGSounds.BLOCK_POLLENHEAD_AWAKEN : JTPGSounds.BLOCK_POLLENHEAD_CLOSE_LUCID;
                world.playSound(null, pos, sound, SoundCategory.BLOCKS, 0.25F, (world.rand.nextFloat() * 0.4F) + 0.8F);

                ((BlockPollenhead)state.getBlock()).placeDoubleAt(world, pos.down(), newLucidity, 3);
            }

            if (WorldPaleGarden.isNight(world) && currentResin > 0) this.isAwake = true;
            else this.isAwake = false;


            /* Resin Collection is allowed while Lucid. */
            if (currentResin < resinMinPull)
            { this.setStoredResin(currentResin + WorldPaleGarden.requestBulbResin(world, pos, resinPullQuantity).resinPulled); }

            //System.out.print("Sored Resin: " + currentResin);

            if (!this.isAwake) return;

            spawnAmbinetPollenParticles(pos);
            poisonNearbyEntities();

            if (this.storedResin > 8) tryHybridize(world, pos, 10);

            /** 20 = 1 second, so Resin lowers by 1 every 10 seconds. */
            if (world.getTotalWorldTime() % 200L != 0L) this.setStoredResin(this.getStoredResin() - 1);
        }
    }

    public void poisonNearbyEntities()
    {
        for(EntityLivingBase entity : world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.add(-poisonDistance, -poisonDistance, -poisonDistance), pos.add(poisonDistance + 1, poisonDistance + 1, poisonDistance + 1))))
        {
            if (WorldPaleGarden.isPaleEntity(entity)) return;

            int sphereCheck = (poisonDistance + 1) * (poisonDistance + 1);
            if (entity.getDistanceSqToCenter(pos) < sphereCheck)
            {
                entity.addPotionEffect(new PotionEffect(MobEffects.POISON, 6 * 20 + 18, 0));

                if (entity instanceof EntityCreeper && entity.getHealth() < entity.getMaxHealth()/2)
                {
                    WorldPaleGarden.convertCreeperToPale(world, entity);
                }
            }
        }
    }

    /** Gets a random nearby block, checks if it is a hybridizes one (from config), and replaces it if so. */
    public void tryHybridize(World world, BlockPos pos, int maxAttempts)
    {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int attempts = 0; attempts < maxAttempts; attempts++)
        {
            int offsetX = world.rand.nextInt(hybridizeDistance * 2) - hybridizeDistance;
            int offsetY = world.rand.nextInt(hybridizeDistance * 2) - hybridizeDistance;
            int offsetZ = world.rand.nextInt(hybridizeDistance * 2) - hybridizeDistance;

            mutablePos.setPos(pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ);

            IBlockState blockState = world.getBlockState(mutablePos);

            /* If the random position is Air, 50% chance to add another attempt, then continue to immediately re-roll. */
            if (world.isAirBlock(mutablePos) && world.rand.nextBoolean())
            {
                attempts--;
                continue;
            }

            if (!naturalFloorBlock(world.getBlockState(mutablePos.down()).getBlock()))
            {
                if (world.rand.nextInt(4) == 0) attempts -= 1;
                continue;
            }

            int index = ConfigParser.blockPollenheadHybridFROM.indexOf(blockState);

            /* If not in the list, immediately re-roll, with a 1/4 chance to add another attempt. */
            if (index < 0)
            {
                if (world.rand.nextInt(4) == 0) attempts -= 1;
                continue;
            }

            world.setBlockToAir(mutablePos);
            if (blockState.getBlock() instanceof BlockDoublePlant)
            { if (world.getBlockState(mutablePos.up()).getBlock() == blockState.getBlock()) world.setBlockToAir(mutablePos.up()); }

            IBlockState hybridState = ConfigParser.blockPollenheadHybridTO.get(index);

            if (hybridState.getBlock() instanceof BlockDoublePalePlant)
            { ((BlockDoublePalePlant)hybridState.getBlock()).placeDoubleAt(world, mutablePos, hybridState.getBlock().getMetaFromState(hybridState), false, 2); }

            if (hybridState.getBlock() instanceof BlockPalePetals)
            { hybridState = hybridState.withProperty(BlockPalePetals.AMOUNT, world.rand.nextInt(4) + 1).withProperty(BlockPalePetals.FACING, EnumFacing.byHorizontalIndex(world.rand.nextInt(4))); }

            world.setBlockState(mutablePos, hybridState, 2);

            world.playSound(null, pos, JTPGSounds.BLOCK_RESIN_PLACE, SoundCategory.BLOCKS, 0.25F, 1.0F);
            WorldPaleGarden.spawnParticles(world, mutablePos, pos, 16, 1);
            WorldPaleGarden.spawnParticles(world, mutablePos.up(2), mutablePos, 10, 2);

            this.setStoredResin(Math.max(this.getStoredResin() - 8, 0));
            spawnSelfPollenParticles(pos);

            return;
        }
    }

    /** If the given block is natural, thus allowing hybridizing. */
    public boolean naturalFloorBlock(Block block)
    { return block == JTPGBlocks.PALE_MOSS || block == Blocks.GRASS || block == Blocks.DIRT; }


    public void spawnAmbinetPollenParticles(BlockPos pos)
    {
        for (int i = 0; i < 6; ++i)
        {
            double posX = pos.getX() + 0.5 + ((world.rand.nextFloat() * 2 - 1) * pollenDistanceXZ);
            double posY = pos.getY() + ((world.rand.nextFloat() * 2 - 1) * pollenDistanceY);
            double posZ = pos.getZ() + 0.5 + ((world.rand.nextFloat() * 2 - 1) * pollenDistanceXZ);
            double speedX = (world.rand.nextFloat() * 2 - 1) * 0.05F;
            double speedY = (world.rand.nextFloat() * 2 - 1) * 0.02F;
            double speedZ = (world.rand.nextFloat() * 2 - 1) * 0.05F;

            paleBloom.proxy.spawnParticle(2, world, posX, posY, posZ, speedX, speedY, speedZ, 2);
        }
    }

    public void spawnSelfPollenParticles(BlockPos pos)
    {
        for (int i = 0; i < 10; ++i)
        {
            double posX = pos.getX() + 1 + (world.rand.nextDouble() * 0.5 - 0.5);
            double posY = pos.getY() - 1 + ((world.rand.nextFloat() * 2 - 1));
            double posZ = pos.getZ() + 1 + (world.rand.nextDouble() * 0.5 - 0.5);
            double speedX = (world.rand.nextFloat() * 2 - 1) * 0.01F;
            double speedY = world.rand.nextDouble() * 0.1;
            double speedZ = (world.rand.nextFloat() * 2 - 1) * 0.01F;

            paleBloom.proxy.spawnParticle(2, world, posX, posY, posZ, speedX, speedY, speedZ, 2);
        }
    }

    public int getStoredResin() { return this.storedResin; }
    public void setStoredResin(int resinIn) { this.storedResin = resinIn; }

    /** Keeps the tile entity around even if the block gets its state changed. */
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    { return oldState.getBlock() != newState.getBlock(); }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setByte("StoredResin", (byte) this.storedResin);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.storedResin = compound.getByte("StoredResin");
    }
}
