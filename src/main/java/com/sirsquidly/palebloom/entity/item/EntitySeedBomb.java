package com.sirsquidly.palebloom.entity.item;

import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.paleBloom;
import com.sirsquidly.palebloom.world.WorldPaleGarden;
import com.sirsquidly.palebloom.world.feature.WorldGenMoss;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nullable;
import java.util.List;

public class EntitySeedBomb extends EntityTNTPrimed
{
    private static final DataParameter<Integer> FUSE = EntityDataManager.<Integer>createKey(EntitySeedBomb.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(FUSE, 80);
    }

    public EntitySeedBomb(World worldIn)
    { super(worldIn); }

    public EntitySeedBomb(World worldIn, double x, double y, double z, EntityLivingBase igniter)
    {
        this(worldIn);
        this.setPosition(x, y, z);
        float f = (float)(Math.random() * (Math.PI * 2D));
        this.motionX = (double)(-((float)Math.sin((double)f)) * 0.02F);
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double)(-((float)Math.cos((double)f)) * 0.02F);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.tntPlacedBy = igniter;
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (!this.hasNoGravity())
        { this.motionY -= 0.03999999910593033D; }

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        this.setFuse(this.getFuse() - 1);

        if (this.getFuse() <= 0)
        {
            this.setDead();

            this.explodeUnderwater();
        }
        else
        {
            this.handleWaterMovement();
            //this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void explodeUnderwater()
    {
        if (this.world.isRemote) return;

        WorldGenerator mossGen = new WorldGenMoss(10, 1.0F);

        mossGen.generate(this.world, rand, this.getPosition().down());

        world.playSound(null, this.getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1.0F, 0.001F);

        ((WorldServer)this.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY + (double)this.height / 1.5D, this.posZ, 80, this.width / 2.0F, this.height / 2.0F, this.width / 2.0F, 0.05D, Block.getStateId(JTPGBlocks.PALE_OAK_LEAVES.getDefaultState()));
        ((WorldServer)this.world).spawnParticle(EnumParticleTypes.TOWN_AURA, this.posX, this.posY + 1, this.posZ, 200, 1, 1, 1, 0.1D);

        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().grow(4.0D, 2.0D, 4.0D);
        List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

        for (int i = 0; i < 100; i++)
        {
            double cX = this.posX + (world.rand.nextDouble() * 0.5 - 0.25);
            double cY = this.posY + 1 + (world.rand.nextDouble() * this.height - (this.height/2));
            double cZ = this.posZ + (world.rand.nextDouble() * 0.5 - 0.25);

            double dX = this.posX + (this.rand.nextDouble() * 6 - 3);
            double dY = this.posY + (this.rand.nextDouble() * 6 - 3);
            double dZ = this.posZ + (this.rand.nextDouble() * 6 - 3);

            paleBloom.proxy.spawnParticle(0, world, cX, cY, cZ, dX, dY, dZ, 0, 3);
            //palebloom.proxy.spawnParticle(0, world, hx, hy, hz, cX, cY, cZ, 0, 0, 0, 1);
        }

        if (list.isEmpty()) return;

        for (EntityLivingBase entity : list)
        {
            if (WorldPaleGarden.isPaleEntity(entity)) continue;
            if (entity instanceof EntityCreeper) WorldPaleGarden.convertCreeperToPale(world, entity);

            double d0 = this.getDistanceSq(entity);

            if (d0 < 16.0D)
            {
                double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

                int i = (int)(d1 * (double)(16 * 20) + 0.5D);

                if (i > 20)
                { entity.addPotionEffect(new PotionEffect(MobEffects.WITHER, i, 2, false, true)); }
            }
        }
    }

    public int getFuse()
    { return this.dataManager.get(FUSE); }

    public void setFuse(int fuseIn)
    { this.dataManager.set(FUSE, fuseIn); }
}