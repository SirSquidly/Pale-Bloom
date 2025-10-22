package com.sirsquidly.palebloom.entity;

import com.sirsquidly.palebloom.init.JTPGLootTables;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.paleBloom;
import com.sirsquidly.palebloom.world.WorldPaleGarden;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class EntityReapingWillow extends EntityMob implements IAnimatable, IAnimationTickable, IEyeGlow
{
    private static final ResourceLocation EYEGLOW_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/entities/reaping_willow/reaping_willow_awake_e.png");

    /** If the Reaping Willow's eyes should be glowing. */
    private static final DataParameter<Boolean> EYES_GLOW = EntityDataManager.createKey(EntityReapingWillow.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Byte> ANIM_TICK = EntityDataManager.createKey(EntityReapingWillow.class, DataSerializers.BYTE);

    protected static final AnimationBuilder IDLE_ANIMATION = new AnimationBuilder().addAnimation("idle", false);
    protected static final AnimationBuilder WALK_ANIMATION = new AnimationBuilder().addAnimation("walk", true);
    protected static final AnimationBuilder ATTACK_ANIMATION = new AnimationBuilder().addAnimation("attack", false);

    public int attackTick;

    public EntityReapingWillow(World worldIn)
    {
        super(worldIn);
        this.setSize(0.9F, 2.7F);
        this.stepHeight = 1.0F;

        this.experienceValue = 20;
    }

    protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(EYES_GLOW, false);
        this.getDataManager().register(ANIM_TICK, (byte) 0);
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 3.0D, true));
        this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 3.0D, 32.0F));
        this.tasks.addTask(3, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));

        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        /* Speed need to get raised to 0.12D when chasing. */
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.12D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    protected ResourceLocation getLootTable() { return JTPGLootTables.ENTITIES_REAPING_WILLOW; }

    protected void playStepSound(BlockPos pos, Block blockIn) { this.playSound(JTPGSounds.ENTITY_REAPING_WILLOW_STEP, 0.15F, 1.0F); }

    protected SoundEvent getAmbientSound() { return this.getAttackTarget() != null ? JTPGSounds.ENTITY_REAPING_WILLOW_ANGER: JTPGSounds.ENTITY_CREAKING_AMBIENT; }

    protected SoundEvent getDeathSound() { return JTPGSounds.ENTITY_REAPING_WILLOW_DEATH; }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return JTPGSounds.ENTITY_REAPING_WILLOW_HURT; }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.isSwingInProgress)
        {
            this.attackTick = 20;
            this.isSwingInProgress = false;
        }

        if (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.00001D)
        {
            // Add more than usual to make steps happen faster
            this.distanceWalkedModified += 0.046F; // Vanilla is ~0.1F per step
            this.distanceWalkedOnStepModified += 0.046F;
        }

        --this.attackTick;

        /* Attempt a little healing every 5 seconds. */
        if (this.ticksExisted % 100 == 0)
        {
            if (this.getHealth() < this.getMaxHealth())
            {
                int i = WorldPaleGarden.requestBulbResin(world, this.getPosition().up(2), 10, true).resinPulled;
                if (i >= 10)
                {
                    this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 60, 3));
                }
            }
        }
    }

    /** Reaping Willows are created, so do not despawn. */
    protected boolean canDespawn() { return false; }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        float damage = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), damage);

        if (flag)
        {
            entityIn.motionY += 0.4000000059604645D;
            this.applyEnchantments(this, entityIn);
        }

        this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

    /** Whenever the Attack Target gets updated to something new, play a targeting sound. */
    @Override
    public void setAttackTarget(@Nullable EntityLivingBase attackTarget)
    {
        /* Do not play any sounds if the target is the exact same as prior! */
        if (attackTarget != this.getAttackTarget())
        {
            if (attackTarget != null)
            {
                this.playSound(JTPGSounds.ENTITY_CREAKING_TARGET, 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
                this.setGlowingEyes(true);
            }
            else
            {
                this.playSound(JTPGSounds.ENTITY_CREAKING_NOTARGET, 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
                this.setGlowingEyes(false);
            }
        }

        super.setAttackTarget(attackTarget);
    }

    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn)
    { return entityIn.canBePushed() && !this.getGlowingEyes() ? entityIn.getEntityBoundingBox() : null; }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox()
    { return this.getGlowingEyes() ? null : this.getEntityBoundingBox(); }

    /** Ahead is GeckoLib implimentations! */
    private AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void registerControllers(AnimationData animationData)
    {
        animationData.shouldPlayWhilePaused = true;
        AnimationController walkController = new AnimationController(this, "walking_controller", 4, this::predicateWalking);
        animationData.addAnimationController(walkController);
        animationData.addAnimationController(new AnimationController(this, "attack_controller", 1, this::predictAttack));
        //animationData.addAnimationController(new AnimationController(this, "twitch_controller", 1, this::predicateTwitch));
    }

    private <E extends IAnimatable> PlayState predicateWalking(AnimationEvent<E> event)
    {
        if (event.getLimbSwingAmount() > 0.005F && this.attackTick <= 0)
        {
            event.getController().setAnimation(WALK_ANIMATION);

            /* Speed adjustment is based around keeping it close to 1 at the default walking speed, but going far higher at high speeds. */
            event.getController().setAnimationSpeed(event.getLimbSwingAmount() * 2 + 0.88f);
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(IDLE_ANIMATION);
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predictAttack(AnimationEvent<E> event)
    {
        if (this.attackTick > 0)
        {
            event.getController().setAnimation(ATTACK_ANIMATION);
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }


    @Override public AnimationFactory getFactory() { return this.factory; }

    @Override public void tick() {}

    @Override public int tickTimer() { return this.ticksExisted; }

    public ResourceLocation getEyeGlowTexture() { return EYEGLOW_TEXTURE; }
    public boolean getGlowingEyes() { return this.dataManager.get(EYES_GLOW); }
    public void setGlowingEyes(boolean bool) { this.dataManager.set(EYES_GLOW, bool); }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("EyesGlow", getGlowingEyes());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setGlowingEyes(compound.getBoolean("EyesGlow"));
    }
}