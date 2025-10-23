package com.sirsquidly.palebloom.common.entity.item;

import com.sirsquidly.palebloom.init.JTPGBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityThorn extends EntityThrowable
{
    public EntityThorn(World worldIn)
    { super(worldIn); }

    public EntityThorn(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    public EntityThorn(World worldIn, double x, double y, double z, EntityLivingBase throwerIn)
    {
        this(worldIn);
        this.setPosition(x, y, z);
        this.ticksExisted = 2;
        this.thrower = throwerIn;
    }

    protected void onImpact(RayTraceResult result)
    {
        if (result.entityHit != null)
        {
            int i = 4;
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)i);
        }

        if (!this.world.isRemote)
        {
            ((WorldServer)this.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, 2, this.width / 4.0F, this.height / 4.0F, this.width / 4.0F, 0.05D, Block.getStateId(JTPGBlocks.PALE_OAK_LOG.getDefaultState()));

            if (world.rand.nextBoolean()) this.world.playSound(null, this.getPosition(), SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 0.1F,1.0F);

            this.world.setEntityState(this, (byte)3);
            this.setDead();
        }
    }
}