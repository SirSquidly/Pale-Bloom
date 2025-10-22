package com.sirsquidly.palebloom.blocks.tileentity;

import com.sirsquidly.palebloom.blocks.BlockIncenseThorn;
import com.sirsquidly.palebloom.blocks.IGardenState;
import com.sirsquidly.palebloom.paleBloom;
import com.sirsquidly.palebloom.world.WorldPaleGarden;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Collections;
import java.util.List;

public class TileIncenseThorn extends TileEntity implements ITickable
{
    public int storedResin;
    public int resinPullQuantity = 8;
    /** The absolute minium stored Resin, before this block attempts to take more. */
    public int resinMinPull = 32;
    public int effectDistance = 5;

    private Potion potionType;
    /** Default color is White. */
    public int potionColor = 16777215;
    private int potionTypeBaseDuration = 21;
    private int potionTypeBaseAmplifier = 0;

    public boolean isAwake = false;

    public boolean cachedAwake = false;


    @Override
    public void update()
    {
        if(world.isRemote || world.getTotalWorldTime() % 20L != 0L) return;

        isAwake = WorldPaleGarden.isNight(world);

        int currentResin = this.getStoredResin();
        IBlockState state = world.getBlockState(pos);
        IGardenState.EnumLucidityState lucidity = state.getValue(BlockIncenseThorn.LUCIDITY_STATE);
        /* Wait for the plant to become Lucid. */
        if (lucidity == IGardenState.EnumLucidityState.DORMANT) return;

        IGardenState.EnumLucidityState newLucidity = this.isAwake ? IGardenState.EnumLucidityState.AWAKE : IGardenState.EnumLucidityState.LUCID;
        if (lucidity != newLucidity)
        {
            world.setBlockState(pos, state.withProperty(BlockIncenseThorn.LUCIDITY_STATE, newLucidity));
        }

        if (WorldPaleGarden.isNight(world) && currentResin > 0) this.isAwake = true;
        else this.isAwake = false;


        /* Resin Collection is allowed while Lucid. */
        if (currentResin < resinMinPull)
        { this.setStoredResin(currentResin + WorldPaleGarden.requestBulbResin(world, pos, resinPullQuantity).resinPulled); }

        if (!this.isAwake) return;

        spawnPotionParticles(1);
        spawnFloorMistParticles(pos);
        effectNearbyEntities();

        /** 20 = 1 second, so Resin only lowers by 1 every 6 seconds, or 16 seconds if there is no Potion. */
        if (world.getTotalWorldTime() % (getPotion() == null ? 320L : 120L) == 0L) this.setStoredResin(this.getStoredResin() - 1);
    }

    public void effectNearbyEntities()
    {
        if (potionType == null) return;

        for(EntityLivingBase entity : world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.add(-effectDistance, -effectDistance, -effectDistance), pos.add(effectDistance + 1, effectDistance + 1, effectDistance + 1))))
        {
            if (WorldPaleGarden.isPaleEntity(entity)) return;

            int sphereCheck = (effectDistance + 1) * (effectDistance + 1);
            if (entity.getDistanceSqToCenter(pos) < sphereCheck)
            {
                entity.addPotionEffect(new PotionEffect(potionType, potionTypeBaseDuration, potionTypeBaseAmplifier, true, true));
            }
        }
    }

    public void spawnPotionParticles(int quantityIn)
    {
        //float r = ((potionColor >> 16) & 0xFF) / 255.0F;
        //float g = ((potionColor >> 8) & 0xFF) / 255.0F;
        //float b = (potionColor & 0xFF) / 255.0F;

        for (int i = 0; i < quantityIn; i++)
        {
            double x = pos.getX() + 0.5F + ((world.rand.nextDouble() * 0.6F) - 0.3F);
            double y = pos.getY() + 0.7F + ((world.rand.nextDouble() * 0.4F) - 0.2F);
            double z = pos.getZ() + 0.5F + ((world.rand.nextDouble() * 0.6F) - 0.3F);

            //((WorldServer) world).spawnParticle( EnumParticleTypes.SPELL_MOB, true, x, y, z, 0, r, g, b, 1.0D );

            if (world.rand.nextBoolean()) continue;
            paleBloom.proxy.spawnParticle(5, world, x, y, z, 0, 0, 0, potionColor);
        }
    }

    public void spawnFloorMistParticles(BlockPos pos)
    {
        if (world.rand.nextInt(Math.max(1, (int)(2F / effectDistance))) == 0)
        {
            float f1 = world.rand.nextFloat() * ((float)Math.PI * 2F);
            float f2 = MathHelper.sqrt(world.rand.nextFloat()) * 0.5F;
            float f3 = MathHelper.cos(f1) * effectDistance * f2;
            float f4 = MathHelper.sin(f1) * effectDistance * f2;

            float sX = (world.rand.nextFloat() - 0.5F) * 0.04F;
            float sZ = (world.rand.nextFloat() - 0.5F) * 0.04F;

            BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos((int) (pos.getX() + f3), pos.getY(), (int) (pos.getZ() + f4));

            /* First, move DOWN if this is in the air, until entering a block. */
            while (!(world.getBlockState(checkPos).isSideSolid(world, checkPos, EnumFacing.UP)) && checkPos.getY() > pos.getY() - effectDistance)
            { checkPos.setY(checkPos.getY() - 1); }
            /* Then, move UP out of any blocks. */
            while (world.getBlockState(checkPos).isSideSolid(world, checkPos, EnumFacing.UP) && checkPos.getY() < pos.getY() + effectDistance)
            { checkPos.setY(checkPos.getY() + 1); }

            paleBloom.proxy.spawnParticle(6, this.world, pos.getX() + f3, checkPos.getY() + 0.1F, pos.getZ() + f4, sX, 0.01D, sZ, potionColor);
        }
    }

    /** Sets Color based on the Potion Effect. */
    public void setupPotionColor()
    {
        if (potionType == null) return;
        List<PotionEffect> effects = Collections.singletonList(new PotionEffect(potionType, 200));
        setColor(PotionUtils.getPotionColorFromEffectList(effects));
    }

    public void setPotion(Potion potion) { this.potionType = potion; }
    public Potion getPotion() { return this.potionType; }

    public void setColor(int colorIn) { this.potionColor = colorIn; }
    public int getColor() { return this.potionColor; }

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
        compound.setInteger("StoredResin", this.storedResin);
        if (potionType != null) compound.setString("Potion", potionType.getRegistryName().toString());
        compound.setInteger("Color", getColor());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.storedResin = compound.getInteger("StoredResin");
        if (compound.hasKey("Potion")) this.potionType = Potion.getPotionFromResourceLocation(compound.getString("Potion"));
        if (compound.hasKey("Color")) setColor(compound.getInteger("Color"));
    }
}