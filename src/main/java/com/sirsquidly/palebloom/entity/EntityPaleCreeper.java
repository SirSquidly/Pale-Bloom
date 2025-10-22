package com.sirsquidly.palebloom.entity;

import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.init.JTPGItems;
import com.sirsquidly.palebloom.init.JTPGLootTables;
import com.sirsquidly.palebloom.paleBloom;
import com.sirsquidly.palebloom.world.WorldPaleGarden;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class EntityPaleCreeper extends EntityCreeper implements IEyeGlow
{
    private static final ResourceLocation EYEGLOW_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/entities/pale_creeper/pale_creeper_awake_e.png");

    private static final DataParameter<Boolean> EYES_GLOW = EntityDataManager.createKey(EntityPaleCreeper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STATE = EntityDataManager.createKey(EntityPaleCreeper.class, DataSerializers.BOOLEAN);

    private int lastActiveTime;
    private int timeSinceIgnited;
    private int fuseTime = 30;
    private int explosionRadius = 3;

    public EntityPaleCreeper(World worldIn)
    { super(worldIn); }

    protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(EYES_GLOW, false);
        this.getDataManager().register(STATE, false);
    }

    protected ResourceLocation getLootTable() { return JTPGLootTables.ENTITIES_PALE_CREEPER; }

    public void onUpdate()
    {
        if (this.isEntityAlive())
        {
            this.lastActiveTime = this.timeSinceIgnited;

            this.setPaleCreeperState(this.getCreeperState() > 0 || this.hasIgnited());

            if (getPaleCreeperState())
            {
                if (this.timeSinceIgnited == 0) this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
                this.timeSinceIgnited += 1;
            }
            else this.timeSinceIgnited -= 1;

            if (this.timeSinceIgnited < 0)
            { this.timeSinceIgnited = 0; }

            if (this.timeSinceIgnited >= this.fuseTime)
            {
                this.timeSinceIgnited = this.fuseTime;
                this.explodePaleCreeperDAMN();
            }
        }

        /* Get OVERRIDDEN CREEPER, HA! */
        this.setCreeperState(-1);
        super.onUpdate();
    }


    private void explodePaleCreeperDAMN()
    {
        if (!this.world.isRemote)
        {
            this.dead = true;
            this.setDead();

            world.playSound(null, this.getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1.0F, 0.001F);

            ((WorldServer)this.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY + (double)this.height / 1.5D, this.posZ, 80, this.width / 3.0F, this.height / 4.0F, this.width / 3.0F, 0.05D, Block.getStateId(JTPGBlocks.PALE_OAK_LEAVES.getDefaultState()));
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

                paleBloom.proxy.spawnParticle(0, world, cX, cY, cZ, dX, dY, dZ, 0, 2);
                //palebloom.proxy.spawnParticle(0, world, hx, hy, hz, cX, cY, cZ, 0, 0, 0, 1);
            }

            if (list.isEmpty()) return;

            for (EntityLivingBase entity : list)
            {
                if (WorldPaleGarden.isPaleEntity(entity)) continue;

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
    }

    @SideOnly(Side.CLIENT)
    public float getCreeperFlashIntensity(float p_70831_1_)
    { return ((float)this.lastActiveTime + (float)(this.timeSinceIgnited - this.lastActiveTime) * p_70831_1_) / (float)(this.fuseTime - 2); }

    /** Whenever the Attack Target gets updated to something new, play a targeting sound. */
    @Override
    public void setAttackTarget(@Nullable EntityLivingBase attackTarget)
    {
        /* Do not play any sounds if the target is the exact same as prior! */
        if (attackTarget != this.getAttackTarget())
        {
            if (attackTarget != null)
            {
                this.setGlowingEyes(true);
            }
            else
            {
                this.setGlowingEyes(false);
            }
        }

        super.setAttackTarget(attackTarget);
    }

    public void onDeath(DamageSource cause)
    {
        if (this.world.getGameRules().getBoolean("doMobLoot"))
        {
            if (cause.getTrueSource() instanceof EntitySkeleton)
            {
                this.dropItem(JTPGItems.PALE_PAINTING, 1);

                /* Return a generic damage source, to prevent the Skeleton Music Disc Drop from the inherited Creeper code.*/
                super.onDeath(new DamageSource("generic"));
                return;
            }
        }
        super.onDeath(cause);
    }

    public void onStruckByLightning(EntityLightningBolt lightningBolt)
    {
        if (!this.world.isRemote && !this.isDead)
        {
            EntityCreeper creeper = new EntityCreeper(this.world);
            creeper.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            creeper.setNoAI(this.isAIDisabled());

            if (this.hasCustomName())
            {
                creeper.setCustomNameTag(this.getCustomNameTag());
                creeper.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
            }

            this.world.spawnEntity(creeper);
            this.setDead();
        }
    }

    public boolean getPaleCreeperState() { return this.dataManager.get(STATE); }
    public void setPaleCreeperState(boolean bool) { this.dataManager.set(STATE, bool); }

    public ResourceLocation getEyeGlowTexture() { return EYEGLOW_TEXTURE; }
    public boolean getGlowingEyes() { return this.dataManager.get(EYES_GLOW); }
    public void setGlowingEyes(boolean bool) { this.dataManager.set(EYES_GLOW, bool); }
}